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
            if (isRoot(orgUnit)) {
                createRoot(orgUnit);
            }
            else {
                if (parentExistInTree(orgUnit)) {
                    setParent(orgUnit, getParent(orgUnit));
                }
                else {
                    createRoot(getParent(orgUnit));
                    setParent(orgUnit, getParent(orgUnit));
                }
            }
        }
        return orgUnitTree;
    }

    private boolean isRoot(final OrganizationalUnit orgUnit) {
        return orgUnit.getParents() == null || !hasParent(orgUnit);
    }

    private void createRoot(final ResourceRef orgUnit) {
        if (hasChildren(orgUnit)) {
            TreeHelper.addRoot(orgUnitTree, resourceRef2Display(orgUnit), true);
        }
        else {
            TreeHelper
                .addRoot(orgUnitTree, resourceRef2Display(orgUnit), false);
        }
    }

    private void setParent(final OrganizationalUnit orgUnit, final Parent parent) {
        if (hasChildren(orgUnit)) {
            TreeHelper
                .addChildrenNotExpand(orgUnitTree, resourceRef2Display(parent),
                    resourceRef2Display(orgUnit), true);
        }
        else {
            TreeHelper.addChildrenNotExpand(orgUnitTree,
                resourceRef2Display(parent), resourceRef2Display(orgUnit),
                false);
        }
    }

    private boolean hasParent(final OrganizationalUnit orgUnit) {
        return isNotEmpty(orgUnit) && getParentRef(orgUnit) != null;
    }

    private boolean parentExistInTree(final OrganizationalUnit orgUnit) {
        return orgUnitTree.containsId(resourceRef2Display(getParent(orgUnit)));
    }

    private boolean isNotEmpty(final OrganizationalUnit orgUnit) {
        return getParentRef(orgUnit) != null
            && getParentRef(orgUnit).size() > 0;
    }

    private Parent getParent(final OrganizationalUnit orgUnit) {
        final Parent parent = getParentRef(orgUnit).get(0);
        return parent;
    }

    private List<Parent> getParentRef(final OrganizationalUnit orgUnit) {
        final List<Parent> parentRef =
            (List<Parent>) orgUnit.getParents().getParentRef();
        return parentRef;
    }

    private boolean hasChildren(final ResourceRef resourceRef) {
        final OrganizationalUnit cachedOrgUnit =
            orgUnitsByObjectId.get(resourceRef.getObjid());
        return cachedOrgUnit.getProperties().getHasChildren();
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