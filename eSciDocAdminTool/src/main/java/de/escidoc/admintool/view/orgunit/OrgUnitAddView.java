package de.escidoc.admintool.view.orgunit;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.OrgUnitEditor;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.editor.IPredecessorEditor;
import de.escidoc.admintool.view.orgunit.predecessor.BlankPredecessorView;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.PredecessorForm;
import de.escidoc.vaadin.dialog.ErrorDialog;
import de.escidoc.vaadin.utilities.LayoutHelper;

@SuppressWarnings("serial")
public class OrgUnitAddView extends CustomComponent
    implements ClickListener, Serializable {

    private static final Logger log = LoggerFactory
        .getLogger(OrgUnitAddView.class);

    private final Button save = new Button("Save", this);

    private final Button cancel = new Button("Cancel", this);

    private final AdminToolApplication app;

    private final OrgUnitService service;

    private final Collection<OrganizationalUnit> allOrgUnits;

    private final OrgUnitList orgUnitList;

    private final FormLayout form = new FormLayout();

    private final Panel panel = new Panel();

    private final TextField titleField = new TextField();

    private final TextField descriptionField = new TextField();

    private final TextField alternativeField = new TextField();

    private final TextField identifierField = new TextField();

    private final TextField orgTypeField = new TextField();

    private final TextField cityField = new TextField();

    private final TextField countryField = new TextField();

    private final TextField coordinatesField = new TextField();

    private final ListSelect orgUnitListSelect = new ListSelect();

    private final Button addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton = new Button(
        ViewConstants.REMOVE_LABEL);

    private final Button addPredecessorButton = new Button(
        ViewConstants.ADD_LABEL);

    private AbstractComponent predecessorResult;

    private HorizontalLayout predecessorLayout;

    final HorizontalLayout footer = new HorizontalLayout();

    private final ListSelect predecessorTypeSelect = new ListSelect("",
        Arrays.asList(new PredecessorType[] { PredecessorType.BLANK,
            PredecessorType.SPLITTING, PredecessorType.FUSION,
            PredecessorType.SPIN_OFF, PredecessorType.AFFILIATION,
            PredecessorType.REPLACEMENT }));

    public OrgUnitAddView(final AdminToolApplication app,
        final OrgUnitService service,
        final Collection<OrganizationalUnit> allOrgUnits,
        final OrgUnitList orgUnitList) {
        this.app = app;
        this.service = service;
        this.allOrgUnits = allOrgUnits;
        this.orgUnitList = orgUnitList;
        init();
    }

    private void init() {
        setCompositionRoot(panel);
        panel.setCaption("Add " + ViewConstants.ORGANIZATION_UNITS_LABEL);
        panel.setContent(form);
        panel.setSizeUndefined();
        final int labelWidth = 140;

        // Title
        titleField.setWidth("400px");
        form.addComponent(LayoutHelper.create(ViewConstants.TITLE_LABEL,
            titleField, labelWidth, true));
        titleField.focus();
        // titleProperty = mapBinding("", titleField);

        // Description
        descriptionField.setWidth("400px");
        descriptionField.setRows(5);
        form.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL,
            descriptionField, labelWidth, 100, true));

        // Alternative Title
        alternativeField.setWidth("400px");
        form.addComponent(LayoutHelper.create(ViewConstants.ALTERNATIVE_LABEL,
            alternativeField, labelWidth, false));

        // identifier
        identifierField.setWidth("400px");
        form.addComponent(LayoutHelper.create(ViewConstants.IDENTIFIER_LABEL,
            identifierField, labelWidth, false));

        // Org Type
        orgTypeField.setWidth("400px");
        form.addComponent(LayoutHelper.create(ViewConstants.ORGANIZATION_TYPE,
            orgTypeField, labelWidth, false));

        // city
        cityField.setWidth("400px");
        form.addComponent(LayoutHelper.create("City", cityField, labelWidth,
            false));

        // Country
        countryField.setWidth("400px");
        form.addComponent(LayoutHelper.create("Country", countryField,
            labelWidth, false));

        // coordinates
        coordinatesField.setWidth("400px");
        form.addComponent(LayoutHelper.create(ViewConstants.COORDINATES_LABEL,
            coordinatesField, labelWidth, false));

        // Parent
        orgUnitListSelect.setRows(5);
        orgUnitListSelect.setWidth("400px");
        orgUnitListSelect.setNullSelectionAllowed(true);
        orgUnitListSelect.setMultiSelect(true);
        orgUnitListSelect.setImmediate(true);

        form.addComponent(LayoutHelper.create(ViewConstants.PARENTS_LABEL,
            new OrgUnitEditor("Add Parents", orgUnitListSelect,
                addOrgUnitButton, removeOrgUnitButton), labelWidth, 100, false,
            new Button[] { addOrgUnitButton, removeOrgUnitButton }));

        // Predecessor Type
        predecessorTypeSelect.setRows(1);
        predecessorTypeSelect.setImmediate(true);
        predecessorTypeSelect.setNullSelectionAllowed(false);

        final HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(predecessorTypeSelect);
        hl.setComponentAlignment(predecessorTypeSelect, Alignment.TOP_RIGHT);
        hl.addComponent(addPredecessorButton);
        hl.setComponentAlignment(addPredecessorButton, Alignment.MIDDLE_LEFT);
        predecessorTypeSelect.setWidth("400px");

        form.addComponent(LayoutHelper.create("Predessor Type", hl, labelWidth,
            40, false));
        predecessorResult = new BlankPredecessorView();
        predecessorLayout =
            LayoutHelper.create(ViewConstants.PREDECESSORS_LABEL,
                predecessorResult, labelWidth, 100, false);
        predecessorLayout.setSizeFull();
        form.addComponent(predecessorLayout);

        addPredecessorButton.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                onAddPredecessorClicked();
            }
        });
        form.addComponent(addFooter());
    }

    private void onAddPredecessorClicked() {
        try {
            if (titleField.getValue() != null
                && (!((String) titleField.getValue()).isEmpty())) {
                Object selectObject = predecessorTypeSelect.getValue();
                if (selectObject == null) {
                    selectObject = PredecessorType.BLANK;
                }
                final Class<?> c =
                    Class.forName(((PredecessorType) selectObject)
                        .getExecutionClass());
                final IPredecessorEditor editor =
                    (IPredecessorEditor) c.newInstance();
                editor.setNewOrgUnit((String) titleField.getValue());
                final Window window = editor.getWidget();
                window.setModal(true);
                editor.setMainWindow(app.getMainWindow());
                editor.setList(predecessorTypeSelect);
                editor.setOrgUnitAddView(this);
                app.getMainWindow().addWindow(window);
            }
            else {
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), "Error",
                        "Enter a title first, please."));

            }
        }
        catch (final ClassNotFoundException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final InstantiationException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (final IllegalAccessException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
    }

    public void showAddedPredecessors(
        final AbstractComponent addedPredecessorView) {
        predecessorLayout.replaceComponent(predecessorResult,
            addedPredecessorView);
        predecessorResult = addedPredecessorView;
    }

    private HorizontalLayout addFooter() {
        footer.setSpacing(true);
        footer.addComponent(save);
        footer.addComponent(cancel);
        return footer;
    }

    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source == save) {
            // setValidationVisible(true);
            if (validate()) {
                saveUserInput();
            }
        }
        else if (source == cancel) {
            // discard();
            app.showOrganizationalUnitView();
        }
        else {
            throw new RuntimeException("Unknown button.");
        }
    }

    private boolean validate() {
        boolean valid = true;
        valid =
            EmptyFieldValidator.isValid(titleField, "Please enter a "
                + ViewConstants.TITLE_ID);
        valid &=
            (EmptyFieldValidator.isValid(descriptionField, "Please enter a "
                + ViewConstants.DESCRIPTION_ID));
        return valid;
    }

    // try {

    @SuppressWarnings("unchecked")
    private void saveUserInput() {

        // TODO this is crazy. Data binding to the rescue for later.
        // final String title = (String) getField(TITLE_ID).getValue();
        // final String description = (String)
        // getField(DESCRIPTION_ID).getValue();
        // final String identifier = (String)
        // getField(IDENTIFIER_ID).getValue();
        // final String alternative = (String)
        // getField(ALTERNATIVE_ID).getValue();
        // final String orgType = (String) getField(ORG_TYPE_ID).getValue();
        // final String country = (String) getField(COUNTRY_ID).getValue();
        // final String city = (String) getField(CITY_ID).getValue();
        // final String coordinates =
        // (String) getField(ViewConstants.COORDINATES_ID).getValue();
        // final Set<String> parents =
        // (Set<String>) getField(PARENTS_ID).getValue();

        final Set<String> predecessors = null;
        final PredecessorForm predecessorType = null;

        final boolean addPredecessor = false;
        // TODO fix this! due possible bug in Vaadin.
        if (addPredecessor) {
            System.out.println("adding");
            // predecessors =
            // (Set<String>) getField(ViewConstants.PREDECESSORS_ID)
            // .getValue();
            // predecessorType =
            // (PredecessorForm) getField(ViewConstants.PREDECESSORS_TYPE_ID)
            // .getValue();

            System.out.println("type: " + predecessorType);

            for (final String predecessor : predecessors) {
                System.out.println("pre: " + predecessor);
            }
        }
        // try {
        // if (isValid()) {
        // final OrganizationalUnit createdOrgUnit =
        // storeInRepository(new OrgUnitFactory()
        // .create(title, description).parents(parents)
        // .predecessors(predecessors, predecessorType)
        // .identifier(identifier).alternative(alternative)
        // .orgType(orgType).country(country).city(city)
        // .coordinates(coordinates).build());
        // commit();
        // updateOrgUnitTableView(createdOrgUnit);
        // app.showOrganizationalUnitView();
        // }
        // }
        // TODO refactor exception handling
        // catch (final EscidocException e) {
        // if (e instanceof
        // de.escidoc.core.client.exceptions.application.invalid.InvalidStatusException)
        // {
        // ((ListSelect) getField(ViewConstants.PREDECESSORS_ID))
        // .setComponentError(new UserError(e.getHttpStatusMsg()));
        // System.out.println(e.getHttpStatusMsg());
        // }
        // e.printStackTrace();
        // }
        // catch (final InternalClientException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // catch (final TransportException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // catch (final ParserConfigurationException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // catch (final SAXException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // catch (final IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
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