package de.escidoc.admintool.view.resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

import de.escidoc.admintool.view.ViewConstants;

public class PropertiesFieldsImpl extends CustomComponent
    implements PropertiesFields {

    private static final long serialVersionUID = -1808186834466896787L;

    private final static Logger log = LoggerFactory
        .getLogger(PropertiesFieldsImpl.class);

    private final FormLayout fieldLayout = new FormLayout();

    final TextField titleField = new TextField(ViewConstants.TITLE_LABEL);

    final TextField descField = new TextField(ViewConstants.DESCRIPTION_LABEL);

    final TextField createdBy = new TextField(ViewConstants.CREATED_BY_LABEL);

    final TextField modifiedBy = new TextField(ViewConstants.MODIFIED_BY_LABEL);

    final TextField createdOn = new TextField(ViewConstants.CREATED_ON_LABEL);

    final TextField modifiedOn = new TextField(ViewConstants.MODIFIED_ON_LABEL);

    private final FieldsBinder binder = new FieldsBinder(this);

    private final List<Field> allFields = new ArrayList<Field>();

    Item item;

    final TextField statusField = new TextField(
        ViewConstants.PUBLIC_STATUS_LABEL);

    final TextField statusComment = new TextField(
        ViewConstants.PUBLIC_STATUS_COMMENT_LABEL);

    public PropertiesFieldsImpl() {
        buildLayout();
        createAndAddPropertiesFields();
    }

    private void buildLayout() {
        setCompositionRoot(fieldLayout);
        fieldLayout.setMargin(true);
        fieldLayout.setWidth("400px");
        fieldLayout.setHeight(100, UNITS_PERCENTAGE);
    }

    private void createAndAddPropertiesFields() {
        addTitleField();
        addDescriptionField();
        addReadOnlyProperties();
        addStatus();
        addStatusComment();
    }

    private void addStatus() {
        statusField.setWidth("300px");
        statusField.setReadOnly(true);
        fieldLayout.addComponent(statusField);
    }

    private void addTitleField() {
        titleField.setWidth("300px");

        configure(descField);

        fieldLayout.addComponent(titleField);
    }

    private void addDescriptionField() {
        descField.setWidth("300px");
        descField.setRows(ViewConstants.DESCRIPTION_ROWS);

        configure(descField);

        fieldLayout.addComponent(descField);
    }

    private void configure(final TextField field) {
        field.setInvalidCommitted(false);
        field.setNullRepresentation("");
        field.setNullSettingAllowed(false);
        field.setWriteThrough(false);
    }

    private void addReadOnlyProperties() {
        modifiedOn.setReadOnly(true);
        fieldLayout.addComponent(modifiedOn);

        modifiedBy.setReadOnly(true);
        fieldLayout.addComponent(modifiedBy);

        createdBy.setReadOnly(true);
        fieldLayout.addComponent(createdBy);

        createdOn.setReadOnly(true);
        fieldLayout.addComponent(createdOn);
    }

    private void addStatusComment() {
        statusComment.setWidth("300px");
        statusComment.setReadOnly(true);
        configure(statusComment);
        fieldLayout.addComponent(statusComment);
    }

    @Override
    public void bind(final Item item) {
        Preconditions.checkNotNull(item, "item is null: %s", item);

        this.item = item;
        binder.bindFields();
    }

    public List<Field> getAllFields() {
        final Iterator<Component> iterator = fieldLayout.getComponentIterator();
        while (iterator.hasNext()) {
            final Component component = iterator.next();
            if (component instanceof Field) {
                allFields.add((Field) component);
            }
        }
        return allFields;
    }

}
