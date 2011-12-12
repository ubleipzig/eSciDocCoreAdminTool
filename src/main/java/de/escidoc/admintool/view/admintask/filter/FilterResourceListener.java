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

import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.admintask.ResourceType;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

public class FilterResourceListener implements ClickListener {

    private static final Logger LOG = LoggerFactory.getLogger(FilterResourceListener.class);

    private static final long serialVersionUID = 2859820395161737640L;

    private final Window mainWindow;

    private final ServiceContainer serviceContainer;

    private ShowFilterResultCommand command;

    private OptionGroup optionGroup;

    private TextField rawFilterTextArea;

    private ResourceService resourceService;

    public FilterResourceListener(final Window mainWindow, final ServiceContainer serviceContainer) {
        preconditions(mainWindow, serviceContainer);
        this.mainWindow = mainWindow;
        this.serviceContainer = serviceContainer;
    }

    private void preconditions(final Window mainWindow, final ServiceContainer serviceContainer) {
        Preconditions.checkNotNull(mainWindow, "mainWindow can not be null.");
        Preconditions.checkNotNull(serviceContainer, "serviceContainer can not be null.");
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        checkPreConditions();
        filterByType();
    }

    private void filterByType() {
        getServiceByType();
        showResult();
    }

    private void showResult() {
        Preconditions.checkNotNull(resourceService, "resourceService is null: %s", resourceService);
        try {
            if (filterFieldIsEmpty(resourceService)) {
                command.execute(resourceService.findAll());
            }
            else {
                command.execute(new HashSet<Resource>(resourceService.filterUsingInput(getRawFilter())));
            }
        }
        catch (final EscidocClientException e) {
            LOG.warn("EscidocClientException, show error to user", e);
            ModalDialog.show(mainWindow, e);
        }
    }

    private boolean filterFieldIsEmpty(final ResourceService resourceService) {
        return getRawFilter().isEmpty();
    }

    private ResourceService getServiceByType() {
        switch (getSelectedType()) {
            case ITEM:
                resourceService = serviceContainer.getItemService();
                break;
            case CONTAINER:
                resourceService = serviceContainer.getContainerService();
                break;
            case CONTEXT:
                resourceService = serviceContainer.getContextService();
                break;
            case ORGANIZATIONAL_UNIT:
                resourceService = serviceContainer.getOrgUnitService();
                break;
            case CONTENT_MODEL:
                resourceService = serviceContainer.getContentModelService();
                break;
            case CONTENT_RELATION:
                resourceService = serviceContainer.getContentRelationService();
                break;
            default:
                break;
        }
        Preconditions.checkNotNull(resourceService, "resourceService is null: %s", resourceService);
        return resourceService;
    }

    private void checkPreConditions() {
        Preconditions.checkArgument(serviceContainer.getContainerService() != null, "container service is null",
            serviceContainer.getContainerService());
        Preconditions.checkArgument(serviceContainer.getItemService() != null, "item service is null",
            serviceContainer.getItemService());
        Preconditions.checkArgument(serviceContainer.getContextService() != null, "context service is null",
            serviceContainer.getContextService());
    }

    private ResourceType getSelectedType() {
        final Object value = optionGroup.getValue();
        if (value instanceof ResourceType) {
            return (ResourceType) value;
        }
        return ResourceType.ITEM;
    }

    private String getRawFilter() {
        final Object value = rawFilterTextArea.getValue();
        if (value instanceof String) {
            return ((String) value).trim();
        }
        return AppConstants.EMPTY_STRING;
    }

    public void setCommand(final ShowFilterResultCommand command) {
        this.command = command;
    }

    public void setTextArea(final TextField textArea) {
        rawFilterTextArea = textArea;
    }

    public void setResourceOption(final OptionGroup resourceOption) {
        optionGroup = resourceOption;
    }

    public void setRawFilterTextArea(final TextField rawFilterTextArea) {
        this.rawFilterTextArea = rawFilterTextArea;
    }
}