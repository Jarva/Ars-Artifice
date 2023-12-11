package com.github.jarva.arsartifice.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.IWrappedCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.PlayerCaster;
import com.hollingsworth.arsnouveau.common.spell.augment.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Set;

public abstract class AbstractArtificeMethod extends AbstractSpellPart {
        public AbstractArtificeMethod(ResourceLocation tag, String description) {
        super(tag, description);
    }

    public Integer getTypeIndex() {
        return 1;
    }

    @Override
    public boolean shouldShowInSpellBook() {
        return false;
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentSensitive.INSTANCE);
    }

    public SpellStats getSpellStats(ISpellCaster caster, LivingEntity entity, Spell spell) {
        IWrappedCaster wrapped = entity instanceof Player player ? new PlayerCaster(player) : new LivingCaster(entity);
        SpellContext context = new SpellContext(entity.level(), caster.modifySpellBeforeCasting(entity.level(), entity, InteractionHand.MAIN_HAND, spell), entity, wrapped);
        SpellStats stats = new SpellStats.Builder().setAugments(spell.getAugments(0, entity))
                .addItemsFromEntity(entity)
                .build(this, null, entity.level(), entity, context);
        return stats;
    }

    public Component format(double value, String descriptor) {
        NumberFormat nf = DecimalFormat.getNumberInstance();
        return Component.translatable("ars_artifice.tooltip." + descriptor, nf.format(value));
    }

    abstract public Component getMeta(HashMap<AbstractAugment, Integer> augments);

    public double getAmpMultiplier(HashMap<AbstractAugment, Integer> augments) {
        return getAmpMultiplier(augments.getOrDefault(AugmentAmplify.INSTANCE, 0), augments.getOrDefault(AugmentDampen.INSTANCE, 0));
    }

    public double getAmpMultiplier(double amplify, double dampen) {
        return amplify - dampen;
    }

    public double getDurationMultiplier(HashMap<AbstractAugment, Integer> augments) {
        return getDurationMultiplier(augments.getOrDefault(AugmentExtendTime.INSTANCE, 0), augments.getOrDefault(AugmentDurationDown.INSTANCE, 0));
    }

    public double getDurationMultiplier(double extend, double down) {
        return extend - down;
    }
}
