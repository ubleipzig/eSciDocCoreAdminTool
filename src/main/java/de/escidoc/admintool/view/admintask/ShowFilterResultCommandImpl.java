package de.escidoc.admintool.view.admintask;

import java.util.Set;

import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.FilterResourceView.ShowFilterResultCommand;
import de.escidoc.core.resources.Resource;

final class ShowFilterResultCommandImpl implements ShowFilterResultCommand {

    private final FormLayout formLayout = new FormLayout();

    final FilterResourceView filterResourceView;

    Table filteredList;

    POJOContainer<Resource> filteredResourcesContainer;

    /**
     * @param filterResourceView
     */
    public ShowFilterResultCommandImpl(
        final FilterResourceView filterResourceView) {
        this.filterResourceView = filterResourceView;
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
                PropertyId.OBJECT_ID, PropertyId.NAME);

        filteredList =
            new Table("Filtered Resources", filteredResourcesContainer);

        filteredList.setVisibleColumns(new Object[] { PropertyId.NAME });
        filteredList
            .setColumnHeader(PropertyId.NAME, ViewConstants.TITLE_LABEL);

        filteredList.setSelectable(true);
        filteredList.setMultiSelect(true);
    }

    private void showFilterResultView() {
        formLayout.addComponent(filteredList);
        addPurgeButton();
    }

    private void addPurgeButton() {
        final Button purgeBtn = new Button(ViewConstants.PURGE);
        formLayout.addComponent(purgeBtn);
        purgeBtn.addListener(new PurgeResourcesListener(this));
    }
}