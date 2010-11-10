package de.escidoc.admintool.view.admintask;

import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;

public class Style {

    static class Ruler extends Label {
        public Ruler() {
            super("<hr />", Label.CONTENT_XHTML);
        }
    }

    static class H1 extends Label {
        public H1(final String caption) {
            super(caption);
            setSizeUndefined();
            setStyleName(Reindeer.LABEL_H1);
        }
    }

    static class H2 extends Label {
        public H2(final String caption) {
            super(caption);
            setSizeUndefined();
            setStyleName(Reindeer.LABEL_H2);
        }
    }

}
