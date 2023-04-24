package com.github.jarva.arsartifice.recipes;

import com.github.jarva.arsartifice.registry.ModRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.jarva.arsartifice.ArsArtifice.prefix;
import static com.github.jarva.arsartifice.registry.ModRegistry.NO_DAMAGE_RECIPE_ID;

public class NoDamageEnchantmentRecipe extends EnchantingApparatusRecipe {
    public NoDamageEnchantmentRecipe(ResourceLocation id, Ingredient reagent, ItemStack result, List<Ingredient> pedestalItems, int cost) {
        super(id, pedestalItems, reagent, result, cost, false);
    }

    @Override
    public boolean doesReagentMatch(ItemStack stack) {
        return super.doesReagentMatch(stack) && stack.getDamageValue() == 0;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRegistry.NO_DAMAGE_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRegistry.NO_DAMAGE_TYPE.get();
    }

    @Override
    public JsonElement asRecipe() {
        JsonObject jsonobject = (JsonObject) super.asRecipe();
        jsonobject.addProperty("type", prefix(NO_DAMAGE_RECIPE_ID).toString());
        return jsonobject;
    }

    public static class Serializer implements RecipeSerializer<NoDamageEnchantmentRecipe> {

        private final EnchantingApparatusRecipe.Serializer serializer;

        public Serializer() {
            serializer = new EnchantingApparatusRecipe.Serializer();
        }

        @Override
        public NoDamageEnchantmentRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            EnchantingApparatusRecipe recipe = serializer.fromJson(recipeId, serializedRecipe);
            return new NoDamageEnchantmentRecipe(recipeId, recipe.reagent, recipe.result, recipe.pedestalItems, recipe.sourceCost);
        }

        @Override
        public @Nullable NoDamageEnchantmentRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            EnchantingApparatusRecipe recipe = serializer.fromNetwork(recipeId, buffer);
            return new NoDamageEnchantmentRecipe(recipeId, recipe.reagent, recipe.result, recipe.pedestalItems, recipe.sourceCost);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, NoDamageEnchantmentRecipe recipe) {
            serializer.toNetwork(buffer, recipe);
        }
    }
}
