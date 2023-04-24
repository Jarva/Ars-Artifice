package com.github.jarva.arsartifice.item;

import com.github.jarva.arsartifice.glyphs.AbstractArtificeMethod;
import com.github.jarva.arsartifice.glyphs.TickableArtificeMethod;
import com.hollingsworth.arsnouveau.api.item.ArsNouveauCurio;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
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
        AbstractArtificeMethod method = (AbstractArtificeMethod) artifice.recipe.get(0);
        if (method instanceof TickableArtificeMethod tickable) {
            LivingEntity entity = slotContext.entity();
            SpellStats stats = method.getSpellStats(caster, entity, artifice);
            tickable.tick(entity, stack, caster, stats, caster.getSpell(1));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip2, TooltipFlag flagIn) {
        if (worldIn == null)
            return;

        ISpellCaster caster = getSpellCaster(stack);
        Spell method = caster.getSpell(0);
        if (method.isValid()) {
            tooltip2.add(Component.literal("Trigger: " + method.getDisplayString()));
        }

        Spell spell = caster.getSpell(1);
        if (spell.isValid()) {
            tooltip2.add(Component.literal("Spell: " + spell.getDisplayString()));
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
            spell.addDiscount(-spell.getNoDiscountCost());
            return super.castSpell(worldIn, entity, handIn, invalidMessage, spell);
        }
    }
}
