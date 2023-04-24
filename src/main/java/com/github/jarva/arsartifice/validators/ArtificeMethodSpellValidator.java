package com.github.jarva.arsartifice.validators;

import com.github.jarva.arsartifice.glyphs.AbstractArtificeMethod;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import com.hollingsworth.arsnouveau.api.spell.ISpellValidator;
import com.hollingsworth.arsnouveau.api.spell.SpellValidationError;
import com.hollingsworth.arsnouveau.common.spell.validation.AbstractSpellValidator;
import com.hollingsworth.arsnouveau.common.spell.validation.BaseSpellValidationError;
import com.hollingsworth.arsnouveau.common.spell.validation.CombinedSpellValidator;
import com.hollingsworth.arsnouveau.common.spell.validation.ScanningSpellValidator;

import java.util.LinkedList;
import java.util.List;

public class ArtificeMethodSpellValidator implements ISpellValidator {
    private static final ISpellValidator MAX_ONE = new MaxOneSpellValidator();
    private static final ISpellValidator REQUIRE_START = new RequireStartSpellValidator();

    private final ISpellValidator combinedValidator;

    public ArtificeMethodSpellValidator() {
        List<ISpellValidator> validators = new LinkedList<>();
        validators.add(MAX_ONE);
        validators.add(REQUIRE_START);

        this.combinedValidator = new CombinedSpellValidator(validators);
    }

    public List<SpellValidationError> validate(List<AbstractSpellPart> spellRecipe) {
        return combinedValidator.validate(spellRecipe);
    }

    public static class RequireStartSpellValidator extends AbstractSpellValidator {
        @Override
        protected void validateImpl(List<AbstractSpellPart> recipe, List<SpellValidationError> errors) {
            if (recipe.size() > 0 && !(recipe.get(0) instanceof AbstractArtificeMethod)) {
                errors.add(new RequireStartSpellValidationError());
            }
        }

        private static class RequireStartSpellValidationError extends BaseSpellValidationError {
            public RequireStartSpellValidationError() {
                super(0, (AbstractSpellPart)null, "starting_artifice_method");
            }
        }
    }

    public static class MaxOneSpellValidator extends ScanningSpellValidator<MaxOneSpellValidator.OneCastContext> {
        public MaxOneSpellValidator() {
        }

        protected OneCastContext initContext() {
            return new MaxOneSpellValidator.OneCastContext();
        }

        protected void digestSpellPart(OneCastContext context, int position, AbstractSpellPart spellPart, List<SpellValidationError> validationErrors) {
            if (spellPart instanceof AbstractArtificeMethod) {
                ++context.count;
                if (context.count > 1) {
                    validationErrors.add(new OneCastMethodSpellValidationError(position, (AbstractArtificeMethod) spellPart));
                }
            }
        }

        protected void finish(OneCastContext context, List<SpellValidationError> validationErrors) {}

        public static class OneCastContext {
            int count = 0;

            public OneCastContext() {
            }
        }

        private static class OneCastMethodSpellValidationError extends BaseSpellValidationError {
            public OneCastMethodSpellValidationError(int position, AbstractArtificeMethod method) {
                super(position, method, "max_one_artifice_method", new AbstractSpellPart[]{method});
            }
        }
    }

}