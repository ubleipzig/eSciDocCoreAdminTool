package de.escidoc.admintool.domain;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.escidoc.admintool.domain.Builder;
import de.escidoc.core.resources.common.MetadataRecord;

public class PubMan {

    public static class BuilderImpl implements Builder<MetadataRecord> {
        private final MetadataRecord metadata;

        private Document doc;

        private Element mpdlMdRecord;

        public BuilderImpl(final MetadataRecord metadata) {
            this.metadata = metadata;
        }

        public BuilderImpl alternative(final String alternative) {
            final Element identifierElmt =
                doc.createElementNS("http://purl.org/dc/terms/", "alternative");
            identifierElmt.setPrefix("dcterms");
            identifierElmt.setTextContent(alternative);
            mpdlMdRecord.appendChild(identifierElmt);
            return this;
        }

        @Override
        public MetadataRecord build() throws ParserConfigurationException {
            return metadata;
        }
    }
}
