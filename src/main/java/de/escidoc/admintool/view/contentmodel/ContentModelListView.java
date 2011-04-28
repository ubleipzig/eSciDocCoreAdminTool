package de.escidoc.admintool.view.contentmodel;

import com.vaadin.ui.Component;

import de.escidoc.core.client.exceptions.EscidocClientException;

public interface ContentModelListView extends Component {
    void init() throws EscidocClientException;
}
