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

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.internal.OrgUnitServiceLab;
import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.admintool.view.ViewConstants;

@SuppressWarnings("serial")
public class ResourceAddViewImpl extends CustomComponent implements ResourceAddView {

    private final Panel panel = new Panel(ViewConstants.ADD_ORG_UNIT);

    private final VerticalLayout vLayout = new VerticalLayout();

    private final FormLayout formLayout = FormLayoutFactory.create();

    private final SaveAndCancelButtons footers = new SaveAndCancelButtons();

    private final Window mainWindow;

    private final ResourceService resourceService;

    private final PropertiesFields propertyFields;

    private final ResourceContainer resourceContainer;

    private final ResourceView resourceView;

    private ResourceBtnListener createOrgUnitBtnListener;

    final Map<String, Field> fieldByName = new HashMap<String, Field>();

    public ResourceAddViewImpl(final AdminToolApplication app, final Window mainWindow,
        final ResourceView resourceView, final ResourceService resourceService,
        final ResourceContainer resourceContainer, final PdpRequest pdpRequest) {

        checkPreconditions(mainWindow, resourceView, resourceService, resourceContainer);

        this.mainWindow = mainWindow;
        this.resourceView = resourceView;
        this.resourceService = resourceService;
        this.resourceContainer = resourceContainer;

        propertyFields = new PropertiesFieldsImpl(app, vLayout, formLayout, fieldByName, pdpRequest);
        propertyFields.removeOthers();

        createOrgUnitSpecificView(mainWindow, resourceService, resourceContainer);

        buildView();
    }

    private void checkPreconditions(
        final Window mainWindow, final ResourceView resourceViewImpl, final ResourceService orgUnitService,
        final ResourceContainer resourceContainer) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(resourceViewImpl, "resourceViewImpl is null: %s", resourceViewImpl);
        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(resourceContainer, "resourceContainer is null: %s", resourceContainer);
    }

    private void buildView() {
        setCompositionRoot(panel);
        panel.setContent(vLayout);
        panel.setStyleName(Reindeer.PANEL_LIGHT);
        vLayout.setHeight(100, UNITS_PERCENTAGE);
        formLayout.setWidth(517, UNITS_PIXELS);
        vLayout.addComponent(propertyFields);
        addSpace();
        addSaveAndCancelButtons();
    }

    private void addSpace() {
        formLayout.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    private void addSaveAndCancelButtons() {
        createOrgUnitBtnListener =
            new CreateOrgUnitBtnListener(propertyFields.getAllFields(), fieldByName, mainWindow, resourceView,
                resourceService, resourceContainer);
        footers.setOkButtonListener(createOrgUnitBtnListener);

        footers.getCancelBtn().addListener(new CancelResourceAddView(fieldByName));
        formLayout.addComponent(footers);
    }

    private OrgUnitSpecificView createOrgUnitSpecificView(
        final Window mainWindow, final ResourceService orgUnitService, final ResourceContainer resourceContainer) {

        final OrgUnitSpecificView orgUnitSpecificView =
            new OrgUnitSpecificView(mainWindow, (OrgUnitServiceLab) orgUnitService, resourceContainer, formLayout,
                fieldByName);
        orgUnitSpecificView.init();
        orgUnitSpecificView.addAddParentOkBtnListener();
        orgUnitSpecificView.setNoParents();

        return orgUnitSpecificView;
    }
}
