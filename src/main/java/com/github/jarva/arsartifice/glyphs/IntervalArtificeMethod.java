package com.github.jarva.arsartifice.glyphs;

import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDurationDown;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.github.jarva.arsartifice.ArsArtifice.prefix;

public class IntervalArtificeMethod extends AbstractArtificeMethod implements TickableArtificeMethod {
    public static IntervalArtificeMethod INSTANCE = new IntervalArtificeMethod(prefix("glyph_interval"), "Interval");
    public IntervalArtificeMethod(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public String getBookDescription() {
        return "Interval triggers on a loop. Amplify increases the duration between triggers. Dampen reduces the duration between triggers.";
    }

    @Override
    public int getDefaultManaCost() {
        return 0;
    }

    public ForgeConfigSpec.IntValue INTERVAL;
    public ForgeConfigSpec.IntValue STEP;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        INTERVAL = builder.comment("Interval, in seconds").defineInRange("interval", 10, 1, Integer.MAX_VALUE);
        STEP = builder.comment("Interval change step, in seconds").defineInRange("step", 1, 1, Integer.MAX_VALUE);
    }

    @Override
    public void tick(LivingEntity entity, ItemStack stack, ISpellCaster caster, SpellStats stats, Spell spell) {
        int duration = (int) (INTERVAL.get() + (stats.getDurationMultiplier() * STEP.get()));
        if (entity.level().getGameTime() % duration != 0) return;
        caster.castSpell(entity.level(), entity, InteractionHand.MAIN_HAND, Component.translatable("ars_nouveau.spell.validation.crafting.invalid"), spell);
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentExtendTime.INSTANCE, AugmentDurationDown.INSTANCE);
    }
}
