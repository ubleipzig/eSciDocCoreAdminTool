package de.escidoc.admintool.service;

import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.core.client.Authentication;

public class AskPdpForCreatingContentModelActionTest {

    private static final String LOCALHOST_8080 = "http://localhost:8080";

    private final PdpService service = new PdpServiceImpl(LOCALHOST_8080);

    @Test
    public void shouldReturnTrueForIsDenied() throws Exception {
        // When:
        final boolean isDenied =
            service
                .isAction(ActionIdConstants.CREATE_CONTENT_MODEL)
                .forResource(AppConstants.EMPTY_STRING)
                .forUser(AppConstants.EMPTY_STRING).denied();
        // AssertThat:
        assertTrue(isDenied);
    }

    @Ignore("need to adapted for CI: no running eSciDoc")
    @Test
    public void shouldReturnTrueIfSysAdminAskIt() throws Exception {
        // Given:
        final Authentication authentication =
            new Authentication(new URL(LOCALHOST_8080), "sysadmin", "escidoc");
        final String handle = authentication.getHandle();
        service.loginWith(handle);

        // When:
        final boolean isDenied =
            service
                .isAction(ActionIdConstants.CREATE_CONTENT_MODEL)
                .forResource(AppConstants.EMPTY_STRING)
                .forUser(AppConstants.EMPTY_STRING).denied();

        // AssertThat:
        assertTrue(isDenied);
    }
}
