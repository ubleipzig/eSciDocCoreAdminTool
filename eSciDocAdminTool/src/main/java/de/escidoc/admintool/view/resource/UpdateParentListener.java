package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;

final class UpdateParentListener extends AbstractAddOrChangeParentListener {

    protected static final long serialVersionUID = 7022982222058387053L;

    public UpdateParentListener(final AddOrEditParentModalWindow addOrEditParentModalWindow,
        final OrgUnitServiceLab orgUnitService) {

        Preconditions.checkNotNull(addOrEditParentModalWindow, "modalWindow is null: %s", addOrEditParentModalWindow);
        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);

        super.addOrEditParentModalWindow = addOrEditParentModalWindow;
        super.orgUnitService = orgUnitService;
        super.resourceContainer = addOrEditParentModalWindow.resourceContainer;
    }

    @Override
    protected void addOrUpdateParent() {
        updateParent();
    }

    private void updateParent() {
        try {
            selectedOrgUnit = getChild();
            selectedParent = getSelectedParent();
            updatePersistence();
            updateResourceContainer();
            updateItem();
            checkPostConditions(selectedOrgUnit);
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(getMainWindow(), e);
        }
    }
}