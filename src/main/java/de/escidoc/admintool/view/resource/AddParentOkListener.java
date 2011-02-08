package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

final class AddParentOkListener extends AbstractAddOrChangeParentListener {

    private static final long serialVersionUID = 3458364398356140363L;

    private final OrgUnitSpecificView orgUnitSpecificView;

    public AddParentOkListener(
        final AddOrEditParentModalWindow addOrEditParentModalWindow,
        final OrgUnitServiceLab orgUnitService,
        final OrgUnitSpecificView orgUnitSpecificView) {

        Preconditions.checkNotNull(addOrEditParentModalWindow,
            "modalWindow is null: %s", addOrEditParentModalWindow);
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(orgUnitSpecificView,
            "orgUnitSpecificView is null: %s", orgUnitSpecificView);

        super.addOrEditParentModalWindow = addOrEditParentModalWindow;
        super.orgUnitService = orgUnitService;
        super.resourceContainer = addOrEditParentModalWindow.resourceContainer;
        this.orgUnitSpecificView = orgUnitSpecificView;
    }

    @Override
    public void buttonClick(final ClickEvent arg0) {
        addOrUpdateParent();
        addOrEditParentModalWindow.closeWindow();
    }

    @Override
    protected void addOrUpdateParent() {
        addParent();
    }

    private void addParent() {
        try {
            final OrganizationalUnit selected = getSelectedParent();
            final ResourceRefDisplay resourceRefDisplay =
                new ResourceRefDisplay(selected.getObjid(),
                    selected.getXLinkTitle());
            getParentProperty().setValue(resourceRefDisplay);
            orgUnitSpecificView.showRemoveButton();
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(getMainWindow(), e);
        }
    }

}