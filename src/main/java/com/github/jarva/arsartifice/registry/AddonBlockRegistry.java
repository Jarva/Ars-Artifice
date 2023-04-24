package com.github.jarva.arsartifice.registry;

import com.github.jarva.arsartifice.ArsArtifice;
import com.github.jarva.arsartifice.block.ArtificersWorkbenchBlock;
import com.hollingsworth.arsnouveau.ArsNouveau;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static com.hollingsworth.arsnouveau.setup.ItemsRegistry.defaultItemProperties;

public class AddonBlockRegistry {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArsNouveau.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ArsNouveau.MODID);

    static final String BlockRegistryKey = "minecraft:block";
    static final String BlockEntityRegistryKey = "minecraft:block_entity_type";
    static final String prepend = ArsArtifice.MODID + ":";

    @ObjectHolder(value = prepend + "artificers_workbench", registryName = BlockRegistryKey)
    public static ArtificersWorkbenchBlock ARTIFICER_TABLE_BLOCK;

    public static void onBlocksRegistry(final IForgeRegistry<Block> registry) {
        registry.register("artificers_workbench", new ArtificersWorkbenchBlock());
    }

    public static void onBlockItemsRegistry(IForgeRegistry<Item> registry) {
        registry.register("artificers_workbench", new BlockItem(AddonBlockRegistry.ARTIFICER_TABLE_BLOCK, defaultItemProperties()));
    }
}
