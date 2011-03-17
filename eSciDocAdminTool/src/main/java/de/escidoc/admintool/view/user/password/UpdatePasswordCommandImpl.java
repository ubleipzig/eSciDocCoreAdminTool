package de.escidoc.admintool.view.user.password;

import org.joda.time.DateTime;

import de.escidoc.admintool.service.UserService;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class UpdatePasswordCommandImpl implements UpdatePasswordCommand {

    private final UserService userService;

    private String selectedUserId;

    private DateTime lastModificationDate;

    public UpdatePasswordCommandImpl(final UserService userService) {
        this.userService = userService;
    }

    public void setSelectedUserId(final String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public void setLastModificationDate(final DateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    @Override
    public void execute(final String newPassword) throws EscidocClientException {
        userService.updatePassword(selectedUserId, newPassword,
            lastModificationDate);
    }

}
