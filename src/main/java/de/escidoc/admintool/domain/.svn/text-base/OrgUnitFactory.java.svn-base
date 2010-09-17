package de.escidoc.admintool.domain;

import java.io.IOException;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;
import de.escidoc.core.resources.oum.Predecessor;
import de.escidoc.core.resources.oum.PredecessorForm;
import de.escidoc.core.resources.oum.Predecessors;
import de.escidoc.core.resources.oum.Properties;

public class OrgUnitFactory {

    private static final Logger log =
        LoggerFactory.getLogger(OrgUnitFactory.class);

    private static final String ESCIDOC_METADATA_TERMS_NS =
        "http://purl.org/escidoc/metadata/terms/0.1/";

    private OrganizationalUnit ou;

    public OrgUnitFactory update(
        final OrganizationalUnit ou, final String title,
        final String description) throws ParserConfigurationException,
        SAXException, IOException {
        this.ou = ou;

        // add mdRecord to set
        final MetadataRecords mdRecords = new MetadataRecords();
        mdRecords.add(eSciDocMdRecord(title, description));

        // add metadata-records to OU
        this.ou.setMetadataRecords(mdRecords);

        return this;
    }

    public OrgUnitFactory create(final String title, final String description)
        throws ParserConfigurationException, SAXException, IOException {
        ou = new OrganizationalUnit();

        ou.setProperties(new Properties());

        // add mdRecord to set
        final MetadataRecords mdRecords = new MetadataRecords();
        mdRecords.add(eSciDocMdRecord(title, description));

        // add metadata-records to OU
        ou.setMetadataRecords(mdRecords);

        return this;
    }

    public OrganizationalUnit build() {
        return ou;
    }

    private Document doc;

    private Element mpdlMdRecordContent;

    private MetadataRecord eSciDocMdRecord(
        final String title, final String description)
        throws ParserConfigurationException, SAXException, IOException {

        final MetadataRecord mdRecord = new MetadataRecord();
        mdRecord.setName("escidoc");
        buildNewDocument();
        mpdlMdRecordContent =
            doc
                .createElementNS(
                    "http://purl.org/escidoc/metadata/profiles/0.1/organizationalunit",
                    "organizational-unit");
        mpdlMdRecordContent.setPrefix("mdou");

        mdRecord.setContent(mpdlMdRecordContent);

        title(title);
        description(description);

        return mdRecord;
    }

    private void buildNewDocument() throws ParserConfigurationException {
        final DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setCoalescing(true);
        factory.setValidating(true);
        final DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
    }

    // TODO refactor this: a lot of duplication
    private Node title(final String title) {
        final Element titleElmt =
            doc.createElementNS("http://purl.org/dc/elements/1.1/", "title");
        titleElmt.setPrefix("dc");
        titleElmt.setTextContent(title);
        mpdlMdRecordContent.appendChild(titleElmt);
        return mpdlMdRecordContent;
    }

    private Node description(final String description) {
        final Element descriptionElmt =
            doc.createElementNS("http://purl.org/dc/elements/1.1/",
                "description");
        descriptionElmt.setPrefix("dc");
        descriptionElmt.setTextContent(description);
        mpdlMdRecordContent.appendChild(descriptionElmt);
        return mpdlMdRecordContent;
    }

    public OrgUnitFactory identifier(final String identifier) {
        if (isNotSet(identifier)) {
            return this;
        }

        assertNotEmpty(identifier);

        final Element identifierElmt =
            doc.createElementNS("http://purl.org/dc/elements/1.1/",
                "identifier");
        identifierElmt.setPrefix("dc");
        identifierElmt.setTextContent(identifier);
        mpdlMdRecordContent.appendChild(identifierElmt);
        return this;
    }

    private boolean isNotSet(final String identifier) {
        return identifier == null || identifier.isEmpty();
    }

    public OrgUnitFactory alternative(final String alternative) {
        final Element identifierElmt =
            doc.createElementNS("http://purl.org/dc/terms/", "alternative");
        identifierElmt.setPrefix("dcterms");
        identifierElmt.setTextContent(alternative);
        mpdlMdRecordContent.appendChild(identifierElmt);
        return this;
    }

    private void assertNotEmpty(final String value) {
        assert value != null : "Null reference ";
        assert !value.isEmpty() : "Empty string";
    }

    public OrgUnitFactory country(final String country) {
        final Element element =
            doc.createElementNS(ESCIDOC_METADATA_TERMS_NS, "country");
        element.setPrefix("eterms");
        element.setTextContent(country);
        mpdlMdRecordContent.appendChild(element);
        return this;
    }

    public OrgUnitFactory city(final String city) {
        final Element element =
            doc.createElementNS(ESCIDOC_METADATA_TERMS_NS, "city");
        element.setPrefix("eterms");
        element.setTextContent(city);
        mpdlMdRecordContent.appendChild(element);
        return this;
    }

    public OrgUnitFactory orgType(final String orgType) {
        final Element element =
            doc.createElementNS(ESCIDOC_METADATA_TERMS_NS, "organization-type");
        element.setPrefix("eterms");
        element.setTextContent(orgType);
        mpdlMdRecordContent.appendChild(element);
        return this;
    }

    public OrgUnitFactory coordinates(final String coordinates) {
        final Element element =
            doc
                .createElementNS("http://www.opengis.net/kml/2.2",
                    "coordinates");
        element.setPrefix("kml");
        element.setTextContent(coordinates);
        mpdlMdRecordContent.appendChild(element);
        return this;
    }

    public OrgUnitFactory parents(final Set<String> parentObjectIds) {

        if (parentObjectIds == null || parentObjectIds.size() == 0) {
            return this;
        }

        final Parents parents = new Parents();
        for (final String parentObjectId : parentObjectIds) {
            parents.addParentRef(new Parent(parentObjectId));
        }

        ou.setParents(parents);
        return this;
    }

    public OrgUnitFactory predecessors(
        final Set<String> predecessorsObjectIds,
        final PredecessorForm predecessorType) {

        if (predecessorsObjectIds == null || predecessorsObjectIds.size() == 0) {
            log.info("empty predecessor.");
            return this;
        }

        final Predecessors predecessor = new Predecessors();
        for (final String predecessorId : predecessorsObjectIds) {
            predecessor.addPredecessorRef(new Predecessor(predecessorId,
                predecessorType));
        }

        ou.setPredecessors(predecessor);
        return this;
    }

}