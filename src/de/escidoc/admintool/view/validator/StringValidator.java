package de.escidoc.admintool.view.validator;

import com.vaadin.data.validator.AbstractValidator;

@SuppressWarnings("serial")
public class StringValidator extends AbstractValidator {

    private final boolean allowNull = false;

    public StringValidator(final String errorMessage) {
        super(errorMessage);
    }

    public boolean isValid(final Object value) {
        if (value == null) {
            return allowNull;
        }
        final String s = value.toString();
        if (s == null) {
            return allowNull;
        }

        return s.trim().length() != 0;
    }
}