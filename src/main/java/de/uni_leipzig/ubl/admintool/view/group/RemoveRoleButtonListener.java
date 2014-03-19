package de.uni_leipzig.ubl.admintool.view.group;

import java.util.Set;

import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;

import de.escidoc.admintool.app.Command;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.role.RevokeGrantWindow;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.uni_leipzig.ubl.admintool.view.group.GroupEditForm;
import de.uni_leipzig.ubl.admintool.view.role.RevokeGrantCommand;

public class RemoveRoleButtonListener implements ClickListener {

	private static final long serialVersionUID = -2125733049252623555L;
	
	private final GroupEditForm groupEditForm;
	
	/**
	 * @param groupEditForm
	 */
	public RemoveRoleButtonListener(GroupEditForm groupEditForm) {
		this.groupEditForm = groupEditForm;
	}

	@Override
	public void buttonClick(ClickEvent event) {
		final Object selectedGrants = this.groupEditForm.roles.getValue();
		
		if (selectedGrants instanceof Set<?>) {
			for (final Object grant : ((Set<?>) selectedGrants)) {
				if ( grant instanceof Grant) {
					final Window commentWindow = createModalWindow((Grant) grant).getModalWindow();
					commentWindow.setCaption("Revoke Grant " + ((Grant) grant).getXLinkTitle());
					this.groupEditForm.app.getMainWindow().addWindow(commentWindow);
				}
			}
		}
	}
	
	private RevokeGrantWindow createModalWindow(final Grant grant) {
		return new RevokeGrantWindow(createRevokeGrantCommand(grant), grant, this.groupEditForm.grantContainer);
	}

	private Command createRevokeGrantCommand(final Grant grant) {
		return new RevokeGrantCommand(this.groupEditForm.groupService, this.groupEditForm.groupObjectId, grant);
	}

}
