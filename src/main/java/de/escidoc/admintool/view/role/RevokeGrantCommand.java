package de.escidoc.admintool.view.role;

import de.escidoc.admintool.app.Command;
import de.escidoc.admintool.service.UserService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.Grant;

public class RevokeGrantCommand implements Command {

    private final UserService userService;

    private final String userId;

    private final Grant grant;

    private String comment;

    public RevokeGrantCommand(final UserService userService,
        final String userId, final Grant grant) {
        this.userService = userService;
        this.userId = userId;
        this.grant = grant;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    @Override
    public void execute() throws EscidocClientException {
        userService.revokeGrant(userId, grant, comment);
    }
}
