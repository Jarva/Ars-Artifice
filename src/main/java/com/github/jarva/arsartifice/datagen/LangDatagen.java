package com.github.jarva.arsartifice.datagen;

import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.function.Supplier;

public class LangDatagen extends LanguageProvider {

    public LangDatagen(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations() {
        ArsNouveauAPI api = ArsNouveauAPI.getInstance();
        for (Supplier<Glyph> supplier : api.getGlyphItemMap().values()) {
            Glyph glyph = supplier.get();
            if (!glyph.spellPart.getRegistryName().getNamespace().equals(Setup.root)) continue;
            this.add(Setup.root + ".glyph_desc." + glyph.spellPart.getRegistryName().getPath(), glyph.spellPart.getBookDescription());
            this.add(Setup.root + ".glyph_name." + glyph.spellPart.getRegistryName().getPath(), glyph.spellPart.getName());
        }

        this.add("item.ars_artifice.spell_storing_ring", "Ring of Spell Storing");
        this.add("item.ars_artifice.spell_storing_amulet", "Amulet of Spell Storing");
        this.add("item.ars_artifice.spell_storing_belt", "Belt of Spell Storing");
        this.add("item.ars_artifice.spell_gem_t3", "Spell Gem");
        this.add("item.ars_artifice.spell_gem_t2", "Spell Gem");
        this.add("item.ars_artifice.spell_gem_t1", "Spell Gem");

        this.add("block.ars_artifice.artificers_workbench", "Artificer's Workbench");

        this.add("ars_artifice.spell_gem.invalid", "There are problems with this spell.");
    }
}
