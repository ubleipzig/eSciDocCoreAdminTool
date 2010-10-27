package de.escidoc.admintool.view.admintask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ResourceService;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.admintask.FilterResourceView.ShowFilterResultCommand;
import de.escidoc.core.client.exceptions.EscidocClientException;

public class FilterResourceListener implements ClickListener {

    private static final Logger log = LoggerFactory
        .getLogger(FilterResourceListener.class);

    private static final long serialVersionUID = 2859820395161737640L;

    private ShowFilterResultCommand command;

    private final Window mainWindow;

    private final ResourceService itemService;

    public FilterResourceListener(final Window mainWindow,
        final ResourceService itemService) {
        Preconditions.checkNotNull(mainWindow, "mainWindow can not be null.");
        Preconditions.checkNotNull(itemService, "itemService can not be null.");

        this.mainWindow = mainWindow;
        this.itemService = itemService;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        try {
            command.execute(itemService.findAll());
        }
        catch (final EscidocClientException e) {
            log.warn("EscidocClientException, show error to user", e);
            ErrorMessage.show(mainWindow, e);
        }
    }

    public void setCommand(final ShowFilterResultCommand command) {
        this.command = command;
    }
}