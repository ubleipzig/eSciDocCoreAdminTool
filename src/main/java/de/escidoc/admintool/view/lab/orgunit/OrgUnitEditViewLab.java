package de.escidoc.admintool.view.lab.orgunit;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.MetadataExtractor;
import de.escidoc.admintool.domain.OrgUnitFactory;
import de.escidoc.admintool.messages.Messages;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.PublicStatus;
import de.escidoc.admintool.view.orgunit.PredecessorType;
import de.escidoc.admintool.view.orgunit.editor.IPredecessorEditor;
import de.escidoc.admintool.view.orgunit.predecessor.AbstractPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.AffiliationPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.BlankPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.SpinOffPredecessorView;
import de.escidoc.admintool.view.util.Converter;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;
import de.escidoc.core.resources.oum.Predecessor;
import de.escidoc.core.resources.oum.PredecessorForm;
import de.escidoc.core.resources.oum.Predecessors;

public class OrgUnitEditViewLab extends AbstractOrgUnitViewLab {

    private static final long serialVersionUID = -584963284131407616L;

    private static final Logger log = LoggerFactory
        .getLogger(OrgUnitEditViewLab.class);

    // TODO move this to constant, rename variable to a more meaningful name.
    private static final int HEIGHT = 15;

    private final Label objIdField = new Label();

    private final Label modifiedOn = new Label();

    private final Label modifiedBy = new Label();

    private final Label createdOn = new Label();

    private final Label createdBy = new Label();

    private final Label publicStatus = new Label();

    private final Label publicStatusComment = new Label();

    private OrgUnitToolbarLab toolbar;

    private Item item;

    private OrgUnitViewLab orgUnitViewLab;

    private final OrgUnitContainerFactory orgUnitContainerFactory;

    public OrgUnitEditViewLab(final OrgUnitService service,
        final Window mainWindow,
        final OrgUnitContainerFactory orgUnitContainerFactory) {
        super(service, mainWindow);

        this.orgUnitContainerFactory = orgUnitContainerFactory;
        middleInit();
        postInit();
    }

    private void middleInit() {
        form.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL,
            objIdField, ViewConstants.LABEL_WIDTH, false));

        form.addComponent(LayoutHelper.create("Modified", "by", modifiedOn,
            modifiedBy, ViewConstants.LABEL_WIDTH, HEIGHT, false));

        form.addComponent(LayoutHelper.create("Created", "by", createdOn,
            createdBy, ViewConstants.LABEL_WIDTH, HEIGHT, false));
        form.addComponent(LayoutHelper.create("Status", publicStatus,
            ViewConstants.LABEL_WIDTH, false));

        if (!publicStatusComment.getValue().equals(ViewConstants.EMPTY_STRING)) {
            form.addComponent(LayoutHelper.create("Status Comment",
                publicStatusComment, ViewConstants.LABEL_WIDTH, false));
        }
    }

    public void setOrgUnit(final Item item) {
        this.item = item;
        if (item != null) {
            final Property publicStatusProperty =
                item.getItemProperty(PropertyId.PUBLIC_STATUS);
            assert publicStatusProperty != null : "status can not be null";
            final String status = (String) publicStatusProperty.getValue();
            assert status != null : "status can not be null";
            publicStatus.setPropertyDataSource(publicStatusProperty);
            final PublicStatus publicStatus =
                PublicStatus.valueOf(status.toUpperCase());
            switch (publicStatus) {
                case CREATED: {
                    setFormReadOnly(false);
                    footer.setVisible(true);
                    break;
                }
                case OPENED: {
                    setFormReadOnly(false);
                    footer.setVisible(true);
                    break;
                }
                case CLOSED: {
                    setFormReadOnly(true);
                    footer.setVisible(false);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown status");
                }
            }
            titleField.setPropertyDataSource(item
                .getItemProperty(PropertyId.NAME));

            descriptionField.setPropertyDataSource(item
                .getItemProperty(PropertyId.DESCRIPTION));

            objIdField.setPropertyDataSource(item
                .getItemProperty(PropertyId.OBJECT_ID));
            objIdField.setValue(item.getItemProperty(PropertyId.OBJECT_ID));

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

            publicStatusComment.setPropertyDataSource(item
                .getItemProperty(PropertyId.PUBLIC_STATUS_COMMENT));

            final Parents parents =
                (Parents) item.getItemProperty(PropertyId.PARENTS).getValue();

            if (parents != null && parents.getParentRef() != null
                && parents.getParentRef().iterator() != null
                && parents.getParentRef().iterator().hasNext()) {
                final Parent parent = parents.getParentRef().iterator().next();

                String parentName;
                try {
                    parentName =
                        service
                            .find(parent.getObjid()).getProperties().getName();
                    parentList.removeAllItems();
                    parentList.addItem(new ResourceRefDisplay(
                        parent.getObjid(), parentName));
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
            else {
                parentList.removeAllItems();
            }
            // Predecessor
            bindPredecessor();
            if (publicStatus != PublicStatus.CLOSED) {
                final String objectId =
                    (String) item
                        .getItemProperty(PropertyId.OBJECT_ID).getValue();
                log.info("objectId: " + objectId);

                OrganizationalUnit orgUnit;
                try {
                    orgUnit = service.find(objectId);
                    assert orgUnit != null : "org Unit can not be null";
                    final MetadataExtractor metadataExtractor =
                        new MetadataExtractor(orgUnit);
                    final String alternative =
                        metadataExtractor.get("dcterms:alternative");
                    final String identifier =
                        metadataExtractor.get("dc:identifier");
                    final String orgType =
                        metadataExtractor.get("eterms:organization-type");
                    final String country =
                        metadataExtractor.get("eterms:country");
                    final String city = metadataExtractor.get("eterms:city");
                    final String coordinate =
                        metadataExtractor.get("kml:coordinates");

                    alternativeField.setValue(alternative);
                    identifierField.setValue(identifier);
                    orgTypeField.setValue(orgType);
                    countryField.setValue(country);
                    cityField.setValue(city);
                    coordinatesField.setValue(coordinate);
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
            toolbar.changeState(PublicStatus.valueOf(status.toUpperCase()));
        }

    }

    private void setFormReadOnly(final boolean isReadOnly) {
        titleField.setReadOnly(isReadOnly);
        descriptionField.setReadOnly(isReadOnly);
        alternativeField.setReadOnly(isReadOnly);
        identifierField.setReadOnly(isReadOnly);
        orgTypeField.setReadOnly(isReadOnly);
        cityField.setReadOnly(isReadOnly);
        countryField.setReadOnly(isReadOnly);
        coordinatesField.setReadOnly(isReadOnly);
        parentList.setReadOnly(isReadOnly);
        predecessorTypeSelect.setReadOnly(isReadOnly);
        addOrgUnitButton.setVisible(!isReadOnly);
        removeOrgUnitButton.setVisible(!isReadOnly);
        addPredecessorButton.setVisible(!isReadOnly);
    }

    private void bindPredecessor() {
        final Predecessors predecessors =
            (Predecessors) item
                .getItemProperty(PropertyId.PREDECESSORS).getValue();
        if (hasPredecessors()) {
            for (final Predecessor predecessor : predecessors
                .getPredecessorRef()) {

                final String predecessorObjectId = predecessor.getObjid();
                log.info("predecessor found: " + predecessorObjectId);

                final String predecessorTitle = getTitle(predecessorObjectId);
                log.info("predecessor found: " + predecessorTitle);

                final PredecessorForm predecessorForm = predecessor.getForm();
                log.info("predecessor found: " + predecessorForm);

                showPredecessorView(predecessorForm, predecessorObjectId,
                    predecessorTitle);
            }
        }
        else {
            final BlankPredecessorView blankPredecessorView =
                new BlankPredecessorView();
            predecessorLayout.replaceComponent(predecessorResult,
                blankPredecessorView);
            predecessorResult = blankPredecessorView;
        }
    }

    private boolean hasPredecessors() {
        final Predecessors predecessors =
            (Predecessors) item
                .getItemProperty(PropertyId.PREDECESSORS).getValue();
        return predecessors != null && predecessors.getPredecessorRef() != null
            && predecessors.getPredecessorRef().size() > 0;
    }

    private String getTitle(final String predecessorObjectId) {
        try {
            return service.findOrgUnitTitleById(predecessorObjectId);
        }
        catch (final EscidocException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow, Messages
                .getString("AdminToolApplication.15"), e.getMessage())); //$NON-NLS-1$
        }
        catch (final InternalClientException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow, Messages
                .getString("AdminToolApplication.15"), e.getMessage())); //$NON-NLS-1$
        }
        catch (final TransportException e) {
            mainWindow.addWindow(new ErrorDialog(mainWindow, Messages
                .getString("AdminToolApplication.15"), e.getMessage())); //$NON-NLS-1$
        }
        return ViewConstants.EMPTY_STRING;
    }

    private void showPredecessorView(
        final PredecessorForm predecessorForm,
        final String predecessorObjectId, final String predecessorTitle) {
        // TODO replace with factory
        final AbstractPredecessorView addedPredecessorView =
            createPredecessorView(predecessorForm, predecessorTitle);

        addedPredecessorView.setResourceRefDisplay(new ResourceRefDisplay(
            predecessorObjectId, predecessorTitle));
        predecessorLayout.replaceComponent(predecessorResult,
            addedPredecessorView);
        predecessorResult = addedPredecessorView;
    }

    private AbstractPredecessorView createPredecessorView(
        final PredecessorForm predecessorForm, final String predecessorTitle) {
        AbstractPredecessorView addedPredecessorView;
        switch (predecessorForm) {
            case SPINOFF: {
                // select predecessor type in combo box.
                predecessorTypeSelect.select(PredecessorType.SPIN_OFF);
                // show spin-off view
                addedPredecessorView =
                    new SpinOffPredecessorView(predecessorTitle,
                        (String) titleField.getValue());
                // TODO remove "add" predecessor button
                // TODO add "edit" predecessor button
                // TODO add "remove" predecessor button
                return addedPredecessorView;

            }
            case AFFILIATION: {
                // select predecessor type in combo box.
                predecessorTypeSelect.select(PredecessorType.AFFILIATION);
                // show spin-off view
                addedPredecessorView =
                    new AffiliationPredecessorView(predecessorTitle,
                        (String) titleField.getValue());
                // TODO remove "add" predecessor button
                // TODO add "edit" predecessor button
                // TODO add "remove" predecessor button
                return addedPredecessorView;

            }
                // TODO create appropriate view
            case SPLITTING: {
                // select predecessor type in combo box.
                predecessorTypeSelect.select(PredecessorType.SPLITTING);
                // show spin-off view
                addedPredecessorView =
                    new AffiliationPredecessorView(predecessorTitle,
                        (String) titleField.getValue());
                // TODO remove "add" predecessor button
                // TODO add "edit" predecessor button
                // TODO add "remove" predecessor button
                return addedPredecessorView;
            }
                // TODO create appropriate view
            case FUSION: {
                // select predecessor type in combo box.
                predecessorTypeSelect.select(PredecessorType.FUSION);
                // show spin-off view
                addedPredecessorView =
                    new AffiliationPredecessorView(predecessorTitle,
                        (String) titleField.getValue());
                // TODO remove "add" predecessor button
                // TODO add "edit" predecessor button
                // TODO add "remove" predecessor button
                return addedPredecessorView;

            }
            case REPLACEMENT: {
                // select predecessor type in combo box.
                predecessorTypeSelect.select(PredecessorType.REPLACEMENT);
                // show spin-off view
                addedPredecessorView =
                    new AffiliationPredecessorView(predecessorTitle,
                        (String) titleField.getValue());
                // TODO remove "add" predecessor button
                // TODO add "edit" predecessor button
                // TODO add "remove" predecessor button
                return addedPredecessorView;
            }
            default:
                throw new RuntimeException("Unsupported predecessor form");
        }
    }

    @Override
    protected String getViewCaption() {
        return ViewConstants.EDIT + ViewConstants.ORGANIZATION_UNITS_LABEL;
    }

    @Override
    protected Component addToolbar() {
        toolbar = new OrgUnitToolbarLab(mainWindow);
        return toolbar;
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

    @Override
    protected void saveClicked(final ClickEvent event) {
        final OrganizationalUnit updateOrgUnit = updateOrgUnit();
    }

    private OrganizationalUnit updateOrgUnit() {
        final Set<String> parents = getSelectedParents();
        // TODO update Predecessors;
        final Set<String> predecessors = null;
        OrganizationalUnit backedOrgUnit = null;
        try {
            final OrganizationalUnit oldOrgUnit =
                service.find((String) objIdField.getValue());
            backedOrgUnit =
                new OrgUnitFactory()
                    .update(oldOrgUnit, (String) titleField.getValue(),
                        (String) descriptionField.getValue())
                    .alternative((String) alternativeField.getValue())
                    .identifier((String) identifierField.getValue())
                    .orgType((String) orgTypeField.getValue())
                    .country((String) countryField.getValue())
                    .city((String) cityField.getValue())
                    .coordinates((String) coordinatesField.getValue())
                    .parents(parents).build();
            service.update(backedOrgUnit);
            titleField.setComponentError(null);
            descriptionField.setComponentError(null);
            titleField.commit();
            descriptionField.commit();
            if (parentList.isModified() && parents != null
                && !parents.isEmpty()) {
                updateParent(backedOrgUnit);
            }
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
        catch (final EscidocException e) {
            log.error("An unexpected error occured! See log for details.", e);
        }
        catch (final InternalClientException e) {
            log.error("An unexpected error occured! See log for details.", e);
        }
        catch (final TransportException e) {
            log.error("An unexpected error occured! See log for details.", e);
        }

        return backedOrgUnit;
    }

    private void updateParent(final OrganizationalUnit updateOrgUnit)
        throws EscidocException, InternalClientException, TransportException {

        final OrganizationalUnit parentOrgUnit = getParentOrgUnit();

        final Item child = orgUnitContainerFactory.getItem(updateOrgUnit);
        final Item parent = orgUnitContainerFactory.getItem(parentOrgUnit);
        orgUnitContainerFactory.create().setParent(child, parent);
        final Item addedItem = orgUnitContainerFactory.getItem(updateOrgUnit);
        orgUnitViewLab.showEditView(addedItem);
    }

    private OrganizationalUnit getParentOrgUnit() throws EscidocException,
        InternalClientException, TransportException {
        return service.find(getSelectedParents().iterator().next());
    }

    @Override
    protected void cancelClicked(final ClickEvent event) {
        orgUnitViewLab.showAddView();
    }

    public void setOrgUnitView(final OrgUnitViewLab orgUnitViewLab) {
        this.orgUnitViewLab = orgUnitViewLab;
        toolbar.setOrgUnitView(orgUnitViewLab);
    }

    public OrganizationalUnit open(final String comment)
        throws EscidocException, InternalClientException, TransportException {
        final OrganizationalUnit openedOrgUnit = openOrgUnit(comment);
        updateEditView(PublicStatus.OPENED);
        return openedOrgUnit;
    }

    private void updateEditView(final PublicStatus status) {
        item.getItemProperty(PropertyId.PUBLIC_STATUS).setValue(status);
        setOrgUnit(item);
    }

    private String getSelectedOrgUnitId() {
        final String objid =
            (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
        return objid;
    }

    private OrganizationalUnit openOrgUnit(final String comment)
        throws EscidocException, InternalClientException, TransportException {
        return service.open(getSelectedOrgUnitId(), comment);
    }

    public OrganizationalUnit close(final String comment)
        throws EscidocException, InternalClientException, TransportException {
        final OrganizationalUnit closedOrgUnit = closeOrgUnit(comment);
        updateEditView(PublicStatus.CLOSED);
        return closedOrgUnit;
    }

    private OrganizationalUnit closeOrgUnit(final String comment)
        throws EscidocException, InternalClientException, TransportException {
        return service.close(getSelectedOrgUnitId(), comment);
    }

    public OrganizationalUnit deleteOrgUnit() throws EscidocException,
        InternalClientException, TransportException {
        final OrganizationalUnit fromCache = getFromCache();
        service.delete(fromCache);
        return fromCache;
    }

    private OrganizationalUnit getFromCache() throws EscidocException,
        InternalClientException, TransportException {
        return service.find(getSelectedOrgUnitId());
    }
}