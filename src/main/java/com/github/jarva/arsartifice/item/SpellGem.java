package com.github.jarva.arsartifice.item;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.api.item.ICasterTool;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpellGem extends Item implements ICasterTool {

    public SpellGem(int durability) {
        super(new Item.Properties().durability(durability).tab(ArsNouveau.itemGroup));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        player.startUsingItem(usedHand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        int heldDuration = this.getUseDuration(stack) - timeCharged;

        if (heldDuration < 25) return;

        ISpellCaster caster = getSpellCaster(stack);
        Spell spell = caster.getSpell();
        InteractionHand handIn = livingEntity.getUsedItemHand();
        InteractionResultHolder<ItemStack> holder = caster.castSpell(level, livingEntity, handIn, Component.translatable("ars_artifice.spell_gem.invalid"), spell);
        if (holder.getResult() == InteractionResult.CONSUME) {
            stack.hurtAndBreak(spell.getDiscountedCost(), livingEntity, (e) -> {
                e.broadcastBreakEvent(handIn);
            });
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.literal("Durability: " + (stack.getMaxDamage()-stack.getDamageValue()) + " / " + stack.getMaxDamage()).withStyle(ChatFormatting.GRAY));
        ISpellCaster caster = getSpellCaster(stack);
        Spell method = caster.getSpell();
        if (method.isValid()) {
            tooltipComponents.add(Component.literal(method.getDisplayString()));
        }

        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }
}
