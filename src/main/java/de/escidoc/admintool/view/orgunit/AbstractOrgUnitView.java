/**
 * 
 */
package de.escidoc.admintool.view.orgunit;

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

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.predecessor.AbstractPredecessorView;
import de.escidoc.admintool.view.orgunit.predecessor.BlankPredecessorView;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;

/**
 * @author ASP
 * 
 */
public abstract class AbstractOrgUnitView extends CustomComponent
    implements ClickListener, Serializable {

    private static final String ADD_PARENT_LABEL = "Add Parent";

    private static final long serialVersionUID = 8351229526921020901L;

    private final Button saveButton = new Button("Save", this);

    private final Button cancelButton = new Button("Cancel", this); // NOPMD by
                                                                    // CHH on
                                                                    // 9/17/10
                                                                    // 10:39 AM

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

    protected final AdminToolApplication app;

    protected final OrgUnitService service;

    protected OrgUnitListView orgUnitList;

    protected final Button addOrgUnitButton = new Button(
        ViewConstants.ADD_LABEL);

    protected final Button removeOrgUnitButton = new Button(
        ViewConstants.REMOVE_LABEL);

    protected final Button addPredecessorButton = new Button(
        ViewConstants.ADD_LABEL);

    final HorizontalLayout footer = new HorizontalLayout();

    protected AbstractPredecessorView predecessorResult;

    protected HorizontalLayout predecessorLayout;

    public final int LABEL_WIDTH = 140;;

    protected final ListSelect predecessorTypeSelect = new ListSelect("",
        Arrays.asList(new PredecessorType[] { PredecessorType.BLANK,
            PredecessorType.SPLITTING, PredecessorType.FUSION,
            PredecessorType.SPIN_OFF, PredecessorType.AFFILIATION,
            PredecessorType.REPLACEMENT }));

    public AbstractOrgUnitView(final AdminToolApplication app,
        final OrgUnitService service) {
        assert service != null : "Service must not be null.";
        assert app != null : "AdminToolApplication must not be null.";
        this.app = app;
        this.service = service;
        preInit();
    }

    public void setOrgUnitList(final OrgUnitListView orgUnitList) {
        this.orgUnitList = orgUnitList;
    }

    protected abstract String getViewCaption();

    protected abstract Component addToolbar();

    private void preInit() {
        setCompositionRoot(panel);
        panel.setCaption(getViewCaption());
        panel.setContent(form);
        panel.setSizeUndefined();

        form.addComponent(addToolbar());

        // Title
        titleField.setWidth("400px");
        // attachedFields.add(titleField);
        form.addComponent(LayoutHelper.create(ViewConstants.TITLE_LABEL,
            titleField, LABEL_WIDTH, true));
        titleField.focus();

        // Description
        descriptionField.setWidth("400px");
        descriptionField.setRows(5);
        // attachedFields.add(descriptionField);
        form.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL,
            descriptionField, LABEL_WIDTH, 100, true));
    }

    protected void postInit() {
        // Alternative Title
        alternativeField.setWidth("400px");
        // attachedFields.add(alternativeField);
        form.addComponent(LayoutHelper.create(ViewConstants.ALTERNATIVE_LABEL,
            alternativeField, LABEL_WIDTH, false));

        // identifier
        identifierField.setWidth("400px");
        // attachedFields.add(identifierField);
        form.addComponent(LayoutHelper.create(ViewConstants.IDENTIFIER_LABEL,
            identifierField, LABEL_WIDTH, false));

        // Org Type
        orgTypeField.setWidth("400px");
        // attachedFields.add(orgTypeField);
        form.addComponent(LayoutHelper.create(ViewConstants.ORGANIZATION_TYPE,
            orgTypeField, LABEL_WIDTH, false));

        // city
        cityField.setWidth("400px");
        // attachedFields.add(cityField);
        form.addComponent(LayoutHelper.create("City", cityField, LABEL_WIDTH,
            false));

        // Country
        countryField.setWidth("400px");
        // attachedFields.add(countryField);
        form.addComponent(LayoutHelper.create("Country", countryField,
            LABEL_WIDTH, false));

        // coordinates
        coordinatesField.setWidth("400px");
        // attachedFields.add(coordinatesField);
        form.addComponent(LayoutHelper.create(ViewConstants.COORDINATES_LABEL,
            coordinatesField, LABEL_WIDTH, false));

        // Parent
        parentList.setRows(5);
        parentList.setWidth("400px");
        parentList.setNullSelectionAllowed(true);
        parentList.setMultiSelect(true);
        parentList.setImmediate(true);

        form.addComponent(LayoutHelper.create(ViewConstants.PARENTS_LABEL,
            new OrgUnitEditor(ADD_PARENT_LABEL, parentList, addOrgUnitButton,
                removeOrgUnitButton, service), LABEL_WIDTH, 100, false,
            new Button[] { addOrgUnitButton, removeOrgUnitButton }));

        // Predecessor Type
        predecessorTypeSelect.setRows(1);
        predecessorTypeSelect.setImmediate(true);
        predecessorTypeSelect.setNullSelectionAllowed(false);
        // attachedFields.add(predecessorTypeSelect);

        final HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(predecessorTypeSelect);
        hl.setComponentAlignment(predecessorTypeSelect, Alignment.TOP_RIGHT);
        hl.addComponent(addPredecessorButton);
        hl.setComponentAlignment(addPredecessorButton, Alignment.MIDDLE_LEFT);
        predecessorTypeSelect.setWidth("400px");

        form.addComponent(LayoutHelper.create("Predessor Type", hl,
            LABEL_WIDTH, 40, false));
        predecessorResult = new BlankPredecessorView();
        predecessorLayout =
            LayoutHelper.create(ViewConstants.PREDECESSORS_LABEL,
                predecessorResult, LABEL_WIDTH, 100, false);
        predecessorLayout.setSizeFull();
        form.addComponent(predecessorLayout);

        addPredecessorButton.addListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                onAddPredecessorClicked();
            }
        });
        form.addComponent(addFooter());
    }

    protected abstract void onAddPredecessorClicked();

    private HorizontalLayout addFooter() {
        footer.setSpacing(true);
        footer.addComponent(saveButton);
        footer.addComponent(cancelButton);
        return footer;
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