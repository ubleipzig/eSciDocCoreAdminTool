package de.escidoc.admintool.view.admintask;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.Style.H2;
import de.escidoc.admintool.view.admintask.Style.Ruler;

public class LoadExample extends AbstractAdminTaskView {

    private static final long serialVersionUID = -7128844384392979070L;

    private AddToContainer command;

    public LoadExample(final ServiceContainer services, final Window mainWindow) {
        super(services, mainWindow);
    }

    public void setCommand(final AddToContainer command) {
        this.command = command;
    }

    @Override
    public void addView() {
        Label text = new H2(ViewConstants.LOAD_EXAMPLES_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);
        cssLayout.addComponent(text);

        cssLayout.addComponent(new Ruler());

        text = new Label(ViewConstants.LOAD_EXAMPLE_TEXT, Label.CONTENT_XHTML);
        cssLayout.addComponent(text);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth(100, UNITS_PERCENTAGE);
        hLayout.setHeight(100, UNITS_PERCENTAGE);

        final LoadExampleView filterView =
            new LoadExampleResourceViewImpl(mainWindow,
                services.getAdminService(), command);
        hLayout.addComponent(filterView);
        cssLayout.addComponent(hLayout);
    }
}