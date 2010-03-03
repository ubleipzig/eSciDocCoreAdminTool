/*
 * CDDL HEADER START
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License, Version 1.0 only (the "License"). You may not use
 * this file except in compliance with the License.
 * 
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE or
 * http://www.escidoc.de/license. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL HEADER in each file and
 * include the License file at license/ESCIDOC.LICENSE. If applicable, add the
 * following below this CDDL HEADER, with the fields enclosed by brackets "[]"
 * replaced with your own identifying information: Portions Copyright [yyyy]
 * [name of copyright owner]
 * 
 * CDDL HEADER END
 */

/*
 * Copyright 2006-2010 Fachinformationszentrum Karlsruhe Gesellschaft fuer
 * wissenschaftlich-technische Information mbH and Max-Planck- Gesellschaft zur
 * Foerderung der Wissenschaft e.V. All rights reserved. Use is subject to
 * license terms.
 */
/**
 * @author CHH
 */
qx.Class.define("org.escidoc.admintool.model.RootFolder",
{
  extend : qx.core.Object,


  construct : function(title)
  {
    this.base(arguments);

    this.setTitle(title);
    this.setChildren(new qx.data.Array());
  },

  properties :
  {
    /** Title / Name of the item */
    title :
    {
      check : "String",
      event : "changeTitle",
      init: "Folder"
    },


    /** The feed category */
    category :
    {
      check : "String",
      init : "",
      event : "dataModified"
    },


    children :
    {
      check : "qx.data.Array",
      event: "changeChildren"
    },


    /** Array of articles. This is needed for the data binding. */
    articles :
    {
      check : "qx.data.Array",
      event : "changeArticles",
      init: new qx.data.Array()
    },


    /** The loading state of the folder. Needed for data binding. */
    state :
    {
      check : ["new", "loading", "loaded", "error"],
      init : "null",
      event : "stateModified",
      apply: "_applyState"
    }
  }
});