/**
 * 
 */
package de.escidoc.admintool.view.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.util.POJOItem;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.view.util.interfaces.IMenuItem;

/**
 * @author ASP
 * 
 */
public final class LayoutHelper {

    private LayoutHelper() {
        // Utility classes should not have a public or default constructor.
    }

    private final static Map<String, List<Field>> ATTACHED_FIELD_MAP =
        new HashMap<String, List<Field>>();

    /**
     * Helper method. Puts a blank in front of a component.
     * 
     * @param label
     *            The label in front of the control.
     * @param comp
     *            The component to display.
     * @param width
     *            the fixed size of the label. The parameter has to be in CSS
     *            style, i.e. 400px for instance.
     * @param required
     *            should it be marked with an asterisk.
     * @return The component in an horizontal layout. A blank in front and
     *         afterwards is inserted.
     */
    public static synchronized HorizontalLayout create(
        final String label, final Component comp, final int width,
        final boolean required) {
        final HorizontalLayout hor = new HorizontalLayout();
        hor.setHeight(Constants.DEFAULT_HEIGHT);
        hor.addComponent(new Label(" "));
        final String text = Constants.P_ALIGN_RIGHT + label + Constants.P;
        Label l;
        hor.addComponent(l = new Label(text, Label.CONTENT_XHTML));
        l.setSizeUndefined();
        l.setWidth(width + Constants.PX);
        hor.setComponentAlignment(l, com.vaadin.ui.Alignment.MIDDLE_RIGHT);

        if (required) {
            hor
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }
        hor.addComponent(comp);
        hor.setComponentAlignment(comp, Alignment.MIDDLE_RIGHT);
        hor.addComponent(new Label(" "));
        hor.setSpacing(false);
        return hor;
    }

    /**
     * Helper method. Puts a blank in front of a component.
     * 
     * @param label
     *            The label in front of the control.
     * @param comp
     *            The component to display.
     * @param width
     *            the fixed size of the label. The parameter has to be in CSS
     *            style, i.e. 400px for instance.
     * @param required
     *            should it be marked with an asterisk.
     * @return The component in an horizontal layout. A blank in front and
     *         afterwards is inserted.
     */
    public static synchronized HorizontalLayout create(
        final String label, final CheckBox comp, final int width,
        final boolean required) {
        final HorizontalLayout hor = new HorizontalLayout();
        hor.setHeight(Constants.DEFAULT_HEIGHT);
        hor.addComponent(new Label(" "));
        final String text = Constants.P_ALIGN_RIGHT + label + Constants.P;
        Label l;
        hor.addComponent(l = new Label(text, Label.CONTENT_XHTML));
        l.setSizeUndefined();
        l.setWidth(width + Constants.PX);
        hor.setComponentAlignment(l, Alignment.MIDDLE_RIGHT);

        if (required) {
            hor
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }
        hor.addComponent(comp);
        hor.setComponentAlignment(comp, Alignment.MIDDLE_RIGHT);
        hor.addComponent(new Label(" "));
        hor.setSpacing(false);
        return hor;
    }

    /**
     * Helper method. Puts a blank in front of a component.
     * 
     * @param label
     *            The label in front of the control.
     * @param comp
     *            The component to display.
     * @param width
     *            the fixed size of the label. The parameter has to be in CSS
     *            style, i.e. 400px for instance.
     * @param height
     *            the height of the layout
     * @param required
     *            should it be marked with an asterisk.
     * @return The component in an horizontal layout. A blank in front and
     *         afterwards is inserted.
     */
    public static synchronized HorizontalLayout create(
        final String label, final Component comp, final int width,
        final int height, final boolean required) {
        final HorizontalLayout hor = new HorizontalLayout();
        // hor.setHeight("30px");
        hor.setHeight(height + Constants.PX);
        hor.addComponent(new Label(" "));
        final String text = Constants.P_ALIGN_RIGHT + label + Constants.P;
        Label l;
        hor.addComponent(l = new Label(text, Label.CONTENT_XHTML));
        l.setSizeUndefined();
        l.setWidth(width + Constants.PX);
        hor.setComponentAlignment(l, Alignment.MIDDLE_RIGHT);

        if (required) {
            hor
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:"
                        + (height / 2 - 13) + "px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }
        hor.addComponent(comp);
        hor.setComponentAlignment(comp, Alignment.MIDDLE_RIGHT);
        hor.addComponent(new Label(" "));
        hor.setSpacing(false);
        return hor;
    }

    /**
     * Helper method. Puts a blank in front of a component.
     * 
     * @param label
     *            The label in front of the control.
     * @param comp
     *            The component to display.
     * @param labelWidth
     *            the fixed size of the label. The parameter has to be in CSS
     *            style, i.e. 400px for instance.
     * @param height
     * @param required
     *            should it be marked with an asterisk.
     * @param buttons
     * @return The component in an horizontal layout. A blank in front and
     *         afterwards is inserted.
     */
    public static synchronized VerticalLayout create(
        final String label, final Component comp, final int labelWidth,
        final int height, final boolean required, final Button[] buttons) {

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setSpacing(false);
        hLayout.setHeight(height + Constants.PX);
        hLayout.addComponent(new Label(" "));
        final Label textLabel =
            new Label(Constants.P_ALIGN_RIGHT + label + Constants.P,
                Label.CONTENT_XHTML);
        hLayout.addComponent(textLabel);
        textLabel.setSizeUndefined();
        textLabel.setWidth(labelWidth + Constants.PX);
        hLayout.setComponentAlignment(textLabel, Alignment.MIDDLE_RIGHT);

        if (required) {
            hLayout
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:"
                        + (height / 2 - 13) + "px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hLayout
                .addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }
        hLayout.addComponent(comp);
        hLayout.setComponentAlignment(comp, Alignment.MIDDLE_RIGHT);
        hLayout.addComponent(new Label(" &nbsp; ", Label.CONTENT_XHTML));

        final VerticalLayout vLayout = new VerticalLayout();
        vLayout.addComponent(hLayout);

        final HorizontalLayout buttonLayout = new HorizontalLayout();
        vLayout.addComponent(buttonLayout);

        final Label la = new Label("&nbsp;", Label.CONTENT_XHTML);
        la.setSizeUndefined();
        la.setWidth(labelWidth + 7 + Constants.PX);

        buttonLayout.addComponent(la);
        for (final Button b : buttons) {
            buttonLayout.addComponent(b);
        }

        return vLayout;
    }

    /**
     * Helper method. Puts a blank in front of a component.
     * 
     * @param label
     *            The label in front of the control.
     * @param accordion
     *            The accordion to display.
     * @param width
     *            the fixed size of the label. The parameter has to be in CSS
     *            style, i.e. 400px for instance.
     * @param height
     * @param required
     *            should it be marked with an asterisk.
     * @return The component in an horizontal layout. A blank in front and
     *         afterwards is inserted.
     */
    public static synchronized HorizontalLayout create(
        final String label, final Accordion accordion, final int width,
        final int height, final boolean required) {
        final HorizontalLayout hor = new HorizontalLayout();
        hor.setHeight(height + Constants.PX);
        hor.addComponent(new Label(" "));
        final String text = Constants.P_ALIGN_RIGHT + label + Constants.P;
        Label l;
        hor.addComponent(l = new Label(text, Label.CONTENT_XHTML));
        l.setSizeUndefined();
        l.setWidth(width + Constants.PX);
        hor.setComponentAlignment(l, Alignment.MIDDLE_RIGHT);
        if (required) {
            hor
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:"
                        + (height / 2 - 13) + "px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }

        final Panel pan = new Panel();
        pan.setSizeFull();
        // Have it take all space available in the layout.
        accordion.setSizeFull();
        // Some components to put in the Accordion.

        final String xml =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n\t<X>\n\t\t<today>\n\t\t</today>\n\t\t<today/>\n\t\t<today/>\n\t</X>\n</root>";

        for (int i = 0; i < 30; i++) {
            accordion.addTab(new Label(xml, Label.CONTENT_PREFORMATTED), "Tab"
                + i, null);
        }

        pan.addComponent(accordion);
        // pan.setSizeUndefined();
        pan.setWidth(accordion.getWidth() + Constants.PX);
        // pan.setHeight("500px");
        pan.setStyleName(Reindeer.PANEL_LIGHT);
        hor.addComponent(pan);
        hor.setComponentAlignment(pan, Alignment.MIDDLE_RIGHT);
        hor.addComponent(new Label(" "));
        hor.setSpacing(false);
        return hor;
    }

    /**
     * Helper method. Puts a blank in front of a component. Two labels with
     * different size in front of two components.
     * 
     * @param labelLeft
     *            the left (leading) label.
     * @param labelRight
     *            the right (leading) label.
     * @param compLeft
     *            the left component.
     * @param compRight
     *            the right component.
     * @param widthLeft
     *            the width of the left label.
     * @param widthRight
     *            the width of the right label.
     * @param required
     *            true if the component is required, otherwise false.
     * @return the customized component placed in a horizontal layout.
     */
    public static synchronized HorizontalLayout create(
        final String labelLeft, final String labelRight,
        final Component compLeft, final Component compRight,
        final int widthLeft, final int widthRight, final boolean required) {
        final HorizontalLayout hor = new HorizontalLayout();
        hor.setHeight(Constants.DEFAULT_HEIGHT);
        hor.addComponent(new Label(" "));
        final String text = Constants.P_ALIGN_RIGHT + labelLeft + Constants.P;
        Label ll, lr;
        hor.addComponent(ll = new Label(text, Label.CONTENT_XHTML));
        ll.setSizeUndefined();
        ll.setWidth(widthLeft + Constants.PX);
        hor.setComponentAlignment(ll, Alignment.MIDDLE_RIGHT);
        if (required) {
            hor
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }
        hor.addComponent(compLeft);
        hor.setComponentAlignment(compLeft, Alignment.MIDDLE_RIGHT);
        hor.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
        final String text2 = Constants.P_ALIGN_RIGHT + labelRight + Constants.P;
        hor.addComponent(lr = new Label(text2, Label.CONTENT_XHTML));
        lr.setSizeUndefined();
        lr.setWidth(widthRight + Constants.PX);
        hor.setComponentAlignment(lr, Alignment.MIDDLE_RIGHT);
        if (required) {
            hor
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }
        hor.addComponent(compRight);
        hor.setComponentAlignment(compRight, Alignment.MIDDLE_RIGHT);
        hor.addComponent(new Label(" "));
        hor.setSpacing(false);
        return hor;
    }

    /**
     * 
     * @param labelLeft
     * @param labelRight
     * @param compLeft
     * @param compRight
     * @param widthLeft
     * @param widthRight
     * @param required
     * @return the constructed result.
     */
    public static synchronized HorizontalLayout create(
        final String labelLeft, final String labelRight, final Label compLeft,
        final Label compRight, final int widthLeft, final int widthRight,
        final boolean required) {
        final HorizontalLayout hor = new HorizontalLayout();
        hor.setHeight(Constants.DEFAULT_HEIGHT);
        hor.addComponent(new Label(" "));
        final String text = Constants.P_ALIGN_RIGHT + labelLeft + Constants.P;
        Label ll, lr;
        hor.addComponent(ll = new Label(text, Label.CONTENT_XHTML));
        ll.setSizeUndefined();
        ll.setWidth(widthLeft + Constants.PX);
        hor.setComponentAlignment(ll, Alignment.MIDDLE_RIGHT);
        if (required) {
            hor
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }
        hor.addComponent(compLeft);
        hor.setComponentAlignment(compLeft, Alignment.BOTTOM_RIGHT);
        hor.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
        final String text2 = Constants.P_ALIGN_RIGHT + labelRight + Constants.P;
        hor.addComponent(lr = new Label(text2, Label.CONTENT_XHTML));
        lr.setSizeUndefined();
        lr.setWidth(widthRight + Constants.PX);
        hor.setComponentAlignment(lr, Alignment.BOTTOM_RIGHT);
        if (required) {
            hor
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }
        hor.addComponent(compRight);
        hor.setComponentAlignment(compRight, Alignment.MIDDLE_RIGHT);
        hor.addComponent(new Label(" "));
        hor.setSpacing(false);
        return hor;
    }

    /**
     * Helper method for placing components.
     * 
     * @param labelLeft
     *            the left (leading) label.
     * @param labelRight
     *            the right (leading) label.
     * @param compLeft
     *            the left component.
     * @param compRight
     *            the right component.
     * @param width
     *            the width of the label.
     * @param required
     *            true if the component is required, otherwise false.
     * @return the customized component placed in a horizontal layout.
     */
    public static synchronized HorizontalLayout create(
        final String labelLeft, final String labelRight,
        final Component compLeft, final Component compRight, final int width,
        final boolean required) {
        final HorizontalLayout hor = new HorizontalLayout();
        hor.setHeight(Constants.DEFAULT_HEIGHT);
        hor.addComponent(new Label(" "));
        final String text = Constants.P_ALIGN_RIGHT + labelLeft + Constants.P;
        Label ll, lr;
        hor.addComponent(ll = new Label(text, Label.CONTENT_XHTML));
        ll.setSizeUndefined();
        ll.setWidth(width + Constants.PX);
        hor.setComponentAlignment(ll, Alignment.MIDDLE_RIGHT);
        if (required) {
            hor
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }
        hor.addComponent(compLeft);
        hor.setComponentAlignment(compLeft, Alignment.MIDDLE_RIGHT);
        hor.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
        final String text2 = Constants.P_ALIGN_RIGHT + labelRight + Constants.P;
        hor.addComponent(lr = new Label(text2, Label.CONTENT_XHTML));
        lr.setSizeUndefined();
        lr.setWidth(width + Constants.PX);
        hor.setComponentAlignment(lr, Alignment.MIDDLE_RIGHT);
        if (required) {
            hor
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:13px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hor.addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }
        hor.addComponent(compRight);
        hor.setComponentAlignment(compRight, Alignment.MIDDLE_RIGHT);
        hor.addComponent(new Label(" "));
        hor.setSpacing(false);
        return hor;
    }

    /**
     * Helper method. Puts a blank in front of a component. Useful for buttons.
     * 
     * @param comp
     *            The component to display.
     * @return The component in an grid layout. A blank in front and afterwards
     *         is inserted.
     */
    public static synchronized GridLayout create(final Component comp) {
        final GridLayout hor = new GridLayout(3, 1);
        hor.setHeight(Constants.DEFAULT_HEIGHT);
        hor.addComponent(new Label(" "), 0, 0);
        hor.addComponent(comp, 1, 0);
        hor.setComponentAlignment(comp, Alignment.MIDDLE_RIGHT);
        hor.addComponent(new Label(" "), 2, 0);
        hor.setSpacing(false);
        return hor;
    }

    /**
     * Creates an element depending on its state.
     * 
     * @param className
     * @param item
     * 
     * @param readOnly
     *            can the values be changed.
     * @param propertyName
     *            the name of the binding property.
     * @return the initialized component.
     */
    public static synchronized AbstractComponent createElement(
        final String className, final POJOItem<?> item, final boolean readOnly,
        final String propertyName) {
        AbstractComponent comp;
        if (readOnly) {
            comp = new Label();
            ((Label) comp).setPropertyDataSource(item
                .getItemProperty(propertyName));
        }
        else {
            comp = new TextField();
            ((TextField) comp).setNullRepresentation("");
            ((TextField) comp).setWriteThrough(false);
            ((TextField) comp).setPropertyDataSource(item
                .getItemProperty(propertyName));
            List<Field> attachedFields = ATTACHED_FIELD_MAP.get(className);
            if (attachedFields == null) {
                attachedFields = new ArrayList<Field>();
                ATTACHED_FIELD_MAP.put(className, attachedFields);
            }
            attachedFields.add((Field) comp);
        }
        return comp;
    }

    /**
     * @param className
     * @param item
     * @param readOnly
     * @param propertyName
     * @param values
     * @return
     */
    public static synchronized AbstractComponent createSelectElement(
        final String className, final POJOItem<?> item, final boolean readOnly,
        final String propertyName, final String[] values) {
        AbstractComponent comp;
        if (readOnly) {
            comp = new Label();
            ((Label) comp).setPropertyDataSource(item
                .getItemProperty(propertyName));
        }
        else {
            comp = new Select();
            for (final String theItem : values) {
                ((Select) comp).addItem(theItem);
            }
            ((Select) comp).setWriteThrough(false);
            ((Select) comp).setPropertyDataSource(item
                .getItemProperty(propertyName));
            List<Field> attachedFields = ATTACHED_FIELD_MAP.get(className);
            if (attachedFields == null) {
                attachedFields = new ArrayList<Field>();
                ATTACHED_FIELD_MAP.put(className, attachedFields);
            }
            attachedFields.add((Field) comp);
        }
        return comp;
    }

    /**
     * @param className
     * @param item
     * @param readOnly
     * @param propertyName
     * @param values
     * @return AbstractComponent
     */
    public static synchronized AbstractComponent createSelectElement(
        final String className, final POJOItem<?> item, final boolean readOnly,
        final String propertyName, final Enum<?>[] values) {
        AbstractComponent comp;
        if (readOnly) {
            comp = new Label();
            ((Label) comp).setPropertyDataSource(item
                .getItemProperty(propertyName));
        }
        else {
            comp = new Select();
            for (final Enum<?> theItem : values) {
                ((Select) comp).addItem(theItem);
            }
            ((Select) comp).setWriteThrough(false);
            ((Select) comp).setPropertyDataSource(item
                .getItemProperty(propertyName));
            List<Field> attachedFields = ATTACHED_FIELD_MAP.get(className);
            if (attachedFields == null) {
                attachedFields = new ArrayList<Field>();
                ATTACHED_FIELD_MAP.put(className, attachedFields);
            }
            attachedFields.add((Field) comp);
        }
        return comp;
    }

    /**
     * @param className
     * @param item
     * @param readOnly
     * @param propertyName
     * @param values
     * @return AbstractComponent
     */
    public static synchronized AbstractComponent createSelectElement(
        final String className, final POJOItem<?> item, final boolean readOnly,
        final String propertyName, final IMenuItem[] values) {
        AbstractComponent comp;
        if (readOnly) {
            comp = new Label();
            ((Label) comp).setPropertyDataSource(item
                .getItemProperty(propertyName));
        }
        else {
            comp = new Select();
            for (final IMenuItem theItem : values) {
                ((Select) comp).addItem(theItem);
            }
            ((Select) comp).setWriteThrough(false);
            ((Select) comp).setPropertyDataSource(item
                .getItemProperty(propertyName));
            List<Field> attachedFields = ATTACHED_FIELD_MAP.get(className);
            if (attachedFields == null) {
                attachedFields = new ArrayList<Field>();
                ATTACHED_FIELD_MAP.put(className, attachedFields);
            }
            attachedFields.add((Field) comp);
        }
        return comp;
    }

    /**
     * @param className
     * @param item
     * @param readOnly
     * @param propertyName
     * @return AbstractComponent
     */
    public static synchronized AbstractComponent createListElement(
        final String className, final POJOItem<?> item, final boolean readOnly,
        final String propertyName) {
        final AbstractComponent comp = new ListSelect();
        ((ListSelect) comp).setReadOnly(readOnly);
        ((ListSelect) comp).setPropertyDataSource(item
            .getItemProperty(propertyName));
        ((ListSelect) comp).setWriteThrough(false);
        List<Field> attachedFields = ATTACHED_FIELD_MAP.get(className);
        if (attachedFields == null) {
            attachedFields = new ArrayList<Field>();
            ATTACHED_FIELD_MAP.put(className, attachedFields);
        }
        attachedFields.add((Field) comp);
        return comp;
    }

    /**
     * @param className
     * @param item
     * @param readOnly
     * @param propertyName
     * @param resolution
     * @return AbstractComponent
     */
    public static synchronized AbstractComponent createDateElement(
        final String className, final POJOItem<?> item, final boolean readOnly,
        final String propertyName, final int resolution) {
        AbstractComponent comp;
        if (readOnly) {
            comp = new Label();
            ((Label) comp).setPropertyDataSource(item
                .getItemProperty(propertyName));
        }
        else {
            comp = new DateField();
            ((DateField) comp).setResolution(resolution);
            ((DateField) comp).setWriteThrough(false);
            ((DateField) comp).setPropertyDataSource(item
                .getItemProperty(propertyName));
            List<Field> attachedFields = ATTACHED_FIELD_MAP.get(className);
            if (attachedFields == null) {
                attachedFields = new ArrayList<Field>();
                ATTACHED_FIELD_MAP.put(className, attachedFields);
            }
            attachedFields.add((Field) comp);
        }
        return comp;
    }

    /**
     * @param className
     * @param item
     * @param readOnly
     * @param text
     * @param propertyName
     * @return AbstractComponent
     */
    public static synchronized AbstractComponent createCheckBoxElement(
        final String className, final POJOItem<?> item, final boolean readOnly,
        final String text, final String propertyName) {
        final AbstractComponent comp = new CheckBox(text);
        comp.setReadOnly(readOnly);
        ((CheckBox) comp).setWriteThrough(false);
        ((CheckBox) comp).setPropertyDataSource(item
            .getItemProperty(propertyName));
        List<Field> attachedFields = ATTACHED_FIELD_MAP.get(className);
        if (attachedFields == null) {
            attachedFields = new ArrayList<Field>();
            ATTACHED_FIELD_MAP.put(className, attachedFields);
        }
        attachedFields.add((Field) comp);
        return comp;
    }

    /**
     * @param form
     * @param comp
     * @param label
     * @param labelWidth
     * @param width
     * @param height
     * @param required
     */
    public static synchronized void addElement(
        final FormLayout form, final AbstractComponent comp,
        final String label, final int labelWidth, final int width,
        final int height, final boolean required) {
        comp.setWidth(width + Constants.PX);
        form.addComponent(LayoutHelper.create(label, comp, labelWidth, height,
            required));
    }

    /**
     * @param form
     * @param comp
     * @param label
     * @param labelWidth
     * @param width
     * @param height
     * @param required
     * @param buttons
     */
    public static synchronized void addElement(
        final FormLayout form, final AbstractComponent comp,
        final String label, final int labelWidth, final int width,
        final int height, final boolean required, final Button[] buttons) {
        comp.setWidth(width + Constants.PX);
        form.addComponent(LayoutHelper.create(label, comp, labelWidth, height,
            required, buttons));
    }

    /**
     * @param form
     * @param comp
     * @param label
     * @param labelWidth
     * @param width
     * @param required
     */
    public static synchronized void addElement(
        final FormLayout form, final AbstractComponent comp,
        final String label, final int labelWidth, final int width,
        final boolean required) {
        comp.setWidth(width + Constants.PX);
        form.addComponent(LayoutHelper
            .create(label, comp, labelWidth, required));

    }

    /**
     * @param className
     * @return List of Fields
     */
    public static List<Field> getAttachedFields(final String className) {
        return ATTACHED_FIELD_MAP.get(className);
    }

}
