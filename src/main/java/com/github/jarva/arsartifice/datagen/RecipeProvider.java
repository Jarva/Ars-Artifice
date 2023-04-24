package com.github.jarva.arsartifice.datagen;

import com.github.jarva.arsartifice.registry.AddonBlockRegistry;
import com.hollingsworth.arsnouveau.common.datagen.RecipeDatagen;
import com.hollingsworth.arsnouveau.setup.BlockRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class RecipeProvider extends RecipeDatagen {
    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        this.consumer = consumer;
        this.shapedBuilder(AddonBlockRegistry.ARTIFICER_TABLE_BLOCK)
                .pattern("xyx")
                .pattern("z z")
                .pattern("w w")
                .define('y', BlockRegistry.SCRIBES_BLOCK)
                .define('x', BlockRegistry.ARCHWOOD_SLABS)
                .define('w', Items.IRON_INGOT)
                .define('z', Items.GOLD_NUGGET)
                .save(consumer);
    }
}
