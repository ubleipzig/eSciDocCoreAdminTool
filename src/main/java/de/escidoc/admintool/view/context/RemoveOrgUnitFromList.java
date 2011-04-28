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
package de.escidoc.admintool.view.context;

import java.util.Set;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ListSelect;

final class RemoveOrgUnitFromList implements ClickListener {

    private final ListSelect orgUnitList;

    RemoveOrgUnitFromList(final ListSelect orgUnitList) {
        this.orgUnitList = orgUnitList;
    }

    private static final long serialVersionUID = 5568510941842372569L;

    @Override
    public void buttonClick(final ClickEvent event) {
        final Object o = orgUnitList.getValue();

        if (o instanceof Set) {
            final Set set = (Set) o;
            for (final Object ob : set) {
                orgUnitList.removeItem(ob);
            }
        }
        else if (o != null) {
            orgUnitList.removeItem(o);
        }
    }
}