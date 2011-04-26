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

    @Override
    public ResourceService getContentModelService() {
        return (ResourceService) services.get(ContentModelService.class);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ServiceContaiterImpl [");
        if (services != null) {
            builder.append("services=").append(services).append(", ");
        }
        if (getAdminService() != null) {
            builder.append("getAdminService()=").append(getAdminService()).append(", ");
        }
        if (getContainerService() != null) {
            builder.append("getContainerService()=").append(getContainerService()).append(", ");
        }
        if (getItemService() != null) {
            builder.append("getItemService()=").append(getItemService()).append(", ");
        }
        if (getContextService() != null) {
            builder.append("getContextService()=").append(getContextService()).append(", ");
        }
        if (getOrgUnitService() != null) {
            builder.append("getOrgUnitService()=").append(getOrgUnitService()).append(", ");
        }
        if (getContentModelService() != null) {
            builder.append("getContentModelService()=").append(getContentModelService());
        }
        builder.append("]");
        return builder.toString();
    }

}