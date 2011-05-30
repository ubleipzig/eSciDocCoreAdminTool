package de.escidoc.admintool.view.admintask;

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
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ModalDialog;
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
        return resourceService != null && getRawFilter().isEmpty();
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
            default:
                break;
        }
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
        else {
            return AppConstants.EMPTY_STRING;
        }
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