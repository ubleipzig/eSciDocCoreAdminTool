package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;

import de.escidoc.core.client.exceptions.EscidocClientException;

public interface Updateable {

    void updatePersistence() throws EscidocClientException;

    void updateResourceContainer() throws EscidocClientException;

    void updateItem();

    void checkPostConditions();

    void bind(Item item);

}
