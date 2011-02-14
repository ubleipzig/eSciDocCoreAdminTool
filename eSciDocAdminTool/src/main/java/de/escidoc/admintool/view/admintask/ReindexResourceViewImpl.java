package de.escidoc.admintool.view.admintask;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.AdminService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.adm.MessagesStatus;

public class ReindexResourceViewImpl extends AbstractCustomView
    implements ReindexResourceView {

    private static final long serialVersionUID = 2997054515640202370L;

    private final Button reindexResourceBtn = new Button(ViewConstants.REINDEX);

    private final ClickListener showStatusListener =
        new ShowReindexingStatusListener(this);

    private final Button showStatusButton = new Button(
        ViewConstants.SHOW_STATUS);

    private final CheckBox clearIndexBox = new CheckBox(
        ViewConstants.CLEAR_INDEX);

    private final ComboBox indexNameSelect = new ComboBox(
        ViewConstants.INDEX_NAME, ViewConstants.INDEX_NAMES);

    private final ReindexButtonListener listener = new ReindexButtonListener(
        this, clearIndexBox, indexNameSelect);

    final Label statusLabel = new Label(ViewConstants.STATUS);

    final AdminService adminService;

    final Window mainWindow;

    private ShowStatusCommand showStatusCommand;

    public ReindexResourceViewImpl(final AdminService adminService,
        final Window mainWindow) {
        preconditions(adminService, mainWindow);
        this.adminService = adminService;
        this.mainWindow = mainWindow;
        init();
    }

    private void preconditions(
        final AdminService adminService, final Window mainWindow) {
        Preconditions.checkNotNull(adminService,
            "adminService can not be null: %s", adminService);
        Preconditions.checkNotNull(adminService,
            "mainWindow can not be null: %s", mainWindow);
    }

    private void init() {
        createShowStatusCommand();
        addClearIndexBox();
        addIndexNameSelection();
        addReindexButton();
        addShowStatusButton();
        addStatusLabel();
        addListener();
    }

    private void addShowStatusButton() {
        showStatusButton.setWidth(150, UNITS_PIXELS);
        getViewLayout().addComponent(showStatusButton);
    }

    private void addStatusLabel() {
        getViewLayout().addComponent(statusLabel);
    }

    private void createShowStatusCommand() {
        showStatusCommand = new ShowStatusCommand() {

            @Override
            public void execute(final MessagesStatus status) {
                statusLabel.setValue(status.getStatusMessage());
            }
        };
    }

    private void addClearIndexBox() {
        setDefaultAsTrue();
        getViewLayout().addComponent(clearIndexBox);
    }

    // TODO refactor index name to ENUM
    private void addIndexNameSelection() {
        indexNameSelect.setNullSelectionAllowed(false);
        indexNameSelect.select(ViewConstants.REINDEX_ALL);
        getViewLayout().addComponent(indexNameSelect);
    }

    private void setDefaultAsTrue() {
        clearIndexBox.setValue(Boolean.TRUE);
    }

    private void addReindexButton() {
        reindexResourceBtn.setWidth(150, UNITS_PIXELS);
        getViewLayout().addComponent(reindexResourceBtn);
    }

    private void addListener() {
        reindexResourceBtn.addListener(listener);
        showStatusButton.addListener(showStatusListener);
    }
}