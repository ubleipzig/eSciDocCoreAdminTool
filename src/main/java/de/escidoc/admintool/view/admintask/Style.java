package de.escidoc.admintool.view.admintask;

import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;

public class Style {

    private Style() {
        // do not init.
    }

    public static class Ruler extends Label {
        private static final long serialVersionUID = -4909196895183387829L;

        public Ruler() {
            super("<hr />", Label.CONTENT_XHTML);
        }
    }

    public static class H1 extends Label {
        private static final long serialVersionUID = -2843233317747887008L;

        public H1(final String caption) {
            super(caption);
            setSizeUndefined();
            setStyleName(Reindeer.LABEL_H1);
        }
    }

    public static class H2 extends Label {
        private static final long serialVersionUID = 1210257960304559971L;

        public H2(final String caption) {
            super(caption);
            setSizeUndefined();
            setStyleName(Reindeer.LABEL_H2);
        }
    }

}
