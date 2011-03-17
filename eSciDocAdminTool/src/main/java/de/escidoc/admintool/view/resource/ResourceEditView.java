package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;

public interface ResourceEditView extends Component {

    void bind(Item item);

    void setFormReadOnly(boolean b);

    void setFooterVisible(boolean b);

}
