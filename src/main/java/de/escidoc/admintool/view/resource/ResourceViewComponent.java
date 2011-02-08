package de.escidoc.admintool.view.resource;

import com.vaadin.ui.Component;

import de.escidoc.core.client.exceptions.EscidocClientException;

public interface ResourceViewComponent {

    void init() throws EscidocClientException;

    Component getResourceView();

    void showFirstItemInEditView();

    // ResourceContainer getResourceContainer() throws EscidocException,
    // InternalClientException, TransportException;

    // ResourceContainer createResourceContainer() throws EscidocException,
    // InternalClientException, TransportException;
}
