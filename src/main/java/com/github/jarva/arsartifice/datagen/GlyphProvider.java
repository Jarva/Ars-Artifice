package com.github.jarva.arsartifice.datagen;

import com.github.jarva.arsartifice.glyphs.AnguishArtificeMethod;
import com.github.jarva.arsartifice.glyphs.FallingArtificeMethod;
import com.github.jarva.arsartifice.glyphs.IntervalArtificeMethod;
import com.github.jarva.arsartifice.glyphs.LandingArtificeMethod;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.datagen.GlyphRecipeProvider;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import com.hollingsworth.arsnouveau.setup.ItemsRegistry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

import java.io.IOException;
import java.nio.file.Path;

import static com.hollingsworth.arsnouveau.api.RegistryHelper.getRegistryName;

public class GlyphProvider extends GlyphRecipeProvider {

    public GlyphProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void run(CachedOutput cache) throws IOException {
        addEntries();
        Path output = this.generator.getOutputFolder();
        for (GlyphRecipe recipe : recipes) {
            Path path = getScribeGlyphPath(output, recipe.output.getItem());
            DataProvider.saveStable(cache, recipe.asRecipe(), path);
        }
    }

    public void addEntries() {
        add(get(AnguishArtificeMethod.INSTANCE).withItem(Items.SHIELD).withIngredient(getPotionIngredient(ModPotions.DEFENCE_POTION.get())));
        add(get(FallingArtificeMethod.INSTANCE).withItem(Items.FEATHER).withIngredient(getPotionIngredient(Potions.SLOW_FALLING)));
        add(get(IntervalArtificeMethod.INSTANCE).withItem(Items.CLOCK).withItem(ItemsRegistry.SOURCE_GEM));
        add(get(LandingArtificeMethod.INSTANCE).withItem(Items.DIRT).withItem(Items.IRON_BOOTS));
    }

    public Ingredient getPotionIngredient(Potion potion) {
        return Ingredient.of(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), potion));
    }

    protected static Path getScribeGlyphPath(Path pathIn, Item glyph) {
        return pathIn.resolve("data/" + Setup.root + "/recipes/" + getRegistryName(glyph).getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Example Glyph Recipes";
    }
}
