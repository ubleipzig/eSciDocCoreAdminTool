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
 * 
 * Copyright 2015 Leipzig University Library
 * 
 * This code is the result of the project
 * "Die Bibliothek der Milliarden Woerter". This project is funded by
 * the European Social Fund. "Die Bibliothek der Milliarden Woerter" is
 * a cooperation project between the Leipzig University Library, the
 * Natural Language Processing Group at the Institute of Computer
 * Science at Leipzig University, and the Image and Signal Processing
 * Group at the Institute of Computer Science at Leipzig University.
 * 
 * All rights reserved.  Use is subject to license terms.
 * 
 * @author Uwe Kretschmer <u.kretschmer@denk-selbst.de>
 * 
 */
package de.uni_leipzig.ubl.admintool.view.role;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.core.resources.Resource;

@SuppressWarnings("serial")
final class ResourceSelectionListener implements ValueChangeListener {

    private final RoleView roleView;

    ResourceSelectionListener(final RoleView roleView) {
        this.roleView = roleView;
    }

    @Override
    public void valueChange(final ValueChangeEvent event) {
        if (isSelected(event)) {
            roleView.searchBox.setReadOnly(false);
            roleView.searchBox.setValue(((Resource) event.getProperty().getValue()).getXLinkTitle());
            roleView.searchBox.setReadOnly(true);
        }
        else {
            roleView.searchBox.setReadOnly(false);
            roleView.searchBox.setValue(AppConstants.EMPTY_STRING);
            roleView.searchBox.setReadOnly(true);
        }
    }

    private boolean isSelected(final ValueChangeEvent event) {
        return event.getProperty() != null && event.getProperty().getValue() != null
            && event.getProperty().getValue() instanceof Resource;
    }
}