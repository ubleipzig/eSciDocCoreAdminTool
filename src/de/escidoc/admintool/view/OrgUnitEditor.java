package de.escidoc.admintool.view;

import java.io.Serializable;
import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.vaadin.utilities.LayoutHelper;

public class OrgUnitEditor extends CustomComponent implements Serializable {
    private static final long serialVersionUID = 2985333143579122542L;

    private final Panel panel = new Panel();

    private final ListSelect orgUnitList;

    private final Button addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton = new Button(
        ViewConstants.REMOVE_LABEL);

    public OrgUnitEditor(final ListSelect orgUnitList) {
        this.orgUnitList = orgUnitList;
        panel.setStyleName(Reindeer.PANEL_LIGHT);
        panel.addComponent(orgUnitList);
        panel.addComponent(LayoutHelper.create("", "", addOrgUnitButton,
            removeOrgUnitButton, "150px", "0px", false));
        addListeners();
        setCompositionRoot(panel);
    }

    private void addListeners() {
        addOrgUnitButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -266152738843712262L;

            public void buttonClick(ClickEvent event) {
                addButtonClicked(event);
            }
        });

        removeOrgUnitButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -8884073931795851352L;

            public void buttonClick(final ClickEvent event) {
                removeButtonClicked(event);
            }
        });
    }

    public void addButtonClicked(ClickEvent event) {
        getApplication().getMainWindow().addWindow(
            new OrgUnitSelectorView(orgUnitList).getWidget());
    }

    public void removeButtonClicked(final ClickEvent event) {
        final Object o = orgUnitList.getValue();
        if (o instanceof Set) {
            final Set set = (Set) o;
            for (final Object ob : set) {
                orgUnitList.removeItem(ob);
            }
        }
        else if (o instanceof Object) {
            orgUnitList.removeItem(o);
        }
    }
}
