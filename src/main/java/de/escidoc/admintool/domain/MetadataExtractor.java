package de.escidoc.admintool.domain;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Preconditions;

import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class MetadataExtractor {

    private final OrganizationalUnit ou;

    public MetadataExtractor(final OrganizationalUnit ou) {
        Preconditions.checkNotNull(ou, "final OrgUnit must final not be null.");
        this.ou = ou;
    }

    private String get(
        final MetadataRecords metadataRecords, final String nodeName) {
        final MetadataRecord escidocMdRecord = metadataRecords.get("escidoc");

        if (escidocMdRecord == null) {
            return "";
        }

        final Element content = escidocMdRecord.getContent();

        for (int index = 0; index < content.getChildNodes().getLength(); index++) {
            final Node item = content.getChildNodes().item(index);

            if (item.getNodeName().equals(nodeName)) {
                return item.getTextContent();
            }
        }
        return "";
    }

    public String get(final String nodeName) {
        return get(ou.getMetadataRecords(), nodeName);
    }
}