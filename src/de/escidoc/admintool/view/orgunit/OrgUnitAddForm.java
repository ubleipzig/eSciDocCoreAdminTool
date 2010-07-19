package de.escidoc.admintool.view.orgunit;

import static de.escidoc.admintool.view.ViewConstants.ALTERNATIVE_ID;
import static de.escidoc.admintool.view.ViewConstants.CITY_ID;
import static de.escidoc.admintool.view.ViewConstants.COUNTRY_ID;
import static de.escidoc.admintool.view.ViewConstants.DESCRIPTION_ID;
import static de.escidoc.admintool.view.ViewConstants.IDENTIFIER_ID;
import static de.escidoc.admintool.view.ViewConstants.IDENTIFIER_LABEL;
import static de.escidoc.admintool.view.ViewConstants.ORGANIZATION_TYPE;
import static de.escidoc.admintool.view.ViewConstants.ORG_TYPE_ID;
import static de.escidoc.admintool.view.ViewConstants.PARENTS_ID;
import static de.escidoc.admintool.view.ViewConstants.TITLE_ID;
import static de.escidoc.admintool.view.ViewConstants.VISIBLE_PARENTS;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.vaadin.data.Property;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.OrgUnitFactory;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.PredecessorForm;

@SuppressWarnings("serial")
public class OrgUnitAddForm extends Form implements ClickListener, Serializable {

    private final Button save = new Button("Save", (ClickListener) this);

    private final Button cancel = new Button("Cancel", (ClickListener) this);

    private final AdminToolApplication app;

    private final OrgUnitService service;

    private final Collection<OrganizationalUnit> allOrgUnits;

    private final OrgUnitList orgUnitList;

    private ListSelect predecessorsListSelect;

    private Button addPredecessorLink;

    private Button removePredecessor;

    public OrgUnitAddForm(final AdminToolApplication app,
        final OrgUnitService service,
        final Collection<OrganizationalUnit> allOrgUnits,
        final OrgUnitList orgUnitList) {
        this.app = app;
        this.service = service;
        this.allOrgUnits = allOrgUnits;
        this.orgUnitList = orgUnitList;
        buildForm();
    }

    private void buildForm() {
        addTitleField()
            .addDescriptionField().addAlternativeField().addIdentifierField()
            .addOrgType().addCountryField().addCityField()
            .addCoordinatesField().addParentsListSelect()
            // .addPredecessorsListSelect().addPredecessorsType()
            .predessorAddButton().predecessorTypeComboBox()
            .removeAddPredecessor().predecessorListSelect();
        addFooter();
        setWriteThrough(false);
        setInvalidCommitted(false);
    }

    private OrgUnitAddForm addTitleField() {
        final TextField titleField = new TextField("Title");
        addField(TITLE_ID, titleField);
        titleField.setRequired(true);
        titleField.setRequiredError("Please enter a " + ViewConstants.TITLE_ID);
        titleField.setWidth("400px");
        return this;
    }

    private OrgUnitAddForm addDescriptionField() {
        final TextField descriptionField = new TextField("Description", "");
        descriptionField.setRequired(true);
        descriptionField.setRequiredError("Please enter a "
            + ViewConstants.DESCRIPTION_ID);
        descriptionField.setWidth("400px");
        addField(DESCRIPTION_ID, descriptionField);
        return this;
    }

    private OrgUnitAddForm addAlternativeField() {
        final TextField textField = new TextField("Alternative Title", "");
        textField.setWidth("400px");
        addField(ALTERNATIVE_ID, textField);
        return this;
    }

    private OrgUnitAddForm addIdentifierField() {
        final TextField identifierField = new TextField(IDENTIFIER_LABEL, "");
        identifierField.setWidth("400px");
        addField(IDENTIFIER_ID, identifierField);
        return this;
    }

    private OrgUnitAddForm addOrgType() {
        final TextField orgTypeField = new TextField(ORGANIZATION_TYPE, "");
        orgTypeField.setWidth("400px");
        addField(ORG_TYPE_ID, orgTypeField);
        return this;
    }

    private OrgUnitAddForm addCountryField() {
        final TextField countryField = new TextField("Country", "");
        countryField.setWidth("400px");
        addField(COUNTRY_ID, countryField);
        return this;
    }

    private OrgUnitAddForm addCityField() {
        final TextField countryField = new TextField("City", "");
        countryField.setWidth("400px");
        addField(CITY_ID, countryField);
        return this;
    }

    private OrgUnitAddForm addCoordinatesField() {
        final TextField coordinatesField =
            new TextField(ViewConstants.COORDINATES_LABEL, "");
        coordinatesField.setWidth("400px");
        addField(ViewConstants.COORDINATES_ID, coordinatesField);
        return this;
    }

    private OrgUnitAddForm addParentsListSelect() {
        // TODO move it to a data source class.
        final List<String> objIds = new ArrayList<String>();
        objIds.add("");
        for (final OrganizationalUnit ou : allOrgUnits) {
            objIds.add(ou.getObjid());
        }
        final ListSelect parentListSelect = new ListSelect("Parents", objIds);

        parentListSelect.setRows(VISIBLE_PARENTS);
        parentListSelect.setNullSelectionAllowed(true);
        parentListSelect.setMultiSelect(true);
        parentListSelect.setWidth("200px");
        addField(PARENTS_ID, parentListSelect);

        return this;
    }

    private OrgUnitAddForm predessorAddButton() {
        addPredecessorLink = new Button("Add Predecessor");
        addPredecessorLink.setStyleName(Button.STYLE_LINK);
        addField(ViewConstants.PREDECESSORS_TYPE_ID, addPredecessorLink);
        addPredecessorLink.addListener(new AddPredecessorListener());
        return this;
    }

    private ComboBox predecessorTypeComboBox;

    private OrgUnitAddForm predecessorTypeComboBox() {
        predecessorTypeComboBox =
            new ComboBox(ViewConstants.PREDECESSORS_TYPE_LABEL, Arrays
                .asList(PredecessorForm.values()));
        OrgUnitAddForm.this.addField(ViewConstants.PREDECESSORS_TYPE_ID,
            predecessorTypeComboBox);
        predecessorTypeComboBox.setImmediate(true);
        predecessorTypeComboBox.setVisible(false);
        predecessorTypeComboBox
            .addListener(new PredecessorTypeComboBoxListener());
        return this;
    }

    private List<String> objIds;

    private OrgUnitAddForm predecessorListSelect() {
        objIds = new ArrayList<String>();
        for (final OrganizationalUnit ou : allOrgUnits) {
            objIds.add(ou.getObjid());
        }
        predecessorsListSelect =
            new ListSelect(ViewConstants.PREDECESSORS_LABEL, objIds);

        predecessorsListSelect.setRows(VISIBLE_PARENTS);
        predecessorsListSelect.setMultiSelect(true);
        predecessorsListSelect.setVisible(false);

        OrgUnitAddForm.this.addField(ViewConstants.PREDECESSORS_ID,
            predecessorsListSelect);

        return this;
    }

    private boolean addPredecessor = false;

    private OrgUnitAddForm removeAddPredecessor() {
        removePredecessor = new Button("Remove");
        removePredecessor.setStyleName(Button.STYLE_LINK);
        OrgUnitAddForm.this.addField("removeAddPredecessor", removePredecessor);
        removePredecessor.addListener(new RemoveAddPredecessorListener());
        removePredecessor.setVisible(false);
        addPredecessor = false;
        return this;
    }

    private class RemoveAddPredecessorListener implements ClickListener {

        public void buttonClick(final ClickEvent event) {
            System.out.println("discard all changes.");
            predecessorTypeComboBox.discard();
            predecessorTypeComboBox.setVisible(false);

            predecessorsListSelect.discard();
            predecessorsListSelect.setVisible(false);

            removePredecessor.setVisible(false);

            addPredecessorLink.addListener(new AddPredecessorListener());
        }
    }

    private class PredecessorTypeComboBoxListener
        implements Property.ValueChangeListener {

        public void valueChange(
            final com.vaadin.data.Property.ValueChangeEvent event) {

            final PredecessorForm selected =
                (PredecessorForm) event.getProperty().getValue();

            if (selected.equals(PredecessorForm.FUSION)) {
                showMultipleSelection();
            }
            else {
                showMultipleSelection();
            }
        }

        private void showMultipleSelection() {
            predecessorsListSelect.setVisible(true);
        }
    }

    private class AddPredecessorListener implements ClickListener {

        public void buttonClick(final ClickEvent event) {
            predecessorTypeComboBox.setVisible(true);
            removePredecessor.setVisible(true);
            addPredecessorLink.removeListener(this);
            addPredecessor = true;
        }

    }

    private OrgUnitAddForm addFooter() {
        final HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addComponent(save);
        footer.addComponent(cancel);
        setFooter(footer);
        return this;
    }

    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source == save) {
            setValidationVisible(true);
            saveUserInput();
        }
        else if (source == cancel) {
            discard();
            app.showOrganizationalUnitView();
        }
        else {
            throw new RuntimeException("Unknown button.");
        }
    }

    @SuppressWarnings("unchecked")
    private void saveUserInput() {
        // TODO this is crazy. Data binding to the rescue for later.
        final String title = (String) getField(TITLE_ID).getValue();
        final String description = (String) getField(DESCRIPTION_ID).getValue();
        final String identifier = (String) getField(IDENTIFIER_ID).getValue();
        final String alternative = (String) getField(ALTERNATIVE_ID).getValue();
        final String orgType = (String) getField(ORG_TYPE_ID).getValue();
        final String country = (String) getField(COUNTRY_ID).getValue();
        final String city = (String) getField(CITY_ID).getValue();
        final String coordinates =
            (String) getField(ViewConstants.COORDINATES_ID).getValue();
        final Set<String> parents =
            (Set<String>) getField(PARENTS_ID).getValue();

        Set<String> predecessors = null;
        PredecessorForm predecessorType = null;

        // TODO fix this! due possible bug in Vaadin.
        if (addPredecessor) {
            System.out.println("adding");
            predecessors =
                (Set<String>) getField(ViewConstants.PREDECESSORS_ID)
                    .getValue();
            predecessorType =
                (PredecessorForm) getField(ViewConstants.PREDECESSORS_TYPE_ID)
                    .getValue();

            System.out.println("type: " + predecessorType);

            for (final String predecessor : predecessors) {
                System.out.println("pre: " + predecessor);
            }
        }
        try {
            if (isValid()) {
                final OrganizationalUnit createdOrgUnit =
                    storeInRepository(new OrgUnitFactory()
                        .create(title, description).parents(parents)
                        .predecessors(predecessors, predecessorType)
                        .identifier(identifier).alternative(alternative)
                        .orgType(orgType).country(country).city(city)
                        .coordinates(coordinates).build());
                commit();
                updateOrgUnitTableView(createdOrgUnit);
                app.showOrganizationalUnitView();
            }
        }
        // TODO refactor exception handling
        catch (final EscidocException e) {
            if (e instanceof de.escidoc.core.client.exceptions.application.invalid.InvalidStatusException) {
                ((ListSelect) getField(ViewConstants.PREDECESSORS_ID))
                    .setComponentError(new UserError(e.getHttpStatusMsg()));
                System.out.println(e.getHttpStatusMsg());
            }
            e.printStackTrace();
        }
        catch (final InternalClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (final TransportException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (final ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (final SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private OrganizationalUnit storeInRepository(
        final OrganizationalUnit orgUnit) throws EscidocException,
        InternalClientException, TransportException {
        return service.create(orgUnit);
    }

    private void updateOrgUnitTableView(final OrganizationalUnit createdOrgUnit) {
        // TODO: fix me. Bad! everytime we do changes in the repository we have
        // to change model and the view
        // we should only have to change the model, and the view will get notify
        // and update it self.
        // Investigate how to use Vaadin's DataBinding
        allOrgUnits.add(createdOrgUnit);
        orgUnitList.addOrgUnit(createdOrgUnit);
    }

    @Override
    public void setReadOnly(final boolean readOnly) {
        super.setReadOnly(readOnly);
        save.setVisible(!readOnly);
        cancel.setVisible(!readOnly);
    }
}