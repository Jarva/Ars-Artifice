package com.github.jarva.arsartifice.glyphs;

import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.IWrappedCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.LivingCaster;
import com.hollingsworth.arsnouveau.api.spell.wrapped_caster.PlayerCaster;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentSensitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

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
}
