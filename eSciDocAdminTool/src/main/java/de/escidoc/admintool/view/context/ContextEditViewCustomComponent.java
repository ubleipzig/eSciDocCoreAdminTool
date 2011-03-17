//package de.escidoc.admintool.view.context;
//
//import com.vaadin.data.Item;
//import com.vaadin.ui.CustomComponent;
//
//import de.escidoc.admintool.app.AdminToolApplication;
//import de.escidoc.admintool.service.ContextService;
//import de.escidoc.admintool.service.OrgUnitService;
//import de.escidoc.core.client.exceptions.EscidocException;
//import de.escidoc.core.client.exceptions.InternalClientException;
//import de.escidoc.core.client.exceptions.TransportException;
//import de.escidoc.core.resources.om.context.Context;
//
//public class ContextEditViewCustomComponent extends CustomComponent {
//
//    public ContextEditViewCustomComponent(
//        final AdminToolApplication adminToolApplication,
//        final ContextService contextService, final OrgUnitService orgUnitService) {
//        // TODO Auto-generated constructor stub
//    }
//
//    public Context openContext(final String comment) throws EscidocException,
//        InternalClientException, TransportException {
//        // final Context openedContext =
//        // contextService.open(getSelectedItemId(), comment);
//        // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(false);
//        // getField(ViewConstants.PUBLIC_STATUS_ID).setValue("opened");
//        // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(true);
//        // ((ContextView)
//        // getParent().getParent()).updateList(getSelectedItemId());
//
//        // return openedContext;
//        return null;
//    }
//
//    public Context closeContext() throws EscidocException,
//        InternalClientException, TransportException {
//        // final Context closedContext =
//        // contextService.close(getSelectedItemId());
//        // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(false);
//        // getField(ViewConstants.PUBLIC_STATUS_ID).setValue("closed");
//        // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(true);
//        // footer.setVisible(false);
//        // setReadOnly(true);
//        // ((ContextView)
//        // getParent().getParent()).updateList(getSelectedItemId());
//        // return closedContext;
//        return null;
//    }
//
//    public void deleteContext() throws EscidocException,
//        InternalClientException, TransportException {
//        // contextService.delete(getSelectedItemId());
//    }
//
//    public Context closeContext(final String comment) throws EscidocException,
//        InternalClientException, TransportException {
//        // final Context closedContext =
//        // contextService.close(getSelectedItemId(), comment);
//        // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(false);
//        // getField(ViewConstants.PUBLIC_STATUS_ID).setValue("closed");
//        // getField(ViewConstants.PUBLIC_STATUS_ID).setReadOnly(true);
//        // footer.setVisible(false);
//        // setReadOnly(true);
//        // ((ContextView)
//        // getParent().getParent()).updateList(getSelectedItemId());
//        // return closedContext;
//        return null;
//    }
//
//    public void setSelected(final Item item) {
//        // TODO Auto-generated method stub
//
//    }
//
// }
