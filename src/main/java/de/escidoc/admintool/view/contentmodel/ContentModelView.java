package de.escidoc.admintool.view.contentmodel;

import com.vaadin.ui.Component;

import de.escidoc.core.resources.Resource;

public interface ContentModelView extends Component {
    void init();

    void showAddView();

    void showEditView(Resource contentModel);
}
