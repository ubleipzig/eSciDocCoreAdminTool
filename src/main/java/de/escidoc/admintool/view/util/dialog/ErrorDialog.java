package de.escidoc.admintool.view.util.dialog;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.util.LayoutHelper;

/**
 * 
 * @author ASP
 * 
 */
public class ErrorDialog extends Window implements Button.ClickListener {
    private static final long serialVersionUID = 6255824594582824620L;

    private final FormLayout layout = new FormLayout();

    private final Window mainWindow;

    /**
     * Displays an error message dialog to the customer.
     * 
     * @param mainWindow
     *            the main window of the application.
     * @param caption
     *            the headline.
     * @param errorMessage
     *            the message, describing what went wrong.
     */
    public ErrorDialog(final Window mainWindow, final String caption,
        final String errorMessage) {
        this(mainWindow, caption, errorMessage, 600, 300);
    }

    /**
     * Displays an error message dialog to the customer.
     * 
     * @param mainWindow
     *            the main window of the application.
     * @param caption
     *            the headline.
     * @param errorMessage
     *            the message, describing what went wrong.
     * @param width
     *            the width of the window.
     * @param height
     *            the height of the window.
     */
    public ErrorDialog(final Window mainWindow, final String caption,
        final String errorMessage, final int width, final int height) {

        Preconditions.checkNotNull(mainWindow,
            "mainWindow can not be null: %s", mainWindow);
        Preconditions.checkNotNull(caption, "caption can not be null: %s",
            caption);
        Preconditions.checkNotNull(mainWindow,
            "errorMessage can not be null: %s", errorMessage);

        this.mainWindow = mainWindow;
        super.setWidth(width + "px");
        super.setHeight(height + "px");
        super.setCaption(caption);
        super.setModal(true);

        final Label errorMassageLabel = new Label(errorMessage);
        layout.addComponent(errorMassageLabel);
        layout.setExpandRatio(errorMassageLabel, 1);
        // layout.addComponent(LayoutHelper.createContextView("", new Label(errorMessage),
        // 10, false));
        final Button button = new Button("OK");
        layout.addComponent(LayoutHelper.create("", button, 10, false));
        button.addListener(this);
        super.addComponent(layout);
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        mainWindow.removeWindow(ErrorDialog.this);
    }
}
