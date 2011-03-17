/**
 * 
 */
package de.escidoc.admintool.view.factory;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.view.ViewConstants;

/**
 * @author ASP
 * 
 */
public class ToolbarFactory {

    private static final int ROWS = 1;

    private static final int COLUMNS = 3;

    private final HorizontalLayout hLayout = new HorizontalLayout();

    private final Embedded embedded = new Embedded();

    private final ThemeResource imageResource = new ThemeResource(
        AppConstants.ESCIDOC_LOGO);

    private GridLayout gLayout;

    public ToolbarFactory() {
        // do not init
    }

    public GridLayout createToolbar(final HorizontalLayout layout) {
        if (gLayout == null) {
            gLayout = new GridLayout(COLUMNS, ROWS);
            gLayout.setMargin(false);
            gLayout.setSpacing(false);
            gLayout.setWidth("100%");
            gLayout.setStyleName(ViewConstants.TOOLBAR_STYLE_NAME);
            gLayout.addComponent(hLayout, 0, 0);
            addBackgroundImage();
            add(layout);
        }

        return gLayout;
    }

    private void add(final HorizontalLayout layout) {
        gLayout.addComponent(layout, 2, 0);
        gLayout.setComponentAlignment(layout, Alignment.MIDDLE_RIGHT);
    }

    private void addBackgroundImage() {
        embedded.setType(Embedded.TYPE_IMAGE);
        embedded.setSource(imageResource);
        gLayout.setComponentAlignment(embedded, Alignment.TOP_LEFT);
        hLayout.addComponent(embedded);
        hLayout.setExpandRatio(embedded, 1);
    }

}