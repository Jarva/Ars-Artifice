package com.github.jarva.arsartifice.glyphs;

import com.github.jarva.arsartifice.ArsArtifice;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentExtendTime;
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
public class LandingArtificeMethod extends AbstractArtificeMethod implements TickableArtificeMethod {
    public static LandingArtificeMethod INSTANCE = new LandingArtificeMethod(prefix("glyph_landing"), "Landing");
    public LandingArtificeMethod(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public Component getBookDescLang() {
        return super.getBookDescLang();
    }

    @Override
    public String getBookDescription() {
        return "Landing triggers when you land. Amplify increases the distance you need to have fallen for it to trigger.";
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
        DISTANCE = builder.comment("Default distance fall to trigger from, in blocks").defineInRange("distance", 0, 0, Integer.MAX_VALUE);
        STEP = builder.comment("Distance increase, in blocks").defineInRange("step", 1, 1, Integer.MAX_VALUE);
    }
    public boolean triggered = false;
    public float previousDistance = 0;

    public double getDistanceThreshold(double amplifier) {
        return DISTANCE.get() + (amplifier * STEP.get());
    }

    @Override
    public void tick(LivingEntity entity, ItemStack stack, ISpellCaster caster, SpellStats stats, Spell spell) {
        double distance = getDistanceThreshold (stats.getAmpMultiplier());
        if (!triggered && entity.fallDistance == 0 && previousDistance >= distance) {
            InteractionResultHolder<ItemStack> result = caster.castSpell(entity.level(), entity, InteractionHand.MAIN_HAND, Component.translatable("ars_nouveau.spell.validation.crafting.invalid"), spell);
            if (result.getResult() == InteractionResult.SUCCESS) {
                triggered = true;
            }
        }
        if (triggered && entity.fallDistance >= 0) {
            previousDistance = entity.fallDistance;
            triggered = false;
        }
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE);
    }

    @Override
    public Component getMeta(HashMap<AbstractAugment, Integer> augments) {
        double distance = getDistanceThreshold(augments.getOrDefault(AugmentExtendTime.INSTANCE, 0));
        return format(distance, "blocks");
    }
}
