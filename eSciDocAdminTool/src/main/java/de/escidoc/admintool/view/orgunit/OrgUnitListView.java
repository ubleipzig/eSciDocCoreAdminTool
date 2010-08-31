package de.escidoc.admintool.view.orgunit;

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
public class OrgUnitListView extends Table {

    // TODO move this to another class
    private Collection<OrganizationalUnit> allOrgUnits;

    private final AdminToolApplication app;

    private final OrgUnitService service;

    private POJOContainer<OrganizationalUnit> dataSource;

    public OrgUnitListView(final AdminToolApplication app,
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
        dataSource =
            new POJOContainer<OrganizationalUnit>(allOrgUnits,
                PropertyId.OBJECT_ID, PropertyId.NAME, PropertyId.DESCRIPTION,
                PropertyId.CREATED_ON, PropertyId.CREATED_BY,
                PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
                PropertyId.PUBLIC_STATUS, PropertyId.PUBLIC_STATUS_COMMENT,
                PropertyId.PARENTS, PropertyId.PREDECESSORS);
        setContainerDataSource(dataSource);
        setVisibleColumns(new Object[] { PropertyId.NAME });
        sort(new Object[] { PropertyId.LAST_MODIFICATION_DATE },
            new boolean[] { false });
        setColumnHeader(PropertyId.NAME, ViewConstants.TITLE_LABEL);
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
        dataSource.addItem(ou);
        sort(new Object[] { PropertyId.LAST_MODIFICATION_DATE },
            new boolean[] { false });
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

    public void removeOrgUnit(final OrganizationalUnit selected) {
        assert selected != null : "orgUnit must not be null.";

        final boolean succesful = dataSource.removeItem(selected);

        assert succesful == true : "Removing context to the list failed.";
    }

    public void updateOrgUnit(
        final OrganizationalUnit oldOrgUnit, final OrganizationalUnit newOrgUnit) {
        removeOrgUnit(oldOrgUnit);
        addOrgUnit(newOrgUnit);
        sort(new Object[] { ViewConstants.MODIFIED_ON_ID },
            new boolean[] { false });
        setValue(newOrgUnit);
    }
}