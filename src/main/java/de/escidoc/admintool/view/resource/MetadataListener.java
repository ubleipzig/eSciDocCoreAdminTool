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

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ViewConstants;

public final class MetadataListener implements ValueChangeListener {

    private static final long serialVersionUID = -189510362510619884L;

    private final FormLayout formLayout;

    private final Window mainWindow;

    public MetadataListener(final Window mainWindow, final FormLayout formLayout) {
        this.mainWindow = mainWindow;
        this.formLayout = formLayout;
    }

    @Override
    public void valueChange(final ValueChangeEvent event) {
        final Object value = event.getProperty().getValue();
        if (value instanceof String) {
            final String metaDataType = (String) value;

            if (metaDataType.equals(ViewConstants.PUB_MAN_METADATA)) {
                showPubManMetadataFields();
            }
            else if (metaDataType.equals(ViewConstants.RAW_XML)) {
                showRawXmlTextArea();
            }
            else if (metaDataType.equals(ViewConstants.FREE_FORM)) {
                showFreeForm();
            }
        }
    }

    private void showFreeForm() {
        mainWindow.addWindow(new FreeFormWindow(mainWindow));
    }

    private void showPubManMetadataFields() {
        mainWindow.addWindow(new PubmanWindow(mainWindow));

    }

    private void showRawXmlTextArea() {
        mainWindow.addWindow(new RawXmlWindow(mainWindow));
    }

}