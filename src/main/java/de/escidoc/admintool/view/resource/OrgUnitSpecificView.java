/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package de.escidoc.admintool.view.resource;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
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
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;

public class OrgUnitSpecificView {

    private static final long serialVersionUID = -3927641436455665147L;

    private static final Logger LOG = LoggerFactory.getLogger(OrgUnitSpecificView.class);

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

    TextField parentsField;

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

    private AddOrEditParentModalWindow addOrEditParentModalWindow;

    private ObjectProperty<ResourceRefDisplay> parentProperty;

    public OrgUnitSpecificView(final Window mainWindow, final OrgUnitServiceLab orgUnitService,
        final ResourceContainer resourceContainer, final FormLayout formLayout, final Map<String, Field> fieldByName) {

        checkPreconditions(mainWindow, orgUnitService, resourceContainer, formLayout);

        this.mainWindow = mainWindow;
        this.orgUnitService = orgUnitService;
        this.resourceContainer = resourceContainer;
        this.formLayout = formLayout;
        this.fieldByName = fieldByName;
    }

    private void checkPreconditions(
        final Window mainWindow, final OrgUnitServiceLab orgUnitService, final ResourceContainer resourceContainer,
        final FormLayout formLayout) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(resourceContainer, "resourceContainer is null: %s", resourceContainer);
        Preconditions.checkNotNull(formLayout, "formLayout is null: %s", formLayout);
        Preconditions.checkNotNull(formLayout, "formLayout is null: %s", formLayout);
    }

    public void init() {
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
        parentsField = new TextField(ViewConstants.PARENTS_LABEL);
        parentsField.setReadOnly(true);
        parentsField.setWidth("300px");

        editParentBtn.setStyleName(Reindeer.BUTTON_SMALL);
        removeParentBtn.setStyleName(Reindeer.BUTTON_SMALL);

        hLayout.setSpacing(true);
        hLayout.addComponent(parentsField);
        hLayout.addComponent(editParentBtn);
        hLayout.addComponent(removeParentBtn);

        fieldByName.put("parents", parentsField);

        formLayout.addComponent(parentsField);
        formLayout.addComponent(hLayout);
    }

    private void createEditParentListener() {
        addOrEditParentModalWindow =
            new AddOrEditParentModalWindow(this, resourceContainer, orgUnitService, mainWindow);
        addOrEditParentModalWindow.addUpdateParentOkListener();
        editParentListener = new EditParentListener(mainWindow, addOrEditParentModalWindow);
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
        fieldByName.put(ViewConstants.CITY_LABEL, cityField);
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

    private void configure(final TextField field) {
        field.setPropertyDataSource(new ObjectProperty<String>(ViewConstants.EMPTY_STRING));
        field.setImmediate(false);
        field.setInvalidCommitted(false);
        field.setNullRepresentation(ViewConstants.EMPTY_STRING);
        field.setNullSettingAllowed(false);
        field.setWriteThrough(false);
    }

    private void addEditParentListener() {
        editParentBtn.addListener(editParentListener);
    }

    public void bind(final Item item) {
        Preconditions.checkNotNull(item, "item is null: %s", item);
        this.item = item;

        bindParents(item);
        binder.bindFields();
        bindPubmanMetadata();
    }

    private void bindParents(final Item item) {
        parentProperty = createParentProperty();
        binder = new ParentOrgUnitBinder(this, parentProperty);
        addOrEditParentModalWindow.setParentPropertyForUpdate(parentProperty);
        removeParentListener.setParentProperty(parentProperty);
        editParentListener.bind(item);
    }

    private ObjectProperty<ResourceRefDisplay> createParentProperty() {
        final ObjectProperty<ResourceRefDisplay> parentProperty =
            new ObjectProperty<ResourceRefDisplay>(new ResourceRefDisplay(), ResourceRefDisplay.class, false);
        if (hasParents(getParentsFromItem())) {
            final Parent parent = getParentsFromItem().get(0);
            parentProperty.setValue(new ResourceRefDisplay(parent.getObjid(), parent.getXLinkTitle()));
        }
        return parentProperty;
    }

    private boolean hasParents(final Parents parentsFromItem) {
        return parentsFromItem != null && parentsFromItem.size() > 0;
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
        coordinatesField.setPropertyDataSource(createObjectProperty(AppConstants.KML_COORDINATES));
    }

    private ObjectProperty<String> createObjectProperty(final String value) {
        return new ObjectProperty<String>(metadataExtractor.get(value));
    }

    private void bindType() {
        typeField.setPropertyDataSource(createObjectProperty(AppConstants.ETERMS_ORGANIZATION_TYPE));

    }

    private void bindIdentifier() {
        identifierField.setPropertyDataSource(createObjectProperty(AppConstants.DC_IDENTIFIER));
    }

    private void bindAlternativeTitle() {
        alternativeField.setPropertyDataSource(createObjectProperty(AppConstants.DCTERMS_ALTERNATIVE));
    }

    private void bindCity() {
        cityField.setPropertyDataSource(createObjectProperty(AppConstants.ETERMS_CITY));
    }

    private void bindCountry() {
        countryField.setPropertyDataSource(createObjectProperty(AppConstants.ETERMS_COUNTRY));
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

    private Parents getParentsFromItem() {
        return (Parents) item.getItemProperty(PropertyId.PARENTS).getValue();
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
    }

    public void setNoParents() {
        removeParentBtn.setVisible(false);
        editParentBtn.setCaption(ViewConstants.ADD);

        bindParentsForAddView();
    }

    private void bindParentsForAddView() {
        final ResourceRefDisplay resourceRefDisplay = new ResourceRefDisplay();
        final ObjectProperty<ResourceRefDisplay> parentPropertyForAddView =
            new ObjectProperty<ResourceRefDisplay>(resourceRefDisplay, ResourceRefDisplay.class, false);

        parentsField.setPropertyDataSource(parentPropertyForAddView);
        addOrEditParentModalWindow.setParentPropertyForAdd(parentPropertyForAddView);
    }

    public void addAddParentOkBtnListener() {
        addOrEditParentModalWindow.addAddParentOkLisner();
    }

    public void showRemoveButton() {
        removeParentBtn.setVisible(true);
        removeParentBtn.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = 8560744974716622255L;

            @Override
            public void buttonClick(final ClickEvent event) {
                parentsField.getPropertyDataSource().setValue(new ResourceRefDisplay());
                removeParentBtn.setVisible(false);
            }
        });
    }
}