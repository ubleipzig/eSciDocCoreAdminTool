package de.escidoc.admintool.view.orgunit;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.vaadin.dialog.ErrorDialog;

@SuppressWarnings("serial")
public class OrgUnitListView extends Table {

    private static final Logger log = LoggerFactory
        .getLogger(OrgUnitListView.class);

    private Collection<OrganizationalUnit> allOrgUnits;

    private final AdminToolApplication app;

    private final OrgUnitService orgUnitService;

    private POJOContainer<OrganizationalUnit> orgUnitContainer;

    public OrgUnitListView(final AdminToolApplication app,
        final OrgUnitService orgUnitService) {
        assert app != null : "app must not be null.";
        assert orgUnitService != null : "orgUnitService must not be null.";
        this.app = app;
        this.orgUnitService = orgUnitService;
        buildView();
        findAllOrgUnits();
        bindDataSource();
    }

    private void buildView() {
        setSizeFull();
        setSelectable(true);
        setImmediate(true);
        addListener((ValueChangeListener) app);
        setNullSelectionAllowed(false);
    }

    private void bindDataSource() {
        if (isOrgUnitExist()) {
            initOrgUnitContainer();
        }
    }

    private void findAllOrgUnits() {
        try {
            allOrgUnits = orgUnitService.findAll();
        }
        catch (final EscidocException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See log for details."));
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final InternalClientException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See log for details."));
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final TransportException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error",
                    "An unexpected error occured! See log for details."));
            log.error("An unexpected error occured! See log for details.", e);

        }
    }

    private boolean isOrgUnitExist() {
        return !allOrgUnits.isEmpty();
    }

    private void initOrgUnitContainer() {
        orgUnitContainer =
            new POJOContainer<OrganizationalUnit>(allOrgUnits,
                PropertyId.OBJECT_ID, PropertyId.NAME, PropertyId.DESCRIPTION,
                PropertyId.CREATED_ON, PropertyId.CREATED_BY,
                PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
                PropertyId.PUBLIC_STATUS, PropertyId.PUBLIC_STATUS_COMMENT,
                PropertyId.PARENTS, PropertyId.PREDECESSORS);
        setContainerDataSource(orgUnitContainer);
        setVisibleColumns(new Object[] { PropertyId.NAME });
        sort(new Object[] { PropertyId.LAST_MODIFICATION_DATE },
            new boolean[] { false });
        setColumnHeader(PropertyId.NAME, ViewConstants.TITLE_LABEL);
    }

    public void addOrgUnit(final OrganizationalUnit ou) {
        if (orgUnitContainer == null) {
            findAllOrgUnits();
            initOrgUnitContainer();
        }
        String description = ou.getProperties().getDescription();
        if (description == null) {
            description = "";
        }

        orgUnitContainer.addItem(ou);
        sort(new Object[] { PropertyId.LAST_MODIFICATION_DATE },
            new boolean[] { false });
    }

    public Collection<OrganizationalUnit> getAllOrgUnits() {
        return allOrgUnits;
    }

    public void removeOrgUnit(final OrganizationalUnit selected) {
        assert selected != null : "orgUnit must not be null.";
        orgUnitContainer.removeItem(selected);
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