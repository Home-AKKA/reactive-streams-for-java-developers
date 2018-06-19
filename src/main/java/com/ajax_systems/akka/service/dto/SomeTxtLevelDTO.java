package com.ajax_systems.akka.service.dto;

import java.io.Serializable;


/**
 * A DTO for the SomeTxtLevel entity.
 */
public class SomeTxtLevelDTO implements Serializable {

    private String id;

    private String level;

    private String description;

    private int size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SomeTxtLevelDTO{" +
                "id='" + id + '\'' +
                ", level='" + level + '\'' +
                ", description='" + description + '\'' +
                ", size=" + size +
                '}';
    }
}
