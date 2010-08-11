package de.escidoc.admintool.view.orgunit;

import static de.escidoc.admintool.view.ViewConstants.ALTERNATIVE_ID;
import static de.escidoc.admintool.view.ViewConstants.CITY_ID;
import static de.escidoc.admintool.view.ViewConstants.COUNTRY_ID;
import static de.escidoc.admintool.view.ViewConstants.IDENTIFIER_ID;
import static de.escidoc.admintool.view.ViewConstants.ORG_TYPE_ID;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.MetadataExtractor;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parent;

@SuppressWarnings("serial")
public class OrgUnitList extends Table {

    // TODO move this to another class
    private Collection<OrganizationalUnit> allOrgUnits;

    private final AdminToolApplication app;

    private final OrgUnitService service;

    public OrgUnitList(final AdminToolApplication app,
        final OrgUnitService orgUnitService) {
        this.app = app;
        service = orgUnitService;

        buildUI();
        // TODO should not be in constructor
        // TODO handle exception in the right abstraction. Tell the user what
        // happens.
        try {
            bindDataSource();
        }
        catch (final EscidocException e) {
            // TODO Auto-generated catch block
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
    }

    private void buildUI() {
        setSizeFull();
        setColumnReorderingAllowed(true);
        setSelectable(true);
        setImmediate(true);
        this.addListener((ValueChangeListener) app);
        setNullSelectionAllowed(false);
    }

    private void bindDataSource() throws EscidocException,
        InternalClientException, TransportException {
        allOrgUnits = service.all();
        setContainerDataSource(new POJOContainer<OrganizationalUnit>(
            allOrgUnits, PropertyId.OBJECT_ID, PropertyId.NAME,
            PropertyId.DESCRIPTION, PropertyId.CREATED_ON,
            PropertyId.CREATED_BY, PropertyId.LAST_MODIFICATION_DATE,
            PropertyId.MODIFIED_BY, PropertyId.PARENTS, PropertyId.PREDECESSORS));
        setVisibleColumns(new Object[] { PropertyId.NAME });
        sort(new Object[] { PropertyId.LAST_MODIFICATION_DATE },
            new boolean[] { false });
        setColumnHeader(PropertyId.NAME, ViewConstants.TITLE_LABEL);
    }

    // TODO create a delegate for OrgUnit.java to for fetching and storing MPDL
    // Metadata
    private void addColumnsHeader() {
        this.addContainerProperty(ALTERNATIVE_ID, String.class, "");
        this.addContainerProperty(IDENTIFIER_ID, String.class, "");
        this.addContainerProperty(ORG_TYPE_ID, String.class, "");
        this.addContainerProperty(COUNTRY_ID, String.class, "");
        this.addContainerProperty(CITY_ID, String.class, "");
        this.addContainerProperty(ViewConstants.COORDINATES_ID, String.class,
            "");
        this.addContainerProperty(ViewConstants.START_DATE_ID, String.class,
            null);
        this.addContainerProperty(ViewConstants.END_DATE_ID, String.class, "");
        this.addContainerProperty(ViewConstants.PARENTS_ID, Collection.class,
            "");
        this.addContainerProperty(ViewConstants.PREDECESSORS_ID,
            Collection.class, "");
    }

    private void setColumnHeaders() {
        setColumnHeader(PropertyId.NAME, ViewConstants.TITLE_LABEL);
        // this.setColumnHeaders(new String[] { ViewConstants.OBJECT_ID_LABEL,
        // ViewConstants.TITLE_LABEL, ViewConstants.DESCRIPTION_LABEL,
        // ViewConstants.CREATED_ON_LABEL, ViewConstants.CREATED_BY_LABEL,
        // ViewConstants.MODIFIED_ON_LABEL, ViewConstants.MODIFIED_BY_LABEL,
        // ViewConstants.ALTERNATIVE_LABEL, ViewConstants.IDENTIFIER_LABEL,
        // ViewConstants.TYPE_LABEL, ViewConstants.COUNTRY_LABEL,
        // ViewConstants.CITY_LABEL, ViewConstants.COORDINATES_LABEL,
        // ViewConstants.START_DATE_LABEL, ViewConstants.END_DATE_LABEL,
        // ViewConstants.PARENTS_LABEL, ViewConstants.PREDECESSORS_LABEL });
    }

    // TODO Bad design
    // TODO refactor string to constants
    public void addOrgUnit(final OrganizationalUnit ou) {
        final MetadataExtractor metadataExtractor = new MetadataExtractor(ou);
        final String alternative = metadataExtractor.get("dcterms:alternative");
        final String identifier = metadataExtractor.get("dc:identifier");
        final String orgType =
            metadataExtractor.get("eterms:organization-type");
        final String country = metadataExtractor.get("eterms:country");
        final String city = metadataExtractor.get("eterms:city");
        final String coordinate = metadataExtractor.get("kml:coordinates");
        final String startDate = metadataExtractor.get("eterms:start-date");
        final String endDate = metadataExtractor.get("eterms:end-date");

        String description = ou.getProperties().getDescription();
        if (description == null) {
            description = "";
        }
        Collection<String> predecessorsObjectId = null;

        predecessorsObjectId = service.getPredecessorsObjectId(ou);
        this.addItem(new Object[] { ou.getObjid(),
            ou.getProperties().getName(), description,
            ou.getProperties().getCreationDate().toDate(),
            ou.getProperties().getCreatedBy().getObjid(),
            ou.getLastModificationDate().toDate(),
            ou.getProperties().getModifiedBy().getObjid(), alternative,
            identifier, orgType, country, city, coordinate, startDate, endDate,
            getParentsObjectId(ou), predecessorsObjectId }, ou.getObjid());
    }

    private Collection<String> getParentsObjectId(final OrganizationalUnit ou) {
        if (ou.getParents().getParentRef() == null) {
            return Collections.emptyList();
        }
        return getParentsByObjectId(ou).keySet();
    }

    private Collection<Parent> getParents(final OrganizationalUnit ou) {
        if (ou.getParents().getParentRef() == null) {
            return Collections.emptyList();
        }
        return ou.getParents().getParentRef();
    }

    private Map<String, Parent> getParentsByObjectId(final OrganizationalUnit ou) {

        final Map<String, Parent> parentByObjectId =
            new ConcurrentHashMap<String, Parent>();

        final Iterator<Parent> iterator = ou.getParents().iterator();

        assert iterator != null : "iterator can not be null.";
        while (iterator.hasNext()) {
            final Parent parent = iterator.next();
            parentByObjectId.put(parent.getObjid(), parent);
        }
        return parentByObjectId;
    }

    public Collection<OrganizationalUnit> getAllOrgUnits() {
        return allOrgUnits;
    }
}