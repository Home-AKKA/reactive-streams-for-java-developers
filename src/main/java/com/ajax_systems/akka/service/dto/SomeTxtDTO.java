package com.ajax_systems.akka.service.dto;

import java.io.Serializable;


/**
 * A DTO for the SomeTxt entity.
 */
public class SomeTxtDTO implements Serializable {

    private String level;

    private Integer total;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "SomeTxtDTO{" +
                "level='" + level + '\'' +
                ", total=" + total +
                '}';
    }
}
