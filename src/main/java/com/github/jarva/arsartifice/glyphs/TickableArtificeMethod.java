package com.github.jarva.arsartifice.glyphs;

import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface TickableArtificeMethod {
    default void tick(LivingEntity entity, ItemStack stack, ISpellCaster caster, SpellStats stats, Spell spell) {
        this.tick();
    }

    default void tick() {
    }
}
