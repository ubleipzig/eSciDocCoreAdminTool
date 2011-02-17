package de.escidoc.admintool.view.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;

public class PropertiesFieldsImpl extends CustomComponent
    implements PropertiesFields {

    private static final Logger LOG = LoggerFactory
        .getLogger(PropertiesFieldsImpl.class);

    private static final long serialVersionUID = -1808186834466896787L;

    private final FormLayout formLayout;

    private final List<Field> allFields = new ArrayList<Field>();

    final TextField titleField = new TextField(ViewConstants.TITLE_LABEL);

    final TextField descField = new TextField(ViewConstants.DESCRIPTION_LABEL);

    private final HorizontalLayout objectIdLayout = new HorizontalLayout();

    private final HorizontalLayout modifiedLayout = new HorizontalLayout();

    private final HorizontalLayout modifiedByLayout = new HorizontalLayout();

    private final HorizontalLayout createdLayout = new HorizontalLayout();

    private final HorizontalLayout createdByLayout = new HorizontalLayout();

    private final Map<String, Field> fieldByName;

    Label modifiedOn;

    Button modifiedBy;

    Button createdBy;

    Label createdOn;

    Item item;

    final TextField statusField = new TextField(
        ViewConstants.PUBLIC_STATUS_LABEL);

    final TextField statusComment = new TextField(
        ViewConstants.PUBLIC_STATUS_COMMENT_LABEL);

    Label publicStatusValue;

    Label objectId;

    private final AdminToolApplication app;

    private final FieldsBinder binder;

    public PropertiesFieldsImpl(final AdminToolApplication app,
        final VerticalLayout vLayout, final FormLayout formLayout,
        final Map<String, Field> fieldByName) {
        this.app = app;
        this.formLayout = formLayout;
        this.fieldByName = fieldByName;
        binder = new PropertiesBinder(app, this);
        buildLayout();
        createAndAddPropertiesFields();
    }

    private void buildLayout() {
        setCompositionRoot(formLayout);
    }

    private void createAndAddPropertiesFields() {
        addTitleField();
        addDescriptionField();
        addOthers();
    }

    public void addOthers() {
        addReadOnlyProperties();
        addStatus();
    }

    @Override
    public void removeOthers() {
        formLayout.removeComponent(objectIdLayout);
        formLayout.removeComponent(modifiedLayout);
        formLayout.removeComponent(modifiedByLayout);
        formLayout.removeComponent(createdLayout);
        formLayout.removeComponent(createdByLayout);
        formLayout.removeComponent(statusField);
        formLayout.removeComponent(statusComment);
    }

    private void addTitleField() {
        titleField.setWidth(ViewConstants.FIELD_WIDTH);
        titleField.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
        configure(titleField);
        formLayout.addComponent(titleField);
        fieldByName.put("title", titleField);
        titleField.setRequired(true);
    }

    private void addDescriptionField() {
        descField.setWidth(ViewConstants.FIELD_WIDTH);
        descField.setMaxLength(ViewConstants.MAX_DESC_LENGTH);
        descField.setRows(ViewConstants.DESCRIPTION_ROWS);

        configure(descField);
        fieldByName.put("description", descField);
        formLayout.addComponent(descField);
    }

    private void addStatus() {
        statusField.setWidth(ViewConstants.FIELD_WIDTH);
        statusField.setReadOnly(true);
        formLayout.addComponent(statusField);
    }

    private void configure(final TextField field) {
        field.setPropertyDataSource(new ObjectProperty<String>(
            ViewConstants.EMPTY_STRING));
        field.setImmediate(false);
        field.setInvalidCommitted(false);
        field.setNullRepresentation(ViewConstants.EMPTY_STRING);
        field.setNullSettingAllowed(false);
        field.setWriteThrough(false);
    }

    private void addReadOnlyProperties() {
        formLayout.addComponent(objectIdLayout);
        formLayout.addComponent(modifiedLayout);
        formLayout.addComponent(modifiedByLayout);
        formLayout.addComponent(createdLayout);
        formLayout.addComponent(createdByLayout);

        addObjectId();
        addModifiedOn();
        addModifiedBy();
        addCreatedOn();
        addCreatedBy();
    }

    private void addObjectId() {
        final Label objectIdLabel = new Label(ViewConstants.OBJECT_ID_LABEL);
        objectIdLayout.addComponent(objectIdLabel);
        objectIdLayout.setSpacing(true);

        objectId = new Label();
        objectIdLayout.addComponent(objectId);
    }

    private void addCreatedOn() {
        final Label createdOnLabel = new Label(ViewConstants.CREATED_ON_LABEL);
        createdLayout.addComponent(createdOnLabel);
        createdLayout.setSpacing(true);

        createdOn = new Label();
        createdLayout.addComponent(createdOn);
    }

    private void addCreatedBy() {
        final Label createdByLabel = new Label(" by ");
        createdByLayout.addComponent(createdByLabel);
        createdByLayout.setSpacing(true);
        createdBy = new Button();
        createdBy.setStyleName(BaseTheme.BUTTON_LINK);
        createdByLayout.addComponent(createdBy);
    }

    private void addModifiedBy() {
        final Label modifiedByLabel = new Label(" by ");
        modifiedByLayout.addComponent(modifiedByLabel);
        modifiedByLayout.setSpacing(true);
        modifiedBy = new Button();
        modifiedBy.setStyleName(BaseTheme.BUTTON_LINK);
        modifiedByLayout.addComponent(modifiedBy);
    }

    private void addModifiedOn() {
        final Label modifiedOnLabel =
            new Label(ViewConstants.MODIFIED_ON_LABEL);
        modifiedLayout.addComponent(modifiedOnLabel);
        modifiedLayout.setSpacing(true);

        modifiedOn = new Label();
        modifiedLayout.addComponent(modifiedOn);
    }

    @Override
    public List<Field> getAllFields() {
        final Iterator<Component> iterator = formLayout.getComponentIterator();
        while (iterator.hasNext()) {
            final Component component = iterator.next();
            if (component instanceof Field
                && !component.getCaption().equals("Parents")) {
                final Field field = (Field) component;
                allFields.add(field);
            }
        }
        return allFields;
    }

    @Override
    public void bind(final Item item) {
        Preconditions.checkNotNull(item, "item is null: %s", item);
        Preconditions.checkNotNull(binder, "binder is null: %s", binder);

        this.item = item;
        binder.bindFields();
    }

    @Override
    public Map<String, Field> getFieldByName() {
        return fieldByName;
    }

    @Override
    public void setNotEditable(final boolean isReadOnly) {
        titleField.setReadOnly(isReadOnly);
        descField.setReadOnly(isReadOnly);
    }
}
