/**
 * 
 */
package de.escidoc.admintool.view.validator;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ListSelect;

/**
 * @author ASP
 * 
 */
public class EmptyFieldValidator {

    /**
     * A simple validator to test, if the field is filled.
     * 
     * @param field
     *            The field to test.
     * @param message
     *            The message that should be shown (as a tooltip) if the result is bad.
     * @return true if the field is filled, otherwise false.
     */
    public static synchronized boolean isValid(final AbstractField field, final String message) {
        if (!(field.getValue() != null && ((String) field.getValue()).trim().length() > 0)) {
            field.setComponentError(null);
            field.setComponentError(new UserError(message));
            return false;
        }
        field.setComponentError(null);
        return true;
    }

    /**
     * A simple validator to test, if the field is filled.
     * 
     * @param list
     *            The list to test.
     * @param message
     *            The message that should be shown (as a tooltip) if the result is bad.
     * @return true if the field is filled, otherwise false.
     */
    public static synchronized boolean isValid(final ListSelect list, final String message) {
        if (list.getItemIds() != null && list.getItemIds().size() > 0) {
            list.setComponentError(null);
            return true;
        }

        list.setComponentError(null);
        list.setComponentError(new UserError(message));
        return false;
    }

}
