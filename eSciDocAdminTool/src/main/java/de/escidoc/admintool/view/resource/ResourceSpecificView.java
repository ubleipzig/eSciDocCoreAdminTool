package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;

public interface ResourceSpecificView extends Component {

    void bind(final Item item);
}
