package de.uni_leipzig.ubl.admintool.view.group.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.usergroup.Selector;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;
import de.uni_leipzig.ubl.admintool.view.group.GroupEditForm;

public class RemoveSelectorButtonListener implements ClickListener {

	private static final long serialVersionUID = -6241426568514588169L;
	
	private static final Logger LOG = LoggerFactory.getLogger(RemoveSelectorButtonListener.class);
	
	private final GroupEditForm groupEditForm;
	
	private final Table selectorsTable;
	
	private AdminToolApplication app;
	
	private GroupService groupService;
	
	
	public RemoveSelectorButtonListener(final GroupEditForm groupEditForm, final Table selectorsTable) {
		this.groupEditForm = groupEditForm;
		this.selectorsTable = selectorsTable;
		
	}

	
	@Override
	public void buttonClick(final ClickEvent event) {
		this.app = groupEditForm.getApp();
		this.groupService = groupEditForm.getGroupService();
		final List<Selector> removableSelectors = prepareRemoveSelectors();
		if (!removableSelectors.isEmpty()) {
			UserGroup updatedUserGroup = removeSelectors(removableSelectors);
			if (updatedUserGroup != null) {
				updateView(updatedUserGroup);
				LOG.info("Selectors removed from User Group »{}«: {}", updatedUserGroup.getProperties().getName(), removableSelectors);
				showMessage();
			}
		}
	}


	@SuppressWarnings("unchecked")
	private List<Selector> prepareRemoveSelectors() {
		List<Selector> removableSelectors = new ArrayList<Selector>();
		final Object selectedSelectors = selectorsTable.getValue();
		
		if (selectedSelectors != null) {
			if (selectedSelectors instanceof Set<?> && !((Set<?>)selectedSelectors).isEmpty()) {
				for (Object selector : (Set<Object>) selectedSelectors) {
					removableSelectors.add((Selector)selector);
				}
			}
			else if (selectedSelectors instanceof Selector) {
				removableSelectors.add((Selector) selectedSelectors);
			}
		}
		return removableSelectors;
	}


	private UserGroup removeSelectors(final List<Selector> removableSelectors) {
		try {
			UserGroup updatedUserGroup = groupService.removeSelectors(groupService.getGroupById(groupEditForm.getGroupId()), removableSelectors);
			return updatedUserGroup;
		} 
		catch (EscidocException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(app.getMainWindow(), e);
		} 
		catch (InternalClientException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(app.getMainWindow(), e);
		} 
		catch (TransportException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(app.getMainWindow(), e);
		} 
		catch (EscidocClientException e) {
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
			ModalDialog.show(app.getMainWindow(), e);
		}
		return null;
	}


	private void updateView(final UserGroup updatedUserGroup) {
		try {
			// group map must be updated, because app.showGroup() gets cached group from map
			groupService.findAll();
		} catch (EscidocClientException e) {
			ModalDialog.show(app.getMainWindow(), e);
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
		}
		app.getGroupView().getGroupList().updateGroup(updatedUserGroup);
		app.showGroup(updatedUserGroup);
	}


	private void showMessage() {
		app.getMainWindow().showNotification(
	            new Notification("Info", "User Group Selectors are updated", Notification.TYPE_TRAY_NOTIFICATION));
	}
}
