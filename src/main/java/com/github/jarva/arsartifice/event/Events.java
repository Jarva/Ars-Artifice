package com.github.jarva.arsartifice.event;

import com.github.jarva.arsartifice.ArsArtifice;
import com.github.jarva.arsartifice.registry.AddonBlockRegistry;
import com.github.jarva.arsartifice.registry.ModRegistry;
import com.hollingsworth.arsnouveau.setup.registry.CreativeTabRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArsArtifice.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Events {
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeTabRegistry.BLOCKS.getKey()) {
            event.accept(ModRegistry.SPELL_GEM_1);
            event.accept(ModRegistry.SPELL_GEM_2);
            event.accept(ModRegistry.SPELL_GEM_3);

            event.accept(ModRegistry.SPELL_STORING_RING);
            event.accept(ModRegistry.SPELL_STORING_AMULET);
            event.accept(ModRegistry.SPELL_STORING_BELT);

            event.accept(AddonBlockRegistry.ARTIFICER_TABLE_BLOCK);
        }
    }
}
