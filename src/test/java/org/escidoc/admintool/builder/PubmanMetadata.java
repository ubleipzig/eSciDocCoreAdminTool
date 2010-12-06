package org.escidoc.admintool.builder;

import javax.xml.parsers.ParserConfigurationException;

import de.escidoc.admintool.domain.Builder;
import de.escidoc.core.resources.common.MetadataRecord;

public class PubmanMetadata extends MetadataRecord {

    private final String alternative;

    public PubmanMetadata(final BuilderImpl builderImpl) {
        alternative = builderImpl.alternative;
    }

    public String getAlternative() {
        return alternative;
    }

    public static class BuilderImpl implements Builder<PubmanMetadata> {

        private String alternative;

        public BuilderImpl alternative(final String value) {
            alternative = value;
            return this;
        }

        @Override
        public PubmanMetadata build() throws ParserConfigurationException {
            return new PubmanMetadata(this);
        }
    }
}
