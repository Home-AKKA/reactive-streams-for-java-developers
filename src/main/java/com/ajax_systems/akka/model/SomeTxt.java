package com.ajax_systems.akka.model;

import java.io.Serializable;
import java.util.Objects;


/**
 * A SomeTxt.
 */
public class SomeTxt implements Serializable {

    private static final long serialVersionUID = 1L;

    private String level;

    private Integer total;

    public String getLevel() {
        return level;
    }

    public SomeTxt level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getTotal() {
        return total;
    }

    public SomeTxt total(Integer total) {
        this.total = total;
        return this;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SomeTxt someTxt = (SomeTxt) o;
        return Objects.equals(level, someTxt.level);
    }

    @Override
    public int hashCode() {

        return Objects.hash(level);
    }
}
