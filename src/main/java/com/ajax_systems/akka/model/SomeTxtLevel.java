package com.ajax_systems.akka.model;

import java.io.Serializable;
import java.util.Objects;


/**
 * A SomeTxtLevel.
 */
public class SomeTxtLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String level;

    private String description;

    private int size;

    public String getId() {
        return id;
    }

    public SomeTxtLevel id(String id) {
        this.id = id;
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public SomeTxtLevel level(String level) {
        this.level = level;
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public SomeTxtLevel description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public SomeTxtLevel size(int size) {
        this.size = size;
        return this;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SomeTxtLevel that = (SomeTxtLevel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SomeTxtLevel{" +
                "id='" + id + '\'' +
                ", level='" + level + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                '}';
    }
}
