package de.escidoc.admintool.view.admintask;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.Resource;

public class FilterResourceView extends AbstractCustomView {

    private static final long serialVersionUID = -1412202753685048760L;

    private static final String EXAMPLE_QUERY =
        "\"/properties/created-by/id\"=escidoc:exuser1";

    private final Button filterResourceBtn = new Button(
        ViewConstants.FILTERED_RESOURCES);

    private final OptionGroup resourceOption = new OptionGroup(
        ViewConstants.PLEASE_SELECT_A_RESOURCE_TYPE);

    private final ShowFilterResultCommandImpl command =
        new ShowFilterResultCommandImpl(this);

    final AdminService adminService;

    private final FilterResourceListener listener;

    final Window mainWindow;

    private final TextField rawFilterTextArea = new TextField();

    public FilterResourceView(final FilterResourceListener listener,
        final AdminService adminService, final Window mainWindow) {
        checkForNull(listener, adminService, mainWindow);
        this.listener = listener;
        this.adminService = adminService;
        this.mainWindow = mainWindow;
        init();
    }

    private void checkForNull(
        final FilterResourceListener listener, final AdminService adminService,
        final Window mainWindow) {
        Preconditions.checkNotNull(listener, "Listener can not be null.");
        Preconditions.checkNotNull(adminService,
            "AdminService can not be null.");
        Preconditions.checkNotNull(mainWindow, "MainWindow can not be null.");
    }

    private void init() {
        addOptionGroup();
        addTextArea();
        addFilterButton();
    }

    private void addTextArea() {
        rawFilterTextArea.setWidth(800, UNITS_PIXELS);
        rawFilterTextArea.setValue(EXAMPLE_QUERY);
        getViewLayout().addComponent(rawFilterTextArea);
    }

    private void addOptionGroup() {
        final List<ResourceType> list = Arrays.asList(ResourceType.values());

        resourceOption
            .setContainerDataSource(new BeanItemContainer<ResourceType>(list));
        resourceOption.setItemCaptionPropertyId(PropertyId.LABEL);
        resourceOption.select(ResourceType.ITEM);
        getViewLayout().addComponent(resourceOption);
    }

    private void addFilterButton() {
        filterResourceBtn.setWidth(150, UNITS_PIXELS);
        getViewLayout().addComponent(filterResourceBtn);

        addListener();
    }

    interface ShowFilterResultCommand {
        void execute(Set<Resource> repoInfos);
    }

    private void addListener() {
        listener.setCommand(command);
        listener.setResourceOption(resourceOption);
        listener.setTextArea(rawFilterTextArea);
        filterResourceBtn.addListener(listener);
    }
}