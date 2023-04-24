package com.github.jarva.arsartifice;

import com.github.jarva.arsartifice.registry.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArsArtifice.MODID)
public class ArsArtifice {
    public static final String MODID = "ars_artifice";
    public static final Logger LOGGER = LogManager.getLogger();

    public ArsArtifice() {
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(ModRegistry::registerEvents);
        ModRegistry.registerRegistries(modbus);
        ArsNouveauRegistry.register();
        modbus.addListener(this::common);
        modbus.addListener(this::client);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation prefix(String path){
        return new ResourceLocation(MODID, path);
    }

    private void common(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            ArsNouveauRegistry.postInit();
        });
    }

    private void client(final FMLClientSetupEvent event) {

    }
}
