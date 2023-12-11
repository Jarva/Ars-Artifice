package com.github.jarva.arsartifice.datagen;

import com.hollingsworth.arsnouveau.api.ArsNouveauAPI;
import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.common.items.Glyph;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.function.Supplier;

public class LangDatagen extends LanguageProvider {

    public LangDatagen(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        ArsNouveauAPI api = ArsNouveauAPI.getInstance();
        for (Supplier<Glyph> supplier : GlyphRegistry.getGlyphItemMap().values()) {
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

        this.add("ars_nouveau.spell.validation.adding.starting_artifice_method", "The spell must start with an artifice method glyph.");

        this.add("ars_artifice.tooltip.trigger", "Trigger");
        this.add("ars_artifice.tooltip.spell", "Spell");
        this.add("ars_artifice.tooltip.blocks", "(%1$s blocks)");
        this.add("ars_artifice.tooltip.seconds", "(%1$s seconds)");
        this.add("ars_artifice.tooltip.damage", "(%1$s damage)");
    }
}
