package de.escidoc.admintool.service;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.escidoc.admintool.Constants;
import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.UserAccountHandlerClient;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.interfaces.UserAccountHandlerClientInterface;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public class UpdateUserPasswordTest {

    private static final String SYSADMIN_PASSWORD = "eSciDoc";

    private UserService service;

    @Before
    public void setUp() throws Exception {
        createUserService();
    }

    private void createUserService() throws Exception {
        service =
            new UserService(Constants.DEFAULT_SERVICE_URL, new Authentication(
                Constants.DEFAULT_SERVICE_URL, Constants.SYSADMIN_LOGIN_NAME,
                SYSADMIN_PASSWORD).getHandle());
    }

    @Test(expected = AuthenticationException.class)
    public void shouldThrownAuthentificationIfUserEntersTheWrongPassword()
        throws Exception {

        // Given:
        final Authentication wrongAuth =
            new Authentication(Constants.DEFAULT_SERVICE_URL,
                Constants.SYSADMIN_LOGIN_NAME, Constants.WRONG_PASSWORD);

        // When:
        final UserAccountHandlerClientInterface client =
            new UserAccountHandlerClient(wrongAuth.getServiceAddress());
        client.setHandle(wrongAuth.getHandle());
    }

    @Test
    public void shouldUpdateUserPassword() throws Exception {

        // Given X0 && ...Xn
        final UserAccount user = service.getUserById("escidoc:109001");
        final String newPassword = "newpassword";

        // When
        service.updatePassword(user, newPassword);

        // Then ensure that
        final Authentication authentication =
            new Authentication(Constants.DEFAULT_SERVICE_URL, user
                .getProperties().getLoginName(), newPassword);
        final String handle = authentication.getHandle();
        assertTrue("Handle is empty", !handle.isEmpty());
    }
}