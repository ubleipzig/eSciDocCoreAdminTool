package de.escidoc.admintool.app;

import com.vaadin.ui.Component;

import de.escidoc.core.client.exceptions.EscidocClientException;

public interface ContentModelListView extends Component {
    void init() throws EscidocClientException;
}
