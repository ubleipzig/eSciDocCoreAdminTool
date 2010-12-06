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

public class ModalWindow extends Window {

    private final class CancelButtonListener implements ClickListener {
        private static final long serialVersionUID = -1211409730229979129L;

        public void buttonClick(final ClickEvent event) {
            closeWindow();
        }
    }

    private static final long serialVersionUID = -4691275024835350852L;

    private final Button okButton = new Button(ViewConstants.OK_LABEL);

    private final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL);

    private final OrgUnitServiceLab orgUnitService;

    final ResourceContainer resourceContainer;

    String selectedParent;

    private UpdateParentListener updateParentListener;

    private final HorizontalLayout buttons = new HorizontalLayout();

    Window mainWindow;

    public ModalWindow(final ResourceContainer resourceContainer,
        final OrgUnitServiceLab orgUnitService, final Window mainWindow) {

        preconditions(resourceContainer, orgUnitService, mainWindow);
        this.resourceContainer = resourceContainer;
        this.orgUnitService = orgUnitService;
        this.mainWindow = mainWindow;
        init();
    }

    private void preconditions(
        final ResourceContainer resourceContainer,
        final OrgUnitServiceLab orgUnitService, final Window mainWindow) {
        Preconditions.checkNotNull(resourceContainer,
            "resourceContainer is null: %s", resourceContainer);
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
    }

    private void init() {
        configureWindow();

        final ResourceTreeView resourceTreeView =
            new ResourceTreeView(mainWindow, new FolderHeaderImpl(
                ViewConstants.SELECT_A_PARENT_ORGANIZATIONAL_UNIT),
                resourceContainer);
        resourceTreeView.setCommand(new AddChildrenCommandImpl(orgUnitService,
            resourceContainer));
        resourceTreeView.addResourceNodeExpandListener();

        final ResourceSelectedListener selectedListener =
            new ResourceSelectedListener(this);

        resourceTreeView.addListener(selectedListener);
        addComponent(resourceTreeView);
        addButtons();
    }

    private void addButtons() {
        addComponent(buttons);
        addOkButton();
        addCancelButton();
    }

    private void addCancelButton() {
        cancelBtn.addListener(new CancelButtonListener());
        buttons.addComponent(cancelBtn);
    }

    void closeWindow() {
        mainWindow.removeWindow(ModalWindow.this);
    }

    private void addOkButton() {
        updateParentListener = new UpdateParentListener(this, orgUnitService);
        okButton.addListener(updateParentListener);
        buttons.addComponent(okButton);
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

    public void setParentProperty(final Property parentProperty) {
        updateParentListener.setParentProperty(parentProperty);
    }
}