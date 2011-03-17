package de.escidoc.admintool.service;

import de.escidoc.core.client.exceptions.InternalClientException;

public interface LoginService {

    void loginWith(String handle) throws InternalClientException;
}
