package com.example.todotest.Model;

public class Todo {

    private String id;
    private String name;
    private String description;
    private boolean done;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Todo(String i, String t, String d,boolean done)
    {
        id=i;
        name=t;
        description=d;
        this.done=done;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
