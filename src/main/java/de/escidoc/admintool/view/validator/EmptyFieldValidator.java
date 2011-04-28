/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
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
