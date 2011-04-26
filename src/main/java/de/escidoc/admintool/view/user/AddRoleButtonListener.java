package de.escidoc.admintool.view.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;

final class AddRoleButtonListener implements Button.ClickListener {

    private static final Logger LOG = LoggerFactory.getLogger(AddRoleButtonListener.class);

    private final UserEditForm userEditForm;

    AddRoleButtonListener(final UserEditForm userEditForm) {
        this.userEditForm = userEditForm;
    }

    private static final long serialVersionUID = 2520625502594778921L;

    @Override
    public void buttonClick(final ClickEvent event) {
        try {
            userEditForm.app.showRoleView();
            userEditForm.app.showRoleView(userEditForm.userService.getUserById(userEditForm.userObjectId));
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(userEditForm.app.getMainWindow(), e);
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
    }
}