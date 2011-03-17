package de.escidoc.admintool.view.resource;

import java.util.Collection;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

public interface PropertiesFields extends Component {

    void bind(Item item);

    Collection<Field> getAllFields();

    Map<String, Field> getFieldByName();

    void setNotEditable(boolean isReadOnly);

    void removeOthers();
}
