package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;

import de.escidoc.core.resources.Resource;

public interface ResourceView extends Component {

    void showAddView();

    void showEditView(Item item);

    void selectInFolderView(Resource resource);

}
