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
package de.escidoc.admintool.view.resource;

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;

public class ParentOrgUnitBinder implements FieldsBinder {

    private final OrgUnitSpecificView orgUnitSpecificView;

    private Component toBeBind;

    private final Property parentProperty;

    public ParentOrgUnitBinder(final OrgUnitSpecificView orgUnitSpecificView, final Property parentProperty) {
        this.orgUnitSpecificView = orgUnitSpecificView;
        this.parentProperty = parentProperty;
    }

    @Override
    public void bindFields() {
        bind(orgUnitSpecificView.parentsField).with(parentProperty);
    }

    private ParentOrgUnitBinder bind(final Component nameField) {
        toBeBind = nameField;
        return this;
    }

    private void with(final Property itemProperty) {
        if (toBeBind instanceof Label) {
            ((Label) toBeBind).setPropertyDataSource(itemProperty);
        }
        else if (toBeBind instanceof Field) {
            ((Field) toBeBind).setPropertyDataSource(itemProperty);
        }
    }
}