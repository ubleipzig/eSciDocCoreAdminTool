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
//package de.escidoc.admintool.view.factory;
//
//import de.escidoc.admintool.app.AdminToolApplication;
//import de.escidoc.admintool.service.OrgUnitService;
//import de.escidoc.admintool.view.orgunit.OrgUnitAddView;
//import de.escidoc.admintool.view.orgunit.OrgUnitEditView;
//import de.escidoc.admintool.view.orgunit.OrgUnitListView;
//import de.escidoc.admintool.view.orgunit.OrgUnitView;
//import de.escidoc.core.client.exceptions.EscidocException;
//import de.escidoc.core.client.exceptions.InternalClientException;
//import de.escidoc.core.client.exceptions.TransportException;
//
//public class OrgUnitViewFactory {
//
//    private OrgUnitListView orgUnitList;
//
//    private OrgUnitEditView orgUnitEditForm;
//
//    private OrgUnitView orgUnitView;
//
//    private final OrgUnitAddView orgUnitAddForm;
//
//    private final AdminToolApplication app;
//
//    private final OrgUnitService orgUnitService;
//
//    public OrgUnitViewFactory(final AdminToolApplication app,
//        final OrgUnitService orgUnitService, final OrgUnitAddView orgUnitAddForm) {
//        this.app = app;
//        this.orgUnitService = orgUnitService;
//        this.orgUnitAddForm = orgUnitAddForm;
//    }
//
//    public OrgUnitView getOrgUnitView() throws EscidocException,
//        InternalClientException, TransportException {
//        if (orgUnitView == null) {
//            orgUnitView = create();
//        }
//        return orgUnitView;
//    }
//
//    private OrgUnitView create() throws EscidocException,
//        InternalClientException, TransportException {
//        orgUnitList = new OrgUnitListView(app, orgUnitService);
//        orgUnitEditForm = new OrgUnitEditView(app, orgUnitService);
//        orgUnitEditForm.setOrgUnitList(orgUnitList);
//        return new OrgUnitView(app, orgUnitList, orgUnitEditForm,
//            orgUnitAddForm);
//    }
//
// }