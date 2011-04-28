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

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.terminal.SystemError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.listener.EditAdminDescriptorListener;
import de.escidoc.admintool.view.context.listener.NewAdminDescriptorListener;
import de.escidoc.admintool.view.context.listener.RemoveAdminDescriptorListener;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.reference.OrganizationalUnitRef;
import de.escidoc.core.resources.om.context.AdminDescriptor;
import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;

public class ContextAddView extends CustomComponent implements ClickListener {

    private static final long serialVersionUID = 1100228979605484119L;

    private final Logger LOG = LoggerFactory.getLogger(ContextAddView.class);

    private final Panel panel = new Panel();

    private final FormLayout form = new FormLayout();

    private static final int LABEL_WIDTH = 100;

    private final Button save = new Button(ViewConstants.SAVE_LABEL, this);

    private final Button cancel = new Button(ViewConstants.CANCEL_LABEL, this);

    private final Button addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton = new Button(ViewConstants.REMOVE_LABEL);

    private final ContextListView contextListView;

    private final ContextService contextService;

    private TextField nameField;

    private TextField descriptionField;

    private TextField typeField;

    private HorizontalLayout footer;

    private Accordion adminDescriptorAccordion;

    ListSelect orgUnitList;

    final Window mainWindow;

    private final AddOrgUnitToTheList addOrgUnitToTheList;

    private final AdminToolApplication app;

    public ContextAddView(final AdminToolApplication app, final Window mainWindow,
        final ContextListView contextListView, final ContextService contextService,
        final AddOrgUnitToTheList addOrgUnitToTheList) {

        this.app = app;
        this.mainWindow = mainWindow;
        this.contextListView = contextListView;
        this.contextService = contextService;
        this.addOrgUnitToTheList = addOrgUnitToTheList;
    }

    public void init() {
        configureLayout();
        addFields();
        addSpace();
        addFooter();
    }

    private void addSpace() {
        form.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
        form.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    private void configureLayout() {
        setCompositionRoot(panel);
        panel.setStyleName(Reindeer.PANEL_LIGHT);
        panel.setCaption(ViewConstants.ADD_A_NEW_CONTEXT);
        panel.setContent(form);

        form.setSpacing(false);
        form.setWidth(520, UNITS_PIXELS);
    }

    private void addFields() {
        addNameField();
        addDescField();
        addTypeField();
        addOrgUnitField();
        addAdminDescriptorField();
    }

    private void addOrgUnitField() {
        addOrgUnitList();
        addOrgUnitEditor();
        form.addComponent(new Label(" &nbsp; ", Label.CONTENT_XHTML));
    }

    private void addOrgUnitList() {
        orgUnitList = new ListSelect();
        orgUnitList.setRows(5);
        orgUnitList.setWidth(ViewConstants.FIELD_WIDTH);
        orgUnitList.setNullSelectionAllowed(true);
        orgUnitList.setMultiSelect(true);
        orgUnitList.setImmediate(false);

        addOrgUnitToTheList.using(orgUnitList);
    }

    private void addOrgUnitEditor() {
        setButtonsStyleToSmall();
        addOrgUnitButtonListeners();

        form.addComponent(LayoutHelper.create(ViewConstants.ORGANIZATION_UNITS_LABEL, orgUnitList, LABEL_WIDTH, 90,
            true, new Button[] { addOrgUnitButton, removeOrgUnitButton }));
    }

    private void setButtonsStyleToSmall() {
        addOrgUnitButton.setStyleName("small");
        removeOrgUnitButton.setStyleName("small");
    }

    private void addOrgUnitButtonListeners() {
        addOrgUnitButton.addListener(addOrgUnitToTheList);
        removeOrgUnitButton.addListener(new RemoveOrgUnitFromList(orgUnitList));
    }

    private void addAdminDescriptorField() {
        adminDescriptorAccordion = new Accordion();
        adminDescriptorAccordion.setWidth(ViewConstants.FIELD_WIDTH);
        adminDescriptorAccordion.setSizeFull();

        final Panel accordionPanel = new Panel();
        accordionPanel.setContent(adminDescriptorAccordion);
        accordionPanel.setSizeFull();
        accordionPanel.setWidth(ViewConstants.FIELD_WIDTH);

        final Button addButton = new Button(ViewConstants.ADD_LABEL);
        final Button editButton = new Button(ViewConstants.EDIT_LABEL);
        final Button delButton = new Button(ViewConstants.REMOVE_LABEL);

        addButton.setStyleName("small");
        editButton.setStyleName("small");
        delButton.setStyleName("small");

        addButton.addListener(new NewAdminDescriptorListener(mainWindow, adminDescriptorAccordion));
        editButton.addListener(new EditAdminDescriptorListener(mainWindow, adminDescriptorAccordion));
        delButton.addListener(new RemoveAdminDescriptorListener(adminDescriptorAccordion));

        panel.addComponent(LayoutHelper.create("Admin Descriptors", accordionPanel, LABEL_WIDTH + 2, 300, false,
            new Button[] { addButton, editButton, delButton }));
    }

    private void addTypeField() {
        typeField = new TextField();
        typeField.setWidth(ViewConstants.FIELD_WIDTH);
        panel.addComponent(LayoutHelper.create(ViewConstants.TYPE_LABEL, typeField, LABEL_WIDTH, true));
    }

    private void addDescField() {
        descriptionField = new TextField();
        descriptionField.setWidth(ViewConstants.FIELD_WIDTH);
        descriptionField.setRows(3);
        descriptionField.setMaxLength(ViewConstants.MAX_DESC_LENGTH);
        panel.addComponent(LayoutHelper
            .create(ViewConstants.DESCRIPTION_LABEL, descriptionField, LABEL_WIDTH, 80, true));
        mapBinding("", descriptionField);
    }

    private void addNameField() {
        nameField = new TextField();
        nameField.setWidth(ViewConstants.FIELD_WIDTH);
        nameField.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);

        form.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL, nameField, LABEL_WIDTH, true));
    }

    private ObjectProperty mapBinding(final String initText, final TextField tf) {
        final ObjectProperty op = new ObjectProperty(initText, String.class);
        tf.setPropertyDataSource(op);
        return op;
    }

    private void addFooter() {
        footer = new HorizontalLayout();
        footer.setWidth(100, UNITS_PERCENTAGE);
        final HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(save);
        hl.addComponent(cancel);
        footer.addComponent(hl);
        footer.setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
        form.addComponent(footer);
    }

    private void resetFields() {
        nameField.setValue("");
        nameField.setComponentError(null);
        descriptionField.setValue("");
        descriptionField.setComponentError(null);
        typeField.setValue("");
        typeField.setComponentError(null);
        orgUnitList.removeAllItems();
        orgUnitList.setComponentError(null);
        adminDescriptorAccordion.removeAllComponents();
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        onButtonClick(event);
    }

    private void onButtonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source.equals(cancel)) {
            onCancelButtonClick();
        }
        else if (source.equals(save)) {
            onSaveButtonClick();
        }
    }

    private void onCancelButtonClick() {
        resetFields();
    }

    private void onSaveButtonClick() {
        if (isValid()) {
            trySave();
        }
    }

    private boolean isValid() {
        boolean valid = true;
        valid = EmptyFieldValidator.isValid(nameField, "Name can not be empty.");
        valid &= EmptyFieldValidator.isValid(descriptionField, "Description can not be empty.");
        valid &= EmptyFieldValidator.isValid(typeField, ViewConstants.TYPE_LABEL + " can not be empty.");
        valid &= EmptyFieldValidator.isValid(orgUnitList, ViewConstants.ORGANIZATION_UNITS_LABEL + " can not be empty");
        return valid;
    }

    private void trySave() {
        removeAllErrorMessage();
        try {
            addAndSortContextInListView(createContextInRepository());
            resetFields();
            showMessage();
        }
        catch (final EscidocClientException e) {
            final String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
            LOG.error("root cause: " + ExceptionUtils.getRootCauseMessage(e), e);
            mainWindow.addWindow(new ErrorDialog(mainWindow, "Error", rootCauseMessage));
        }
        catch (final ParserConfigurationException e) {
            LOG.error("root cause: " + ExceptionUtils.getRootCauseMessage(e), e);
            mainWindow.addWindow(new ErrorDialog(mainWindow, "Error", e.getMessage()));
        }
    }

    private void showMessage() {
        app.getMainWindow().showNotification(
            new Notification("Info", "Context is created", Notification.TYPE_TRAY_NOTIFICATION));
    }

    private void addAndSortContextInListView(final Context newContext) throws EscidocClientException {
        contextListView.addContext(newContext);
        contextListView.sort();
        contextListView.select(newContext);
        final Item item = contextListView.getItem(newContext);
        showInEditView(item);
    }

    private void showInEditView(final Item item) throws EscidocClientException {
        app.getContextView().showEditView(item);
    }

    private Context createContextInRepository() throws EscidocClientException, ParserConfigurationException {
        return contextService.create((String) nameField.getValue(), (String) descriptionField.getValue(),
            (String) typeField.getValue(), getEnteredOrgUnitRefs(), enteredAdminDescriptors());

    }

    private void removeAllErrorMessage() {
        nameField.setComponentError(null);
        descriptionField.setComponentError(null);
        typeField.setComponentError(null);
        orgUnitList.setComponentError(null);
        adminDescriptorAccordion.setComponentError(null);
    }

    private AdminDescriptors enteredAdminDescriptors() {
        final AdminDescriptors adminDescriptors = new AdminDescriptors();
        final Iterator<Component> it = adminDescriptorAccordion.getComponentIterator();
        while (it != null && it.hasNext()) {
            final Component contentComp = it.next();
            final Tab tab = adminDescriptorAccordion.getTab(contentComp);
            final String adminDescName = tab.getCaption();
            String adminDescContent = "";
            if (contentComp instanceof Label) {
                adminDescContent = ((String) ((Label) contentComp).getValue());
            }
            final AdminDescriptor adminDescriptor = new AdminDescriptor();
            adminDescriptor.setName(adminDescName);
            try {
                adminDescriptor.setContent(adminDescContent);
                adminDescriptors.add(adminDescriptor);
            }
            catch (final ParserConfigurationException e) {
                LOG.error("An unexpected error occured! See LOG for details.", e);
                mainWindow.addWindow(new ErrorDialog(mainWindow, "Error", e.getMessage()));
                setComponentError(new SystemError(e.getMessage()));
            }
            catch (final SAXException e) {
                LOG.error("An unexpected error occured! See LOG for details.", e);
                mainWindow.addWindow(new ErrorDialog(mainWindow, "Error", e.getMessage()));
                setComponentError(new SystemError(e.getMessage()));
            }
            catch (final IOException e) {
                LOG.error("An unexpected error occured! See LOG for details.", e);
                mainWindow.addWindow(new ErrorDialog(mainWindow, "Error", e.getMessage()));
                setComponentError(new SystemError(e.getMessage()));
            }
        }

        return adminDescriptors;
    }

    private OrganizationalUnitRefs getEnteredOrgUnitRefs() {
        final OrganizationalUnitRefs organizationalUnitRefs = new OrganizationalUnitRefs();

        for (final String objectId : getEnteredOrgUnits()) {
            organizationalUnitRefs.add(new OrganizationalUnitRef(objectId));
        }

        return organizationalUnitRefs;
    }

    private Set<String> getEnteredOrgUnits() {

        if (orgUnitList.getContainerDataSource() == null || orgUnitList.getContainerDataSource().getItemIds() == null
            || orgUnitList.getContainerDataSource().getItemIds().size() == 0
            || !orgUnitList.getContainerDataSource().getItemIds().iterator().hasNext()) {
            return Collections.emptySet();
        }

        final Set<String> orgUnits = new HashSet<String>();

        final Collection<?> itemIds = orgUnitList.getContainerDataSource().getItemIds();

        for (final Object object : itemIds) {
            final ResourceRefDisplay ou = (ResourceRefDisplay) object;
            orgUnits.add(ou.getObjectId());
        }

        return orgUnits;
    }
}