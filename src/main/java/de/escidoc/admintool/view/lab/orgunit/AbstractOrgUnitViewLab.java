package de.escidoc.admintool.view.lab.orgunit;

import static de.escidoc.admintool.view.ViewConstants.FIELD_WIDTH;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.OrgUnitEditor;
import de.escidoc.admintool.view.orgunit.PredecessorType;
import de.escidoc.admintool.view.orgunit.predecessor.AbstractPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.BlankPredecessorView;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.vaadin.utilities.LayoutHelper;

public abstract class AbstractOrgUnitViewLab extends CustomComponent
    implements ClickListener, Serializable {

    private static final long serialVersionUID = 8351229526921020901L;

    private final Button saveButton = new Button(ViewConstants.SAVE, this);

    private final Button cancelButton = new Button(ViewConstants.CANCEL, this);

    protected final FormLayout form = new FormLayout();

    private final Panel panel = new Panel();

    protected final TextField titleField = new TextField();

    protected final TextField descriptionField = new TextField();

    protected final TextField alternativeField = new TextField();

    protected final TextField identifierField = new TextField();

    protected final TextField orgTypeField = new TextField();

    protected final TextField cityField = new TextField();

    protected final TextField countryField = new TextField();

    protected final TextField coordinatesField = new TextField();

    protected final ListSelect parentList = new ListSelect();

    protected final Button addOrgUnitButton = new Button(
        ViewConstants.ADD_LABEL);

    protected final Button removeOrgUnitButton = new Button(
        ViewConstants.REMOVE_LABEL);

    protected final Button addPredecessorButton = new Button(
        ViewConstants.ADD_LABEL);

    protected final HorizontalLayout footer = new HorizontalLayout();

    protected final ListSelect predecessorTypeSelect = new ListSelect("",
        Arrays.asList(new PredecessorType[] { PredecessorType.BLANK,
            PredecessorType.SPLITTING, PredecessorType.FUSION,
            PredecessorType.SPIN_OFF, PredecessorType.AFFILIATION,
            PredecessorType.REPLACEMENT }));

    protected OrgUnitService service;

    protected AbstractPredecessorView predecessorResult;

    protected HorizontalLayout predecessorLayout;

    protected final Window mainWindow;

    public AbstractOrgUnitViewLab(final OrgUnitService service,
        final Window mainWindow) {
        assert service != null : "Service must not be null.";
        assert mainWindow != null : "MainWindow must not be null.";
        this.service = service;
        this.mainWindow = mainWindow;
        preInit();
    }

    protected abstract String getViewCaption();

    protected abstract Component addToolbar();

    private void preInit() {
        setSizeFull();
        setCompositionRoot(panel);
        panel.setStyleName(Reindeer.PANEL_LIGHT);
        panel.setCaption(getViewCaption());
        panel.setContent(form);
        panel.setSizeFull();
        form.addComponent(addToolbar());

        titleField.setWidth(FIELD_WIDTH);
        form.addComponent(LayoutHelper.create(ViewConstants.TITLE_LABEL,
            titleField, ViewConstants.LABEL_WIDTH, true));
        titleField.focus();

        descriptionField.setWidth(FIELD_WIDTH);
        descriptionField.setRows(5);
        form.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL,
            descriptionField, ViewConstants.LABEL_WIDTH, 100, true));
    }

    protected void postInit() {
        alternativeField.setWidth(FIELD_WIDTH);
        form.addComponent(LayoutHelper.create(ViewConstants.ALTERNATIVE_LABEL,
            alternativeField, ViewConstants.LABEL_WIDTH, false));

        identifierField.setWidth(FIELD_WIDTH);
        form.addComponent(LayoutHelper.create(ViewConstants.IDENTIFIER_LABEL,
            identifierField, ViewConstants.LABEL_WIDTH, false));

        orgTypeField.setWidth(FIELD_WIDTH);
        form.addComponent(LayoutHelper.create(ViewConstants.ORGANIZATION_TYPE,
            orgTypeField, ViewConstants.LABEL_WIDTH, false));

        cityField.setWidth(FIELD_WIDTH);
        form.addComponent(LayoutHelper.create("City", cityField,
            ViewConstants.LABEL_WIDTH, false));

        countryField.setWidth(FIELD_WIDTH);
        form.addComponent(LayoutHelper.create("Country", countryField,
            ViewConstants.LABEL_WIDTH, false));

        // coordinates
        coordinatesField.setWidth(FIELD_WIDTH);
        form.addComponent(LayoutHelper.create(ViewConstants.COORDINATES_LABEL,
            coordinatesField, ViewConstants.LABEL_WIDTH, false));

        addParentField();
        addPredecessorField();
        addFooter();
    }

    private void addParentField() {
        parentList.setRows(5);
        parentList.setWidth(ViewConstants.FIELD_WIDTH);
        parentList.setNullSelectionAllowed(true);
        parentList.setMultiSelect(true);
        parentList.setImmediate(true);
        form.addComponent(LayoutHelper.create(ViewConstants.PARENTS_LABEL,
            new OrgUnitEditor(ViewConstants.ADD_PARENT_LABEL, parentList,
                addOrgUnitButton, removeOrgUnitButton, service),
            ViewConstants.LABEL_WIDTH, 100, false, new Button[] {
                addOrgUnitButton, removeOrgUnitButton }));
    }

    private void addPredecessorField() {
        predecessorTypeSelect.setRows(1);
        predecessorTypeSelect.setImmediate(true);
        predecessorTypeSelect.setNullSelectionAllowed(false);

        final HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(predecessorTypeSelect);
        hl.setComponentAlignment(predecessorTypeSelect, Alignment.TOP_RIGHT);
        hl.addComponent(addPredecessorButton);
        hl.setComponentAlignment(addPredecessorButton, Alignment.MIDDLE_LEFT);
        predecessorTypeSelect.setWidth(ViewConstants.FIELD_WIDTH);
        form.addComponent(LayoutHelper.create(ViewConstants.PREDESSOR_TYPE, hl,
            ViewConstants.LABEL_WIDTH, 40, false));

        predecessorResult = new BlankPredecessorView();
        predecessorLayout =
            LayoutHelper.create(ViewConstants.PREDECESSORS_LABEL,
                predecessorResult, ViewConstants.LABEL_WIDTH, 100, false);
        addPredecessorButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -8189365334148809529L;

            @Override
            public void buttonClick(final ClickEvent event) {
                onAddPredecessorClicked();
            }
        });
        form.addComponent(predecessorLayout);
    }

    protected abstract void onAddPredecessorClicked();

    private void addFooter() {
        footer.setSpacing(true);
        footer.addComponent(saveButton);
        footer.addComponent(cancelButton);
        form.addComponent(footer);
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source == saveButton) {
            if (validate()) {
                saveClicked(event);
            }
        }
        else if (source == cancelButton) {
            cancelClicked(event);
        }
        else {
            throw new RuntimeException("Unknown button.");
        }
    }

    protected abstract void saveClicked(final ClickEvent event);

    protected abstract void cancelClicked(final ClickEvent event);

    private boolean validate() {
        boolean valid = true;
        valid =
            EmptyFieldValidator.isValid(titleField, "Please enter a "
                + ViewConstants.TITLE_ID);
        valid &=
            (EmptyFieldValidator.isValid(descriptionField, "Please enter a "
                + ViewConstants.DESCRIPTION_ID));
        return valid;
    }

    public void showAddedPredecessors(
        final AbstractPredecessorView addedPredecessorView) {
        predecessorLayout.replaceComponent(predecessorResult,
            addedPredecessorView);
        predecessorResult = addedPredecessorView;
    }

    protected Set<String> getSelectedParents() {
        if (parentList.getContainerDataSource() == null
            || parentList.getContainerDataSource().getItemIds() == null
            || parentList.getContainerDataSource().getItemIds().size() == 0
            || !parentList
                .getContainerDataSource().getItemIds().iterator().hasNext()) {
            return Collections.emptySet();
        }

        final ResourceRefDisplay parentRef =
            (ResourceRefDisplay) parentList
                .getContainerDataSource().getItemIds().iterator().next();
        final Set<String> parents = new HashSet<String>() {
            {
                add(parentRef.getObjectId());
            }
        };

        return parents;
    }
}