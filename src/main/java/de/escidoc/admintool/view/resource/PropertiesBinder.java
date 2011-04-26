package de.escidoc.admintool.view.resource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.domain.PublicStatus;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.admintool.view.util.Converter;
import de.escidoc.core.resources.common.reference.UserAccountRef;

public class PropertiesBinder implements FieldsBinder {

    private static final Logger LOG = LoggerFactory
        .getLogger(PropertiesBinder.class);

    private final PropertiesFieldsImpl propertiesFields;

    private Component toBeBind;

    private final AdminToolApplication app;

    private final PdpRequest pdpRequest;

    PropertiesBinder(final AdminToolApplication app,
        final PropertiesFieldsImpl propertiesFields) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(propertiesFields,
            "propertiesFields is null: %s", propertiesFields);
        this.app = app;
        this.propertiesFields = propertiesFields;
        pdpRequest = this.propertiesFields.getPdpRequest();
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s",
            pdpRequest);
    }

    @Override
    public void bindFields() {
        bindName();
        bindDescriptiond();
        bindObjectId();
        bindModifienOn();
        bindModifiedBy();
        bindCreatedBy();
        bindCreatedOn();
        bindStatus();
        bindStatusComment();
    }

    private void bindStatusComment() {
        bind(propertiesFields.statusComment).with(
            getProperty(PropertyId.PUBLIC_STATUS_COMMENT));
    }

    private void bindStatus() {
        // bind(propertiesFields.statusField).with(
        // getProperty(PropertyId.PUBLIC_STATUS));
        propertiesFields.statusField
            .setPropertyDataSource(new ObjectProperty<PublicStatus>(
                PublicStatus
                    .from((de.escidoc.core.resources.common.properties.PublicStatus) getProperty(
                        PropertyId.PUBLIC_STATUS).getValue()),
                PublicStatus.class));
    }

    private void bindCreatedBy() {
        propertiesFields.createdBy.setCaption(getCreatedByTitle());
        if (isRetrieveUserPermitted(getCreatedById())) {
            propertiesFields.createdBy.addListener(new Button.ClickListener() {

                private static final long serialVersionUID =
                    3530819267393348554L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    app.showUser(getCreatedByValue().getObjid());
                }
            });
        }
        else {
            propertiesFields.createdBy.setEnabled(false);
        }
    }

    private boolean isRetrieveUserPermitted(final String userId) {
        return pdpRequest.isPermitted(ActionIdConstants.RETRIEVE_USER_ACCOUNT,
            userId);
    }

    private String getCreatedById() {
        return getCreatedByValue().getObjid();
    }

    private String getCreatedByTitle() {
        return getCreatedByValue().getXLinkTitle();
    }

    private UserAccountRef getCreatedByValue() {
        return (UserAccountRef) getProperty(PropertyId.CREATED_BY).getValue();
    }

    private void bindObjectId() {
        bind(propertiesFields.objectId).with(getProperty(PropertyId.OBJECT_ID));
    }

    private void bindDescriptiond() {
        bind(propertiesFields.descField).with(
            getProperty(PropertyId.DESCRIPTION));
    }

    private void bindName() {
        bind(propertiesFields.titleField).with(getProperty(PropertyId.NAME));
    }

    private void bindModifiedBy() {
        bindModifiedByLinkWithData();
    }

    private void bindModifiedByLinkWithData() {
        propertiesFields.modifiedBy.setCaption(getModifiedByTitle());

        if (isRetrieveUserPermitted(getModifierId())) {
            propertiesFields.modifiedBy.addListener(new Button.ClickListener() {

                private static final long serialVersionUID =
                    3530819267393348554L;

                @Override
                public void buttonClick(final ClickEvent event) {
                    LOG.debug("modified by clicked: "
                        + getModifiedByValue().getObjid());
                    app.showUser(getModifiedByValue().getObjid());
                }
            });
        }
        else {
            propertiesFields.modifiedBy.setEnabled(false);
        }

    }

    private String getModifierId() {
        return getModifiedByValue().getObjid();
    }

    private String getModifiedByTitle() {
        return getModifiedByValue().getXLinkTitle();
    }

    private UserAccountRef getModifiedByValue() {
        return (UserAccountRef) getProperty(PropertyId.MODIFIED_BY).getValue();
    }

    private void bindCreatedOn() {
        final Object value = getProperty(PropertyId.CREATED_ON).getValue();
        final DateTime dateTime = (DateTime) value;
        propertiesFields.createdOn.setCaption(Converter
            .dateTimeToString(dateTime));
    }

    private void bindModifienOn() {
        final Object value =
            getProperty(PropertyId.LAST_MODIFICATION_DATE).getValue();
        final DateTime dateTime = (DateTime) value;
        propertiesFields.modifiedOn.setCaption(Converter
            .dateTimeToString(dateTime));
    }

    private Property getProperty(final Object id) {
        return propertiesFields.item.getItemProperty(id);
    }

    private PropertiesBinder bind(final Component nameField) {
        toBeBind = nameField;
        return this;
    }

    private void with(final Property itemProperty) {
        if (toBeBind instanceof Label) {
            ((Label) toBeBind).setPropertyDataSource(itemProperty);
        }
        else if (toBeBind instanceof Button) {
            final Button button = (Button) toBeBind;
            button.setCaption((String) itemProperty.getValue());
        }
        else {
            ((com.vaadin.ui.Field) toBeBind)
                .setPropertyDataSource(itemProperty);
        }
    }
}