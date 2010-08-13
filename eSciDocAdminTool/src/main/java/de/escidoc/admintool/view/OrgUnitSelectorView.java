package de.escidoc.admintool.view;

import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.context.OrgUnitTree;
import de.escidoc.vaadin.utilities.LayoutHelper;

@SuppressWarnings("serial")
public class OrgUnitSelectorView extends CustomComponent {

    private final Window openTreeButtonWindow = new Window();

    private final Button okButton = new Button(ViewConstants.OK_LABEL);

    private final Button cancelButton = new Button(ViewConstants.CANCEL_LABEL);

    private final ListSelect orgUnitList;

    private final OrgUnitTree orgUnitTree = new OrgUnitTree();

    private final boolean withCombo;

    private Label predessorType;

    private ListSelect select;

    public OrgUnitSelectorView(final String caption, ListSelect orgUnitList) {
        this.orgUnitList = orgUnitList;
        openTreeButtonWindow.setModal(true);
        openTreeButtonWindow.setCaption(caption);
        openTreeButtonWindow.setHeight("650px");
        openTreeButtonWindow.setWidth("550px");
        openTreeButtonWindow.addComponent(orgUnitTree);
        addListeners();
        final HorizontalLayout hor =
            LayoutHelper.create("", "", okButton, cancelButton, 10, false);
        openTreeButtonWindow.addComponent(hor);
        withCombo = false;
    }

    public OrgUnitSelectorView(final String caption, ListSelect orgUnitList,
        String[] types, final Label predessorType) {
        this.orgUnitList = orgUnitList;
        this.predessorType = predessorType;
        FormLayout form = new FormLayout();
        openTreeButtonWindow.setModal(true);
        openTreeButtonWindow.setCaption(caption);
        openTreeButtonWindow.setHeight("650px");
        openTreeButtonWindow.setWidth("550px");
        select = new ListSelect();
        select.setRows(1);
        for (String type : types) {
            select.addItem(type);
        }
        form.addComponent(select);
        form.addComponent(orgUnitTree);

        addListeners();
        final HorizontalLayout hor =
            LayoutHelper.create("", "", okButton, cancelButton, 10, false);
        form.addComponent(hor);
        openTreeButtonWindow.addComponent(form);
        withCombo = true;
    }

    private void addListeners() {
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
    }

    public Window getWidget() {
        return openTreeButtonWindow;
    }

    public void okButtonClicked(final ClickEvent event) {
        if (withCombo) {
            okButtonWithComboClicked(event);
        }
        else {
            okButtonWithoutComboClicked(event);
        }
    }

    public void okButtonWithComboClicked(final ClickEvent event) {
        final Object o = this.orgUnitTree.getSelectedItems();

        if (o instanceof Set) {
            @SuppressWarnings("unchecked")
            final Set<String> set = (Set<String>) o;
            for (final String str : set) {
                orgUnitList.addItem(str);
            }
        }
        else if (o instanceof Object) {
            orgUnitList.addItem(o);
        }

        predessorType.setCaption((String) select.getValue());
        closeWindow();
    }

    public void okButtonWithoutComboClicked(final ClickEvent event) {
        final Object o = this.orgUnitTree.getSelectedItems();

        if (o instanceof Set) {
            @SuppressWarnings("unchecked")
            final Set<String> set = (Set<String>) o;
            for (final String str : set) {
                orgUnitList.addItem(str);
            }
        }
        else if (o instanceof Object) {
            orgUnitList.addItem(o);
        }
        closeWindow();
    }

    private void closeWindow() {
        ((Window) openTreeButtonWindow.getParent())
            .removeWindow(openTreeButtonWindow);
    }

    public void cancelButtonClick(final ClickEvent event) {
        closeWindow();
    }
}