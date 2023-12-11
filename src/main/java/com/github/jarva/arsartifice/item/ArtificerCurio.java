package com.github.jarva.arsartifice.item;

import com.github.jarva.arsartifice.glyphs.AbstractArtificeMethod;
import com.github.jarva.arsartifice.glyphs.TickableArtificeMethod;
import com.hollingsworth.arsnouveau.api.item.ArsNouveauCurio;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;

public class ArtificerCurio extends ArsNouveauCurio implements ICasterTool {
    public ArtificerCurio() {
        super();
    }

    @Override
    public ISpellCaster getSpellCaster() {
        return new ArtificerCaster(new CompoundTag());
    }

    @Override
    public ISpellCaster getSpellCaster(CompoundTag tag) {
        return new ArtificerCaster(tag);
    }

    @Override
    public @NotNull ISpellCaster getSpellCaster(ItemStack stack) {
        return new ArtificerCaster(stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        ISpellCaster caster = getSpellCaster(stack);
        Spell artifice = caster.getSpell(0);
        if (artifice.isEmpty()) return;
        if (artifice.recipe.get(0) instanceof AbstractArtificeMethod method && method instanceof TickableArtificeMethod tickable) {
            LivingEntity entity = slotContext.entity();
            SpellStats stats = method.getSpellStats(caster, entity, artifice);
            tickable.tick(entity, stack, caster, stats, caster.getSpell(1));
        }
    }

    public Component getDisplayString(Spell spell) {
        MutableComponent str = Component.empty();

        for (int i = 0; i < spell.recipe.size(); i++) {
            AbstractSpellPart spellPart = spell.recipe.get(i);
            if (spellPart instanceof AbstractAugment) continue;
            HashMap<AbstractAugment, Integer> augments = new HashMap<>();
            for (int j = i +1; j < spell.recipe.size(); j++) {
                if (spell.recipe.get(j) instanceof AbstractAugment augment) {
                    augments.compute(augment, (k, v) -> v == null ? 1 : v + 1);
                } else {
                    break;
                }
            }

            str.append(spellPart.getLocaleName());
            if (spellPart instanceof AbstractArtificeMethod artificeMethod) {
                str.append(" ").append(artificeMethod.getMeta(augments));
            } else {
                augments.forEach((augment, num) -> {
                    str.append(augment.getLocaleName()).append(" x").append(String.valueOf(num));
                });
            }
            if (i < spell.recipe.size() - augments.size() - 1) {
                str.append(" -> ");
            }
        }

        return str;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip2, TooltipFlag flagIn) {
        if (worldIn == null)
            return;

        ISpellCaster caster = getSpellCaster(stack);
        Spell method = caster.getSpell(0);
        if (method.isValid()) {
            tooltip2.add(Component.translatable("ars_artifice.tooltip.trigger").append(": ").append(getDisplayString(method)));
        }

        Spell spell = caster.getSpell(1);
        if (spell.isValid()) {
            tooltip2.add(Component.translatable("ars_artifice.tooltip.spell").append(": ").append(spell.getDisplayString()));
        }

        super.appendHoverText(stack, worldIn, tooltip2, flagIn);
    }

    public static class ArtificerCaster extends SpellCaster {
        public ArtificerCaster(ItemStack stack) {
            super(stack);
        }

        public ArtificerCaster(CompoundTag tag) {
            super(tag);
        }

        public int getMaxSlots() {
            return 2;
        }

        @Override
        public int getCurrentSlot() {
            return 1;
        }

        @Override
        public InteractionResultHolder<ItemStack> castSpell(Level worldIn, LivingEntity entity, InteractionHand handIn, @org.jetbrains.annotations.Nullable Component invalidMessage, @NotNull Spell spell) {
//            spell.addDiscount(-spell.getCost());
            return super.castSpell(worldIn, entity, handIn, invalidMessage, spell);
        }
    }
}
