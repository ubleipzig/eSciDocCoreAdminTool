package de.escidoc.admintool.domain;

public interface PdpRequest {

    boolean isAllowed(String actionId);

    boolean isAllowed(String actionId, String string);

}