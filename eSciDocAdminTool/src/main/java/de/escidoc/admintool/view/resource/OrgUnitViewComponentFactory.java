package de.escidoc.admintool.view.resource;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.ComboBox;
import de.escidoc.admintool.view.ViewConstants;

import java.util.Arrays;

public class OrgUnitViewComponentFactory {

    public static ComboBox createMetadataComboBox(
        final ValueChangeListener listener) {
        final ComboBox metadataComboBox =
            new ComboBox(ViewConstants.METADATA_LABEL,
                Arrays.asList(new String[] { ViewConstants.PUB_MAN_METADATA,
                    ViewConstants.RAW_XML, ViewConstants.FREE_FORM }));
        metadataComboBox.setNewItemsAllowed(false);
        metadataComboBox.setWidth(150, Sizeable.UNITS_PIXELS);
        metadataComboBox.addListener(listener);
        metadataComboBox.setImmediate(true);
        return metadataComboBox;
    }

}
