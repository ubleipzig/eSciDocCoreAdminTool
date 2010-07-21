package de.escidoc.admintool.view.context;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.vaadin.terminal.SystemError;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.OrgUnitAddView;
import de.escidoc.admintool.view.validator.EmptyStringValidator;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;

@SuppressWarnings("serial")
public class ContextAddView extends Form implements ClickListener {

    private final AdminToolApplication app;

    private final ContextListView contextListView;

    private final ContextService contextService;

    private AdminDescriptorsAddView adminDescriptorsAddView;

    private OrgUnitAddView orgUnitAddView;

    private final OrgUnitService orgUnitService;

    public ContextAddView(final AdminToolApplication app,
        final ContextListView contextListView,
        final ContextService contextService, final OrgUnitService orgUnitService) {
        this.app = app;
        this.contextListView = contextListView;
        this.contextService = contextService;
        this.orgUnitService = orgUnitService;
        buildUI();
    }

    private void buildUI() {
        setCaption("Add a new Context");
        addNameField()
            .addDescriptionField().addType().addOrgUnits().addAdminDescriptos()
            .addFooter();

        setWriteThrough(false);
        setInvalidCommitted(false);
        // setValidationVisible(true);
    }

    private ContextAddView addType() {
        final TextField typeField = new TextField(ViewConstants.TYPE_LABEL);
        addField(ViewConstants.TYPE_ID, typeField);
        typeField.setWidth("400px");
        typeField.setRequired(true);
        typeField.addValidator(new EmptyStringValidator(ViewConstants.TYPE_LABEL
            + " can not be empty."));
        typeField.setRequiredError("Please enter a " + ViewConstants.TYPE_ID);
        return this;
    }

    private ContextAddView addOrgUnits() {
        orgUnitAddView = new OrgUnitAddView(this, orgUnitService);
        return this;
    }

    private ContextAddView addAdminDescriptos() {
        adminDescriptorsAddView = new AdminDescriptorsAddView(this);
        return this;
    }

    private ContextAddView addNameField() {
        final TextField nameField = new TextField(ViewConstants.NAME_LABEL);
        addField(ViewConstants.NAME_ID, nameField);
        nameField.setWidth("400px");
        nameField.setRequired(true);
        nameField.addValidator(new EmptyStringValidator("Name can not be empty."));
        nameField.setRequiredError("Please enter a " + ViewConstants.NAME_ID);
        return this;
    }

    private ContextAddView addDescriptionField() {
        final TextField descriptionField =
            new TextField(ViewConstants.DESCRIPTION_LABEL);
        addField(ViewConstants.DESCRIPTION_ID, descriptionField);
        descriptionField.setRequired(true);
        descriptionField.setWidth("400px");
        descriptionField.setRows(3);
        descriptionField.addValidator(new EmptyStringValidator(
            "Description can not be empty."));
        descriptionField.setRequiredError("Please enter a "
            + ViewConstants.DESCRIPTION_ID);
        return this;
    }

    private HorizontalLayout footer;

    private final Button save = new Button("Save", (ClickListener) this);

    private final Button cancel = new Button("Cancel", (ClickListener) this);

    private void addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);

        footer.addComponent(save);
        footer.addComponent(cancel);

        setFooter(footer);
    }

    public void buttonClick(final ClickEvent event) {
        final Button clickedButton = event.getButton();
        if (clickedButton == save) {
            save();
        }
        else if (clickedButton == cancel) {
            clear();
        }
        else {
            throw new RuntimeException("Unknown Button: " + clickedButton);
        }
    }

    private void save() {
        if (isValid()) {
            try {
                final String contextName = enteredName();
                final String contextDescription = enteredDescription();
                final String contextType = enteredType();
                final AdminDescriptors adminDescriptors =
                    enteredAdminDescriptors();
                final OrganizationalUnitRefs selectedOrgUnitRefs =
                    enteredOrgUnits();

                final Context newContext =
                    contextService.create(contextName, contextDescription,
                        contextType, selectedOrgUnitRefs, adminDescriptors);
                contextListView.addContext(newContext);
                contextListView.sort();
                contextListView.select(newContext.getObjid());
                setComponentError(null);
            }
            catch (final EscidocException e) {
                System.out.println("root cause: "
                    + ExceptionUtils.getRootCauseMessage(e));

                setComponentError(new UserError(e.getMessage()));
                e.printStackTrace();
            }
            catch (final InternalClientException e) {
                setComponentError(new UserError(e.getMessage()));
                e.printStackTrace();
            }
            catch (final TransportException e) {
                setComponentError(new UserError(e.getMessage()));
                e.printStackTrace();
            }
            catch (final ParserConfigurationException e) {
                setComponentError(new SystemError(e.getMessage()));
                e.printStackTrace();
            }
        }
        else {
            setValidationVisible(true);
        }
    }

    private String enteredType() {
        return (String) getField(ViewConstants.TYPE_ID).getValue();
    }

    private AdminDescriptors enteredAdminDescriptors()
        throws ParserConfigurationException {
        return adminDescriptorsAddView.getAdminDescriptors();
    }

    private OrganizationalUnitRefs enteredOrgUnits() {
        return orgUnitAddView.getSelectedOrgUnits();
    }

    private String enteredName() {
        return (String) getField(ViewConstants.NAME_ID).getValue();
    }

    private String enteredDescription() {
        return (String) getField(ViewConstants.DESCRIPTION_ID).getValue();
    }

    public void clear() {
        getField(ViewConstants.NAME_ID).setValue("");
        getField(ViewConstants.DESCRIPTION_ID).setValue("");
        getField(ViewConstants.TYPE_ID).setValue("");
        adminDescriptorsAddView.clear();
        setComponentError(null);
    }
}