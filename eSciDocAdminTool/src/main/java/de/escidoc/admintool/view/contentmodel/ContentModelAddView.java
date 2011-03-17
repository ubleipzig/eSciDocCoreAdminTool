package de.escidoc.admintool.view.contentmodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.vaadin.appfoundation.view.AbstractView;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.ContentModelContainer;
import de.escidoc.admintool.app.CreateContentModelListener;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.resource.FormLayoutFactory;
import de.escidoc.admintool.view.resource.PropertiesFields;
import de.escidoc.admintool.view.resource.PropertiesFieldsImpl;
import de.escidoc.admintool.view.resource.ResourceBtnListener;
import de.escidoc.admintool.view.resource.SaveAndCancelButtons;

public class ContentModelAddView extends AbstractView<Panel> {

    private static final long serialVersionUID = -9073804431327208286L;

    private final SaveAndCancelButtons footers = new SaveAndCancelButtons();

    private final Map<String, Field> fieldByName = new HashMap<String, Field>();

    private final FormLayout formLayout = FormLayoutFactory.create();

    private final VerticalLayout vLayout = new VerticalLayout();

    private ResourceBtnListener resourceBtnListener;

    private PropertiesFields propertyFields;

    private final Window mainWindow;

    private final ResourceService contentModelService;

    private final ContentModelContainer contentModelContainer;

    private final AdminToolApplication app;

    public ContentModelAddView(final AdminToolApplication app,
        final Window mainWindow, final ResourceService contentModelService,
        final ContentModelContainer contentModelContainer) {
        super(new Panel());
        Preconditions.checkNotNull(app, "app is null: %s", app);
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        Preconditions.checkNotNull(contentModelService,
            "contentModelService is null: %s", contentModelService);
        this.app = app;
        this.mainWindow = mainWindow;
        this.contentModelService = contentModelService;
        this.contentModelContainer = contentModelContainer;
    }

    public void init() {
        getContent().setContent(vLayout);
        getContent().setCaption("Add Content Model");
        getContent().setStyleName(Reindeer.PANEL_LIGHT);
        createPropertiesFields();
        addPropertiesFields();
        resourceBtnListener =
            new CreateContentModelListener(propertyFields.getAllFields(),
                contentModelService, fieldByName, mainWindow,
                contentModelContainer);
        addSaveAndCancelButtons();
    }

    private void addPropertiesFields() {
        getContent().setWidth(500, UNITS_PIXELS);
        getContent().addComponent(propertyFields);
    }

    private void createPropertiesFields() {
        propertyFields =
            new PropertiesFieldsImpl(app, vLayout, formLayout, fieldByName);
        propertyFields.removeOthers();
    }

    private void addSaveAndCancelButtons() {
        footers.setOkButtonListener(resourceBtnListener);
        footers.getCancelBtn().addListener(new ClickListener() {
            private static final long serialVersionUID = 9116178009548492155L;

            @Override
            public void buttonClick(final ClickEvent event) {
                final Collection<Field> values = fieldByName.values();
                for (final Field field : values) {
                    field.discard();
                }
            }
        });

        getContent().addComponent(footers);
    }

    @Override
    public void activated(final Object... params) {
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void deactivated(final Object... params) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
