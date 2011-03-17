package de.escidoc.admintool.service;

public interface ServiceContainer {

    void add(EscidocService containerService);

    AdminService getAdminService();

    ResourceService getContainerService();

    ResourceService getItemService();

    ResourceService getContextService();

    ResourceService getOrgUnitService();

}
