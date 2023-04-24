package com.github.jarva.arsartifice;

import com.github.jarva.arsartifice.glyphs.FallingArtificeMethod;
import com.github.jarva.arsartifice.glyphs.IntervalArtificeMethod;
import com.github.jarva.arsartifice.glyphs.AnguishArtificeMethod;
import com.github.jarva.arsartifice.glyphs.LandingArtificeMethod;
import com.github.jarva.arsartifice.registry.ModRegistry;
import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;

import java.util.ArrayList;
import java.util.List;

public class ArsNouveauRegistry {
    public static List<AbstractSpellPart> registeredSpells = new ArrayList<>();

    public static void register() {
        registerGlyphs();
    }

    public static void postInit() {
        registerRecipeTypes();
    }

    public static void registerRecipeTypes() {
        ArsNouveauAPI api = ArsNouveauAPI.getInstance();
        api.getEnchantingRecipeTypes().add(ModRegistry.NO_DAMAGE_TYPE.get());
    }

    public static void registerGlyphs(){
        register(IntervalArtificeMethod.INSTANCE);
        register(AnguishArtificeMethod.INSTANCE);
        register(FallingArtificeMethod.INSTANCE);
        register(LandingArtificeMethod.INSTANCE);
    }

    public static void register(AbstractSpellPart spellPart){
        ArsNouveauAPI.getInstance().registerSpell(spellPart);
        registeredSpells.add(spellPart);
    }
}
