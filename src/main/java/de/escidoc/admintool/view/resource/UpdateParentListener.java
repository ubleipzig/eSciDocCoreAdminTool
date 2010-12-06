package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.service.OrgUnitServiceLab;

final class UpdateParentListener extends AbstractModifyParentOrgUnitListener
    implements ClickListener {

    protected static final long serialVersionUID = 7022982222058387053L;

    protected final ModalWindow modalWindow;

    public UpdateParentListener(final ModalWindow modalWindow,
        final OrgUnitServiceLab orgUnitService) {

        Preconditions.checkNotNull(modalWindow, "modalWindow is null: %s",
            modalWindow);
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService is null: %s", orgUnitService);

        this.modalWindow = modalWindow;

        super.orgUnitService = orgUnitService;
        super.resourceContainer = modalWindow.resourceContainer;
    }

    @Override
    protected void onButtonClick() {
        closeWindow();
        updateParent();
    }

    private void closeWindow() {
        modalWindow.closeWindow();
    }

    @Override
    protected String getSelectedParentId() {
        return modalWindow.selectedParent;
    }
}