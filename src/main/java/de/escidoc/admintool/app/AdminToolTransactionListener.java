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
//package de.escidoc.admintool.app;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.common.base.Preconditions;
//import com.vaadin.Application;
//import com.vaadin.service.ApplicationContext.TransactionListener;
//import com.vaadin.ui.Window;
//
//import de.escidoc.core.client.exceptions.EscidocClientException;

//final class AdminToolTransactionListener implements TransactionListener {
//
//    private static final Cookie EMPTY_COOKIE = new Cookie("", "");
//
//    private static final Logger LOG = LoggerFactory
//        .getLogger(AdminToolTransactionListener.class);
//
//    private static final String ESCIDOC_COOKIE_NAME = "escidocCookie";
//
//    private static final long serialVersionUID = 7130384700120655157L;
//
//    private final AdminToolApplication app;
//
//    private final Window mainWindow;
//
//    public AdminToolTransactionListener(final AdminToolApplication app,
//        final Window mainWindow) {
//        Preconditions.checkNotNull(app, "app is null: %s", app);
//        Preconditions.checkNotNull(mainWindow, "mainWindow is null: %s",
//            mainWindow);
//        this.app = app;
//        this.mainWindow = mainWindow;
//    }
//
//    @Override
//    public void transactionStart(
//        final Application application, final Object transactionData) {
//        final HttpServletRequest request = (HttpServletRequest) transactionData;
//        final Cookie escidocCookie = findEscidocCookie(request);
//
//        try {
//            setToken(escidocCookie);
//        }
//        catch (final EscidocClientException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    private Cookie findEscidocCookie(final HttpServletRequest request) {
//        if (request != null && request.getCookies() != null) {
//
//            for (final Cookie cookie : request.getCookies()) {
//                if (isEscidocCookie(cookie)) {
//                    return cookie;
//                }
//            }
//            return EMPTY_COOKIE;
//        }
//        return EMPTY_COOKIE;
//    }
//
//    void setToken(final Cookie cookie) throws EscidocClientException {
//        if (isEmpty(cookie)) {
//            mainWindow.showNotification("the user does not provide any token.");
//
//            LOG.debug("the user does not provide any token.");
//            app.showLandingView();
//
//        }
//        else {
//
//            LOG.debug("the user has a token.");
//            mainWindow.showNotification("the user has a token.");
//            tryAuthenticate(cookie);
//
//        }
//    }
//
//    private void tryAuthenticate(final Cookie token) {
//        app.loadProtectedResources(token.getValue());
//    }
//
//    private boolean isEmpty(final Cookie cookie) {
//        return "".equals(cookie.getValue());
//    }
//
//    private boolean isEscidocCookie(final Cookie cookie) {
//        return ESCIDOC_COOKIE_NAME.equals(cookie.getName());
//    }
//
//    @Override
//    public void transactionEnd(
//        final Application application, final Object transactionData) {
//
//    }
// }