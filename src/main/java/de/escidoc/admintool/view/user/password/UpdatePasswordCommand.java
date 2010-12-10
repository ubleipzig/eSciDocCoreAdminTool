package de.escidoc.admintool.view.user.password;

import de.escidoc.core.client.exceptions.EscidocClientException;

public interface UpdatePasswordCommand {

    void execute(String newPassword) throws EscidocClientException;

}
