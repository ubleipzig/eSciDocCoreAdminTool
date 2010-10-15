package de.escidoc.admintool.view.validator;

import com.vaadin.data.validator.AbstractValidator;

@SuppressWarnings("serial")
public class EmptyStringValidator extends AbstractValidator {

    private static final boolean ALLOW_NULL = false;

    public EmptyStringValidator(final String errorMessage) {
        super(errorMessage);
    }

    public boolean isValid(final Object value) {
        if (value == null) {
            return ALLOW_NULL;
        }
        final String s = value.toString();
        return s.trim().length() != 0;
    }

}
