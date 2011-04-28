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
package de.escidoc.admintool.view.contentmodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.vaadin.appfoundation.view.AbstractView;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.resource.FormLayoutFactory;
import de.escidoc.admintool.view.resource.PropertiesFields;
import de.escidoc.admintool.view.resource.PropertiesFieldsImpl;
import de.escidoc.admintool.view.resource.SaveAndCancelButtons;

public class ContentModelAddView extends AbstractView<Panel> {

    private static final long serialVersionUID = -9073804431327208286L;

    private final SaveAndCancelButtons footers = new SaveAndCancelButtons();

    private final Map<String, Field> fieldByName = new HashMap<String, Field>();

    private final FormLayout formLayout = FormLayoutFactory.create();

    private final VerticalLayout vLayout = new VerticalLayout();

    private CreateContentModelListener saveBtnListener;

    private PropertiesFields propertyFields;

    private final Window mainWindow;

    private final ResourceService contentModelService;

    private final ContentModelContainerImpl contentModelContainerImpl;

    private final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    public ContentModelAddView(final AdminToolApplication app, final Window mainWindow,
        final ResourceService contentModelService, final ContentModelContainerImpl contentModelContainerImpl,
        final PdpRequest pdpRequest) {
        super(new Panel());
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(contentModelService, "contentModelService is null: %s", contentModelService);
        this.app = app;
        this.mainWindow = mainWindow;
        this.contentModelService = contentModelService;
        this.contentModelContainerImpl = contentModelContainerImpl;
        this.pdpRequest = pdpRequest;
    }

    public void init() {
        getContent().setContent(vLayout);
        getContent().setCaption("Add Content Model");
        getContent().setStyleName(Reindeer.PANEL_LIGHT);
        createPropertiesFields();
        addPropertiesFields();
        saveBtnListener =
            new CreateContentModelListener(propertyFields.getAllFields(), contentModelService, fieldByName, mainWindow,
                contentModelContainerImpl);
        addSaveAndCancelButtons();
    }

    public void setContentModelView(final ContentModelView contentModelView) {
        Preconditions.checkNotNull(contentModelView, "contentModelView is null: %s", contentModelView);
        saveBtnListener.setContentModelView(contentModelView);
    }

    private void addPropertiesFields() {
        getContent().setWidth(500, UNITS_PIXELS);
        getContent().addComponent(propertyFields);
    }

    private void createPropertiesFields() {
        propertyFields = new PropertiesFieldsImpl(app, vLayout, formLayout, fieldByName, pdpRequest);
        propertyFields.setDescriptionRequired();
        propertyFields.removeOthers();
    }

    private void addSaveAndCancelButtons() {
        footers.setOkButtonListener(saveBtnListener);
        footers.getCancelBtn().addListener(new ClickListener() {
            private static final long serialVersionUID = 9116178009548492155L;

            @Override
            public void buttonClick(final ClickEvent event) {
                final Collection<Field> values = fieldByName.values();
                for (final Field field : values) {
                    field.discard();
                }
            }
        });

        getContent().addComponent(footers);
    }

    @Override
    public void activated(final Object... params) {
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void deactivated(final Object... params) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void resetFields() {
        propertyFields.resetFields();
    }

}
