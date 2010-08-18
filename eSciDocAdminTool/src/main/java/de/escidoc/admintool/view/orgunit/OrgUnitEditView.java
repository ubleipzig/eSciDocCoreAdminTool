package de.escidoc.admintool.view.orgunit;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.vaadin.utilities.Converter;
import de.escidoc.vaadin.utilities.LayoutHelper;

public class OrgUnitEditView extends AbstractOrgUnitView {
    private static final long serialVersionUID = -1488130998058019932L;

    private static final Logger log = LoggerFactory
        .getLogger(OrgUnitEditView.class);

    private final Button save = new Button("Save", this);

    private final Button cancel = new Button("Cancel", this);

    private final Button edit = new Button("Edit", this);

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Label objIdField = new Label();

    private final Label modifiedOn = new Label();

    private final Label modifiedBy = new Label();

    private final Label createdOn = new Label();

    private final Label createdBy = new Label();

    private Item item;

    private final AdminToolApplication app;

    private final OrganizationalUnit newOrganizationalUnit = null;

    private OrgUnitService service = null;

    // private final Map<String, OrganizationalUnit> orgUnitById;
    private final int HEIGHT = 15;

    public OrgUnitEditView(final AdminToolApplication app,
        final OrgUnitService service) throws EscidocException,
        InternalClientException, TransportException,
        UnsupportedOperationException {
        super(app, service);
        assert service != null : "Service must not be null.";
        assert app != null : "Aervice must not be null.";
        this.app = app;
        this.service = service;
        middleInit();
        super.postInit();

        // addFooter();
        //
        // orgUnitById = service.getOrgUnitById();
        //
        // for (final OrganizationalUnit orgUnit : service
        // .getOrganizationalUnits()) {
        // parents.addItem(orgUnit.getObjid());
        // }
    }

    private void middleInit() {
        form.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL,
            objIdField, labelWidth, false));

        form.addComponent(LayoutHelper.create("Modified", "by", modifiedOn,
            modifiedBy, labelWidth, HEIGHT, false));

        form.addComponent(LayoutHelper.create("Created", "by", createdOn,
            createdBy, labelWidth, HEIGHT, false));
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();

        if (source == save) {
            /* If the given input is not valid there is no point in continuing */
            // if (!isValid()) {
            // return;
            // }

            // update new data to the repository
            // TODO the GUI is blocked if update takes too long.
            try {
                final OrganizationalUnit updatedOU = update();
                service.update(updatedOU);
                // commit();
            }
            catch (final EscidocException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
            catch (final InternalClientException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
            catch (final TransportException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
            catch (final ParserConfigurationException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
            catch (final SAXException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
            catch (final IOException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();
            }
            setReadOnly(true);
        }
        else if (source == cancel) {
            /* Clear the form and make it invisible */
            // this.setItemDataSource(null);
            // }
            // else {
            // // discard();
            // }
            setReadOnly(true);
        }
        else if (source == edit) {

            setReadOnly(false);

        }
    }

    private OrganizationalUnit update() throws ParserConfigurationException,
        SAXException, IOException, EscidocException, InternalClientException,
        TransportException {
        // TODO slow operation, refactor this. We already have collection of org
        // units
        final OrganizationalUnit toBeUpdate =
            service.retrieve(getSelectedOrgUnitId());

        // TODO code duplication @see OrgUnitAddForm.java
        // final String country = (String) getField(COUNTRY_ID).getValue();
        // final String city = (String) getField(CITY_ID).getValue();
        // // final Set<String> parents =
        // // (Set<String>) this.getField(PARENTS_ID).getValue();
        //
        // final TwinColSelect twinColSelect =
        // (TwinColSelect) getField(PARENTS_ID);
        // final Set<String> parents = (Set<String>) twinColSelect.getValue();
        //
        // System.out.println("Update parents to: ");
        // for (final String parent : parents) {
        // System.out.println(parent);
        // }
        //
        // final OrganizationalUnit updatedOrgUnit =
        // new OrgUnitFactory()
        // .update(toBeUpdate,
        // (String) getField(ViewConstants.TITLE_ID).getValue(),
        // (String) getField(ViewConstants.DESCRIPTION_ID).getValue())
        // .alternative(
        // (String) getField(ViewConstants.ALTERNATIVE_ID).getValue())
        // .identifier(
        // (String) getField(ViewConstants.IDENTIFIER_ID).getValue())
        // .orgType((String) getField(ORG_TYPE_ID).getValue())
        // .country(country)
        // .city(city)
        // .coordinates(
        // (String) getField(ViewConstants.COORDINATES_ID).getValue())
        // .parents(parents).build();

        OrganizationalUnit updatedOrgUnit = null;
        return updatedOrgUnit;
    }

    private String getSelectedOrgUnitId() {
        final String objid = null;
        // (String) getItemDataSource().getItemProperty(
        // ViewConstants.OBJECT_ID).getValue();
        return objid;
    }

    public void setSelected(final Item item) {
        this.item = item;
        if (item != null) {
            // PropertyId.OBJECT_ID, PropertyId.NAME, PropertyId.DESCRIPTION,
            // PropertyId.CREATED_ON, PropertyId.CREATED_BY,
            // PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
            // PropertyId.PARENTS, PropertyId.PREDECESSORS
            titleField.setPropertyDataSource(item
                .getItemProperty(PropertyId.NAME));

            descriptionField.setPropertyDataSource(item
                .getItemProperty(PropertyId.DESCRIPTION));

            objIdField.setPropertyDataSource(item
                .getItemProperty(PropertyId.OBJECT_ID));

            modifiedOn.setCaption(Converter
                .dateTimeToString((org.joda.time.DateTime) item
                    .getItemProperty(PropertyId.LAST_MODIFICATION_DATE)
                    .getValue()));

            modifiedBy.setPropertyDataSource(item
                .getItemProperty(PropertyId.MODIFIED_BY));

            createdOn.setCaption(Converter
                .dateTimeToString((org.joda.time.DateTime) item
                    .getItemProperty(PropertyId.CREATED_ON).getValue()));

            createdBy.setPropertyDataSource(item
                .getItemProperty(PropertyId.CREATED_BY));

            orgUnitListSelect.setPropertyDataSource(item
                .getItemProperty(PropertyId.PARENTS));
        }
    }

    @Override
    protected String getViewCaption() {
        return "Edit " + ViewConstants.ORGANIZATION_UNITS_LABEL;
    }

    @Override
    protected void onAddPredecessorClicked() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void saveClicked(ClickEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void cancelClicked(ClickEvent event) {
        // TODO Auto-generated method stub

    }
}