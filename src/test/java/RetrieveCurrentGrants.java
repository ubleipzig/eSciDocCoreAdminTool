import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.TransportProtocol;
import de.escidoc.core.client.UserAccountHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.useraccount.Grants;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.test.client.Constants;
import de.escidoc.core.test.client.EscidocClientTestBase;

public class RetrieveCurrentGrants {

    private static final String CHRISTIAN_OBJECT_ID = "escidoc:93023";

    private UserAccountHandlerClient client;

    @Before
    public void setUp() throws EscidocClientException {
        createRestClientForUserAccount();
    }

    private void createRestClientForUserAccount() throws EscidocClientException {
        final Authentication auth =
            new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                Constants.SYSTEM_ADMIN_USER, Constants.SYSTEM_ADMIN_PASSWORD);
        client = new UserAccountHandlerClient();
        client.setTransport(TransportProtocol.REST);
        client.setServiceAddress(EscidocClientTestBase.DEFAULT_SERVICE_URL);
        client.setHandle(auth.getHandle());
    }

    @Test
    public void shouldReturnNonEmptyTitleForItsScope()
        throws EscidocClientException {

        final UserAccount userAccount = client.retrieve(CHRISTIAN_OBJECT_ID);
        assertNotNull("Object ID should not be null. " + userAccount.getObjid());
        // Given:
        // a user X with a grant Y for the scope resource Z
        // TODO: mock/inject the requirements, for now using existing user
        final Grants christianCurrentGrants =
            client.retrieveCurrentGrants(CHRISTIAN_OBJECT_ID);

        // When:
        // we ask for the title of resource Z

        // Should:
        // return non null and non empty string
        assertNotNull("The Current grants of the user" + CHRISTIAN_OBJECT_ID
            + " should not be null. ", christianCurrentGrants.getGrants());

        assertTrue("The Current grants of the user" + CHRISTIAN_OBJECT_ID
            + " should not empty. ", !christianCurrentGrants
            .getGrants().isEmpty());

        final Collection<Grant> grants = christianCurrentGrants.getGrants();
        for (final Grant grant : grants) {
            System.out.println(grant.getTitle());

            final ResourceRef role = grant.getGrantProperties().getRole();
            assertNotNull(role);

            final ResourceRef assignedOn =
                grant.getGrantProperties().getAssignedOn();
            assertNotNull(assignedOn);

            final String xLinkTitle = assignedOn.getTitle();
            assertNotNull("XLink Title should not be null. " + xLinkTitle);
            System.out.println(xLinkTitle);
        }
    }
}