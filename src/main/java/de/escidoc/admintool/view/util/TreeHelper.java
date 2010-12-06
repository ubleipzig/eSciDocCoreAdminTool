package de.escidoc.admintool.view.util;

import com.vaadin.ui.Tree;

/**
 * @author ASP
 * 
 */
public final class TreeHelper {

    private TreeHelper() {
        // Utility classes should not have a public or default constructor.
    }

    /**
     * Appends an child object to the parent.
     * 
     * @param tree
     *            the tree, where the child is appended.
     * @param parent
     *            the parent.
     * @param child
     *            the child.
     * @param childrenAllowed
     *            true if more children under the child are allowed, false
     *            otherwise.
     * @return the appended child.
     */
    public static Object addChildren(
        final Tree tree, final Object parent, final Object child,
        final boolean childrenAllowed) {
        tree.addItem(child);
        tree.setParent(child, parent);
        tree.setChildrenAllowed(child, childrenAllowed);
        tree.expandItem(parent);
        return child;
    }

    /**
     * Adds a child node
     * 
     * @param tree
     *            the tree to add
     * @param child
     *            the child node
     * @param childrenAllowed
     *            are children under this node allowed.
     * @return the root node.
     * @deprecated does the same as addRoot. Somebody copied it.
     */
    @Deprecated
    public static Object addChildren(
        final Tree tree, final Object child, final boolean childrenAllowed) {
        tree.addItem(child);
        tree.setChildrenAllowed(child, childrenAllowed);
        return child;
    }

    /**
     * Adds a root node to the tree.
     * 
     * @param tree
     *            the tree to add
     * @param child
     *            the child node
     * @param childrenAllowed
     *            are children under this node allowed.
     * @return the root node.
     */
    public static Object addRoot(
        final Tree tree, final Object child, final boolean childrenAllowed) {
        tree.addItem(child);
        tree.setChildrenAllowed(child, childrenAllowed);
        return child;
    }

    /**
     * Add a child without expanding it.
     * 
     * @param tree
     *            the tree to add
     * @param parent
     *            the parent node.
     * @param child
     *            the child node
     * @param childrenAllowed
     *            are children under this node allowed.
     * @return the child.
     */
    public static Object addChildrenNotExpand(
        final Tree tree, final Object parent, final Object child,
        final boolean childrenAllowed) {
        tree.addItem(child);
        tree.setParent(child, parent);
        tree.setChildrenAllowed(child, childrenAllowed);
        return child;
    }

}