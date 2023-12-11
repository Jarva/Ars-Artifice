package com.github.jarva.arsartifice.glyphs;

import com.github.jarva.arsartifice.ArsArtifice;
import com.github.jarva.arsartifice.item.ArtificerCurio;
import com.hollingsworth.arsnouveau.api.spell.AbstractAugment;
import com.hollingsworth.arsnouveau.api.spell.ISpellCaster;
import com.hollingsworth.arsnouveau.api.spell.Spell;
import com.hollingsworth.arsnouveau.api.spell.SpellStats;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentAmplify;
import com.hollingsworth.arsnouveau.common.spell.augment.AugmentDampen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import static com.github.jarva.arsartifice.ArsArtifice.prefix;

@Mod.EventBusSubscriber(modid = ArsArtifice.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnguishArtificeMethod extends AbstractArtificeMethod {
    public static AnguishArtificeMethod INSTANCE = new AnguishArtificeMethod(prefix("glyph_anguish"), "Anguish");
    public AnguishArtificeMethod(ResourceLocation tag, String description) {
        super(tag, description);
    }

    @Override
    public String getBookDescription() {
        return "Anguish triggers when you take damage. Amplify increases the required damage to trigger. Dampen reduces the required damage to trigger.";
    }

    @Override
    public int getDefaultManaCost() {
        return 0;
    }

    public ForgeConfigSpec.IntValue THRESHOLD;
    public ForgeConfigSpec.IntValue STEP;

    @Override
    public void buildConfig(ForgeConfigSpec.Builder builder) {
        super.buildConfig(builder);
        THRESHOLD = builder.comment("Damage threshold, in half-hearts").defineInRange("threshold", 6, 1, Integer.MAX_VALUE);
        STEP = builder.comment("Damage threshold step, in half-hearts").defineInRange("step", 2, 1, Integer.MAX_VALUE);
    }

    @Override
    protected @NotNull Set<AbstractAugment> getCompatibleAugments() {
        return augmentSetOf(AugmentAmplify.INSTANCE, AugmentDampen.INSTANCE);
    }

    public double getDamageThreshold(double amplifier) {
        return THRESHOLD.get() + (amplifier * STEP.get());
    }

    @SubscribeEvent
    public static void onEntityDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        Optional<ICuriosItemHandler> curios = CuriosApi.getCuriosInventory(entity).resolve();
        curios.ifPresent(curiosHandler -> {
            IItemHandlerModifiable handler = curiosHandler.getEquippedCurios();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack item = handler.getStackInSlot(i);
                if (item.getItem() instanceof ArtificerCurio) {
                    ISpellCaster caster = new ArtificerCurio.ArtificerCaster(item);
                    Spell artifice = caster.getSpell(0);
                    if (artifice.isEmpty()) continue;
                    if (artifice.recipe.get(0) instanceof AbstractArtificeMethod method && method instanceof AnguishArtificeMethod retaliateMethod) {
                        SpellStats stats = retaliateMethod.getSpellStats(caster, entity, artifice);
                        if (retaliateMethod.getDamageThreshold(stats.getAmpMultiplier()) <= event.getAmount()) {
                            caster.castSpell(entity.level(), entity, InteractionHand.MAIN_HAND, Component.translatable("ars_nouveau.spell.validation.crafting.invalid"), caster.getSpell(1));
                        }
                    }
                }
            }
        });
    }

    @Override
    public Component getMeta(HashMap<AbstractAugment, Integer> augments) {
        double amp = getAmpMultiplier(augments);
        return format(getDamageThreshold(amp), "damage");
    }
}
