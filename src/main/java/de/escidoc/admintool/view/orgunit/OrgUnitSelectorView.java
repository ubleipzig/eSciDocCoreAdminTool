package de.escidoc.admintool.view.orgunit;

import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.OrgUnitTree;
import de.escidoc.vaadin.utilities.LayoutHelper;

@SuppressWarnings("serial")
public class OrgUnitSelectorView extends CustomComponent {

    private final Window openTreeButtonWindow = new Window();

    private final Button okButton = new Button(ViewConstants.OK_LABEL);

    private final Button cancelButton = new Button(ViewConstants.CANCEL_LABEL);

    private final ListSelect orgUnitList;

    private OrgUnitTree orgUnitTree;

    public OrgUnitSelectorView(final String caption,
        final ListSelect orgUnitList, final OrgUnitService service) {
        this.orgUnitList = orgUnitList;
        openTreeButtonWindow.setModal(true);
        openTreeButtonWindow.setCaption(caption);
        openTreeButtonWindow.setHeight("650px");
        openTreeButtonWindow.setWidth("550px");
        openTreeButtonWindow.addComponent(orgUnitTree =
            new OrgUnitTree(service));
        addListeners();
        final HorizontalLayout hor =
            LayoutHelper.create("", "", okButton, cancelButton, 10, false);
        openTreeButtonWindow.addComponent(hor);
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

    @SuppressWarnings("unchecked")
    public void okButtonClicked(final ClickEvent event) {
        final Object o = orgUnitTree.getSelectedItems();

        if (o instanceof Set) {
            final Set<ResourceRefDisplay> set = (Set<ResourceRefDisplay>) o;
            for (final ResourceRefDisplay str : set) {
                orgUnitList.addItem(str);
            }
        }
        else {
            orgUnitList.addItem(o);
        }
        closeWindow();
    }

    private void closeWindow() {
        (openTreeButtonWindow.getParent()).removeWindow(openTreeButtonWindow);
    }

    public void cancelButtonClick(final ClickEvent event) {
        closeWindow();
    }
}