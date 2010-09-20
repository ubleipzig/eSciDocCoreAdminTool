/**
 * 
 */
package de.escidoc.admintool.view.orgunit.editor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.context.OrgUnitTree;
import de.escidoc.admintool.view.orgunit.AbstractOrgUnitView;
import de.escidoc.admintool.view.orgunit.PredecessorType;

/**
 * @author ASP
 * 
 */
public abstract class AbstractPredecessorEditor extends CustomComponent
    implements IPredecessorEditor {

    private static final Logger log =
        LoggerFactory.getLogger(AbstractPredecessorEditor.class);

    private static final long serialVersionUID = -9122833046327977836L;

    protected final Window modalWindow = new Window();

    protected HorizontalLayout footer = new HorizontalLayout();

    protected Button okButton = new Button("Ok");

    protected Button cancelButton = new Button("Cancel");

    protected OrgUnitTree tree;

    private Window parent;

    private ListSelect select;

    protected HorizontalLayout predecessorLayout;

    // protected OrgUnitAddView orgUnitAddView;

    private String orgUnitName;

    private final OrgUnitService service;

    protected AbstractOrgUnitView orgUnitEditorView;

    public AbstractPredecessorEditor(final OrgUnitService service) {
        this.service = service;
        tree = new OrgUnitTree(service);
        modalWindow.setCaption(getLabel());
        modalWindow.setWidth("800px");
        modalWindow.addComponent(new Label(getEditorDescription()));
        modalWindow.addComponent(tree);

        modalWindow.addComponent(addFooter());
        addListener();
        setCompositionRoot(new VerticalLayout());
    }

    protected abstract String getLabel();

    protected abstract String getEditorDescription();

    @Override
    public Window getWidget() {
        return modalWindow;
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
        if (isValid(getSelectedPredecessors().size())) {
            showAddedPredecessors();
            closeWindow();
        }
        else {
            showErrorMessage();
        }
    }

    @Override
    public List<ResourceRefDisplay> getSelectedPredecessors() {
        final List<ResourceRefDisplay> selectedOrgUnits =
            new ArrayList<ResourceRefDisplay>();

        final Object selectedItems = tree.getSelectedItems();
        if (selectedItems instanceof Set<?>) {
            for (final Object item : (Set<?>) selectedItems) {
                selectedOrgUnits.add((ResourceRefDisplay) item);
            }
        }
        else if (selectedItems instanceof ResourceRefDisplay) {
            selectedOrgUnits.add((ResourceRefDisplay) selectedItems);
        }
        else {
            return Collections.emptyList();
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

    @Override
    public void setMainWindow(final Window parent) {
        this.parent = parent;
    }

    @Override
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

    public void setOrgUnitEditorView(final AbstractOrgUnitView orgUnitEditorView) {
        this.orgUnitEditorView = orgUnitEditorView;
    }

    @Override
    public void setNewOrgUnit(final String orgUnitName) {
        this.orgUnitName = orgUnitName;
    }

    /**
     * @return the orgUnitName
     */
    public final String getOrgUnitName() {
        return orgUnitName;
    }

}