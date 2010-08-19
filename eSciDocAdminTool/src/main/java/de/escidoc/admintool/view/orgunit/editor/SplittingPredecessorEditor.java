package de.escidoc.admintool.view.orgunit.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.context.OrgUnitTree;
import de.escidoc.admintool.view.orgunit.OrgUnitAddView;
import de.escidoc.admintool.view.orgunit.PredecessorType;
import de.escidoc.admintool.view.orgunit.predecessor.SplittingPredeccesorView;
import de.escidoc.vaadin.dialog.ErrorDialog;
import de.escidoc.vaadin.utilities.LayoutHelper;

public class SplittingPredecessorEditor extends CustomComponent
    implements IPredecessorEditor {
    private static final String WIDTH = "1200px";

    private static final String HEIGHT = "500px";

    private static final long serialVersionUID = 6885054493792037547L;

    private static final String SET_PREDECESSOR_SPLITTING =
        "Set Predecessor Splitting";

    private static final String DESCRIPTION =
        "Select one organizational unit, please";

    protected final Window modalWindow = new Window();

    protected HorizontalLayout footer = new HorizontalLayout();

    protected OrgUnitTree tree;

    protected Button okButton = new Button("Ok");

    protected Button cancelButton = new Button("Cancel");

    private ListSelect select;

    private Window parent;

    private OrgUnitAddView orgUnitAddView;

    private String orgUnitName;

    private final Button addButton = new Button("Add");

    private final Button editButton = new Button("Edit");

    private final Button removebutton = new Button("Remove");

    private final TextField nameTextField = new TextField();

    private final ListSelect ls = new ListSelect();

    private final SplitPanel pan = new SplitPanel();

    private final OrgUnitService service;

    public SplittingPredecessorEditor(OrgUnitService service) {
        this.service = service;
        tree = new OrgUnitTree(service);
    }

    public void buildLayout() {
        tree.setMultiSelect(false);
        modalWindow.setWidth(WIDTH);
        modalWindow.setHeight(HEIGHT);
        modalWindow.setModal(true);
        modalWindow.setCaption(getLabel());
        pan.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);

        pan.setFirstComponent(createLeftSide());
        pan.setSecondComponent(createRightSide());
        modalWindow.addComponent(pan);
        modalWindow.addComponent(addFooter());
        addListener();
        setCompositionRoot(new VerticalLayout());
    }

    private FormLayout createLeftSide() {
        final FormLayout comp = new FormLayout();
        comp.addComponent(new Label(getEditorDescription()));
        comp.addComponent(tree);
        return comp;
    }

    private FormLayout createRightSide() {
        final FormLayout comp = new FormLayout();
        ls.setNullSelectionAllowed(false);
        ls.addItem(orgUnitName);
        ls.setWidth("400px");
        comp.addComponent(LayoutHelper.create("", ls, 0, 100, false,
            new Button[] { addButton, editButton, removebutton }));
        addRightButtonsListener();
        nameTextField.setWidth("400px");
        comp.addComponent(nameTextField);
        return comp;
    }

    private void addRightButtonsListener() {
        addButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -4295712465167241155L;

            @Override
            public void buttonClick(final ClickEvent event) {
                addRightClicked(event);
            }
        });

        editButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -3499158386491222115L;

            @Override
            public void buttonClick(final ClickEvent event) {
                editRightClicked(event);
            }
        });

        removebutton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -940316991862977275L;

            @Override
            public void buttonClick(final ClickEvent event) {
                removeRightClicked(event);
            }
        });

    }

    private void addRightClicked(final ClickEvent event) {
        ls.addItem(nameTextField.getValue());
        nameTextField.setValue("");
    }

    private void editRightClicked(final ClickEvent event) {
        final Object selectedOrgUnit = ls.getValue();
        nameTextField.setValue(selectedOrgUnit);
    }

    private void removeRightClicked(final ClickEvent event) {
        ls.removeItem(ls.getValue());
    }

    private void addListener() {
        okButton.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = -7640719928940296001L;

            @Override
            public void buttonClick(final ClickEvent event) {
                onOkClicked(event);
            }
        });

        cancelButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -965029855181433407L;

            @Override
            public void buttonClick(final ClickEvent event) {
                onCancelClicked(event);
            }
        });
    }

    protected void onOkClicked(final ClickEvent event) {
        if (isValid(getSelected().size())) {
            showAddedPredecessors();
            closeWindow();
        }
        else {
            showErrorMessage();
        }
    }

    protected void onCancelClicked(final ClickEvent event) {
        closeWindow();
        select.setValue(PredecessorType.BLANK);
    }

    private void closeWindow() {
        parent.removeWindow(modalWindow);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getSelected() {
        final List<Object> selectedOrgUnits = new ArrayList<Object>();
        final Object o = tree.getSelectedItems();
        if (o instanceof Set) {
            selectedOrgUnits.addAll((Set) o); // More than one result...
        }
        else if (o instanceof Object) { // Just one selected.
            selectedOrgUnits.add(o);
        }
        return selectedOrgUnits;
    }

    public HorizontalLayout addFooter() {
        footer.addComponent(okButton);
        footer.addComponent(cancelButton);
        return footer;
    }

    @Override
    public void setMainWindow(final Window parent) {
        this.parent = parent;
    }

    @Override
    public void setList(final ListSelect select) {
        this.select = select;
    }

    protected String getLabel() {
        return SET_PREDECESSOR_SPLITTING;
    }

    protected String getEditorDescription() {
        return DESCRIPTION;
    }

    protected boolean isValid(final int sizeOfSelectedOrgUnit) {
        return sizeOfSelectedOrgUnit == 1;
    }

    protected void showAddedPredecessors() {
        final Object o = ls.getValue();

        final SplittingPredeccesorView view =
            new SplittingPredeccesorView((String) tree.getSelectedItems(),
                getNewOrgUnits());
        orgUnitAddView.showAddedPredecessors(view);
    }

    public List<Object> getNewOrgUnits() {
        final List<Object> newOrgUnits = new ArrayList<Object>();
        // final Object o = ls.getItemIds();
        // if (o instanceof Set) {
        // newOrgUnits.addAll((Set) o); // More than one result...
        // }
        // else if (o instanceof Object) { // Just one selected.
        // newOrgUnits.add(o);
        // }

        for (final Object object : ls.getItemIds()) {
            newOrgUnits.add(object);
        }

        return newOrgUnits;
    }

    protected void showErrorMessage() {
        parent.addWindow(new ErrorDialog(parent, "Grave Error",
            "Select one organizational unit, please."));
    }

    @Override
    public void setOrgUnitAddView(final OrgUnitAddView orgUnitAddView) {
        this.orgUnitAddView = orgUnitAddView;
        buildLayout();
    }

    @Override
    public Window getWidget() {
        return modalWindow;
    }

    @Override
    public void setNewOrgUnit(final String orgUnitName) {
        this.orgUnitName = orgUnitName;

    }
}