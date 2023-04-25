package com.github.jarva.arsartifice.datagen;

import com.github.jarva.arsartifice.registry.AddonBlockRegistry;
import com.google.common.collect.ImmutableList;
import com.hollingsworth.arsnouveau.common.datagen.DefaultTableProvider;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DefaultLootProvider extends DefaultTableProvider {
    public DefaultLootProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    public static class BlockLootTableProvider extends BlockLootTable {
        @Override
        protected void addTables() {
            registerDropSelf(AddonBlockRegistry.ARTIFICER_TABLE_BLOCK);
        }
    }

    private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = ImmutableList.of(
            Pair.of(BlockLootTableProvider::new, LootContextParamSets.BLOCK)
    );

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return tables;
    }
}
