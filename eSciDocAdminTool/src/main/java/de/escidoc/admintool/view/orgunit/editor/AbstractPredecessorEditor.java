/**
 * 
 */
package de.escidoc.admintool.view.orgunit.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.view.context.OrgUnitTree;
import de.escidoc.admintool.view.orgunit.OrgUnitAddView;
import de.escidoc.admintool.view.orgunit.PredecessorType;

/**
 * @author ASP
 * 
 */
public abstract class AbstractPredecessorEditor extends CustomComponent
    implements IPredecessorEditor {

    private static final long serialVersionUID = -9122833046327977836L;

    protected final Window modalWindow = new Window();

    protected HorizontalLayout footer = new HorizontalLayout();

    protected Button okButton = new Button("Ok");

    protected Button cancelButton = new Button("Cancel");

    protected OrgUnitTree tree = new OrgUnitTree();

    private Window parent;

    private ListSelect select;

    protected HorizontalLayout predecessorLayout;

    protected OrgUnitAddView orgUnitAddView;

    public AbstractPredecessorEditor() {
        modalWindow.setCaption(getLabel());
        modalWindow.addComponent(new Label(getEditorDescription()));
        modalWindow.addComponent(tree);
        modalWindow.addComponent(addFooter());
        addListener();
        setCompositionRoot(new VerticalLayout());
    }

    protected abstract String getLabel();

    protected abstract String getEditorDescription();

    public Window getWidget() {
        return modalWindow;
    }

    private void addListener() {
        okButton.addListener(new Button.ClickListener() {

            private static final long serialVersionUID = -7640719928940296001L;

            public void buttonClick(final ClickEvent event) {
                onOkClicked(event);
            }
        });

        cancelButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -965029855181433407L;

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

    protected abstract void showErrorMessage();

    protected abstract boolean isValid(int sizeOfSelectedOrgUnit);

    protected abstract void showAddedPredecessors();

    protected void onCancelClicked(final ClickEvent event) {
        closeWindow();
        select.setValue(PredecessorType.BLANK);
    }

    private void closeWindow() {
        parent.removeWindow(modalWindow);
    }

    public HorizontalLayout addFooter() {
        footer.addComponent(okButton);
        footer.addComponent(cancelButton);
        return footer;
    }

    public void setMainWindow(final Window parent) {
        this.parent = parent;
    }

    public void setList(final ListSelect select) {
        this.select = select;
    }

    public void setMultiSelect(final boolean multiSelect) {
        tree.setMultiSelect(multiSelect);
    }

    /**
     * @return the parent
     */
    public final Window getMainWindow() {
        return parent;
    }

    public void setOrgUnitAddView(final OrgUnitAddView orgUnitAddView) {
        this.orgUnitAddView = orgUnitAddView;
    }

}