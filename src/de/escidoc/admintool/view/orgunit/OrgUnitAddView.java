package de.escidoc.admintool.view.orgunit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.ContextAddView;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class OrgUnitAddView {

    private final ContextAddView contextAddView;

    private final OrgUnitService orgUnitService;

    private Select select;

    public OrgUnitAddView(final ContextAddView contextAddView,
        final OrgUnitService orgUnitService) {
        this.contextAddView = contextAddView;
        this.orgUnitService = orgUnitService;
        initUI();
    }

    private OrganizationalUnitRefs organizationalUnitRefs;

    private void initUI() {
        contextAddView.getLayout().addComponent(new Label("Organizations"));

        try {
            final POJOContainer<OrganizationalUnit> pojoContainer =
                new POJOContainer<OrganizationalUnit>(orgUnitService
                    .getOrganizationalUnits(), "properties.name");
            select = new Select("", pojoContainer);
            select.setItemCaptionPropertyId("properties.name");

            select.setMultiSelect(true);
            select.setRequired(true);
            select.setRequiredError("Organization is required");
            // select.setComponentError(new
            // UserError("Organization is required"));
            contextAddView.addField(ViewConstants.ORGANIZATION_UNITS_LABEL,
                select);

            // TODO add debug button to debug user input
            final Button debugInputBtn = new Button("debug");
            debugInputBtn.setVisible(false);
            contextAddView.addField("debugOrgUnitBtn", debugInputBtn);

            // TODO if add clicked, do:
            // add more name, content and remove button
            debugInputBtn.addListener(new Button.ClickListener() {
                public void buttonClick(final ClickEvent event) {
                    buildOrgUnitRefs();
                }
            });
        }
        catch (final IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    private void buildOrgUnitRefs() {
        organizationalUnitRefs = new OrganizationalUnitRefs();
        final Set<OrganizationalUnit> values =
            (Set<OrganizationalUnit>) select.getValue();

        for (final OrganizationalUnit selected : values) {
            System.out.println("selected: " + selected.getObjid());

            final ResourceRef organizationalUnitRef =
                new ResourceRef(selected.getObjid());
            organizationalUnitRefs
                .addOrganizationalUnitRef(organizationalUnitRef);
        }
    }

    private Map<String, String> getOrgUnitsName() throws EscidocException,
        InternalClientException, TransportException {
        final Collection<OrganizationalUnit> organizationalUnits =
            orgUnitService.getOrganizationalUnits();
        final Map<String, String> orgUnitNameById =
            new HashMap<String, String>(organizationalUnits.size());
        for (final OrganizationalUnit organizationalUnit : organizationalUnits) {
            orgUnitNameById.put(organizationalUnit.getObjid(),
                organizationalUnit.getProperties().getName());
        }
        return orgUnitNameById;
    }

    public OrganizationalUnitRefs getSelectedOrgUnits() {
        buildOrgUnitRefs();
        return organizationalUnitRefs;
    }
}