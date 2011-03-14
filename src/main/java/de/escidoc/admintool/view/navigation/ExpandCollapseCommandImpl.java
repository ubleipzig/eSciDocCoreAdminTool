package de.escidoc.admintool.view.navigation;

import com.google.common.base.Preconditions;

public class ExpandCollapseCommandImpl implements ExpandCollapseCommand {
    private final NavigationTree navigationTree;

    public ExpandCollapseCommandImpl(final NavigationTree navigationTree) {
        Preconditions.checkNotNull(navigationTree,
            "navigationTree is null: %s", navigationTree);
        this.navigationTree = navigationTree;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.escidoc.admintool.view.navigation.ExpandCollapseCommand#execute(java
     * .lang.Object)
     */
    @Override
    public void execute(final Object itemId) {
        if (navigationTree.isExpanded(itemId)) {
            doCollapse(itemId);
        }
        else {
            doExpand(itemId);
        }
    }

    private void doExpand(final Object itemId) {
        navigationTree.expandItem(itemId);
    }

    private void doCollapse(final Object itemId) {
        navigationTree.collapseItem(itemId);
    }

}
