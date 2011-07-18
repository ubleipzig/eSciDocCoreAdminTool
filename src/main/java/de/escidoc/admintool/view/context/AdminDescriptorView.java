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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.google.common.base.Preconditions;
import com.vaadin.terminal.SystemError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.resources.om.context.AdminDescriptor;

@SuppressWarnings("serial")
public abstract class AdminDescriptorView extends Window {

    private static final Logger LOG = LoggerFactory.getLogger(AdminDescriptorView.class);

    protected static final String EDIT_ADMIN_DESCRIPTOR = "Edit Admin Descriptor";

    protected final TextField adminDescNameField = new TextField("Name: ");

    protected final TextField adminDescContent = new TextField("Content: ");

    private final FormLayout formLayout = new FormLayout();

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button saveButton = new Button("Save");

    private final Button cancelButton = new Button("Cancel");

    protected Accordion adminDescriptorAccordion = null;

    protected String name;

    protected String content;

    protected final Window mainWindow;

    public AdminDescriptorView(final Window mainWindow, final Accordion adminDescriptorAccordion) {

        Preconditions.checkNotNull(mainWindow, "mainWindow can not be null: %s", mainWindow);
        Preconditions.checkNotNull(adminDescriptorAccordion, "adminDescriptorAccordion can not be null: %s",
            adminDescriptorAccordion);

        this.mainWindow = mainWindow;
        this.adminDescriptorAccordion = adminDescriptorAccordion;
        buildMainLayout();
    }

    public AdminDescriptorView(final Window mainWindow, final Accordion adminDescriptorAccordion, final String name,
        final String content) {

        Preconditions.checkNotNull(mainWindow, "mainWindow can not be null: %s", mainWindow);
        Preconditions.checkNotNull(adminDescriptorAccordion, "adminDescriptorAccordion can not be null: %s",
            adminDescriptorAccordion);
        Preconditions.checkNotNull(name, "name can not be null: %s", name);
        Preconditions.checkNotNull(adminDescriptorAccordion, "content can not be null: %s", content);

        this.mainWindow = mainWindow;
        this.adminDescriptorAccordion = adminDescriptorAccordion;
        this.name = name;
        this.content = content;
        buildMainLayout();
    }

    private void buildMainLayout() {
        setWindowCaption();
        setModal(true);
        setWidth("500px");
        setHeight("250px");

        adminDescNameField.setWidth("400px");
        adminDescContent.setRows(5);
        adminDescContent.setWidth("400px");

        if (name != null) {
            adminDescNameField.setValue(name);
        }

        if (content != null) {
            adminDescContent.setValue(content);
        }
        formLayout.addComponent(adminDescNameField);
        formLayout.addComponent(adminDescContent);

        addFooter();
        addComponent(formLayout);
    }

    protected abstract void setWindowCaption();

    private void addFooter() {
        footer.addComponent(saveButton);
        footer.addComponent(cancelButton);

        getSaveButton().addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                doSave();
            }
        });

        cancelButton.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                closeWindow();
            }
        });

        formLayout.addComponent(footer);
    }

    protected abstract void doSave();

    boolean isValid(final String adminDescriptorName) {
        return !containsSpace(adminDescriptorName);
    }

    private boolean containsSpace(final String adminDescriptorName) {
        final Pattern pattern = Pattern.compile("\\s");
        final Matcher matcher = pattern.matcher(adminDescriptorName);
        return matcher.find();
    }

    protected class SaveButtonListener implements Button.ClickListener {

        public void buttonClick(final ClickEvent event) {
            final String content = (String) adminDescContent.getValue();
            if (validate(content)) {
                adminDescriptorAccordion.addTab(new Label(content, Label.CONTENT_PREFORMATTED),
                    (String) adminDescNameField.getValue(), null);
                closeWindow();
            }
        }
    }

    protected boolean validate(final String value) {
        return enteredAdminDescriptors(value);
    }

    protected void closeWindow() {
        getApplication().getMainWindow().removeWindow(this);
    }

    public Button getSaveButton() {
        return saveButton;
    }

    private boolean enteredAdminDescriptors(final String value) {
        final AdminDescriptor adminDescriptor = new AdminDescriptor((String) adminDescNameField.getValue());
        adminDescriptor.setName((String) adminDescNameField.getValue());
        try {
            adminDescriptor.setContent(value);
            return true;
        }
        catch (final ParserConfigurationException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            getApplication().getMainWindow().addWindow(new ErrorDialog(mainWindow, "Error", e.getMessage()));
            setComponentError(new SystemError(e.getMessage()));
            return false;
        }
        catch (final SAXException e) {
            final ErrorDialog errorDialog = new ErrorDialog(mainWindow, "Error", "XML is not well formed.");
            errorDialog.setWidth("400px");
            errorDialog.setWidth("300px");

            assert getApplication().getMainWindow() != null : "MainWindow can not be null.";

            getApplication().getMainWindow().addWindow(errorDialog);
            LOG.error("An unexpected error occured! See LOG for details.", e);
            setComponentError(new SystemError(e.getMessage()));
            return false;

        }
        catch (final IOException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            getApplication().getMainWindow().addWindow(new ErrorDialog(mainWindow, "Error", e.getMessage()));
            setComponentError(new SystemError(e.getMessage()));
            return false;
        }
    }
}