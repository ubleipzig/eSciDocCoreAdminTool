package de.escidoc.admintool.view;

import java.util.Date;

import org.joda.time.DateTime;

import com.vaadin.data.Property;
import com.vaadin.ui.PopupDateField;

@SuppressWarnings("serial")
public class PopUpJodaDateField extends PopupDateField {

    public PopUpJodaDateField() {
    }

    public PopUpJodaDateField(final String caption) {
        super(caption);
    }

    public PopUpJodaDateField(final String caption, final Property dataSource) {
        super(caption, dataSource);
    }

    public PopUpJodaDateField(final Property dataSource)
        throws IllegalArgumentException {
        super(dataSource);
    }

    public PopUpJodaDateField(final String caption, final Date value) {
        super(caption, value);
    }

    @Override
    public void setPropertyDataSource(final Property newDataSource) {

        if (newDataSource.getType().equals(DateTime.class)) {
            super.setPropertyDataSource(new ZDateTimeProperty(newDataSource));
        }
        else {
            super.setPropertyDataSource(newDataSource);
        }
    }
}