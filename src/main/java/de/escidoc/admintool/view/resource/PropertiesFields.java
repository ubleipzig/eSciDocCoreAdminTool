package de.escidoc.admintool.view.resource;

import java.util.Collection;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

public interface PropertiesFields extends Component {

    void bind(Item item);

    Collection<Field> getAllFields();

}
