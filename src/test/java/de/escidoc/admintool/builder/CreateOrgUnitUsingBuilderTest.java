package de.escidoc.admintool.builder;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.escidoc.admintool.Constants;
import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.domain.MetadataExtractor;
import de.escidoc.admintool.domain.OrgUnit;
import de.escidoc.admintool.domain.RawXml;
import de.escidoc.admintool.domain.RawXmlImpl;
import de.escidoc.admintool.domain.RawXmlMetadata;
import de.escidoc.admintool.domain.RawXmlMetadataImpl;
import de.escidoc.admintool.service.OrgUnitServiceLab;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.Authentication;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class CreateOrgUnitUsingBuilderTest {

    private static final String IDENTIFIER = "identifier";

    private static final String ALTERNATIVE = "alternative";

    private static final String ESCIDOC_METADATA_NAME = "escidoc";

    private Authentication authentication;

    private ResourceService service;

    @Before
    public void setUp() throws Exception {
        authentication =
            new Authentication(Constants.DEFAULT_SERVICE_URL,
                Constants.SYSTEM_ADMIN_USER, "eSciDoc");
        service = new OrgUnitServiceLab(authentication);
        service.login();
    }

    @After
    public void cleanUp() throws Exception {
        authentication.logout();
    }

    @Test
    public void shouldCreateSimpleOrgUnit() throws Exception {
        final OrganizationalUnit build =
            new OrgUnit.BuilderImpl(Constants.NAME, Constants.DESCRIPTION)
                .build();
        final OrganizationalUnit created =
            (OrganizationalUnit) service.create(build);
        assertThat(created.getProperties().getName(), equalTo(Constants.NAME));
    }

    @Test
    public void shouldCreateOrgUnitWithPubManMetadata() throws Exception {
        // given
        final OrganizationalUnit build =
            new OrgUnit.BuilderImpl(Constants.NAME, Constants.DESCRIPTION)
                .alternative(ALTERNATIVE).identifier(IDENTIFIER)
                .country("germany").city("karlsruhe").coordinates("xyz")
                .type("institute").build();

        // when
        final OrganizationalUnit created =
            (OrganizationalUnit) service.create(build);
        final MetadataExtractor metadataExtractor =
            new MetadataExtractor(created);

        final String alternative =
            metadataExtractor.get(AppConstants.DCTERMS_ALTERNATIVE);
        final String identifier =
            metadataExtractor.get(AppConstants.DC_IDENTIFIER);
        final String country =
            metadataExtractor.get(AppConstants.ETERMS_COUNTRY);
        final String city = metadataExtractor.get(AppConstants.ETERMS_CITY);
        final String coordinates =
            metadataExtractor.get(AppConstants.KML_COORDINATES);
        final String type =
            metadataExtractor.get(AppConstants.ETERMS_ORGANIZATION_TYPE);
        // etermns

        // assert
        System.out.println("created: " + created.getObjid());
        assertThat(created.getMetadataRecords().get(ESCIDOC_METADATA_NAME),
            is(not(nullValue())));
        assertThat(alternative, equalToIgnoringCase(ALTERNATIVE));
        assertThat(identifier, equalToIgnoringCase(IDENTIFIER));
        assertThat(country, equalToIgnoringCase("germany"));
        assertThat(city, equalToIgnoringCase("karlsruhe"));
        assertThat(coordinates, equalToIgnoringCase("xyz"));
        assertThat(type, equalToIgnoringCase("institute"));
    }

    @Test
    public void shouldCreateOrgUnitWithRawXmlAsMetadata() throws Exception {
        final RawXml xml = new RawXmlImpl("<xml></xml>");
        final String metadataName = "foo";
        final RawXmlMetadata rawXmlMetadata =
            new RawXmlMetadataImpl(metadataName, xml);

        final OrganizationalUnit build =
            new OrgUnit.BuilderImpl(Constants.NAME, Constants.DESCRIPTION)
                .metadata(rawXmlMetadata).build();

        // when
        final OrganizationalUnit created =
            (OrganizationalUnit) service.create(build);

        // assert
        assertThat(created.getMetadataRecords().get(ESCIDOC_METADATA_NAME),
            is(not(nullValue())));
        assertThat(created.getMetadataRecords().get(metadataName),
            is(not(nullValue())));
    }

    @Test
    public void shouldCreateOrgUnitWithParent() throws Exception {
        final Set<String> parents = new HashSet<String>() {
            private static final long serialVersionUID = 5288522526434156128L;

            {
                add("escidoc:155003");
            }
        };

        final OrganizationalUnit build =
            new OrgUnit.BuilderImpl(Constants.NAME, Constants.DESCRIPTION)
                .parents(parents).build();

        // when
        final OrganizationalUnit created =
            (OrganizationalUnit) service.create(build);

        // assert
        assertThat(created.getMetadataRecords().get(ESCIDOC_METADATA_NAME),
            is(not(nullValue())));
    }
}