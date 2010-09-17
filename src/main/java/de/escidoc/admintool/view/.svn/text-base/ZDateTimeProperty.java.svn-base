package de.escidoc.admintool.view;

import java.util.Date;

import org.joda.time.DateTime;

import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class ZDateTimeProperty implements Property {

    private final Property sourceProperty;

    public ZDateTimeProperty(final Property sourceProperty) {
        this.sourceProperty = sourceProperty;

        if (!sourceProperty.getType().equals(DateTime.class)) {
            throw new RuntimeException(
                "Source property a not a DateTime, but is a "
                    + sourceProperty.getType().getName());
        }
    }

    public Object getValue() {
        final DateTime sourceDate = (DateTime) sourceProperty.getValue();
        Object result = null;
        if (sourceDate != null) {
            result = sourceDate.toDate();
        }
        return result;
    }

    public void setValue(final Object o) throws ReadOnlyException,
        ConversionException {

        DateTime value = null;
        if (o != null) {
            if (o instanceof Date) {
                final Date date = (Date) o;
                value = new DateTime(date);
            }
            else {
                throw new RuntimeException(
                    "Value supplied was not a Date.class");
            }
        }

        sourceProperty.setValue(value);
    }

    public Class<?> getType() {
        return Date.class;
    }

    public boolean isReadOnly() {
        return sourceProperty.isReadOnly();
    }

    public void setReadOnly(final boolean b) {
        sourceProperty.setReadOnly(b);
    }
}
