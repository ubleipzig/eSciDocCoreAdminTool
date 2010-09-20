//package de.escidoc.admintool.view.role;
//
//import java.util.Collections;
//import java.util.Set;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.ui.Button.ClickListener;
//
//import de.escidoc.admintool.view.ViewConstants;
//import de.escidoc.core.client.exceptions.EscidocClientException;
//import de.escidoc.core.resources.ResourceRef;
//import de.escidoc.core.resources.aa.role.Role;
//import de.escidoc.core.resources.aa.useraccount.UserAccount;
//import de.escidoc.vaadin.dialog.ErrorDialog;
//
//public class SaveBtnListener implements ClickListener {
//
//    private static final Logger log =
//        LoggerFactory.getLogger(SaveBtnListener.class);
//
//    private final RoleView roleView;
//
//    public SaveBtnListener(final RoleView roleView) {
//        this.roleView = roleView;
//    }
//
//    @Override
//    public void buttonClick(final ClickEvent event) {
//        onSaveClick();
//
//    }
//
//    private void onSaveClick() {
//        if (scopeNeeded()) {
//            roleView.mainWindow
//                .showNotification("Assign Grants to user account is not yet implemented.");
//        }
//        else {
//            if (isValid()) {
//                mainWindow.showNotification("Assign role: "
//                    + getSelectedRole().getProperties().getName()
//                    + " to user: "
//                    + getSelectedUser().getProperties().getName());
//
//                assignRole();
//            }
//        }
//    }
//
//    private boolean isValid() {
//        if (resourceTypeComboBox.isEnabled()) {
//            mainWindow.showNotification("Scopingg...");
//            return userComboBox.isValid() && getSelectedResources().size() > 0;
//        }
//
//        return true && userComboBox.isValid()
//            && getSelectedResources().size() > 0;
//    }
//
//    private Set<ResourceRef> getSelectedResources() {
//        final Object value = resouceResult.getValue();
//        if (value instanceof Set) {
//            final Set<ResourceRef> toBeScopes = (Set<ResourceRef>) value;
//        }
//        return Collections.emptySet();
//    }
//
//    private void assignRole() {
//        try {
//            userService.assign(getSelectedUser().getObjid(), getSelectedRole()
//                .getObjid());
//            // userService.
//        }
//        catch (final EscidocClientException e) {
//            app.getMainWindow().addWindow(
//                new ErrorDialog(app.getMainWindow(),
//                    ViewConstants.ERROR_DIALOG_CAPTION,
//                    "An unexpected error occured! See log for details."));
//            log.error("An unexpected error occured! See log for details.", e);
//        }
//    }
//
//    private UserAccount getSelectedUser() {
//        if (selectedUser == null) {
//            return (UserAccount) userComboBox.getValue();
//        }
//        return selectedUser;
//    }
//
//    private boolean scopeNeeded() {
//        return false;
//    }
//
//    private Role getSelectedRole() {
//        final Object value = roleComboBox.getValue();
//        if (value instanceof Role) {
//            return (Role) value;
//        }
//        return new Role();
//    }
//
// }