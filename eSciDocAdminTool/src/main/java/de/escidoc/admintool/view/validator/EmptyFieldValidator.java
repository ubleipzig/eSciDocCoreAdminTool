/**
 * 
 */
package de.escidoc.admintool.view.validator;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;

/**
 * @author ASP
 * 
 */
public class EmptyFieldValidator {

    /**
     * A simple validator to test, if the field is filled.
     * 
     * @param textField
     *            The textfield to test.
     * @param message
     *            The message that should be shown (as a tooltip) if the result
     *            is bad.
     * @return true if the field is filled, otherwise false.
     */
    public static synchronized boolean isValid(
        final TextField textField, final String message) {
        if (!(textField.getValue() != null && ((String) textField.getValue())
            .trim().length() > 0)) {
            textField.setComponentError(null);
            textField.setComponentError(new UserError(message));
            return false;
        }
        textField.setComponentError(null);
        return true;
    }

    /**
     * A simple validator to test, if the field is filled.
     * 
     * @param list
     *            The list to test.
     * @param message
     *            The message that should be shown (as a tooltip) if the result
     *            is bad.
     * @return true if the field is filled, otherwise false.
     */
    public static synchronized boolean isValid(
        final ListSelect list, final String message) {
        if (list.getItemIds() != null && list.getItemIds().size() > 0) {
            list.setComponentError(null);
            return true;
        }

        list.setComponentError(null);
        list.setComponentError(new UserError(message));
        return false;
    }

}
