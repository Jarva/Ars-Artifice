package com.github.jarva.arsartifice.datagen;

import com.github.jarva.arsartifice.glyphs.AnguishArtificeMethod;
import com.github.jarva.arsartifice.glyphs.FallingArtificeMethod;
import com.github.jarva.arsartifice.glyphs.IntervalArtificeMethod;
import com.github.jarva.arsartifice.glyphs.LandingArtificeMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.common.crafting.recipes.GlyphRecipe;
import com.hollingsworth.arsnouveau.common.datagen.GlyphRecipeProvider;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

import java.nio.file.Path;

import static com.hollingsworth.arsnouveau.setup.registry.RegistryHelper.getRegistryName;

public class GlyphProvider extends GlyphRecipeProvider {

    public GlyphProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    public void collectJsons(CachedOutput cache) {
        addEntries();
        for (GlyphRecipe recipe : recipes) {
            Path path = getScribeGlyphPath(output, recipe.output.getItem());
            saveStable(cache, recipe.asRecipe(), path);
        }
    }

    public void addEntries() {
        addRecipe(AnguishArtificeMethod.INSTANCE, i(Items.SHIELD), getPotionIngredient(ModPotions.DEFENCE_POTION.get()));
        addRecipe(FallingArtificeMethod.INSTANCE, i(Items.FEATHER), getPotionIngredient(Potions.SLOW_FALLING));
        addRecipe(IntervalArtificeMethod.INSTANCE, i(Items.CLOCK), i(ItemsRegistry.SOURCE_GEM));
        addRecipe(LandingArtificeMethod.INSTANCE, i(Items.DIRT), i(Items.IRON_BOOTS));
    }

    public Ingredient i(ItemLike item) {
        return Ingredient.of(item);
    }

    public void addRecipe(AbstractSpellPart part, Ingredient... items) {
        GlyphRecipe recipe = get(part);
        for (Ingredient item : items ) {
            recipe.withIngredient(item);
        }
        recipes.add(recipe);
    }

    public Ingredient getPotionIngredient(Potion potion) {
        return StrictNBTIngredient.of(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), potion));
    }

    protected static Path getScribeGlyphPath(Path pathIn, Item glyph) {
        return pathIn.resolve("data/" + Setup.root + "/recipes/" + getRegistryName(glyph).getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Example Glyph Recipes";
    }
}
