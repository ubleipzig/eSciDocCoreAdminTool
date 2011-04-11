package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.domain.PublicStatus;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.navigation.ActionIdConstants;

public class ResourceToolbar extends CustomComponent {

    private static final long serialVersionUID = 1436927861311900013L;

    private final HorizontalLayout hLayout = new HorizontalLayout();

    private Button newBtn;

    private Button delBtn;

    private Button openBtn;

    private final ResourceViewImpl resourceViewImpl;

    private final Window mainWindow;

    private final ResourceService resourceService;

    private final ResourceContainer resourceContainer;

    private Button closeBtn;

    private OpenResourceListener openResourceListener;

    private CloseResourceListener closeResourceListener;

    private DelResourceListener delResourceListener;

    private Item item;

    private final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    public ResourceToolbar(final AdminToolApplication app,
        final ResourceViewImpl resourceViewImpl, final Window mainWindow,
        final ResourceService resourceService,
        final ResourceContainer resourceContainer, final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(resourceViewImpl,
            "resourceViewImpl is null: %s", resourceViewImpl);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        Preconditions.checkNotNull(resourceService,
            "resourceService is null: %s", resourceService);
        Preconditions.checkNotNull(resourceContainer,
            "resourceContainer is null: %s", resourceContainer);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s",
            pdpRequest);
        this.app = app;
        this.resourceViewImpl = resourceViewImpl;
        this.mainWindow = mainWindow;
        this.resourceService = resourceService;
        this.resourceContainer = resourceContainer;
        this.pdpRequest = pdpRequest;
        init();
    }

    private void init() {
        configureLayout();
        createButtonsAndListeners();
        addButtons();
    }

    private void addButtons() {
        hLayout.addComponent(newBtn);
        hLayout.addComponent(delBtn);
        hLayout.addComponent(openBtn);
        hLayout.addComponent(closeBtn);
    }

    private void configureLayout() {
        setCompositionRoot(hLayout);
        setSizeFull();

        hLayout.setHeight(100, UNITS_PERCENTAGE);
        hLayout.setSpacing(true);
        hLayout.setMargin(true);
    }

    private void createButtonsAndListeners() {
        newBtn =
            new Button(ViewConstants.NEW, new ShowNewResourceListener(
                resourceViewImpl));

        openResourceListener =
            new OpenResourceListener(mainWindow, resourceService,
                resourceContainer, this);
        openBtn = new Button(ViewConstants.OPEN, openResourceListener);

        closeResourceListener =
            new CloseResourceListener(mainWindow, resourceService,
                resourceContainer, this);
        closeBtn = new Button(ViewConstants.CLOSE, closeResourceListener);

        delResourceListener =
            new DelResourceListener(app, mainWindow, resourceService,
                resourceContainer);
        delBtn = new Button(ViewConstants.DELETE, delResourceListener);
    }

    public void bind(final Item item) {
        this.item = item;
        bindUserRightsWithView();
        bindStatusAndRightWithView();
        openResourceListener.bind(item);
        closeResourceListener.bind(item);
        delResourceListener.bind(item);
    }

    private void bindUserRightsWithView() {
        newBtn.setVisible(isCreatePermitted());
        delBtn.setVisible(isDeletePermitted());
    }

    private boolean isOpenPermitted() {
        return pdpRequest.isPermitted(ActionIdConstants.OPEN_ORG_UNIT,
            getSelectedItemId());
    }

    private boolean isClosePermitted() {
        return pdpRequest.isPermitted(ActionIdConstants.CLOSE_ORG_UNIT,
            getSelectedItemId());
    }

    private boolean isDeletePermitted() {
        return pdpRequest.isPermitted(ActionIdConstants.DELETE_ORG_UNIT,
            getSelectedItemId());
    }

    private boolean isCreatePermitted() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_ORG_UNIT,
            getSelectedItemId());
    }

    private boolean isUpdateDenied() {
        return pdpRequest.isDenied(ActionIdConstants.CREATE_ORG_UNIT,
            getSelectedItemId());
    }

    private boolean isUpdatePermitted() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_ORG_UNIT,
            getSelectedItemId());
    }

    private String getSelectedItemId() {
        return (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
    }

    private void bindStatusAndRightWithView() {
        switch (getPublicStatus()) {
            case CREATED: {
                openBtn.setVisible(true && isOpenPermitted());
                closeBtn.setVisible(false);
                delBtn.setVisible(true && isDeletePermitted());

                resourceViewImpl.setFormReadOnly(false || isUpdateDenied());
                resourceViewImpl.setFooterVisible(true && isUpdatePermitted());
                break;
            }
            case OPENED: {
                openBtn.setVisible(false);
                closeBtn.setVisible(true && isClosePermitted());
                delBtn.setVisible(false);

                resourceViewImpl.setFormReadOnly(false || isUpdateDenied());
                resourceViewImpl.setFooterVisible(true && isUpdatePermitted());
                break;
            }
            case CLOSED: {
                openBtn.setVisible(false);
                closeBtn.setVisible(false);
                delBtn.setVisible(false);

                resourceViewImpl.setFormReadOnly(true);
                resourceViewImpl.setFooterVisible(false);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown status");
            }
        }
    }

    private PublicStatus getPublicStatus() {
        final String status =
            (String) item.getItemProperty(PropertyId.PUBLIC_STATUS).getValue();
        return PublicStatus.valueOf(status.toUpperCase());
    }
}