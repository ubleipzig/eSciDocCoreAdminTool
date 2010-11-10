package de.escidoc.admintool.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceContaiterImpl implements ServiceContainer {

    private final Map<Class<? extends EscidocService>, EscidocService> services =
        new HashMap<Class<? extends EscidocService>, EscidocService>();

    @Override
    public void add(final EscidocService service) {
        services.put(service.getClass(), service);
    }

    @Override
    public AdminService getAdminService() {
        return (AdminService) services.get(AdminServiceImpl.class);
    }

    @Override
    public ResourceService getContainerService() {
        return (ResourceService) services.get(ContainerService.class);
    }

    @Override
    public ResourceService getItemService() {
        return (ResourceService) services.get(ItemService.class);
    }

    @Override
    public ResourceService getContextService() {
        return (ResourceService) services.get(ContextServiceLab.class);
    }

    @Override
    public ResourceService getOrgUnitService() {
        return (ResourceService) services.get(OrgUnitServiceLab.class);
    }

}
