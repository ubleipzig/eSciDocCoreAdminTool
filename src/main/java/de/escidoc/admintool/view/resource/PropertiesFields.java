package de.escidoc.admintool.view.resource;

import java.util.Collection;
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import de.escidoc.admintool.domain.PdpRequest;

public interface PropertiesFields extends Component {

    void bind(Item item);

    Collection<Field> getAllFields();

    Map<String, Field> getFieldByName();

    void setNotEditable(boolean isReadOnly);

    void removeOthers();

    void setPdpRequest(PdpRequest pdpRequest);

    void setDescriptionRequired();
}
