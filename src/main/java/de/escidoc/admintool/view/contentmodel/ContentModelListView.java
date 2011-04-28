package de.escidoc.admintool.view.contentmodel;

import com.vaadin.ui.Component;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

public interface ContentModelListView extends Component {
    void init() throws EscidocClientException;

    void setContentModelView(ContentModelView view);

    void setContentModel(Resource contentModel);
}
