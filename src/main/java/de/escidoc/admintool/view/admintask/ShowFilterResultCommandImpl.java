package de.escidoc.admintool.view.admintask;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.core.resources.Resource;

final class ShowFilterResultCommandImpl implements ShowFilterResultCommand {

    private final FormLayout formLayout = new FormLayout();

    private final Button showStatusButton = new Button(
        ViewConstants.SHOW_STATUS);

    private final Label statusLabel = new Label(ViewConstants.STATUS);

    private final ShowPurgeStatusListener showPurgeStatusListener;

    final FilterResourceView filterResourceView;

    Table filteredList;

    POJOContainer<Resource> filteredResourcesContainer;

    private final PdpRequest pdpRequest;

    public ShowFilterResultCommandImpl(
        final FilterResourceView filterResourceView, final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(filterResourceView,
            "filterResourceView is null: %s", filterResourceView);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s",
            pdpRequest);
        this.filterResourceView = filterResourceView;
        this.pdpRequest = pdpRequest;
        showPurgeStatusListener =
            new ShowPurgeStatusListener(filterResourceView.adminService,
                filterResourceView.mainWindow, statusLabel);
    }

    @Override
    public void execute(final Set<Resource> filteredResources) {
        showFilteredResources(filteredResources);
    }

    private void showFilteredResources(final Set<Resource> filteredResources) {
        resetFilterResultView();
        if (isEmpty(filteredResources)) {
            showNoResult();
        }
        else {
            createFilterResultView(filteredResources);
            showFilterResultView();
        }
    }

    private boolean isEmpty(final Set<Resource> filteredResources) {
        return filteredResources == null || filteredResources.isEmpty();
    }

    private void showNoResult() {
        formLayout.addComponent(new Label(ViewConstants.NO_RESULT));
    }

    private void resetFilterResultView() {
        filterResourceView.getViewLayout().addComponent(formLayout);
        formLayout.removeAllComponents();
    }

    private void createFilterResultView(final Set<Resource> filteredResources) {
        filteredResourcesContainer =
            new POJOContainer<Resource>(filteredResources,
                PropertyId.OBJECT_ID, PropertyId.XLINK_TITLE);

        filteredList =
            new Table(ViewConstants.FILTERED_RESOURCES,
                filteredResourcesContainer);

        filteredList.setVisibleColumns(new Object[] { PropertyId.OBJECT_ID,
            PropertyId.XLINK_TITLE });
        filteredList.setColumnHeader(PropertyId.OBJECT_ID,
            ViewConstants.OBJECT_ID_LABEL);
        filteredList.setColumnHeader(PropertyId.XLINK_TITLE,
            ViewConstants.TITLE_LABEL);

        filteredList.setSelectable(true);
        filteredList.setMultiSelect(true);
    }

    private void showFilterResultView() {
        formLayout.addComponent(filteredList);
        if (isPurgePermitted()) {
            addPurgeButton();
            addShowPurgeStatusButton();
            addStatusLabel();
        }
    }

    private boolean isPurgePermitted() {
        return pdpRequest.isPermitted(ActionIdConstants.PURGE_RESOURCES);
    }

    private void addPurgeButton() {
        final Button purgeBtn = new Button(ViewConstants.PURGE);
        purgeBtn.setWidth("150px");
        formLayout.addComponent(purgeBtn);
        purgeBtn.addListener(new PurgeResourcesListener(this));
    }

    private void addStatusLabel() {
        formLayout.addComponent(statusLabel);
    }

    private void addShowPurgeStatusButton() {
        showStatusButton.setWidth("150px");
        formLayout.addComponent(showStatusButton);
        showStatusButton.addListener(showPurgeStatusListener);
    }
}
