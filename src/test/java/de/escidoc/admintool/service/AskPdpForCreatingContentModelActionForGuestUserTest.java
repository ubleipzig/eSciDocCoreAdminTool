package de.escidoc.admintool.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.view.navigation.ActionIdConstants;

public class AskPdpForCreatingContentModelActionForGuestUserTest {
    private final PdpService service = new PdpServiceImpl(
        "http://localhost:8080");

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
}
