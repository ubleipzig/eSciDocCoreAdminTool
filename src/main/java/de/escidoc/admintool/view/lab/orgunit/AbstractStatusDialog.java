package de.escidoc.admintool.view.lab.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public abstract class AbstractStatusDialog extends CustomComponent {

    private static final long serialVersionUID = 7257217308593700424L;

    private static final Logger LOG = LoggerFactory
        .getLogger(AbstractStatusDialog.class);

    protected final OrgUnitViewLab orgUnitEditView;

    private VerticalLayout mainLayout = new VerticalLayout();

    protected Window subwindow = new Window(getWindowCaption());

    private final TextField commentField = new TextField(ViewConstants.COMMENT);

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button submitBtn = new Button(getSubmitBtnText());

    private final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL);

    protected final OrgUnitToolbarLab toolbar;

    public AbstractStatusDialog(final OrgUnitToolbarLab toolbar,
        final OrgUnitViewLab orgUnitEditView) {
        this.toolbar = toolbar;
        this.orgUnitEditView = orgUnitEditView;
        buildUI();
        addButtonListeners();
    }

    public Window getSubWindow() {
        return subwindow;
    }

    protected String getWindowCaption() {
        return getSubmitBtnText() + " "
            + ViewConstants.ORGANIZATIONAL_UNIT_LABEL;
    }

    protected abstract String getSubmitBtnText();

    private void buildUI() {
        modalWindow().commentTextField().footer();
    }

    private AbstractStatusDialog modalWindow() {
        subwindow.setModal(true);
        subwindow.setWidth(ViewConstants.MODAL_WINDOW_WIDTH);
        subwindow.setHeight(ViewConstants.MODAL_WINDOW_HEIGHT);

        mainLayout = (VerticalLayout) subwindow.getContent();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        return this;
    }

    private AbstractStatusDialog commentTextField() {
        commentField.setWidth(ViewConstants.FIELD_WIDTH);
        commentField.setRows(3);
        subwindow.addComponent(commentField);
        return this;
    }

    private AbstractStatusDialog footer() {
        footer.addComponent(submitBtn);
        footer.addComponent(cancelBtn);
        mainLayout.addComponent(footer);
        return this;
    }

    private void addButtonListeners() {
        addSubmitButtonListener();
        addCancelButtonListener();
    }

    protected void addSubmitButtonListener() {
        submitBtn.addListener(new SubmitButtonListener());
    }

    protected abstract void doAction(String enteredComment)
        throws EscidocException, InternalClientException, TransportException;

    private void closeWindow() {
        (subwindow.getParent()).removeWindow(subwindow);
    }

    private void addCancelButtonListener() {
        cancelBtn.addListener(new CancelButtonListener());
    }

    private final class CancelButtonListener implements Button.ClickListener {
        private static final long serialVersionUID = 1839906716209667918L;

        @Override
        public void buttonClick(final ClickEvent event) {
            closeWindow();
        }
    }

    private final class SubmitButtonListener implements Button.ClickListener {
        private static final long serialVersionUID = 3694320420679990411L;

        @Override
        public void buttonClick(final ClickEvent event) {
            final String enteredComment = (String) commentField.getValue();
            assert enteredComment != null : "comment can not be null.";
            try {
                doAction(enteredComment);
                closeWindow();
            }
            // FIXME: show error message to user.
            catch (final EscidocException e) {
                LOG.error("An unexpected error occured! See LOG for details.",
                    e);
            }
            catch (final InternalClientException e) {
                LOG.error("An unexpected error occured! See LOG for details.",
                    e);
            }
            catch (final TransportException e) {
                LOG.error("An unexpected error occured! See LOG for details.",
                    e);
            }
        }
    }
}