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
import de.escidoc.admintool.view.orgunit.OrgUnitAddView;

public class BlankPredecessorEditor extends CustomComponent
    implements IPredecessorEditor {
    private static final long serialVersionUID = 6885054493792037547L;

    protected Button okButton = new Button("Ok");

    protected final Window modalWindow = new Window();

    private Window parent;

    public BlankPredecessorEditor(OrgUnitService service) {
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
    public List<Object> getSelected() {
        return new ArrayList<Object>();
    }

    @Override
    public void setOrgUnitAddView(final OrgUnitAddView orgUnitAddView) {

    }

    @Override
    public void setNewOrgUnit(String orgUnitName) {
        // TODO Auto-generated method stub

    }
}