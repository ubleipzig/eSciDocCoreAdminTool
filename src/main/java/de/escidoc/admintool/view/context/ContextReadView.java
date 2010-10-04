package de.escidoc.admintool.view.context;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.common.reference.Reference;
import de.escidoc.core.resources.om.context.AdminDescriptor;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;

@SuppressWarnings("serial")
public class ContextReadView extends Form implements ClickListener {

    private final AdminToolApplication app;

    public ContextReadView(final AdminToolApplication app) {
        this.app = app;
    }

    private VerticalLayout verticalLayout;

    private void buildUI() {
        verticalLayout = new VerticalLayout();
        setLayout(verticalLayout);
        createHeader();
        verticalLayout.addComponent(header);

        if (context == null) {
            return;
        }
        name().objectId().description().status().orgUnits().adminDesriptors();
    }

    private ContextReadView adminDesriptors() {
        final List<String> adminDescriptorsName = new ArrayList<String>();
        for (final AdminDescriptor adminDescriptor : context
            .getAdminDescriptors()) {
            adminDescriptorsName.add(adminDescriptor.getName());
        }
        verticalLayout.addComponent(new Label(
            ViewConstants.ADMIN_DESRIPTORS_LABEL));
        for (final String adminDescName : adminDescriptorsName) {
            verticalLayout.addComponent(new Label(adminDescName));
        }

        return this;
    }

    private ContextReadView name() {
        nameAndObjectId = new HorizontalLayout();
        nameAndObjectId.setSpacing(true);
        final FormLayout formLayout = new FormLayout();
        final Field nameField =
            new TextField(ViewConstants.NAME_LABEL, context
                .getProperties().getName());
        nameField.setWidth("400px");
        nameField.setReadOnly(true);
        formLayout.addComponent(nameField);
        nameAndObjectId.addComponent(formLayout);

        verticalLayout.addComponent(nameAndObjectId);
        return this;
    }

    private ContextReadView objectId() {
        nameAndObjectId.addComponent(new Label(context.getObjid()));
        return this;
    }

    private ContextReadView description() {
        final HorizontalLayout description = new HorizontalLayout();
        final FormLayout formLayout = new FormLayout();
        final Field nameField =
            new TextField(ViewConstants.DESCRIPTION_LABEL, context
                .getProperties().getDescription());
        nameField.setWidth("400px");
        nameField.setReadOnly(true);
        formLayout.addComponent(nameField);
        description.addComponent(formLayout);
        verticalLayout.addComponent(description);
        return this;
    }

    private ContextReadView orgUnits() {
        final Label orgUnitLabel =
            new Label(ViewConstants.ORGANIZATION_UNITS_LABEL);

        verticalLayout.addComponent(orgUnitLabel);

        final OrganizationalUnitRefs organizationalUnitRefs =
            context.getProperties().getOrganizationalUnitRefs();
        for (final Reference resourceRef : organizationalUnitRefs) {
            verticalLayout.addComponent(new Label(resourceRef.getObjid()));
        }

        return this;
    }

    private Button open;

    private Button close;

    private ContextReadView status() {

        final HorizontalLayout status = new HorizontalLayout();
        status.setSpacing(true);

        final String publicStatus = context.getProperties().getPublicStatus();
        status.addComponent(new Label(publicStatus));

        if (publicStatus.equals("created")) {
            open = new Button("open", (ClickListener) this);

            open.setStyleName(BaseTheme.BUTTON_LINK);
            status.addComponent(open);
            delete.setVisible(true);
        }
        else if (publicStatus.equals("opened")) {
            close = new Button("close");
            close.setStyleName(BaseTheme.BUTTON_LINK);

            status.addComponent(close);
            delete.setVisible(false);
        }

        verticalLayout.addComponent(status);
        return this;
    }

    private HorizontalLayout header;

    private final Button edit = new Button("Edit", (ClickListener) this);

    private final Button delete = new Button("Delete", (ClickListener) this);

    private Context context;

    private HorizontalLayout nameAndObjectId;

    private void createHeader() {
        header = new HorizontalLayout();
        header.setMargin(true);
        header.setSpacing(true);
        header.addComponent(edit);
        header.addComponent(delete);

        header.setVisible(true);
    }

    public void setSelected(final Item item) {
        if (item != getItemDataSource()) {
            this.setItemDataSource(item);
        }
        setReadOnly(true);
    }

    public void setSelected(final Context context) {
        this.context = context;
        buildUI();
    }

    public void buttonClick(final ClickEvent event) {
        final Button clickedButton = event.getButton();
        if (clickedButton == edit) {
            app.getMainWindow().showNotification(
                "Show edit view: " + context.getProperties().getName());
        }
        else if (clickedButton == delete) {
            app.getMainWindow().showNotification(
                "Delete? " + context.getProperties().getName());
        }
        else if (clickedButton == open) {
            app.getMainWindow().showNotification(
                "Opening... " + context.getProperties().getName());
        }
        else if (clickedButton == close) {
            app.getMainWindow().showNotification(
                "Closing... " + context.getProperties().getName());
        }
        else {
            throw new IllegalArgumentException("Unknown Button "
                + clickedButton);
        }
    }
}