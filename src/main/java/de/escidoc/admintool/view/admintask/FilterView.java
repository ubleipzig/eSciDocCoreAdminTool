package de.escidoc.admintool.view.admintask;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.Style.H2;
import de.escidoc.admintool.view.admintask.Style.Ruler;

public class FilterView extends AbstractAdminTaskView {

    private static final long serialVersionUID = -1412202753685048760L;

    public FilterView(final ServiceContainer services, final Window mainWindow) {
        super(services, mainWindow);
    }

    @Override
    public void addView() {
        Label text = new H2(ViewConstants.FILTERING_RESOURCES_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);
        cssLayout.addComponent(text);

        cssLayout.addComponent(new Ruler());

        text = new Label(ViewConstants.FILTER_TEXT, Label.CONTENT_XHTML);
        cssLayout.addComponent(text);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth(100, UNITS_PERCENTAGE);
        hLayout.setHeight(100, UNITS_PERCENTAGE);

        final FilterResourceView filterView =
            new FilterResourceView(services, mainWindow);
        hLayout.addComponent(filterView);
        cssLayout.addComponent(hLayout);
    }
}