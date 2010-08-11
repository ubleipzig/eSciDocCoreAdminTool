package de.escidoc.admintool.view;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.context.OrgUnitTree;
import de.escidoc.vaadin.utilities.LayoutHelper;

public class OrgUnitTreeComponent extends CustomComponent
    implements Serializable {
    private static final long serialVersionUID = 2985333143579122542L;

    private final Window openTreeButtonWindow = new Window(
        ViewConstants.ORGANIZATION_UNITS_LABEL);

    private final ListSelect orgUnitList;

    private final Button addOrgUnitButton = new Button(ViewConstants.ADD_LABEL);

    private final Button removeOrgUnitButton = new Button(
        ViewConstants.REMOVE_LABEL);

    private final Button okButton = new Button(ViewConstants.OK_LABEL);

    private final Button cancelButton = new Button(ViewConstants.CANCEL_LABEL);

    private final OrgUnitTree tree = new OrgUnitTree(openTreeButtonWindow);

    private final Panel panel = new Panel();

    public OrgUnitTreeComponent(final ListSelect orgUnitList) {
        this.orgUnitList = orgUnitList;
        panel.addComponent(orgUnitList);
        addOrgUnitButton.addListener(new Button.ClickListener() {

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
        panel.addComponent(LayoutHelper.create("", "", addOrgUnitButton,
            removeOrgUnitButton, "150px", "0px", false));
        setCompositionRoot(panel);
    }

    public void addButtonClicked(ClickEvent event) {
        openTreeButtonWindow.setModal(true);
        /* Set window size. */
        openTreeButtonWindow.setHeight("650px");
        openTreeButtonWindow.setWidth("550px");
        openTreeButtonWindow.addComponent(tree);

        okButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = 3557479016087679412L;

            public void buttonClick(final ClickEvent event) {
                okButtonClicked(event);
            }
        });

        cancelButton.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = -779500960758708585L;

            public void buttonClick(final ClickEvent event) {
                cancelButtonClick(event);
            }
        });

        final HorizontalLayout hor =
            LayoutHelper.create("", "", okButton, cancelButton, "10px", false);
        openTreeButtonWindow.addComponent(hor);
        getApplication().getMainWindow().addWindow(openTreeButtonWindow);

    }

    public void okButtonClicked(final ClickEvent event) {
        final Object o = tree.getSelectedItems();
        if (o instanceof HashSet) {
            @SuppressWarnings("unchecked")
            final HashSet<String> set = (HashSet<String>) o;
            for (final String str : set) {
                orgUnitList.addItem(str);
            }
        }
        else if (o instanceof Set) {
            @SuppressWarnings("unchecked")
            final Set<String> set = (Set<String>) o;
            for (final String str : set) {
                orgUnitList.addItem(str);
            }
        }
        else if (o instanceof Object) {
            orgUnitList.addItem(o);
        }
        ((Window) openTreeButtonWindow.getParent())
            .removeWindow(openTreeButtonWindow);
    }

    public void cancelButtonClick(final ClickEvent event) {
        ((Window) openTreeButtonWindow.getParent())
            .removeWindow(openTreeButtonWindow);
    }

    public void removeButtonClicked(final ClickEvent event) {
        final Object o = orgUnitList.getValue();
        if (o instanceof HashSet) {
            final HashSet set = (HashSet) o;
            for (final Object ob : set) {
                orgUnitList.removeItem(ob);
            }
        }
        else if (o instanceof Set) {
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
