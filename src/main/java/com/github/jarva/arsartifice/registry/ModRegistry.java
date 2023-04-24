package com.github.jarva.arsartifice.registry;

import com.github.jarva.arsartifice.item.ArtificerCurio;
import com.github.jarva.arsartifice.item.SpellGem;
import com.github.jarva.arsartifice.recipes.NoDamageEnchantmentRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import java.util.Objects;

import static com.github.jarva.arsartifice.ArsArtifice.MODID;
import static com.github.jarva.arsartifice.ArsArtifice.prefix;

public class ModRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    public static void registerRegistries(IEventBus bus){
        AddonBlockRegistry.BLOCKS.register(bus);
        AddonBlockRegistry.BLOCK_ENTITIES.register(bus);
        RECIPES.register(bus);
        SERIALIZERS.register(bus);
        ITEMS.register(bus);
    }

    public static final RegistryObject<Item> SPELL_STORING_RING;
    public static final RegistryObject<Item> SPELL_STORING_AMULET;
    public static final RegistryObject<Item> SPELL_STORING_BELT;
    public static final RegistryObject<Item> SPELL_GEM_1;
    public static final RegistryObject<Item> SPELL_GEM_2;
    public static final RegistryObject<Item> SPELL_GEM_3;

    public static final String NO_DAMAGE_RECIPE_ID = "no_damage_enchantment";
    public static final RegistryObject<RecipeType<NoDamageEnchantmentRecipe>> NO_DAMAGE_TYPE;
    public static final RegistryObject<RecipeSerializer<NoDamageEnchantmentRecipe>> NO_DAMAGE_RECIPE;

    static {
        SPELL_STORING_RING = ITEMS.register("spell_storing_ring", ArtificerCurio::new);
        SPELL_STORING_AMULET = ITEMS.register("spell_storing_amulet", ArtificerCurio::new);
        SPELL_STORING_BELT = ITEMS.register("spell_storing_belt", ArtificerCurio::new);
        SPELL_GEM_1 = ITEMS.register("spell_gem_t1", () -> new SpellGem(250));
        SPELL_GEM_2 = ITEMS.register("spell_gem_t2", () -> new SpellGem(500));
        SPELL_GEM_3 = ITEMS.register("spell_gem_t3", () -> new SpellGem(1000));
        NO_DAMAGE_TYPE = RECIPES.register(NO_DAMAGE_RECIPE_ID, () -> RecipeType.simple(prefix(NO_DAMAGE_RECIPE_ID)));
        NO_DAMAGE_RECIPE = SERIALIZERS.register(NO_DAMAGE_RECIPE_ID, NoDamageEnchantmentRecipe.Serializer::new);
    }

    public static void registerEvents(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
            IForgeRegistry<Block> registry = Objects.requireNonNull(event.getForgeRegistry());
            AddonBlockRegistry.onBlocksRegistry(registry);
        }
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
            IForgeRegistry<Item> registry = Objects.requireNonNull(event.getForgeRegistry());
            AddonBlockRegistry.onBlockItemsRegistry(registry);
        }
    }
}
