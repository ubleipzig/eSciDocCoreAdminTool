/**
 * 
 */
package de.escidoc.admintool.view.orgunit.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.context.OrgUnitTree;
import de.escidoc.admintool.view.orgunit.PredecessorType;

/**
 * @author ASP
 * 
 */
public abstract class AbstractPredecessorEditor extends CustomComponent
    implements IPredecessorEditor {

    private static final long serialVersionUID = -9122833046327977836L;

    protected final Window modalWindow = new Window();

    private final VerticalLayout fl = new VerticalLayout();

    protected HorizontalLayout footer = new HorizontalLayout();

    protected Button okButton = new Button("Ok");

    protected Button cancelButton = new Button("Cancel");

    protected OrgUnitTree tree = new OrgUnitTree();

    private Window parent;

    private ListSelect select;

    private AbstractComponent predecessor;

    private HorizontalLayout predecessorLayout;

    public AbstractPredecessorEditor() {
        modalWindow.setCaption(getLabel());
        modalWindow.addComponent(new Label(getEditorDescription()));
        modalWindow.addComponent(tree);

        addFooter();
        modalWindow.addComponent(footer);
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

            public void buttonClick(ClickEvent event) {
                onOkClicked(event);
            }
        });

        cancelButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -965029855181433407L;

            public void buttonClick(ClickEvent event) {
                onCancelClicked(event);
            }
        });
    }

    public List<Object> getSelected() {
        List<Object> returnVal = new ArrayList<Object>();
        Object o = getOrgUnitTreeSelectedItems();
        if (o instanceof Set) {
            returnVal.addAll((Set) o); // More than one result...
        }
        else if (o instanceof Object) { // Just one selected.
            returnVal.add(o);
        }
        return returnVal;
    }

    protected abstract void onOkClicked(ClickEvent event);

    protected void onCancelClicked(ClickEvent event) {
        parent.removeWindow(modalWindow);
        select.setValue(PredecessorType.BLANK);
    }

    public void addFooter() {
        footer.addComponent(okButton);
        footer.addComponent(cancelButton);
    }

    public void setParent(final Window parent) {
        this.parent = parent;
    }

    public void setList(final ListSelect select) {
        this.select = select;
    }

    public Object getOrgUnitTreeSelectedItems() {
        return tree.getSelectedItems();
    }

    public void setMultiSelect(boolean multiSelect) {
        tree.setMultiSelect(multiSelect);
    }

    /**
     * @return the parent
     */
    public final Window getWidgetParent() {
        return parent;
    }

    public void setPredecessorResult(AbstractComponent predecessor) {
        this.predecessor = predecessor;
    }

    /**
     * @return the predecessor
     */
    public final AbstractComponent getPredecessor() {
        return predecessor;
    }

    public void setPredecessorLayout(HorizontalLayout predecessorLayout) {
        this.predecessorLayout = predecessorLayout;
    }

    public void replacePredecessorView(AbstractComponent newPredecessor) {
        this.predecessorLayout.replaceComponent(this.predecessor,
            newPredecessor);
        this.predecessor = newPredecessor;
    }

}