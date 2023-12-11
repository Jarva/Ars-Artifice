package com.github.jarva.arsartifice.glyphs;

import com.github.jarva.arsartifice.ArsArtifice;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;

import static com.github.jarva.arsartifice.ArsArtifice.prefix;

@Mod.EventBusSubscriber(modid = ArsArtifice.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FallingArtificeMethod extends AbstractArtificeMethod implements TickableArtificeMethod {
    public static FallingArtificeMethod INSTANCE = new FallingArtificeMethod(prefix("glyph_falling"), "Falling");
    public FallingArtificeMethod(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public String getBookDescription() {
        return "Falling triggers after falling. Amplify increases the amount of blocks to fall before triggering. Dampen reduces the amount of blocks to fall before triggering.";
    }

    @Override
    public int getDefaultManaCost() {
        return 0;
    }

    public ForgeConfigSpec.IntValue DISTANCE;
    public ForgeConfigSpec.IntValue STEP;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        DISTANCE = builder.comment("Default fall distance to trigger from, in blocks").defineInRange("distance", 5, 1, Integer.MAX_VALUE);
        STEP = builder.comment("Distance change step, in blocks").defineInRange("step", 1, 1, Integer.MAX_VALUE);
    }

    public boolean triggered = false;

    public double getFallingThreshold(double amplifier) {
        return DISTANCE.get() + (amplifier * STEP.get());
    }

    @Override
    public void tick(LivingEntity entity, ItemStack stack, ISpellCaster caster, SpellStats stats, Spell spell) {
        double distance = getFallingThreshold(stats.getAmpMultiplier());
        if (!triggered && entity.fallDistance >= distance) {
            InteractionResultHolder<ItemStack> result = caster.castSpell(entity.level(), entity, InteractionHand.MAIN_HAND, Component.translatable("ars_nouveau.spell.validation.crafting.invalid"), spell);
            if (result.getResult() == InteractionResult.SUCCESS) {
                triggered = true;
            }
        }
        if (triggered && entity.fallDistance == 0) {
            triggered = false;
        }
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE);
    }

    @Override
    public Component getMeta(HashMap<AbstractAugment, Integer> augments) {
        double amp = getAmpMultiplier(augments);
        return format(getFallingThreshold(amp), "blocks");
    }
}
