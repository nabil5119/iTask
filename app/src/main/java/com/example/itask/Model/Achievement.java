package com.example.itask.Model;

import java.util.List;

public class Achievement
{

    private String id;
    private String name;
    private String description;
    private long level;
    private List<Long> steps;

    public Achievement(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setSteps(List<Long> steps) {
        this.steps = steps;
    }

    public List<Long> getSteps() {
        return steps;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public long getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
