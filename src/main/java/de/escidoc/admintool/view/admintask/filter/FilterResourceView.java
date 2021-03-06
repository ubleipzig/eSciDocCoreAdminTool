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
package de.escidoc.admintool.view.admintask.filter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.AbstractCustomView;
import de.escidoc.admintool.view.admintask.ResourceType;

@SuppressWarnings("serial")
public class FilterResourceView extends AbstractCustomView {
    final AdminService adminService;

    final Window mainWindow;

    private final OptionGroup resourceOption = new OptionGroup(ViewConstants.PLEASE_SELECT_A_RESOURCE_TYPE);

    private final TextField rawFilterTextArea = new TextField();

    private final Button filterResourceBtn = new Button(ViewConstants.FILTER_LABEL);

    private final ShowFilterResultCommandImpl command;

    private final FilterResourceListener listener;

    private HorizontalLayout filterLayout;

    public FilterResourceView(final ServiceContainer serviceContainer, final Window mainWindow,
        final PdpRequest pdpRequest) {
        preconditions(serviceContainer, mainWindow, pdpRequest);
        this.mainWindow = mainWindow;
        adminService = serviceContainer.getAdminService();
        listener = new FilterResourceListener(mainWindow, serviceContainer);
        command = new ShowFilterResultCommandImpl(this, pdpRequest);
    }

    private void preconditions(
        final ServiceContainer serviceContainer, final Window mainWindow, final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(serviceContainer, "serviceContainer is null: %s", serviceContainer);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
    }

    public void init() {
        getViewLayout().setSizeFull();
        filterLayout = new HorizontalLayout();
        filterLayout.setMargin(true);
        filterLayout.setSpacing(true);
        addResourceTypeOption();
        addFilterQueryTexField();
        addFilterButton();
    }

    private void addFilterQueryTexField() {
        rawFilterTextArea.setWidth(800, UNITS_PIXELS);

        final Label popUpContent = new Label(ViewConstants.FILTER_EXAMPLE_TOOLTIP_TEXT, Label.CONTENT_XHTML);
        popUpContent.setWidth(400, UNITS_PIXELS);
        final PopupView popup = new PopupView(ViewConstants.TIP, popUpContent);
        filterLayout.addComponent(popup);
        filterLayout.addComponent(rawFilterTextArea);
    }

    private void addResourceTypeOption() {
        final List<ResourceType> allResourceTypeList =
            new LinkedList<ResourceType>(Arrays.asList(ResourceType.values()));
        allResourceTypeList.remove(ResourceType.COMPONENT);
        allResourceTypeList.remove(ResourceType.USERACCOUNT);
        allResourceTypeList.remove(ResourceType.USERGROUP);
        resourceOption.setContainerDataSource(new BeanItemContainer<ResourceType>(ResourceType.class,
            allResourceTypeList));
        resourceOption.setItemCaptionPropertyId(PropertyId.LABEL);
        resourceOption.select(ResourceType.ITEM);
        getViewLayout().addComponent(resourceOption);
    }

    private void addFilterButton() {
        filterResourceBtn.setWidth(150, UNITS_PIXELS);
        filterLayout.addComponent(filterResourceBtn);
        getViewLayout().addComponent(filterLayout);
        addListener();
    }

    private void addListener() {
        listener.setCommand(command);
        listener.setResourceOption(resourceOption);
        listener.setTextArea(rawFilterTextArea);
        filterResourceBtn.addListener(listener);
    }
}