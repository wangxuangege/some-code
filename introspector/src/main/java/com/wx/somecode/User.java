package com.wx.somecode;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author xinquan.huangxq
 */
public class User {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private String name;

    private String a = "2";

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    public String getAAA() {
        return a;
    }

    public void setAAA(String a) {
        String old = this.a;
        this.a = a;
        this.pcs.firePropertyChange("a", old == null ? "null" : old, this.a);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        this.pcs.firePropertyChange("name", old == null ? "null" : old, this.name);
    }
}
