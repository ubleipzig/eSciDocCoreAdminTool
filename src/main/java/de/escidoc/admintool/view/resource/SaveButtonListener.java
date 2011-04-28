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

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.vaadin.data.Item;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.OrgUnitBuilder;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class SaveButtonListener implements ClickListener {

    private static final long serialVersionUID = 4095932748716005999L;

    private final Collection<Field> allFields;

    private final Window mainWindow;

    private final ResourceService resourceService;

    private Item item;

    private OrganizationalUnit toBeUpdated;

    private final Map<String, Field> fieldByName;

    public SaveButtonListener(final Collection<Field> allFields, final Map<String, Field> fieldByName,
        final Window mainWindow, final ResourceService resourceService) {
        this.allFields = allFields;
        this.fieldByName = fieldByName;
        this.mainWindow = mainWindow;
        this.resourceService = resourceService;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        if (validateAllFields()) {
            saveToReposity();
            if (isSuccessfull()) {
                removeValidationErrors();
                commitAllFields();
                showSuccessMessage();
            }
            else {
                showErrorMessage();

            }
        }
    }

    private void removeValidationErrors() {
        for (final Field field : allFields) {
            ((AbstractComponent) field).setComponentError(null);
        }
    }

    private boolean validateAllFields() {
        for (final Field field : allFields) {
            try {
                field.validate();
            }
            catch (final Exception e) {
                ((AbstractComponent) field).setComponentError(new UserError(field.getCaption() + " is required"));
                return false;
            }
        }
        return true;
    }

    private void showErrorMessage() {
        throw new UnsupportedOperationException("not-yet-implemented.");

    }

    private boolean isSuccessfull() {
        return true;
    }

    private void saveToReposity() {
        try {
            updateModel();
            updatePersistence();
            for (final Field field : allFields) {
                field.commit();
            }
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
        }
        catch (final ParserConfigurationException e) {
            ModalDialog.show(mainWindow, e);
        }
        catch (final SAXException e) {
            ModalDialog.show(mainWindow, e);
        }
        catch (final IOException e) {
            ModalDialog.show(mainWindow, e);
        }

    }

    private void updateModel() throws ParserConfigurationException, SAXException, IOException, EscidocClientException {
        final OrgUnitBuilder builder = new OrgUnitBuilder(getOldOrgUnit());
        toBeUpdated =
            builder.with((String) fieldByName.get("title").getValue(),
                (String) fieldByName.get("description").getValue()).build();
    }

    private OrganizationalUnit getOldOrgUnit() throws EscidocClientException {
        return (OrganizationalUnit) resourceService.findById((String) item
            .getItemProperty(PropertyId.OBJECT_ID).getValue());
    }

    private void updatePersistence() throws EscidocClientException {
        resourceService.update(toBeUpdated);
    }

    private void commitAllFields() {
        for (final Field field : allFields) {
            field.commit();
        }
    }

    private void showSuccessMessage() {
        mainWindow.showNotification(ViewConstants.SUCCESFULLY_UPDATED_ORGANIZATIONAL_UNIT);
    }

    public void bind(final Item item) {
        this.item = item;
    }
}
