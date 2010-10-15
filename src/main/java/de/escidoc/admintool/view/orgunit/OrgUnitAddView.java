package de.escidoc.admintool.view.orgunit;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.OrgUnitFactory;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.editor.IPredecessorEditor;
import de.escidoc.admintool.view.orgunit.predecessor.AbstractPredecessorView;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.PredecessorForm;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class OrgUnitAddView extends AbstractOrgUnitView
    implements ClickListener, Serializable {
    private static final long serialVersionUID = 8351229526921020901L;

    private static final Logger log = LoggerFactory
        .getLogger(OrgUnitAddView.class);

    private final ObjectProperty titleProperty = new ObjectProperty("",
        String.class);

    private final ObjectProperty descProperty = new ObjectProperty("",
        String.class);

    public OrgUnitAddView(final AdminToolApplication app,
        final OrgUnitService service) {
        super(app, service);
        super.postInit();
        super.orgUnitList = app.getOrgUnitTable();
        mapObjectWithProperty();
    }

    private void mapObjectWithProperty() {
        titleField.setPropertyDataSource(titleProperty);
        descriptionField.setPropertyDataSource(descProperty);
    }

    @Override
    public void showAddedPredecessors(
        final AbstractPredecessorView addedPredecessorView) {
        predecessorLayout.replaceComponent(predecessorResult,
            addedPredecessorView);
        predecessorResult = addedPredecessorView;
    }

    private OrganizationalUnit storeInRepository(
        final OrganizationalUnit orgUnit) throws EscidocException,
        InternalClientException, TransportException {
        return service.create(orgUnit);
    }

    @Override
    protected String getViewCaption() {
        return "Add " + ViewConstants.ORGANIZATION_UNITS_LABEL;
    }

    @Override
    protected void onAddPredecessorClicked() {
        try {
            if (titleField.getValue() != null
                && (!((String) titleField.getValue()).isEmpty())) {
                Object selectObject = predecessorTypeSelect.getValue();
                if (selectObject == null) {
                    selectObject = PredecessorType.BLANK;
                }
                final Class<?>[] argsClass =
                    new Class<?>[] { OrgUnitService.class };
                final Object[] args = new Object[] { service };

                final Class<?> c =
                    Class.forName(((PredecessorType) selectObject)
                        .getExecutionClass());
                final Constructor<?> constructor = c.getConstructor(argsClass);

                final Object object = constructor.newInstance(args);

                final IPredecessorEditor editor = (IPredecessorEditor) object;
                editor.setNewOrgUnit((String) titleField.getValue());
                final Window window = editor.getWidget();
                window.setModal(true);
                editor.setMainWindow(app.getMainWindow());
                editor.setList(predecessorTypeSelect);
                editor.setOrgUnitEditorView(this);
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

        }
        catch (final InstantiationException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final IllegalAccessException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final SecurityException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final NoSuchMethodException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final IllegalArgumentException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final InvocationTargetException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
    }

    // TODO this is crazy. Data binding to the rescue for later.
    @Override
    protected void saveClicked(final ClickEvent event) {
        titleField.setComponentError(null);
        descriptionField.setComponentError(null);

        final Set<String> parents = getSelectedParents();

        try {
            final PredecessorForm predecessorForm =
                getSelectedPredecessorForm();
            final Set<String> predecessors = getSelectedPredecessors();
            final OrganizationalUnit createdOrgUnit =
                storeInRepository(new OrgUnitFactory()
                    .create((String) titleField.getValue(),
                        (String) descriptionField.getValue()).parents(parents)
                    .predecessors(predecessors, predecessorForm)
                    .identifier((String) identifierField.getValue())
                    .alternative((String) alternativeField.getValue())
                    .orgType((String) orgTypeField.getValue())
                    .country((String) countryField.getValue())
                    .city((String) cityField.getValue())
                    .coordinates((String) coordinatesField.getValue()).build());
            orgUnitList.addOrgUnit(createdOrgUnit);
            orgUnitList.select(createdOrgUnit);
        }
        catch (final EscidocException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final InternalClientException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final TransportException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final ParserConfigurationException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final SAXException e) {
            log.error("An unexpected error occured! See log for details.", e);
        }
        catch (final IOException e) {
            log.error("An unexpected error occured! See log for details.", e);
        }
    }

    private Set<String> getSelectedPredecessors() {
        final Set<String> predecessors = new HashSet<String>();
        if (predecessorResult.isSelected()) {
            final ResourceRefDisplay resourceRefDisplay =
                predecessorResult.getResourceRefDisplay();
            if (resourceRefDisplay == null) {
                final List<ResourceRefDisplay> resourceRefList =
                    predecessorResult.getResourceRefList();
                for (final ResourceRefDisplay ref : resourceRefList) {
                    log.info("saving: " + ref.getTitle());
                    predecessors.add(ref.getObjectId());
                }
            }
            else {
                log.info("saving: " + resourceRefDisplay.getTitle());
                predecessors.add(resourceRefDisplay.getObjectId());
            }

        }

        return predecessors;
    }

    private PredecessorForm getSelectedPredecessorForm() {
        final PredecessorType predecessorType =
            (PredecessorType) predecessorTypeSelect.getValue();
        PredecessorForm predecessorForm = null;
        if (predecessorType != null) {
            try {
                predecessorForm =
                    PredecessorForm.fromString(predecessorType
                        .toString().toLowerCase());
                return predecessorForm;
            }
            catch (final EscidocClientException e) {
                log.error("An unexpected error occured! See log for details.",
                    e);
            }
        }
        return predecessorForm;
    }

    // TODO: discard changes
    @Override
    protected void cancelClicked(final ClickEvent event) {
        super.app.showOrganizationalUnitView();
    }

    @Override
    protected Component addToolbar() {
        return new Label();
    }
}