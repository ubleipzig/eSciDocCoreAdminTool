package de.escidoc.admintool.view;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Tree;

import de.escidoc.admintool.view.context.OrgUnitTree;
import de.escidoc.core.resources.ResourceRef;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;
import de.escidoc.vaadin.utilities.TreeHelper;

public class OrgUnitTreeFactory {
    private final static Logger log =
        LoggerFactory.getLogger(OrgUnitTree.class);

    private final Tree orgUnitTree;

    private final List<OrganizationalUnit> allOrgUnits;

    private final Map<String, OrganizationalUnit> orgUnitsByObjectId;

    public OrgUnitTreeFactory(final Tree orgUnitTree,
        final List<OrganizationalUnit> allOrgUnits,
        final Map<String, OrganizationalUnit> orgUnitsByObjectId) {
        this.orgUnitTree = orgUnitTree;
        this.allOrgUnits = allOrgUnits;
        this.orgUnitsByObjectId = orgUnitsByObjectId;
    }

    public Tree create() {
        for (final OrganizationalUnit orgUnit : allOrgUnits) {
            final Parents parents = orgUnit.getParents();
            if (parents != null) {
                final List<Parent> parentRef =
                    (List<Parent>) parents.getParentRef();
                if (parentRef != null && parentRef.size() > 0) {
                    final Parent parent = parentRef.get(0);
                    if (parent != null) {
                        if (orgUnitTree.containsId(resourceRef2Display(parent))) {
                            TreeHelper.addChildrenNotExpand(orgUnitTree,
                                resourceRef2Display(parent),
                                resourceRef2Display(orgUnit), true);
                        }
                        else {
                            TreeHelper.addRoot(orgUnitTree,
                                resourceRef2Display(parent), true);

                            TreeHelper.addChildrenNotExpand(orgUnitTree,
                                resourceRef2Display(parent),
                                resourceRef2Display(orgUnit), true);
                        }
                    }
                    else {
                        TreeHelper.addRoot(orgUnitTree,
                            resourceRef2Display(orgUnit), true);
                    }
                }
                else {
                    TreeHelper.addRoot(orgUnitTree,
                        resourceRef2Display(orgUnit), true);
                }
            }
            else {
                TreeHelper.addRoot(orgUnitTree, resourceRef2Display(orgUnit),
                    true);
            }

        }

        return orgUnitTree;
    }

    // Item item = null;
    //
    // int itemId = 0; // Increasing numbering for itemId:s

    // // Create new container
    // HierarchicalContainer hwContainer = new HierarchicalContainer();
    // // Create containerproperty for name
    // hwContainer.addContainerProperty(hw_PROPERTY_NAME, String.class, null);
    // // Create containerproperty for icon
    // hwContainer.addContainerProperty(hw_PROPERTY_ICON, ThemeResource.class,
    // new ThemeResource("../runo/icons/16/document.png"));
    // for (int i = 0; i < hardware.length; i++) {
    // // Add new item
    // item = hwContainer.addItem(itemId);
    // // Add name property for item
    // item.getItemProperty(hw_PROPERTY_NAME).setValue(hardware[i][0]);
    // // Allow children
    // hwContainer.setChildrenAllowed(itemId, true);
    // itemId++;
    // for (int j = 1; j < hardware[i].length; j++) {
    // if (j == 1) {
    // item.getItemProperty(hw_PROPERTY_ICON).setValue(
    // new ThemeResource("../runo/icons/16/folder.png"));
    // }
    // // Add child items
    // item = hwContainer.addItem(itemId);
    // item.getItemProperty(hw_PROPERTY_NAME).setValue(hardware[i][j]);
    // hwContainer.setParent(itemId, itemId - j);
    // hwContainer.setChildrenAllowed(itemId, false);
    //
    // itemId++;
    // }
    // }
    // return hwContainer;
    // }
    // public Tree create() {
    // for (final OrganizationalUnit orgUnit : allOrgUnits) {
    //
    // if (orgUnit.getParents() != null
    // && orgUnit.getParents().getParentRef() != null
    // && orgUnit.getParents().getParentRef().size() > 0) {
    //
    // final Parent parent =
    // ((List<Parent>) orgUnit.getParents().getParentRef()).get(0);
    // if (parent != null) {
    // if (orgUnitTree.containsId(resourceRef2Title(parent))) {
    // TreeHelper.addChildren(orgUnitTree,
    // resourceRef2Title(parent),
    // resourceRef2Title(orgUnit), true);
    // }
    // else {
    // TreeHelper.addChildren(orgUnitTree,
    // resourceRef2Title(parent), true);
    // TreeHelper.addChildren(orgUnitTree,
    // resourceRef2Title(parent),
    // resourceRef2Title(orgUnit), true);
    // }
    // }
    // else {
    // TreeHelper.addChildren(orgUnitTree,
    // resourceRef2Title(orgUnit), true);
    // }
    // }
    // else {
    // TreeHelper.addChildren(orgUnitTree, resourceRef2Title(orgUnit),
    // true);
    // }
    // }
    // return orgUnitTree;
    // }

    // TODO put the converter in appropriate class
    // private static String resourceRef2Title(final Parent parent) {
    // return parent.getObjid();
    // }

    private static String orgUnit2ObjectId(final OrganizationalUnit orgUnit) {
        return orgUnit.getObjid();
    }

    private String resourceRef2Title(final ResourceRef resourceRef) {
        return orgUnitsByObjectId
            .get(resourceRef.getObjid()).getProperties().getName();
    }

    private ResourceRefDisplay resourceRef2Display(final ResourceRef resourceRef) {
        final OrganizationalUnit cachedOrgUnit =
            orgUnitsByObjectId.get(resourceRef.getObjid());
        final ResourceRefDisplay resourceRefDisplay =
            new ResourceRefDisplay(resourceRef.getObjid(), cachedOrgUnit
                .getProperties().getName());

        return resourceRefDisplay;
    }
}