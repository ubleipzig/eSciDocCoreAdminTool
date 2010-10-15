package de.escidoc.admintool.view.orgunit;

import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.ListSelect;

import de.escidoc.admintool.service.OrgUnitService;

public class OrgUnitEditor extends CustomComponent {

    private final ListSelect orgUnitList;

    private final Button addOrgUnitButton;

    private final Button removeOrgUnitButton;

    private final String caption;

    private final OrgUnitService service;

    public OrgUnitEditor(final String caption, final ListSelect orgUnitList,
        final Button addOrgUnitButton, final Button removeOrgUnitButton,
        final OrgUnitService service) {
        this.caption = caption;
        this.orgUnitList = orgUnitList;
        this.addOrgUnitButton = addOrgUnitButton;
        this.removeOrgUnitButton = removeOrgUnitButton;
        this.service = service;
        addListeners();
        setCompositionRoot(orgUnitList);
    }

    private void addListeners() {
        addOrgUnitButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -266152738843712262L;

            public void buttonClick(final ClickEvent event) {
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

    public void addButtonClicked(final ClickEvent event) {
        getApplication().getMainWindow().addWindow(
            new OrgUnitSelectorView(caption, orgUnitList, service).getWidget());
    }

    public void removeButtonClicked(final ClickEvent event) {
        final Object o = orgUnitList.getValue();
        if (o instanceof Set) {
            final Set set = (Set) o;
            for (final Object ob : set) {
                orgUnitList.removeItem(ob);
            }
        }
        else {
            orgUnitList.removeItem(o);
        }
    }
}
