package de.escidoc.admintool.view.user.password;

import org.joda.time.DateTime;

import de.escidoc.core.client.exceptions.EscidocClientException;

public interface UpdatePasswordCommand {

    void setSelectedUserId(final String selectedUserId);

    void setLastModificationDate(final DateTime lastModificationDate);

    void execute(String newPassword) throws EscidocClientException;

}
