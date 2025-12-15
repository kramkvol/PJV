module cz.cvut.fit.sudk {
    requires javafx.controls;
    requires static lombok;

    exports cz.cvut.fit.sudk;

    exports cz.cvut.fit.sudk.mvc.controllers;
    exports cz.cvut.fit.sudk.mvc.models;
    exports cz.cvut.fit.sudk.mvc.views;
}
