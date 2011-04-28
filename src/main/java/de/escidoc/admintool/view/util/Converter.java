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
package de.escidoc.admintool.view.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

/**
 * @author ASP
 * 
 */
public final class Converter {

    private Converter() {
        // Utility classes should not have a public or default constructor.
    }

    /**
     * Converts DateTime to String.
     * 
     * @param dateTime
     *            the information to convert.
     * @return the result.
     */
    public static synchronized String dateTimeToString(final DateTime dateTime) {
        final Date date = dateTime.toDate();
        final SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        return sdf.format(date);
    }

    /**
     * Vaadin uses an Object to return either a single value
     * 
     * @param o
     *            the return object from a tree, combobox, list, ....
     * @return a list of selected objects.
     */
    @SuppressWarnings("unchecked")
    public static synchronized List<Object> getSelected(final Object o) {
        final List<Object> selected = new ArrayList<Object>();
        if (o instanceof Set) {
            selected.addAll((Set) o); // More than one result...
        }
        else { // Just one selected.
            selected.add(o);
        }
        return selected;
    }

}
