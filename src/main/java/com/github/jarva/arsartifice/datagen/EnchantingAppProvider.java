package com.github.jarva.arsartifice.datagen;

import com.github.jarva.arsartifice.recipes.NoDamageEnchantmentRecipe;
import com.github.jarva.arsartifice.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import com.hollingsworth.arsnouveau.common.datagen.ApparatusRecipeProvider;
import com.hollingsworth.arsnouveau.common.datagen.RecipeDatagen;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.nio.file.Path;
import java.util.List;

import static com.github.jarva.arsartifice.ArsArtifice.prefix;

public class EnchantingAppProvider extends ApparatusRecipeProvider {
    public EnchantingAppProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    protected static Path getRecipePath(Path pathIn, String str) {
        return pathIn.resolve("data/" + Setup.root + "/recipes/" + str + ".json");
    }

    @Override
    public void collectJsons(CachedOutput cache) {
        addEntries();
        for (EnchantingApparatusRecipe g : recipes) {
            if (g != null) {
                Path path = getRecipePath(this.output, g.getId().getPath());
                DataProvider.saveStable(cache, g.asRecipe(), path);
            }
        }
    }

    @Override
    public void addEntries() {
        this.addRecipe(this.builder().withResult(ModRegistry.SPELL_STORING_RING).withReagent(ItemsRegistry.RING_OF_POTENTIAL).withPedestalItem(3, Items.DIAMOND).withPedestalItem(4, ItemsRegistry.SOURCE_GEM).withPedestalItem(Items.OBSERVER).build());
        this.addRecipe(this.builder().withResult(ModRegistry.SPELL_STORING_AMULET).withReagent(ItemsRegistry.DULL_TRINKET).withPedestalItem(3, Items.DIAMOND).withPedestalItem(4, ItemsRegistry.SOURCE_GEM).withPedestalItem(Items.OBSERVER).build());
        this.addRecipe(this.builder().withResult(ModRegistry.SPELL_STORING_BELT).withReagent(ItemsRegistry.MUNDANE_BELT).withPedestalItem(3, Items.DIAMOND).withPedestalItem(4, ItemsRegistry.SOURCE_GEM).withPedestalItem(Items.OBSERVER).build());

        this.addRecipe(new NoDamageEnchantmentRecipe(prefix("spell_gem_upgrade_t2"), Ingredient.of(ModRegistry.SPELL_GEM_1.get()), new ItemStack(ModRegistry.SPELL_GEM_2.get()), List.of(RecipeDatagen.SOURCE_GEM_BLOCK, RecipeDatagen.SOURCE_GEM_BLOCK, RecipeDatagen.SOURCE_GEM_BLOCK), 2500));
        this.addRecipe(new NoDamageEnchantmentRecipe(prefix("spell_gem_upgrade_t3"), Ingredient.of(ModRegistry.SPELL_GEM_2.get()), new ItemStack(ModRegistry.SPELL_GEM_3.get()), List.of(RecipeDatagen.SOURCE_GEM_BLOCK, RecipeDatagen.SOURCE_GEM_BLOCK, RecipeDatagen.SOURCE_GEM_BLOCK), 5000));
    }
}
