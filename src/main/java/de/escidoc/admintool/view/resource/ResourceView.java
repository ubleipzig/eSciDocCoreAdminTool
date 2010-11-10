package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;

public interface ResourceView extends Component {

    void showAddView();

    void showEditView(Item item);

}
