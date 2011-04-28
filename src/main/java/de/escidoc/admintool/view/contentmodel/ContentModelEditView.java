package de.escidoc.admintool.view.contentmodel;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ContentModelService;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.ResourceEditView;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.cmm.ContentModel;

@SuppressWarnings("serial")
public class ContentModelEditView extends CustomComponent implements ResourceEditView {

    private final Panel panel = new Panel(ViewConstants.EDIT_CONTENT_MODEL);

    private final TextField nameField = new TextField(ViewConstants.NAME_LABEL);

    private final TextArea descriptionField = new TextArea(ViewConstants.DESCRIPTION_LABEL);

    private final ContentModelToolbar toolbar = new ContentModelToolbar();

    private final HorizontalLayout buttonLayout = new HorizontalLayout();

    private final Button saveBtn = new Button(ViewConstants.SAVE_LABEL);

    private final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL);

    private final ContentModelService contentModelService;

    private final Window mainWindow;

    private UpdateContentModelListener listener;

    public ContentModelEditView(final ResourceService contentModelService, final Window mainWindow) {
        Preconditions.checkNotNull(contentModelService, "contentModelService is null: %s", contentModelService);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        this.contentModelService = (ContentModelService) contentModelService;
        this.mainWindow = mainWindow;
        setCompositionRoot(panel);
        panel.setStyleName(Reindeer.PANEL_LIGHT);
        init();
    }

    public void init() {
        addToolbar();
        addSpace();
        addFields();
        addSpace();
        addFooter();
    }

    private void addFooter() {
        final HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.setWidth(100, UNITS_PERCENTAGE);

        addButtons(footerLayout);

        panel.addComponent(footerLayout);
    }

    private void addButtons(final HorizontalLayout footerLayout) {
        addSaveButton();
        addCancelButton();

        footerLayout.addComponent(buttonLayout);
        footerLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_LEFT);
    }

    private void addCancelButton() {
        buttonLayout.addComponent(cancelBtn);
    }

    private void addSaveButton() {
        listener = new UpdateContentModelListener(this, contentModelService, mainWindow);
        saveBtn.addListener(listener);
        buttonLayout.addComponent(saveBtn);
    }

    private void addSpace() {
        panel.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    private void addFields() {
        addNameField();
        addSpace();
        addDescriptionField();
    }

    private void addDescriptionField() {
        descriptionField.setWidth(ViewConstants.FIELD_WIDTH);
        descriptionField.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
        descriptionField.setRequired(true);
        configure(nameField);

        panel.addComponent(descriptionField);
    }

    private void addNameField() {
        nameField.setWidth(ViewConstants.FIELD_WIDTH);
        nameField.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
        nameField.setRequired(true);
        configure(nameField);
        panel.addComponent(nameField);
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
        bindName(resource);
        bindDescription(resource);
        listener.setContentModel(resource);
    }

    private void bindName(final Resource resource) {
        // nameField.setValue(resource.getXLinkTitle());
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
    public void setFormReadOnly(final boolean b) {
        throw new UnsupportedOperationException("Not yet implemented");
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