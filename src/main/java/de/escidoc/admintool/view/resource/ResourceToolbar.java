package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;

import de.escidoc.admintool.view.ViewConstants;

public class ResourceToolbar extends CustomComponent {

    private static final long serialVersionUID = 1436927861311900013L;

    private Button newBtn;

    private Button delBtn;

    private Button openBtn;

    private final CssLayout hLayout = new CssLayout();

    private final ResourceViewImpl resourceViewImpl;

    public ResourceToolbar(final ResourceViewImpl resourceViewImpl) {
        Preconditions.checkNotNull(resourceViewImpl,
            "resourceViewImpl is null: %s", resourceViewImpl);
        this.resourceViewImpl = resourceViewImpl;
        setCompositionRoot(hLayout);

        setSizeFull();
        hLayout.setHeight(100, UNITS_PERCENTAGE);

        hLayout.addStyleName("toolbar-invert");
        hLayout.setMargin(true);
        hLayout.setMargin(true, true, true, true);

        creatButtons();
        hLayout.addComponent(newBtn);
        hLayout.addComponent(delBtn);
        hLayout.addComponent(openBtn);
    }

    private void creatButtons() {
        newBtn =
            new Button(ViewConstants.NEW, new ShowNewResourceListener(
                resourceViewImpl));
        openBtn = new Button(ViewConstants.OPEN, new OpenResourceListener());

        delBtn = new Button(ViewConstants.DELETE, new DelResourceListener());
    }
}