/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
package de.escidoc.admintool.domain;

import javax.xml.parsers.ParserConfigurationException;

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
