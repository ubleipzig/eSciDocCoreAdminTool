package de.escidoc.admintool.view.orgunit.editor;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.orgunit.AbstractOrgUnitView;

public class BlankPredecessorEditor extends CustomComponent
    implements IPredecessorEditor {
    private static final long serialVersionUID = 6885054493792037547L;

    protected Button okButton = new Button("Ok");

    protected final Window modalWindow = new Window();

    private Window parent;

    public BlankPredecessorEditor(final OrgUnitService service) {
        modalWindow.setCaption("Error");
        modalWindow.addComponent(new Label(
            "Select a valid Predecessor Type, please!"));
        modalWindow.addComponent(okButton);
        addListener();
        setCompositionRoot(new VerticalLayout());
    }

    @Override
    public Window getWidget() {
        return modalWindow;
    }

    @Override
    public void setMainWindow(final Window parent) {
        this.parent = parent;

    }

    @Override
    public void setList(final ListSelect select) {
    }

    private void addListener() {
        okButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -8695976921167275051L;

            @Override
            public void buttonClick(final ClickEvent event) {
                parent.removeWindow(modalWindow);
            }
        });
    }

    @Override
    public List<ResourceRefDisplay> getSelectedPredecessors() {
        return new ArrayList<ResourceRefDisplay>();
    }

    @Override
    public void setNewOrgUnit(final String orgUnitName) {
        throw new UnsupportedOperationException(
            "setNewOrgUnit is not supported for BlankPredecessorEditor");
    }

    @Override
    public void setOrgUnitEditorView(final AbstractOrgUnitView orgUnitEditorView) {
        throw new UnsupportedOperationException(
            "setOrgUnitEditorView is not supported for BlankPredecessorEditor");
    }
}