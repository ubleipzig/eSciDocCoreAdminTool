package de.escidoc.admintool.view.admintask;

import java.util.Arrays;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;

public class FilterResourceView extends AbstractCustomView {

    private static final long serialVersionUID = -1412202753685048760L;

    private final OptionGroup resourceOption = new OptionGroup(
        ViewConstants.PLEASE_SELECT_A_RESOURCE_TYPE);

    private final TextField rawFilterTextArea = new TextField();

    private final Button filterResourceBtn = new Button(
        ViewConstants.FILTER_RESOURCES);

    private final ShowFilterResultCommand command;

    final AdminService adminService;

    final Window mainWindow;

    private final FilterResourceListener listener;

    public FilterResourceView(final ServiceContainer serviceContainer,
        final Window mainWindow, final PdpRequest pdpRequest) {
        preconditions(serviceContainer, mainWindow, pdpRequest);
        this.mainWindow = mainWindow;
        adminService = serviceContainer.getAdminService();
        listener = new FilterResourceListener(mainWindow, serviceContainer);
        command = new ShowFilterResultCommandImpl(this, pdpRequest);
    }

    private void preconditions(
        final ServiceContainer serviceContainer, final Window mainWindow,
        final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(serviceContainer,
            "serviceContainer is null: %s", serviceContainer);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s",
            pdpRequest);
    }

    public void init() {
        getViewLayout().setSizeFull();
        addResourceTypeOption();
        addFilterQueryTexField();
        addFilterButton();
    }

    private void addFilterQueryTexField() {
        rawFilterTextArea.setWidth(800, UNITS_PIXELS);
        rawFilterTextArea.setValue(ViewConstants.EXAMPLE_QUERY);
        getViewLayout().addComponent(rawFilterTextArea);
    }

    private void addResourceTypeOption() {
        resourceOption
            .setContainerDataSource(new BeanItemContainer<ResourceType>(
                ResourceType.class, Arrays.asList(ResourceType.values())));
        resourceOption.setItemCaptionPropertyId(PropertyId.LABEL);
        resourceOption.select(ResourceType.ITEM);
        getViewLayout().addComponent(resourceOption);
    }

    private void addFilterButton() {
        filterResourceBtn.setWidth(150, UNITS_PIXELS);
        getViewLayout().addComponent(filterResourceBtn);
        addListener();
    }

    private void addListener() {
        listener.setCommand(command);
        listener.setResourceOption(resourceOption);
        listener.setTextArea(rawFilterTextArea);
        filterResourceBtn.addListener(listener);
    }
}