package de.escidoc.admintool.app;

import de.escidoc.core.client.exceptions.EscidocClientException;

public interface Command {
    void execute() throws EscidocClientException;
}
