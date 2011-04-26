package de.escidoc.admintool.view.admintask.filter;

import com.google.common.base.Preconditions;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.AbstractAdminTaskView;
import de.escidoc.admintool.view.admintask.Style;
import de.escidoc.admintool.view.admintask.Style.H2;

public class FilterView extends AbstractAdminTaskView {

    private static final long serialVersionUID = -1412202753685048760L;

    private final PdpRequest pdpRequest;

    public FilterView(final ServiceContainer services, final Window mainWindow, final PdpRequest pdpRequest) {
        super(services, mainWindow);
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        this.pdpRequest = pdpRequest;
    }

    @Override
    public void addView() {
        addHeader();
        addRuler();
        addDescription();
        addContent();
    }

    private void addContent() {
        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth(100, UNITS_PERCENTAGE);
        hLayout.setHeight(100, UNITS_PERCENTAGE);

        final FilterResourceView filterView = new FilterResourceView(services, mainWindow, pdpRequest);
        filterView.init();
        hLayout.addComponent(filterView);
        cssLayout.addComponent(hLayout);
    }

    private void addDescription() {
        cssLayout.addComponent(new Label(ViewConstants.FILTER_DESCRIPTION_TEXT, Label.CONTENT_XHTML));
    }

    private void addRuler() {
        cssLayout.addComponent(new Style.Ruler());
    }

    private void addHeader() {
        final Label text = new H2(ViewConstants.FILTERING_RESOURCES_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);
        cssLayout.addComponent(text);
    }
}