package de.escidoc.admintool.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import de.escidoc.admintool.Constants;
import de.escidoc.core.client.Authentication;

public class RetrieveUserAccountNameAttributeOrgUnit {

    private static final String SYSADMIN_OBJECT_ID = "escidoc:exuser1";

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

    /**
     * @throws Exception
     */
    @SuppressWarnings("boxing")
    @Test
    public void shouldReturnAllOrgUnitIfExists() throws Exception {
        assertThat(service.retrieveOrgUnitsFor(SYSADMIN_OBJECT_ID).isEmpty(),
            is(false));
    }
}
