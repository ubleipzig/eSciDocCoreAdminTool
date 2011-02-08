package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.view.ViewConstants;

public class AddOrEditParentModalWindow extends Window {

    private final class CancelButtonListener implements ClickListener {
        private static final long serialVersionUID = -1211409730229979129L;

        public void buttonClick(final ClickEvent event) {
            closeWindow();
        }
    }

    private static final long serialVersionUID = -4691275024835350852L;

    private final HorizontalLayout buttons = new HorizontalLayout();

    private final Button okButton = new Button(ViewConstants.OK_LABEL);

    private final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL);

    String selectedParent;

    private final OrgUnitServiceLab orgUnitService;

    private final Window mainWindow;

    final ResourceContainer resourceContainer;

    private UpdateParentListener updateParentListener;

    private AddParentOkListener addParentOkListener;

    private final OrgUnitSpecificView orgUnitSpecificView;

    public AddOrEditParentModalWindow(
        final OrgUnitSpecificView orgUnitSpecificView,
        final ResourceContainer resourceContainer,
        final OrgUnitServiceLab orgUnitService, final Window mainWindow) {

        preconditions(orgUnitSpecificView, resourceContainer, orgUnitService,
            mainWindow);
        this.orgUnitSpecificView = orgUnitSpecificView;
        this.resourceContainer = resourceContainer;
        this.orgUnitService = orgUnitService;
        this.mainWindow = mainWindow;
        init();
    }

    private void preconditions(
        final OrgUnitSpecificView orgUnitSpecificView,
        final ResourceContainer resourceContainer,
        final OrgUnitServiceLab orgUnitService, final Window mainWindow) {
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(resourceContainer,
            "resourceContainer is null: %s", resourceContainer);
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
    }

    private void init() {
        configureWindow();
        addResourceTreeView();
        addButtons();
        addCancelBtnListener();
    }

    private void addResourceTreeView() {
        final ResourceTreeView resourceTreeView = createResourceTreeView();
        addComponent(resourceTreeView);
    }

    private void addButtons() {
        addOkButton();
        addCancelButton();
        addComponent(buttons);
    }

    private void addOkButton() {
        buttons.addComponent(okButton);
    }

    private void addCancelButton() {
        buttons.addComponent(cancelBtn);
    }

    private ResourceTreeView createResourceTreeView() {
        final ResourceTreeView resourceTreeView =
            new ResourceTreeView(getMainWindow(), new FolderHeaderImpl(
                ViewConstants.SELECT_A_PARENT_ORGANIZATIONAL_UNIT),
                resourceContainer);
        resourceTreeView.setCommand(new AddChildrenCommandImpl(orgUnitService,
            resourceContainer));
        resourceTreeView.addResourceNodeExpandListener();

        final ResourceSelectedListener selectedListener =
            new ResourceSelectedListener(this);

        resourceTreeView.addListener(selectedListener);
        return resourceTreeView;
    }

    public void addAddParentOkLisner() {
        addParentOkListener =
            new AddParentOkListener(this, orgUnitService, orgUnitSpecificView);
        okButton.removeListener(updateParentListener);
        okButton.addListener(addParentOkListener);
    }

    public void addUpdateParentOkListener() {
        updateParentListener = new UpdateParentListener(this, orgUnitService);
        okButton.removeListener(addParentOkListener);
        okButton.addListener(updateParentListener);
    }

    private void addCancelBtnListener() {
        cancelBtn.addListener(new CancelButtonListener());
    }

    void closeWindow() {
        getMainWindow().removeWindow(AddOrEditParentModalWindow.this);
    }

    private void configureWindow() {
        setModal(true);
        setCaption(ViewConstants.ADD_PARENT);
        setHeight(ViewConstants.MODAL_DIALOG_HEIGHT);
        setWidth(ViewConstants.MODAL_DIALOG_WIDTH);
    }

    public void setSelected(final String selectedParent) {
        this.selectedParent = selectedParent;
    }

    public void bind(final Item item) {
        updateParentListener.bind(item);
    }

    public void setParentPropertyForAdd(final Property parentProperty) {
        addParentOkListener.setParentProperty(parentProperty);
    }

    public void setParentPropertyForUpdate(final Property parentProperty) {
        updateParentListener.setParentProperty(parentProperty);
    }

    public Window getMainWindow() {
        return mainWindow;
    }
}