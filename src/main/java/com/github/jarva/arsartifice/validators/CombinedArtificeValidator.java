package com.github.jarva.arsartifice.validators;

import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.ISpellValidator;
import com.hollingsworth.arsnouveau.api.spell.SpellValidationError;
import com.hollingsworth.arsnouveau.common.spell.validation.*;

import java.util.LinkedList;
import java.util.List;

public class CombinedArtificeValidator implements ISpellValidator {
    private static final ISpellValidator ARTIFICE_METHOD = new ArtificeMethodSpellValidator();
    private static final ISpellValidator GLYPH_OCCURRENCES_POLICY = new GlyphOccurrencesPolicyValidator();
    private static final ISpellValidator AUGMENT_COMPATIBILITY = new AugmentCompatibilityValidator();
    private static final ISpellValidator INVALID_COMBINATION_POLICY = new InvalidCombinationValidator();

    private final ISpellValidator combinedValidator;
    public CombinedArtificeValidator() {
        List<ISpellValidator> validators = new LinkedList<>();
        validators.add(ARTIFICE_METHOD);
        validators.add(GLYPH_OCCURRENCES_POLICY);
        validators.add(AUGMENT_COMPATIBILITY);
        validators.add(INVALID_COMBINATION_POLICY);

        this.combinedValidator = new CombinedSpellValidator(validators);
    }

    @Override
    public List<SpellValidationError> validate(List<AbstractSpellPart> list) {
        return this.combinedValidator.validate(list);
    }
}
