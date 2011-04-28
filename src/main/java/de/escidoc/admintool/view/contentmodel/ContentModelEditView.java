package de.escidoc.admintool.view.contentmodel;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.ContentModelService;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.admintool.view.resource.ResourceEditView;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.cmm.ContentModel;

@SuppressWarnings("serial")
public class ContentModelEditView extends CustomComponent implements ResourceEditView {

    private final Panel panel = new Panel(ViewConstants.EDIT_CONTENT_MODEL);

    private final FormLayout formLayout = new FormLayout();

    private final TextField nameField = new TextField(ViewConstants.NAME_LABEL);

    private final TextArea descriptionField = new TextArea(ViewConstants.DESCRIPTION_LABEL);

    private final HorizontalLayout buttonLayout = new HorizontalLayout();

    private final Button saveBtn = new Button(ViewConstants.SAVE_LABEL);

    private final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL);

    private final ContentModelService contentModelService;

    private final Window mainWindow;

    private final PdpRequest pdpRequest;

    private final ContentModelToolbar toolbar;

    private UpdateContentModelListener updateListener;

    private Resource resource;

    public ContentModelEditView(final ResourceService contentModelService, final Window mainWindow,
        final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(contentModelService, "contentModelService is null: %s", contentModelService);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        this.contentModelService = (ContentModelService) contentModelService;
        this.mainWindow = mainWindow;
        this.pdpRequest = pdpRequest;
        toolbar = new ContentModelToolbar(pdpRequest);
    }

    private boolean isUpdateNotAllowed() {
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        Preconditions.checkNotNull(getContentModelId(), "getContentModelId() is null: %s", getContentModelId());
        return pdpRequest.isDenied(ActionIdConstants.UPDATE_CONTENT_MODEL, getContentModelId());
    }

    private String getContentModelId() {
        Preconditions.checkNotNull(resource, "resource is null: %s", resource);
        return resource.getObjid();
    }

    public void init() {
        setCompositionRoot(panel);
        panel.setStyleName(Reindeer.PANEL_LIGHT);

        addToolbar();

        panel.addComponent(formLayout);
        addSpace();
        addFields();
        // addSpace();
        addFooter();
    }

    private void addFooter() {
        final HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setWidth(480, UNITS_PIXELS);
        addButtons(footerLayout);
        panel.addComponent(footerLayout);
    }

    private void addButtons(final HorizontalLayout footerLayout) {
        addSaveButton();
        addCancelButton();

        footerLayout.addComponent(buttonLayout);
        footerLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_RIGHT);
    }

    private void addCancelButton() {
        buttonLayout.addComponent(cancelBtn);
    }

    private void addSaveButton() {
        updateListener = new UpdateContentModelListener(this, contentModelService, mainWindow);
        saveBtn.addListener(updateListener);
        buttonLayout.addComponent(saveBtn);
    }

    private void addSpace() {
        panel.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    private void addFields() {
        addNameField();
        // addSpace();
        addDescriptionField();
    }

    private void addDescriptionField() {
        descriptionField.setWidth(ViewConstants.FIELD_WIDTH);
        descriptionField.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
        descriptionField.setRequired(true);
        configure(nameField);

        formLayout.addComponent(descriptionField);
    }

    private void addNameField() {
        nameField.setWidth(ViewConstants.FIELD_WIDTH);
        nameField.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
        nameField.setRequired(true);
        configure(nameField);
        formLayout.addComponent(nameField);
    }

    private void configure(final TextField field) {
        field.setPropertyDataSource(new ObjectProperty<String>(ViewConstants.EMPTY_STRING));
        field.setImmediate(false);
        field.setInvalidCommitted(false);
        field.setNullRepresentation(ViewConstants.EMPTY_STRING);
        field.setNullSettingAllowed(false);
        field.setWriteThrough(false);
    }

    private void addToolbar() {
        toolbar.init();
        panel.addComponent(toolbar);
    }

    public void setContentModelView(final ContentModelView contentModelView) {
        Preconditions.checkNotNull(contentModelView, "contentModelView is null: %s", contentModelView);
        toolbar.setContentModelView(contentModelView);
    }

    public void setContentModel(final Resource resource) {
        Preconditions.checkNotNull(resource, "resource is null: %s", resource);
        this.resource = resource;
        bindDescription(resource);

        bindUserRightWithView();
        updateListener.setContentModel(resource);
    }

    private void bindUserRightWithView() {
        toolbar.bind(getContentModelId());
        setFormReadOnly(isUpdateNotAllowed());
        buttonLayout.setVisible(!isUpdateNotAllowed());
    }

    private void bindDescription(final Resource resource) {
        if (resource instanceof ContentModel) {
            final ContentModel contentModel = (ContentModel) resource;
            String description = contentModel.getProperties().getDescription();
            if (description == null) {
                description = ViewConstants.EMPTY_STRING;
            }
            descriptionField.setValue(description);
        }
    }

    @Override
    public void bind(final Item item) {
        nameField.setPropertyDataSource(item.getItemProperty(PropertyId.XLINK_TITLE));
    }

    @Override
    public void setFormReadOnly(final boolean isReadOnly) {
        nameField.setReadOnly(isReadOnly);
        descriptionField.setReadOnly(isReadOnly);
    }

    @Override
    public void setFooterVisible(final boolean b) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public TextArea getDescriptionField() {
        return descriptionField;
    }

    public TextField getNameField() {
        return nameField;
    }
}