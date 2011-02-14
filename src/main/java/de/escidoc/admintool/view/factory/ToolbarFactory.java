/**
 * 
 */
package de.escidoc.admintool.view.factory;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;

/**
 * @author ASP
 * 
 */
public class ToolbarFactory {

    private static final int ROWS = 1;

    private static final int COLUMNS = 3;

    private static final String ESCIDOC_LOGO = "images/SchriftLogo.jpg";

    private static final String TOOLBAR_STYLE_NAME = "escidoc-toolbar";

    private final HorizontalLayout hLayout = new HorizontalLayout();

    private final Embedded embedded = new Embedded();

    final ThemeResource imageResource = new ThemeResource(ESCIDOC_LOGO);

    private GridLayout gLayout;

    public ToolbarFactory() {
        // do not init
    }

    public GridLayout createToolbar(final Button[] buttons) {
        if (gLayout == null) {
            gLayout = new GridLayout(COLUMNS, ROWS);
            gLayout.setMargin(false);
            gLayout.setSpacing(false);
            gLayout.setWidth("100%");
            gLayout.setStyleName(TOOLBAR_STYLE_NAME);
            gLayout.addComponent(hLayout, 0, 0);
            addBackgroundImage();
            addButtons(buttons);
        }

        return gLayout;
    }

    private void addButtons(final Button[] buttons) {
        for (final Button button : buttons) {
            gLayout.addComponent(button, 2, 0);
            gLayout.setComponentAlignment(button, Alignment.MIDDLE_RIGHT);
        }
    }

    private void addBackgroundImage() {
        embedded.setType(Embedded.TYPE_IMAGE);
        embedded.setSource(imageResource);
        gLayout.setComponentAlignment(embedded, Alignment.TOP_LEFT);
        hLayout.addComponent(embedded);
        hLayout.setExpandRatio(embedded, 1);
    }
}