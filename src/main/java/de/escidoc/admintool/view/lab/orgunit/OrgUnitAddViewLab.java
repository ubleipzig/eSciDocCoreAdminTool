package de.escidoc.admintool.view.lab.orgunit;

import com.google.common.base.Preconditions;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import de.escidoc.admintool.domain.OrgUnitBuilder;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.PredecessorType;
import de.escidoc.admintool.view.orgunit.editor.IPredecessorEditor;
import de.escidoc.admintool.view.orgunit.predecessor.AbstractPredecessorView;
import de.escidoc.admintool.view.resource.ResourceContainer;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.PredecessorForm;

public class OrgUnitAddViewLab extends AbstractOrgUnitViewLab {

    private static final Logger LOG = LoggerFactory
        .getLogger(OrgUnitAddViewLab.class);

    private final ObjectProperty titleProperty = new ObjectProperty("",
        String.class);

    private final ObjectProperty descProperty = new ObjectProperty("",
        String.class);

    private final HierarchicalContainer container;

    private OrgUnitViewLab orgUnitView;

    private final OrgUnitContainerFactory orgUnitContainerFactory;

    public OrgUnitAddViewLab(final OrgUnitService orgUnitService,
        final HierarchicalContainer container, final Window mainWindow,
        final OrgUnitContainerFactory orgUnitContainerFactory,
        OrgUnitServiceLab orgUnitServiceLab, ResourceContainer resourceContainer) {
        super(orgUnitService, mainWindow, orgUnitServiceLab, resourceContainer);
        // super.postInit();
        Preconditions.checkNotNull(container);
        this.container = container;
        this.orgUnitContainerFactory = orgUnitContainerFactory;
        mapObjectWithProperty();
    }

    public void setOrgUnitView(final OrgUnitViewLab orgUnitView) {
        this.orgUnitView = orgUnitView;
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
                editor.setMainWindow(mainWindow);
                editor.setList(predecessorTypeSelect);
                editor.setOrgUnitEditorView(this);
                mainWindow.addWindow(window);
            }
            else {
                mainWindow.addWindow(new ErrorDialog(mainWindow, "Error",
                    "Enter a title first, please."));
            }
        }
        catch (final ClassNotFoundException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final InstantiationException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final IllegalAccessException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final SecurityException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final NoSuchMethodException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final IllegalArgumentException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final InvocationTargetException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
    }

    @Override
    protected void saveClicked(final ClickEvent event) {
        removeComponentErrors();
        updateView(storeInRepository(createOrgUnit()));
        resetFields();
    }

    private void resetFields() {
        titleField.setValue("");
        descriptionField.setValue("");
        alternativeField.setValue("");
        identifierField.setValue("");
        orgTypeField.setValue("");
        cityField.setValue("");
        countryField.setValue("");
        coordinatesField.setValue("");
        parentList.removeAllItems();
        predecessorTypeSelect.setValue("");

        titleField.setComponentError(null);
        descriptionField.setComponentError(null);
        alternativeField.setComponentError(null);
        identifierField.setComponentError(null);
        orgTypeField.setComponentError(null);
        cityField.setComponentError(null);
        countryField.setComponentError(null);
        coordinatesField.setComponentError(null);
        parentList.setComponentError(null);
        predecessorTypeSelect.setComponentError(null);
    }

    private OrganizationalUnit createOrgUnit() {
        try {
            return new OrgUnitBuilder()
                .with((String) titleField.getValue(),
                    (String) descriptionField.getValue())
                .parents(getSelectedParents())
                .predecessors(getSelectedPredecessors(),
                    getSelectedPredecessorForm())
                .identifier((String) identifierField.getValue())
                .alternative((String) alternativeField.getValue())
                .type((String) orgTypeField.getValue())
                .country((String) countryField.getValue())
                .city((String) cityField.getValue())
                .coordinates((String) coordinatesField.getValue()).build();
        }
        catch (final ParserConfigurationException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            ModalDialog.show(mainWindow, e);
        }
        catch (final SAXException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            ModalDialog.show(mainWindow, e);
        }
        catch (final IOException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            ModalDialog.show(mainWindow, e);
        }
        return null;
    }

    private void removeComponentErrors() {
        titleField.setComponentError(null);
        descriptionField.setComponentError(null);
    }

    private OrganizationalUnit storeInRepository(
        final OrganizationalUnit orgUnit) {
        try {
            return service.create(orgUnit);
        }
        catch (final EscidocException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            ModalDialog.show(mainWindow, e);
        }
        catch (final InternalClientException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            ModalDialog.show(mainWindow, e);
        }
        catch (final TransportException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            ModalDialog.show(mainWindow, e);
        }
        return orgUnit;
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
                    LOG.info("saving: " + ref.getTitle());
                    predecessors.add(ref.getObjectId());
                }
            }
            else {
                LOG.info("saving: " + resourceRefDisplay.getTitle());
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
                // TODO Auto-generated catch block
            }
        }
        return predecessorForm;
    }

    @Override
    protected void cancelClicked(final ClickEvent event) {
        resetFields();
    }

    @Override
    protected Component addToolbar() {
        return new Label();
    }

    private void updateView(final OrganizationalUnit createdOrgUnit) {
        assert createdOrgUnit != null : "createdOrgUnit can not be null.";
        assert orgUnitView != null : "orgUnitView can not be null.";

        try {
            orgUnitContainerFactory.addToContainer(createdOrgUnit, null);
            final Item addedItem =
                orgUnitContainerFactory.getItem(createdOrgUnit);
            orgUnitView.showEditView(addedItem);
        }
        catch (final EscidocException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            ModalDialog.show(mainWindow, e);
        }
        catch (final InternalClientException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            ModalDialog.show(mainWindow, e);
        }
        catch (final TransportException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
            ModalDialog.show(mainWindow, e);
        }
    }
}