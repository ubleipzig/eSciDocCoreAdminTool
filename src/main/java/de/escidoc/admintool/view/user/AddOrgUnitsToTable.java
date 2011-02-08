package de.escidoc.admintool.view.user;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.resource.ResourceRefDisplay;
import de.escidoc.admintool.view.resource.ResourceTreeView;
import de.escidoc.core.resources.oum.OrganizationalUnit;

final class AddOrgUnitsToTable implements ClickListener {

    private static final long serialVersionUID = 7991096609461494934L;

    private final Window mainWindow;

    private final Window modalWindow;

    private final ResourceTreeView resourceTreeView;

    private final Table orgUnitTable;

    public AddOrgUnitsToTable(final Window mainWindow,
        final Window modalWindow, final ResourceTreeView resourceTreeView,
        final Table orgUnitTable) {
        this.mainWindow = mainWindow;
        this.modalWindow = modalWindow;
        this.resourceTreeView = resourceTreeView;
        this.orgUnitTable = orgUnitTable;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        addOrgUnitToTable();
        mainWindow.removeWindow(modalWindow);
    }

    private void addOrgUnitToTable() {
        getSelectedOrgUnitFromTree();
    }

    private Set<OrganizationalUnit> notOpened;

    @SuppressWarnings("unchecked")
    private void getSelectedOrgUnitFromTree() {
        notOpened = new HashSet<OrganizationalUnit>();
        final Object selected = resourceTreeView.getSelected();

        if (moreThanOne(selected)) {
            for (final OrganizationalUnit selectedOrgUnit : (Set<OrganizationalUnit>) selected) {
                tryAddToList(selectedOrgUnit);
            }
        }
        else if (selected instanceof OrganizationalUnit) {
            final OrganizationalUnit selectedOrgUnit =
                (OrganizationalUnit) selected;
            tryAddToList(selectedOrgUnit);

        }
        showMessage();

    }

    private void tryAddToList(final OrganizationalUnit selectedOrgUnit) {
        if (isOpen(selectedOrgUnit)) {
            orgUnitTable.addItem(toResourceRefDesplay(selectedOrgUnit));
        }
        else {
            notOpened.add(selectedOrgUnit);
        }

    }

    private void showMessage() {
        if (!notOpened.isEmpty()) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (final OrganizationalUnit notOpenedOrgUnit : notOpened) {
                final String xLinkTitle = notOpenedOrgUnit.getXLinkTitle();
                stringBuilder.append(xLinkTitle).append(", ");
            }
            stringBuilder.append(" is not in status opened.");
            ModalDialog.showMessage(mainWindow,
                "Can only add organizational unit in status open. ",
                stringBuilder.toString());

        }
    }

    private boolean moreThanOne(final Object selected) {
        return selected instanceof Set;
    }

    private boolean isOpen(final OrganizationalUnit selectedOrgUnit) {
        return "opened".equalsIgnoreCase(selectedOrgUnit
            .getProperties().getPublicStatus());
    }

    private ResourceRefDisplay toResourceRefDesplay(
        final OrganizationalUnit selectedOrgUnit) {
        return new ResourceRefDisplay(selectedOrgUnit.getObjid(),
            selectedOrgUnit.getProperties().getName());
    }
}