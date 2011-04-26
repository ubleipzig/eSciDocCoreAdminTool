package de.escidoc.admintool.view.user;

import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.app.Command;
import de.escidoc.admintool.view.role.RevokeGrantCommand;
import de.escidoc.admintool.view.role.RevokeGrantWindow;
import de.escidoc.core.resources.aa.useraccount.Grant;

final class RemoveRoleButtonListener implements Button.ClickListener {

    /**
     * 
     */
    private final UserEditForm userEditForm;

    /**
     * @param userEditForm
     */
    RemoveRoleButtonListener(UserEditForm userEditForm) {
        this.userEditForm = userEditForm;
    }

    private static final long serialVersionUID = -605606788213049694L;

    @Override
    public void buttonClick(final ClickEvent event) {
        final Object selectedGrants = this.userEditForm.roleTable.getValue();

        if (selectedGrants instanceof Set<?>) {
            for (final Object grant : ((Set<?>) selectedGrants)) {
                if (grant instanceof Grant) {
                    this.userEditForm.app.getMainWindow().addWindow(createModalWindow((Grant) grant).getModalWindow());
                }
            }
        }
    }

    private RevokeGrantWindow createModalWindow(final Grant grant) {
        return new RevokeGrantWindow(createRevokeGrantCommand(grant), grant, this.userEditForm.grantContainer);
    }

    private Command createRevokeGrantCommand(final Grant grant) {
        return new RevokeGrantCommand(this.userEditForm.userService, this.userEditForm.userObjectId, grant);
    }
}