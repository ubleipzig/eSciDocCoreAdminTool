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
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.vaadin.data.Item;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.application.security.AuthorizationException;

public abstract class AbstractResourceBtnListener implements ResourceBtnListener {

    private static final long serialVersionUID = -4962907806742105879L;

    private final ResourceBtnListenerData data = new ResourceBtnListenerData();

    protected AbstractResourceBtnListener(final Collection<Field> allFields, final Map<String, Field> fieldByName,
        final Window mainWindow, final ResourceView resourceView, final ResourceService resourceService) {
        data.allFields = allFields;
        data.fieldByName = fieldByName;
        data.mainWindow = mainWindow;
        data.resourceView = resourceView;
        data.resourceService = resourceService;
    }

    public ResourceBtnListenerData getData() {
        return data;
    }

    @Override
    public void bind(final Item item) {
        data.item = item;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        if (validateAllFields() && saveToReposity()) {
            removeValidationErrors();
            commitAllFields();
            updateResourceContainer();
            showInEditView();
            showSuccessMessage();
        }

    }

    private void showSuccessMessage() {
        data.mainWindow.showNotification(new Notification("Info", getSucessMessage(),
            Notification.TYPE_TRAY_NOTIFICATION));
    }

    protected abstract void showInEditView();

    private void removeValidationErrors() {
        for (final Field field : data.allFields) {
            ((AbstractComponent) field).setComponentError(null);
        }
    }

    private boolean validateAllFields() {
        for (final Field field : data.allFields) {
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

    private boolean saveToReposity() {
        try {
            updateModel();
            updatePersistence();
            return true;
        }
        catch (final AuthorizationException e) {
            ErrorMessage.show(data.mainWindow, ViewConstants.NOT_AUTHORIZED, e);
            return false;
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(data.mainWindow, e);
            return false;
        }
        catch (final ParserConfigurationException e) {
            ModalDialog.show(data.mainWindow, e);
            return false;
        }
        catch (final SAXException e) {
            ModalDialog.show(data.mainWindow, e);
            return false;
        }
        catch (final IOException e) {
            ModalDialog.show(data.mainWindow, e);
            return false;
        }
    }

    protected abstract void commitAllFields();

    protected abstract void updateModel() throws ParserConfigurationException, SAXException, IOException,
        EscidocClientException;

    protected abstract void updatePersistence() throws EscidocClientException;

    protected abstract String getSucessMessage();

    protected abstract void updateResourceContainer();

    protected String getCity() {
        return (String) getData().fieldByName.get("city").getValue();
    }

    protected String getCountry() {
        return (String) getData().fieldByName.get("country").getValue();
    }

    protected String getDescription() {
        return (String) getData().fieldByName.get("description").getValue();
    }

    protected String getTitle() {
        return (String) getData().fieldByName.get("title").getValue();
    }

    protected String getAlternative() {
        return (String) getData().fieldByName.get("alternative").getValue();
    }

    protected String getIdentifier() {
        return (String) getData().fieldByName.get("identifier").getValue();
    }

    protected String getCoordinates() {
        return (String) getData().fieldByName.get("coordinates").getValue();
    }

    protected String getType() {
        return (String) getData().fieldByName.get("type").getValue();
    }

    protected Set<String> getParents() {
        return Collections.singleton((String) getData().fieldByName.get("parents").getValue());
    }

    public ResourceView getResourceView() {
        return getData().resourceView;
    }

    protected Field getParentField() {
        return getData().fieldByName.get("parents");
    }
}