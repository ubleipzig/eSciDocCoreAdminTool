package de.escidoc.admintool.view.resource;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.vaadin.data.Item;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;

public abstract class AbstractResourceBtnListener
    implements ResourceBtnListener {

    private static final long serialVersionUID = -4962907806742105879L;

    private final ResourceBtnListenerData data = new ResourceBtnListenerData();

    protected AbstractResourceBtnListener(final Collection<Field> allFields,
        final Map<String, Field> fieldByName, final Window mainWindow,
        final ResourceService resourceService) {
        data.allFields = allFields;
        data.fieldByName = fieldByName;
        data.mainWindow = mainWindow;
        data.resourceService = resourceService;
    }

    public ResourceBtnListenerData getData() {
        return data;
    }

    @Override
    public void bind(final Item item) {
        data.item = item;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        if (validateAllFields()) {
            saveToReposity();
            if (isSuccessfull()) {
                removeValidationErrors();
                commitAllFields();
                updateResourceContainer();
                // updateItem();
                // checkPostConditions(child);
                showSuccessMessage();
            }
            else {
                showErrorMessage();
            }
        }
    }

    private void removeValidationErrors() {
        for (final Field field : data.allFields) {
            ((AbstractComponent) field).setComponentError(null);
        }
    }

    private boolean validateAllFields() {
        for (final Field field : data.allFields) {
            try {
                field.validate();
            }
            catch (final Exception e) {
                ((AbstractComponent) field).setComponentError(new UserError(
                    field.getCaption() + " is required"));
                return false;
            }
        }
        return true;
    }

    private void showErrorMessage() {
        throw new UnsupportedOperationException("not-yet-implemented.");

    }

    private boolean isSuccessfull() {
        return true;
    }

    private void saveToReposity() {
        try {
            updateModel();
            updatePersistence();
            for (final Field field : data.allFields) {
                field.commit();
            }
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(data.mainWindow, e);
        }
        catch (final ParserConfigurationException e) {
            ModalDialog.show(data.mainWindow, e);
        }
        catch (final SAXException e) {
            ModalDialog.show(data.mainWindow, e);
        }
        catch (final IOException e) {
            ModalDialog.show(data.mainWindow, e);
        }
    }

    private void commitAllFields() {
        for (final Field field : data.allFields) {
            field.commit();
        }
    }

    private void showSuccessMessage() {
        data.mainWindow.showNotification(getSucessMessage());
    }

    protected abstract void updateModel() throws ParserConfigurationException,
        SAXException, IOException, EscidocClientException;

    protected abstract void updatePersistence() throws EscidocClientException;

    protected abstract String getSucessMessage();

    protected abstract void updateResourceContainer();

    protected String getCity() {
        return (String) getData().fieldByName.get("city").getValue();
    }

    protected String getCountry() {
        return (String) getData().fieldByName.get("country").getValue();
    }

    protected String getDescription() {
        return (String) getData().fieldByName.get("description").getValue();
    }

    protected String getTitle() {
        return (String) getData().fieldByName.get("title").getValue();
    }

    protected String getAlternative() {
        return (String) getData().fieldByName.get("alternative").getValue();
    }

    protected String getIdentifier() {
        return (String) getData().fieldByName.get("identifier").getValue();
    }

    protected String getCoordinates() {
        return (String) getData().fieldByName.get("coordinates").getValue();
    }

    protected String getType() {
        return (String) getData().fieldByName.get("type").getValue();
    }
}