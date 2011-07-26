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

import java.util.Arrays;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.ComboBox;

import de.escidoc.admintool.view.ViewConstants;

public class OrgUnitViewComponentFactory {

    private OrgUnitViewComponentFactory() {
        // Utility class
    }

    public static ComboBox createMetadataComboBox(final ValueChangeListener listener) {
        final ComboBox metadataComboBox =
            new ComboBox(ViewConstants.METADATA_LABEL, Arrays.asList(new String[] { ViewConstants.PUB_MAN_METADATA,
                ViewConstants.RAW_XML, ViewConstants.FREE_FORM }));
        metadataComboBox.setNewItemsAllowed(false);
        metadataComboBox.setWidth(150, Sizeable.UNITS_PIXELS);
        metadataComboBox.addListener(listener);
        metadataComboBox.setImmediate(true);
        return metadataComboBox;
    }

}
