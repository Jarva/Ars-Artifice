package com.github.jarva.arsartifice.datagen;

import com.github.jarva.arsartifice.block.ArtificersWorkbenchBlock;
import com.github.jarva.arsartifice.registry.AddonBlockRegistry;
import com.hollingsworth.arsnouveau.common.block.ThreePartBlock;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

public class DefaultLootProvider extends LootTableProvider {
    public DefaultLootProvider(PackOutput packOutput) {
        super(packOutput, new HashSet<>(), List.of(new LootTableProvider.SubProviderEntry(BlockLootTableProvider::new, LootContextParamSets.BLOCK)));
    }

    public static class BlockLootTableProvider extends BlockLootSubProvider {
        public List<Block> list = new ArrayList<>();
        protected BlockLootTableProvider() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), new HashMap<>());
        }

        @Override
        public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> p_249322_) {
            this.generate();
            Set<ResourceLocation> set = new HashSet<>();

            for (Block block : list) {
                if (block.isEnabled(this.enabledFeatures)) {
                    ResourceLocation resourcelocation = block.getLootTable();
                    if (resourcelocation != BuiltInLootTables.EMPTY && set.add(resourcelocation)) {
                        LootTable.Builder loottable$builder = this.map.remove(resourcelocation);
                        if (loottable$builder == null) {
                            continue;
                        }

                        p_249322_.accept(resourcelocation, loottable$builder);
                    }
                }
            }
        }

        @Override
        protected void generate() {
            registerBedCondition(AddonBlockRegistry.ARTIFICER_TABLE_BLOCK, ArtificersWorkbenchBlock.PART, ThreePartBlock.HEAD);
        }

        protected <T extends Comparable<T> & StringRepresentable> void registerBedCondition(Block block, Property<T> prop, T isValue) {
            this.list.add(block);
            this.add(block, LootTable.lootTable().withPool((LootPool.Builder)this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(prop, isValue)))))));
        }
    }
}
