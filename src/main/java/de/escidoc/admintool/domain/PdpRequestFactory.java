package de.escidoc.admintool.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;

import de.escidoc.admintool.app.AppConstants;

public class PdpRequestFactory {

    public static Set<Attribute> createActionAttribute(final String actionId)
        throws URISyntaxException {
        final Set<Attribute> actionAttrs = new HashSet<Attribute>();
        actionAttrs.add(new Attribute(new URI(AppConstants.XACML_ACTION_ID), null, null,
            new StringAttribute(actionId)));
        return actionAttrs;
    }
}