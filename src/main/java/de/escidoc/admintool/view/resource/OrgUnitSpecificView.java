package de.escidoc.admintool.view.resource;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.MetadataExtractor;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parents;

public class OrgUnitSpecificView {

    private static final long serialVersionUID = -3927641436455665147L;

    private static final Logger LOG = LoggerFactory
        .getLogger(OrgUnitSpecificView.class);

    private final FormLayout formLayout;

    private FieldsBinder binder;

    private final Button editParentBtn = new Button(ViewConstants.EDIT);

    private final HorizontalLayout hLayout = new HorizontalLayout();

    final OrgUnitServiceLab orgUnitService;

    final Window mainWindow;

    final ResourceContainer resourceContainer;

    private EditParentListener editParentListener;

    Item item;

    Label parentsValue;

    private ModalWindow modalWindow;

    private final Button removeParentBtn = new Button(ViewConstants.REMOVE);

    private RemoveParentListener removeParentListener;

    private final Map<String, Field> fieldByName;

    private TextField countryField;

    private MetadataExtractor metadataExtractor;

    private TextField coordinatesField;

    private TextField cityField;

    private TextField typeField;

    private TextField identifierField;

    private TextField alternativeField;

    public OrgUnitSpecificView(final Window mainWindow,
        final OrgUnitServiceLab orgUnitService,
        final ResourceContainer resourceContainer, final FormLayout formLayout,
        final Map<String, Field> fieldByName) {

        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(resourceContainer,
            "resourceContainer is null: %s", resourceContainer);
        Preconditions.checkNotNull(formLayout, "formLayout is null: %s",
            formLayout);
        Preconditions.checkNotNull(formLayout, "formLayout is null: %s",
            formLayout);

        this.mainWindow = mainWindow;
        this.orgUnitService = orgUnitService;
        this.resourceContainer = resourceContainer;
        this.formLayout = formLayout;
        this.fieldByName = fieldByName;

        init();
    }

    private String getXLinkTitle(final Parents parents) {
        if (parents == null || parents.isEmpty()) {
            return "no parents";
        }
        return parents.get(0).getXLinkTitle();
    }

    private void init() {
        addParentField();
        addParentEditor();
        addPubmanMetadata();
    }

    private void addParentEditor() {
        createEditParentListener();
        addEditParentListener();
        addRemoveParentListener();
    }

    private void addParentField() {
        final Label parentsLabel = new Label(ViewConstants.PARENTS_LABEL + ":");
        parentsValue = new Label();

        editParentBtn.setStyleName(Reindeer.BUTTON_SMALL);
        removeParentBtn.setStyleName(Reindeer.BUTTON_SMALL);

        hLayout.setSpacing(true);
        hLayout.addComponent(parentsLabel);
        hLayout.addComponent(parentsValue);
        hLayout.addComponent(editParentBtn);
        hLayout.addComponent(removeParentBtn);

        formLayout.addComponent(hLayout);
    }

    private void createEditParentListener() {
        modalWindow =
            new ModalWindow(resourceContainer, orgUnitService, mainWindow);
        editParentListener = new EditParentListener(mainWindow, modalWindow);
    }

    private void addRemoveParentListener() {
        removeParentListener = new RemoveParentListener(this);
        removeParentBtn.addListener(removeParentListener);
    }

    private void addPubmanMetadata() {
        addAlternativeTitleField();
        addIdentifierField();
        addOrgType();
        addCountryField();
        addCityField();
        addCoordinates();
    }

    private void addCoordinates() {
        coordinatesField = new TextField(ViewConstants.COORDINATES_LABEL);
        coordinatesField.setWidth(ViewConstants.FIELD_WIDTH);
        configure(coordinatesField);
        fieldByName.put("coordinates", coordinatesField);
        formLayout.addComponent(coordinatesField);
    }

    private void addOrgType() {
        typeField = new TextField(ViewConstants.TYPE_LABEL);
        typeField.setWidth(ViewConstants.FIELD_WIDTH);
        configure(typeField);
        fieldByName.put("type", typeField);
        formLayout.addComponent(typeField);
    }

    private void addIdentifierField() {
        identifierField = new TextField(ViewConstants.IDENTIFIER_LABEL);
        identifierField.setWidth(ViewConstants.FIELD_WIDTH);
        configure(identifierField);
        fieldByName.put("identifier", identifierField);
        formLayout.addComponent(identifierField);
    }

    private void addAlternativeTitleField() {
        alternativeField = new TextField(ViewConstants.ALTERNATIVE_LABEL);
        alternativeField.setWidth(ViewConstants.FIELD_WIDTH);
        configure(alternativeField);
        fieldByName.put("alternative", alternativeField);
        formLayout.addComponent(alternativeField);
    }

    private void addCityField() {
        cityField = new TextField(ViewConstants.CITY_LABEL);
        cityField.setWidth(ViewConstants.FIELD_WIDTH);
        configure(cityField);
        fieldByName.put("city", cityField);
        formLayout.addComponent(cityField);
    }

    private void addCountryField() {
        countryField = createTextField(ViewConstants.COUNTRY_LABEL);
        setWidth(countryField);
        configure(countryField);
        fieldByName.put("country", countryField);
        addToLayout(countryField);
    }

    private void addToLayout(final TextField textField) {
        formLayout.addComponent(textField);
    }

    private static void setWidth(final TextField textField) {
        textField.setWidth(ViewConstants.FIELD_WIDTH);
    }

    private static TextField createTextField(final String caption) {
        return new TextField(caption);
    }

    private static void configure(final TextField field) {
        field.setImmediate(false);
        field.setInvalidCommitted(false);
        field.setNullRepresentation("");
        field.setNullSettingAllowed(false);
        field.setWriteThrough(false);
    }

    private void addEditParentListener() {
        editParentBtn.addListener(editParentListener);
    }

    public void bind(final Item item) {
        Preconditions.checkNotNull(item, "item is null: %s", item);
        this.item = item;

        final ObjectProperty parentProperty = createParentProperty();
        binder = new ParentOrgUnitBinder(this, parentProperty);
        binder.bindFields();
        modalWindow.setParentProperty(parentProperty);
        removeParentListener.setParentProperty(parentProperty);
        editParentListener.bind(item);

        bindPubmanMetadata();
    }

    private void bindPubmanMetadata() {
        metadataExtractor = new MetadataExtractor(getSelectedOrgUnit());

        bindAlternativeTitle();
        bindIdentifier();
        bindType();
        bindCountry();
        bindCity();
        bindCoodinates();
    }

    private void bindCoodinates() {
        // coordinatesField.setValue(metadataExtractor
        // .get(AppConstants.KML_COORDINATES));
        coordinatesField
            .setPropertyDataSource(createObjectProperty(AppConstants.KML_COORDINATES));
    }

    private ObjectProperty createObjectProperty(final String value) {
        return new ObjectProperty(metadataExtractor.get(value));
    }

    private void bindType() {
        // typeField.setValue(metadataExtractor
        // .get(AppConstants.ETERMS_ORGANIZATION_TYPE));

        typeField
            .setPropertyDataSource(createObjectProperty(AppConstants.ETERMS_ORGANIZATION_TYPE));

    }

    private void bindIdentifier() {
        // identifierField.setValue(metadataExtractor
        // .get(AppConstants.DC_IDENTIFIER));

        identifierField
            .setPropertyDataSource(createObjectProperty(AppConstants.DC_IDENTIFIER));
    }

    private void bindAlternativeTitle() {
        // alternativeField.setValue(metadataExtractor
        // .get(AppConstants.DCTERMS_ALTERNATIVE));

        alternativeField
            .setPropertyDataSource(createObjectProperty(AppConstants.DCTERMS_ALTERNATIVE));
    }

    private void bindCity() {
        // cityField.setValue(metadataExtractor.get(AppConstants.ETERMS_CITY));

        cityField
            .setPropertyDataSource(createObjectProperty(AppConstants.ETERMS_CITY));
    }

    private void bindCountry() {
        // countryField.setValue(metadataExtractor
        // .get(AppConstants.ETERMS_COUNTRY));
        //

        countryField
            .setPropertyDataSource(createObjectProperty(AppConstants.ETERMS_COUNTRY));
    }

    private OrganizationalUnit getSelectedOrgUnit() {
        try {
            return orgUnitService.findById(getObjectId());
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(mainWindow, e);
        }
        return new OrganizationalUnit();
    }

    String getObjectId() {
        return (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
    }

    private ObjectProperty createParentProperty() {
        final ObjectProperty parentProperty =
            new ObjectProperty(
                getXLinkTitle(getParentsFromItem(PropertyId.PARENTS)),
                String.class, false);
        return parentProperty;
    }

    private Parents getParentsFromItem(final Object propertyId) {
        return (Parents) item.getItemProperty(propertyId).getValue();
    }

    public void setNotEditable(final boolean isReadOnly) {
        alternativeField.setReadOnly(isReadOnly);
        identifierField.setReadOnly(isReadOnly);
        typeField.setReadOnly(isReadOnly);
        cityField.setReadOnly(isReadOnly);
        countryField.setReadOnly(isReadOnly);
        coordinatesField.setReadOnly(isReadOnly);
        editParentBtn.setVisible(!isReadOnly);
        removeParentBtn.setVisible(!isReadOnly);
        // predecessorTypeSelect.setReadOnly(isReadOnly);
        // addPredecessorButton.setVisible(!isReadOnly);
    }
}