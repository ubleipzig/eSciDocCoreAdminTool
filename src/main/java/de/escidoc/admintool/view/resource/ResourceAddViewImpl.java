package de.escidoc.admintool.view.resource;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ViewConstants;

public class ResourceAddViewImpl extends CustomComponent implements ResourceAddView {

    private static final long serialVersionUID = 8760011504724749455L;

    private final Panel panel = new Panel(ViewConstants.ADD_ORG_UNIT);

    private final VerticalLayout vLayout = new VerticalLayout();

    private final FormLayout formLayout = FormLayoutFactory.create();

    final Map<String, Field> fieldByName = new HashMap<String, Field>();

    private final SaveAndCancelButtons footers = new SaveAndCancelButtons();

    private final Window mainWindow;

    private final ResourceService resourceService;

    private final PropertiesFields propertyFields;

    private final ResourceContainer resourceContainer;

    private final ResourceView resourceView;

    private ResourceBtnListener createOrgUnitBtnListener;

    public ResourceAddViewImpl(final AdminToolApplication app, final Window mainWindow,
        final ResourceView resourceView, final ResourceService resourceService,
        final ResourceContainer resourceContainer, final PdpRequest pdpRequest) {

        checkPreconditions(mainWindow, resourceView, resourceService, resourceContainer);

        this.mainWindow = mainWindow;
        this.resourceView = resourceView;
        this.resourceService = resourceService;
        this.resourceContainer = resourceContainer;

        propertyFields = new PropertiesFieldsImpl(app, vLayout, formLayout, fieldByName, pdpRequest);
        propertyFields.removeOthers();

        createOrgUnitSpecificView(mainWindow, resourceService, resourceContainer);

        buildView();
    }

    private void checkPreconditions(
        final Window mainWindow, final ResourceView resourceViewImpl, final ResourceService orgUnitService,
        final ResourceContainer resourceContainer) {
        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s", mainWindow);
        Preconditions.checkNotNull(resourceViewImpl, "resourceViewImpl is null: %s", resourceViewImpl);
        Preconditions.checkNotNull(orgUnitService, "orgUnitService is null: %s", orgUnitService);
        Preconditions.checkNotNull(resourceContainer, "resourceContainer is null: %s", resourceContainer);
    }

    private void buildView() {
        setCompositionRoot(panel);
        panel.setContent(vLayout);
        panel.setStyleName(Reindeer.PANEL_LIGHT);
        vLayout.setHeight(100, UNITS_PERCENTAGE);
        formLayout.setWidth(517, UNITS_PIXELS);
        vLayout.addComponent(propertyFields);
        addSpace();
        addSaveAndCancelButtons();
    }

    private void addSpace() {
        formLayout.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

    private void addSaveAndCancelButtons() {
        createOrgUnitBtnListener =
            new CreateOrgUnitBtnListener(propertyFields.getAllFields(), fieldByName, mainWindow, resourceView,
                resourceService, resourceContainer);
        footers.setOkButtonListener(createOrgUnitBtnListener);

        footers.getCancelBtn().addListener(new CancelResourceAddView(fieldByName));
        formLayout.addComponent(footers);
    }

    private OrgUnitSpecificView createOrgUnitSpecificView(
        final Window mainWindow, final ResourceService orgUnitService, final ResourceContainer resourceContainer) {

        final OrgUnitSpecificView orgUnitSpecificView =
            new OrgUnitSpecificView(mainWindow, (OrgUnitServiceLab) orgUnitService, resourceContainer, formLayout,
                fieldByName);
        orgUnitSpecificView.init();
        orgUnitSpecificView.addAddParentOkBtnListener();
        orgUnitSpecificView.setNoParents();

        return orgUnitSpecificView;
    }
}
