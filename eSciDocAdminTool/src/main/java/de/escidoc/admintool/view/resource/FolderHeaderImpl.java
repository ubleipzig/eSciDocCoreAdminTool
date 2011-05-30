package de.escidoc.admintool.view.resource;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class FolderHeaderImpl extends CustomComponent implements FolderHeader {

    private static final long serialVersionUID = 6885181952888435844L;

    private final HorizontalLayout hLayout = new HorizontalLayout();

    public FolderHeaderImpl(final String text) {
        setCompositionRoot(hLayout);
        setHeight(20, UNITS_PIXELS);
        final Label label = new Label("<b>" + text + "</b>", Label.CONTENT_XHTML);

        hLayout.addComponent(label);
    }
}
