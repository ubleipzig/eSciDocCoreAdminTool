package de.escidoc.admintool.view.resource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ViewConstants;

public class ResourceEditViewImpl extends CustomComponent
    implements ResourceEditView {

    private static final long serialVersionUID = -7860622778922198542L;

    private final VerticalLayout vLayout = new VerticalLayout();

    private final PropertiesFields propertyFields;

    private final SaveAndCancelButtons footers = new SaveAndCancelButtons();

    private final ResourceToolbar resourceToolbar;

    private OrgUnitSpecificView resourceSpecific;

    private final FormLayout formLayout = FormLayoutFactory.create();

    private final Window mainWindow;

    private final ResourceService resourceService;

    private ResourceBtnListener updateOrgUnitBtnListener;

    private final Map<String, Field> fieldByName = new HashMap<String, Field>();

    private final Panel panel = new Panel(ViewConstants.EDIT_ORG_UNIT);

    public ResourceEditViewImpl(final Window mainWindow,
        final ResourceViewImpl resourceView,
        final ResourceService orgUnitService,
        final ResourceContainer resourceContainer) {

        checkPreconditions(mainWindow, resourceView, orgUnitService,
            resourceContainer);

        this.mainWindow = mainWindow;
        resourceService = orgUnitService;
        formLayout.setWidth(75, UNITS_PERCENTAGE);

        resourceToolbar =
            new ResourceToolbar(resourceView, mainWindow, orgUnitService,
                resourceContainer);
        propertyFields =
            new PropertiesFieldsImpl(vLayout, formLayout, fieldByName);
        resourceSpecific =
            createOrgUnitSpecificView(mainWindow, orgUnitService,
                resourceContainer);

        buildView();
    }

    private void checkPreconditions(
        final Window mainWindow, final ResourceViewImpl resourceViewImpl,
        final ResourceService orgUnitService,
        final ResourceContainer resourceContainer) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
            mainWindow);
        Preconditions.checkNotNull(resourceViewImpl,
            "resourceViewImpl is null: %s", resourceViewImpl);
        Preconditions.checkNotNull(orgUnitService,
            "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(resourceContainer,
            "resourceContainer is null: %s", resourceContainer);
    }

    private OrgUnitSpecificView createOrgUnitSpecificView(
        final Window mainWindow, final ResourceService orgUnitService,
        final ResourceContainer resourceContainer) {
        return new OrgUnitSpecificView(mainWindow,
            (OrgUnitServiceLab) orgUnitService, resourceContainer, formLayout,
            fieldByName);
    }

    private void buildView() {
        setCompositionRoot(panel);
        panel.setContent(vLayout);
        panel.setStyleName(Reindeer.PANEL_LIGHT);
        vLayout.setHeight(100, UNITS_PERCENTAGE);
        formLayout.setWidth(517, UNITS_PIXELS);
        vLayout.addComponent(resourceToolbar);
        vLayout.addComponent(propertyFields);
        addSpace();
        addSaveAndCancelButtons();
    }

    private void addSpace() {
        formLayout.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    public void setResourceSpecificView(
        final OrgUnitSpecificView resourceSpecific) {
        this.resourceSpecific = resourceSpecific;
    }

    private void addSaveAndCancelButtons() {
        updateOrgUnitBtnListener =
            new UpdateOrgUnitBtnListener(propertyFields.getAllFields(),
                fieldByName, mainWindow, resourceService);
        footers.setOkButtonListener(updateOrgUnitBtnListener);

        footers.getCancelBtn().addListener(new ClickListener() {

            @Override
            public void buttonClick(final ClickEvent event) {
                final Collection<Field> values = fieldByName.values();
                for (final Field field : values) {
                    field.discard();
                }
            }
        });
        formLayout.addComponent(footers);
        footers.setWidth(100, UNITS_PERCENTAGE);
    }

    @Override
    public void bind(final Item item) {
        resourceToolbar.bind(item);
        propertyFields.bind(item);
        resourceSpecific.bind(item);
        updateOrgUnitBtnListener.bind(item);
    }

    @Override
    public void setFormReadOnly(final boolean isReadOnly) {
        propertyFields.setNotEditable(isReadOnly);
        resourceSpecific.setNotEditable(isReadOnly);
    }

    @Override
    public void setFooterVisible(final boolean b) {
        footers.setVisible(b);
    }
}