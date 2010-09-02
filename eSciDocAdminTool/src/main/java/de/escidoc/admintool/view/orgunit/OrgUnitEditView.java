package de.escidoc.admintool.view.orgunit;

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
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.MetadataExtractor;
import de.escidoc.admintool.domain.OrgUnitFactory;
import de.escidoc.admintool.messages.Messages;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.PublicStatus;
import de.escidoc.admintool.view.orgunit.editor.IPredecessorEditor;
import de.escidoc.admintool.view.orgunit.predecessor.AbstractPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.AffiliationPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.BlankPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.SpinOffPredecessorView;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;
import de.escidoc.core.resources.oum.Predecessor;
import de.escidoc.core.resources.oum.PredecessorForm;
import de.escidoc.core.resources.oum.Predecessors;
import de.escidoc.vaadin.dialog.ErrorDialog;
import de.escidoc.vaadin.utilities.Converter;
import de.escidoc.vaadin.utilities.LayoutHelper;

public class OrgUnitEditView extends AbstractOrgUnitView {
    private static final String EMPTY_STRING = "";

    private static final long serialVersionUID = -1488130998058019932L;

    private static final Logger log =
        LoggerFactory.getLogger(OrgUnitEditView.class);

    private final Label objIdField = new Label();

    private final Label modifiedOn = new Label();

    private final Label modifiedBy = new Label();

    private final Label createdOn = new Label();

    private final Label createdBy = new Label();

    private final Label publicStatus = new Label();

    private final Label publicStatusComment = new Label();

    private static final int HEIGHT = 15;

    private OrgUnitToolbar toolbar;

    private Item item;

    public OrgUnitEditView(final AdminToolApplication app,
        final OrgUnitService service) throws EscidocException,
        InternalClientException, TransportException,
        UnsupportedOperationException {
        super(app, service);
        middleInit();
        postInit();
        titleField.setWriteThrough(false);
    }

    private void middleInit() {
        form.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL,
            objIdField, labelWidth, false));

        form.addComponent(LayoutHelper.create("Modified", "by", modifiedOn,
            modifiedBy, labelWidth, HEIGHT, false));

        form.addComponent(LayoutHelper.create("Created", "by", createdOn,
            createdBy, labelWidth, HEIGHT, false));
        form.addComponent(LayoutHelper.create("Status", publicStatus,
            labelWidth, false));

        if (!publicStatusComment.getValue().equals(EMPTY_STRING)) {
            form.addComponent(LayoutHelper.create("Status Comment",
                publicStatusComment, labelWidth, false));
        }
    }

    @Override
    protected void saveClicked(final ClickEvent event) {
        updateOrgUnit();
    }

    // TODO: discard changes
    @Override
    protected void cancelClicked(final ClickEvent event) {
        super.app.showOrganizationalUnitView();
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
                        (String) descriptionField.getValue()).alternative(
                        (String) alternativeField.getValue()).identifier(
                        (String) identifierField.getValue()).orgType(
                        (String) orgTypeField.getValue()).country(
                        (String) countryField.getValue()).city(
                        (String) cityField.getValue()).coordinates(
                        (String) coordinatesField.getValue()).parents(parents)
                    .build();
            final OrganizationalUnit updatedOrgUnit =
                service.update(backedOrgUnit);
            // final OrganizationalUnit updatedOrgUnit =
            // service.find((String) objIdField.getValue());
            // TODO
            // sort the table
            // FIXME
            titleField.setComponentError(null);
            descriptionField.setComponentError(null);
            titleField.commit();
            descriptionField.commit();
        }
        catch (final ParserConfigurationException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final SAXException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final IOException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final EscidocException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final InternalClientException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final TransportException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }

        return backedOrgUnit;
    }

    // private void commitFields() {
    // for (final Field field : attachedFields) {
    // field.commit();
    // }
    // }

    private String getSelectedOrgUnitId() {
        final String objid =
            (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
        return objid;
    }

    public void setSelected(final Item item) {
        this.item = item;
        if (item != null) {
            final Property publicStatusProperty =
                item.getItemProperty(PropertyId.PUBLIC_STATUS);
            publicStatus.setPropertyDataSource(publicStatusProperty);
            final String status = (String) publicStatusProperty.getValue();
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
                    break;
                }
                case CLOSED: {
                    setFormReadOnly(true);
                    footer.setVisible(false);
                    break;
                }
                default: {
                    throw new RuntimeException("unknown status");
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

                final String parentName =
                    service.find(parent.getObjid()).getProperties().getName();
                parentList.addItem(new ResourceRefDisplay(parent.getObjid(),
                    parentName));
            }
            else {
                parentList.removeAllItems();
            }

            // Predecessor
            bindPredecessor();

            if (publicStatus != PublicStatus.CLOSED) {
                final OrganizationalUnit orgUnit =
                    service.find((String) item.getItemProperty(
                        PropertyId.OBJECT_ID).getValue());
                final MetadataExtractor metadataExtractor =
                    new MetadataExtractor(orgUnit);
                final String alternative =
                    metadataExtractor.get("dcterms:alternative");
                final String identifier =
                    metadataExtractor.get("dc:identifier");
                final String orgType =
                    metadataExtractor.get("eterms:organization-type");
                final String country = metadataExtractor.get("eterms:country");
                final String city = metadataExtractor.get("eterms:city");
                final String coordinate =
                    metadataExtractor.get("kml:coordinates");
                final String startDate =
                    metadataExtractor.get("eterms:start-date");
                final String endDate = metadataExtractor.get("eterms:end-date");

                alternativeField.setValue(alternative);
                identifierField.setValue(identifier);
                orgTypeField.setValue(orgType);
                countryField.setValue(country);
                cityField.setValue(city);
                coordinatesField.setValue(coordinate);
            }
            toolbar.changeState(PublicStatus.valueOf(status.toUpperCase()));
        }
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
            log.info("no predecessor");
            final BlankPredecessorView blankPredecessorView =
                new BlankPredecessorView();
            predecessorLayout.replaceComponent(predecessorResult,
                blankPredecessorView);
            predecessorResult = blankPredecessorView;
        }
    }

    private String getTitle(final String predecessorObjectId) {
        try {
            return service.findOrgUnitTitleById(predecessorObjectId);
        }
        catch (final EscidocException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), Messages
                    .getString("AdminToolApplication.15"), e.getMessage())); //$NON-NLS-1$
        }
        catch (final InternalClientException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), Messages
                    .getString("AdminToolApplication.15"), e.getMessage())); //$NON-NLS-1$
        }
        catch (final TransportException e) {
            app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), Messages
                    .getString("AdminToolApplication.15"), e.getMessage())); //$NON-NLS-1$
        }
        return EMPTY_STRING;
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

    // TODO replace with factory
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

    private boolean hasPredecessors() {
        final Predecessors predecessors =
            (Predecessors) item
                .getItemProperty(PropertyId.PREDECESSORS).getValue();
        return predecessors != null && predecessors.getPredecessorRef() != null
            && predecessors.getPredecessorRef().size() > 0;
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

    @Override
    protected String getViewCaption() {
        return "Edit " + ViewConstants.ORGANIZATION_UNITS_LABEL;
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
        catch (final SecurityException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (final NoSuchMethodException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (final IllegalArgumentException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (final InvocationTargetException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
    }

    @Override
    protected Component addToolbar() {
        toolbar = new OrgUnitToolbar(app, this);
        return toolbar;
    }

    public void deleteOrgUnit() {
        final OrganizationalUnit selected =
            service.find(getSelectedOrgUnitId());
        try {
            service.delete(selected);
            orgUnitList.removeOrgUnit(selected);
            app.showOrganizationalUnitView();
        }
        catch (final EscidocException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final InternalClientException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final TransportException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }

    }

    public OrganizationalUnit open(final String comment)
        throws EscidocException, InternalClientException, TransportException {
        final OrganizationalUnit oldOrgUnit =
            service.find(getSelectedOrgUnitId());
        final OrganizationalUnit openedOrgUnit =
            service.open(getSelectedOrgUnitId(), comment);
        orgUnitList.updateOrgUnit(oldOrgUnit, openedOrgUnit);
        return openedOrgUnit;

    }

    public OrganizationalUnit close(final String comment)
        throws EscidocException, InternalClientException, TransportException {
        final OrganizationalUnit oldOrgUnit =
            service.find(getSelectedOrgUnitId());
        final OrganizationalUnit closedOrgUnit =
            service.close(getSelectedOrgUnitId(), comment);
        orgUnitList.updateOrgUnit(oldOrgUnit, closedOrgUnit);
        return closedOrgUnit;

    }
}