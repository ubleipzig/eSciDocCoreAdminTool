/**
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
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
     *            true if more children under the child are allowed, false otherwise.
     * @return the appended child.
     */
    public static Object addChildren(
        final Tree tree, final Object parent, final Object child, final boolean childrenAllowed) {
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
    public static Object addChildren(final Tree tree, final Object child, final boolean childrenAllowed) {
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
    public static Object addRoot(final Tree tree, final Object child, final boolean childrenAllowed) {
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
        final Tree tree, final Object parent, final Object child, final boolean childrenAllowed) {
        tree.addItem(child);
        tree.setParent(child, parent);
        tree.setChildrenAllowed(child, childrenAllowed);
        return child;
    }

}