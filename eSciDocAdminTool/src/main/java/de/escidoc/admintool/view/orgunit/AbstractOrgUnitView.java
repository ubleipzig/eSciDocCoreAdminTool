/**
 * 
 */
package de.escidoc.admintool.view.orgunit;

import java.io.Serializable;
import java.util.Arrays;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.OrgUnitEditor;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.predecessor.BlankPredecessorView;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.vaadin.utilities.LayoutHelper;

/**
 * @author ASP
 * 
 */
public abstract class AbstractOrgUnitView extends CustomComponent
    implements ClickListener, Serializable {

    private static final long serialVersionUID = 8351229526921020901L;

    private final Button save = new Button("Save", this);

    private final Button cancel = new Button("Cancel", this);

    protected final AdminToolApplication app;

    protected final OrgUnitService service;

    protected OrgUnitList orgUnitList;

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

    protected final ListSelect orgUnitListSelect = new ListSelect();

    private final Button addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton = new Button(
        ViewConstants.REMOVE_LABEL);

    private final Button addPredecessorButton = new Button(
        ViewConstants.ADD_LABEL);

    protected AbstractComponent predecessorResult;

    protected HorizontalLayout predecessorLayout;

    final HorizontalLayout footer = new HorizontalLayout();

    protected final int labelWidth = 140;;

    protected final ListSelect predecessorTypeSelect = new ListSelect("",
        Arrays.asList(new PredecessorType[] { PredecessorType.BLANK,
            PredecessorType.SPLITTING, PredecessorType.FUSION,
            PredecessorType.SPIN_OFF, PredecessorType.AFFILIATION,
            PredecessorType.REPLACEMENT }));

    public AbstractOrgUnitView(final AdminToolApplication app,
        final OrgUnitService service) {
        this.app = app;
        this.service = service;
        preInit();
    }

    protected abstract String getViewCaption();

    private void preInit() {
        setCompositionRoot(panel);
        panel.setCaption(getViewCaption());
        panel.setContent(form);
        panel.setSizeUndefined();

        // Title
        titleField.setWidth("400px");
        form.addComponent(LayoutHelper.create(ViewConstants.TITLE_LABEL,
            titleField, labelWidth, true));
        titleField.focus();
        // titleProperty = mapBinding("", titleField);

        // Description
        descriptionField.setWidth("400px");
        descriptionField.setRows(5);
        form.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL,
            descriptionField, labelWidth, 100, true));

    }

    protected void postInit() {
        // Alternative Title
        alternativeField.setWidth("400px");
        form.addComponent(LayoutHelper.create(ViewConstants.ALTERNATIVE_LABEL,
            alternativeField, labelWidth, false));

        // identifier
        identifierField.setWidth("400px");
        form.addComponent(LayoutHelper.create(ViewConstants.IDENTIFIER_LABEL,
            identifierField, labelWidth, false));

        // Org Type
        orgTypeField.setWidth("400px");
        form.addComponent(LayoutHelper.create(ViewConstants.ORGANIZATION_TYPE,
            orgTypeField, labelWidth, false));

        // city
        cityField.setWidth("400px");
        form.addComponent(LayoutHelper.create("City", cityField, labelWidth,
            false));

        // Country
        countryField.setWidth("400px");
        form.addComponent(LayoutHelper.create("Country", countryField,
            labelWidth, false));

        // coordinates
        coordinatesField.setWidth("400px");
        form.addComponent(LayoutHelper.create(ViewConstants.COORDINATES_LABEL,
            coordinatesField, labelWidth, false));

        // Parent
        orgUnitListSelect.setRows(5);
        orgUnitListSelect.setWidth("400px");
        orgUnitListSelect.setNullSelectionAllowed(true);
        orgUnitListSelect.setMultiSelect(true);
        orgUnitListSelect.setImmediate(true);

        form.addComponent(LayoutHelper.create(ViewConstants.PARENTS_LABEL,
            new OrgUnitEditor("Add Parents", orgUnitListSelect,
                addOrgUnitButton, removeOrgUnitButton), labelWidth, 100, false,
            new Button[] { addOrgUnitButton, removeOrgUnitButton }));

        // Predecessor Type
        predecessorTypeSelect.setRows(1);
        predecessorTypeSelect.setImmediate(true);
        predecessorTypeSelect.setNullSelectionAllowed(false);

        final HorizontalLayout hl = new HorizontalLayout();
        hl.addComponent(predecessorTypeSelect);
        hl.setComponentAlignment(predecessorTypeSelect, Alignment.TOP_RIGHT);
        hl.addComponent(addPredecessorButton);
        hl.setComponentAlignment(addPredecessorButton, Alignment.MIDDLE_LEFT);
        predecessorTypeSelect.setWidth("400px");

        form.addComponent(LayoutHelper.create("Predessor Type", hl, labelWidth,
            40, false));
        predecessorResult = new BlankPredecessorView();
        predecessorLayout =
            LayoutHelper.create(ViewConstants.PREDECESSORS_LABEL,
                predecessorResult, labelWidth, 100, false);
        predecessorLayout.setSizeFull();
        form.addComponent(predecessorLayout);

        addPredecessorButton.addListener(new Button.ClickListener() {
            public void buttonClick(final ClickEvent event) {
                onAddPredecessorClicked();
            }
        });
        form.addComponent(addFooter());
    }

    protected abstract void onAddPredecessorClicked();

    private HorizontalLayout addFooter() {
        footer.setSpacing(true);
        footer.addComponent(save);
        footer.addComponent(cancel);
        return footer;
    }

    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();
        if (source == save) {
            if (validate()) {
                saveClicked(event);
            }
        }
        else if (source == cancel) {
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
}