// File: Task 4.1/app/src/main/java/com/example/taskmanager/viewmodel/TaskViewModel.java

package com.example.taskmanager.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TaskViewModel extends AndroidViewModel {
    private static final String TAG = "TaskViewModel";
    private TaskRepository repository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application);
        allTasks = repository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<Task> getTaskById(int taskId) {
        try {
            return repository.getTaskById(taskId);
        } catch (Exception e) {
            Log.e(TAG, "Error getting task by ID: " + taskId, e);
            return null;
        }
    }

    public void insert(Task task) {
        try {
            repository.insert(task);
        } catch (Exception e) {
            Log.e(TAG, "Error inserting task: " + task.getTitle(), e);
        }
    }

    public void update(Task task) {
        try {
            repository.update(task);
        } catch (Exception e) {
            Log.e(TAG, "Error updating task: " + task.getTitle(), e);
        }
    }

    public void delete(Task task) {
        try {
            repository.delete(task);
        } catch (Exception e) {
            Log.e(TAG, "Error deleting task: " + task.getTitle(), e);
        }
    }
}