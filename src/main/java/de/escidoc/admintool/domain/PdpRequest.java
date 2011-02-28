package de.escidoc.admintool.domain;

public interface PdpRequest {

    boolean isPermitted(String actionId);

    boolean isPermitted(String actionId, String string);

    boolean isDenied(String actionId, String selectedItemId);

    boolean isDenied(String actionId);

}