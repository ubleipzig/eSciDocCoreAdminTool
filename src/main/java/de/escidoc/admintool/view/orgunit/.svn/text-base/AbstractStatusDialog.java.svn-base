package de.escidoc.admintool.view.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public abstract class AbstractStatusDialog extends CustomComponent {
    private static final long serialVersionUID = 7257217308593700424L;

    private static final Logger log =
        LoggerFactory.getLogger(AbstractStatusDialog.class);

    private static final String COMMENT = "Comment:";

    private static final String ORGANIZATIONAL_UNIT_LABEL =
        "Organizational Unit";

    protected final OrgUnitEditView orgUnitEditView;

    private VerticalLayout mainLayout = new VerticalLayout();

    protected Window subwindow = new Window(getWindowCaption());

    private final TextField commentField = new TextField(COMMENT);

    private final HorizontalLayout footer = new HorizontalLayout();

    private final Button submitBtn = new Button(getSubmitBtnText());

    private final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL);

    public AbstractStatusDialog(final OrgUnitEditView orgUnitEditView) {
        this.orgUnitEditView = orgUnitEditView;
        buildUI();
        addButtonListeners();
    }

    public Window getSubWindow() {
        return subwindow;
    }

    protected String getWindowCaption() {
        return getSubmitBtnText() + " " + ORGANIZATIONAL_UNIT_LABEL;
    }

    protected abstract String getSubmitBtnText();

    private void buildUI() {
        modalWindow().commentTextField().footer();
    }

    private AbstractStatusDialog modalWindow() {
        subwindow.setModal(true);
        subwindow.setWidth("450px");
        subwindow.setHeight("300px");

        mainLayout = (VerticalLayout) subwindow.getContent();
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        return this;
    }

    private AbstractStatusDialog commentTextField() {
        commentField.setWidth("400px");
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
        submitBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(final ClickEvent event) {
                final String enteredComment = (String) commentField.getValue();
                assert enteredComment != null : "comment can not be null.";
                try {
                    doAction(enteredComment);
                    closeWindow();
                }
                catch (final EscidocException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    e.printStackTrace();
                }
                catch (final InternalClientException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    e.printStackTrace();
                }
                catch (final TransportException e) {
                    log.error(
                        "An unexpected error occured! See log for details.", e);
                    e.printStackTrace();
                }
            }
        });
    }

    protected abstract void doAction(String enteredComment)
        throws EscidocException, InternalClientException, TransportException;

    private void closeWindow() {
        ((Window) subwindow.getParent()).removeWindow(subwindow);
    }

    private void addCancelButtonListener() {
        cancelBtn.addListener(new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                closeWindow();
            }

        });
    }
}