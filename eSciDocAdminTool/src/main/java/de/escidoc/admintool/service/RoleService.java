package de.escidoc.admintool.service;

import de.escidoc.core.client.Authentication;

public class RoleService {

    private final Authentication authentification;

    public RoleService(final Authentication authentification) {
        this.authentification = authentification;
    }

}
