package de.escidoc.admintool.view.resource;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Window;
import de.escidoc.admintool.view.ViewConstants;

public final class MetadataListener implements ValueChangeListener {

    private static final long serialVersionUID = -189510362510619884L;

    private final FormLayout formLayout;

    private final Window mainWindow;

    public MetadataListener(final Window mainWindow, final FormLayout formLayout) {
        this.mainWindow = mainWindow;
        this.formLayout = formLayout;
    }

    @Override
    public void valueChange(final ValueChangeEvent event) {
        final Object value = event.getProperty().getValue();
        if (value instanceof String) {
            final String metaDataType = (String) value;

            if (metaDataType.equals(ViewConstants.PUB_MAN_METADATA)) {
                showPubManMetadataFields();
            }
            else if (metaDataType.equals(ViewConstants.RAW_XML)) {
                showRawXmlTextArea();
            }
            else if (metaDataType.equals(ViewConstants.FREE_FORM)) {
                showFreeForm();
            }
        }
    }

    private void showFreeForm() {
        mainWindow.addWindow(new FreeFormWindow(mainWindow));
    }

    private void showPubManMetadataFields() {
        mainWindow.addWindow(new PubmanWindow(mainWindow));

    }

    private void showRawXmlTextArea() {
        mainWindow.addWindow(new RawXmlWindow(mainWindow));
    }

}