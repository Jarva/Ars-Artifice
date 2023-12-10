package com.github.jarva.arsartifice.datagen;

import com.github.jarva.arsartifice.registry.ModRegistry;
import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;
import com.hollingsworth.arsnouveau.common.datagen.ImbuementRecipeProvider;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.nio.file.Path;

public class ImbuementProvider extends ImbuementRecipeProvider {

    public ImbuementProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void collectJsons(CachedOutput cache) {
        addEntries();
        for (ImbuementRecipe g : recipes) {
            Path path = getRecipePath(output, g.getId().getPath());
            saveStable(cache, g.asRecipe(), path);
        }
    }

    public void addEntries() {
        recipes.add(new ImbuementRecipe("spell_gem_upgrade_t1", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ModRegistry.SPELL_GEM_1.get()), 1000).withPedestalItem(ItemsRegistry.SPELL_PARCHMENT));
    }

    protected Path getRecipePath(Path pathIn, String str) {
        return pathIn.resolve("data/" + Setup.root + "/recipes/" + str + ".json");
    }

    @Override
    public String getName() {
        return "Example Imbuement";
    }

}
