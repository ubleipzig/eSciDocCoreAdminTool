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
/**
 * 
 */
package de.escidoc.admintool.view.util.interfaces;

import java.util.Date;

import com.vaadin.event.Action;

/**
 * @author ASP
 * 
 */
public interface IMenuItem {
    /**
     * @return The unique ObjectId.
     */
    public String getObjectId();

    /**
     * @param objId
     *            The unique ObjectId.
     */
    public void setObjectId(String objId);

    /**
     * @return The name of the item.
     */
    public String getName();

    /**
     * @return Who created the item.
     */
    public String getCreatedBy();

    /**
     * @param createdBy
     *            Sets who created the item.
     */
    public void setCreatedBy(String createdBy);

    /**
     * 
     * @return When was the item created.
     */
    public Date getCreatedOn();

    /**
     * 
     * @param createdOn
     *            Sets when the item was created.
     */
    public void setCreatedOn(Date createdOn);

    /**
     * 
     * @return Who modified the item the last time.
     */
    public String getModifiedBy();

    /**
     * 
     * @param modifiedBy
     *            Sets who modified the item the last time.
     */
    public void setModifiedBy(String modifiedBy);

    /**
     * 
     * @return When was the item the last time modified.
     */
    public Date getModifiedOn();

    /**
     * 
     * @param modifiedOn
     *            Sets when the item was modified the last time.
     */
    public void setModifiedOn(Date modifiedOn);

    /**
     * @return an array of action that should be possible in the context menu.
     */
    public Action[] getActions();

    /**
     * 
     * @return an id for the used content model.
     */
    public String getContentModel();

    /**
     * 
     * @param id
     *            an id for the used content model.
     */
    public void setContentModel(String id);

    /**
     * 
     * @return an id for the used context.
     */
    public String getContext();

    /**
     * 
     * @param id
     *            an id for the used context.
     */
    public void setContext(String id);

    /**
     * A unique identifier for determining the EscidocReference id.
     * 
     * @return a String matching a key in the constants definition.
     */
    public String getReferenceType();

    /**
     * 
     * @return the date when the last modification occurred.
     */
    public Date getLastModifiactionDate();

    /**
     * 
     * @param date
     *            the date when the last modification occurred.
     */
    public void setLastModifiactionDate(Date date);

}
