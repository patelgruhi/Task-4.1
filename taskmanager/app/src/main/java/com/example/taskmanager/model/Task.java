package com.example.taskmanager.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private long dueDate;
    private boolean isCompleted;

    public Task(String title, String description, long dueDate) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (description == null) {
            description = "";
        }
        if (dueDate < 0) {
            throw new IllegalArgumentException("Due date cannot be negative");
        }
        
        this.title = title.trim();
        this.description = description.trim();
        this.dueDate = dueDate;
        this.isCompleted = false;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description != null ? description.trim() : "";
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        if (dueDate < 0) {
            throw new IllegalArgumentException("Due date cannot be negative");
        }
        this.dueDate = dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}