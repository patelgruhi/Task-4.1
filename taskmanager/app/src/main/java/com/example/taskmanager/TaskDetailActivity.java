// File: Task 4.1/app/src/main/java/com/example/taskmanager/TaskDetailActivity.java

package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.viewmodel.TaskViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.taskmanager.EXTRA_ID";
    private TaskViewModel taskViewModel;
    private Task currentTask;
    private int taskId;

    private TextView textViewTitle;
    private TextView textViewDueDate;
    private TextView textViewStatus;
    private TextView textViewDescription;
    private Button buttonEdit;
    private Button buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        textViewTitle = findViewById(R.id.text_view_detail_title);
        textViewDueDate = findViewById(R.id.text_view_detail_due_date);
        textViewStatus = findViewById(R.id.text_view_detail_status);
        textViewDescription = findViewById(R.id.text_view_detail_description);
        buttonEdit = findViewById(R.id.button_edit);
        buttonDelete = findViewById(R.id.button_delete);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            taskId = intent.getIntExtra(EXTRA_ID, -1);

            taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
            taskViewModel.getTaskById(taskId).observe(this, task -> {
                if (task != null) {
                    currentTask = task;
                    updateUI();
                }
            });
        } else {
            Toast.makeText(this, "Error: Task not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        buttonEdit.setOnClickListener(v -> editTask());
        buttonDelete.setOnClickListener(v -> confirmDelete());
    }

    private void updateUI() {
        textViewTitle.setText(currentTask.getTitle());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String dueDate = dateFormat.format(new Date(currentTask.getDueDate()));
        textViewDueDate.setText(dueDate);

        textViewStatus.setText(currentTask.isCompleted() ? "Completed" : "Pending");
        textViewDescription.setText(currentTask.getDescription());
    }

    private void editTask() {
        Intent intent = new Intent(TaskDetailActivity.this, AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskActivity.EXTRA_ID, taskId);
        intent.putExtra(AddEditTaskActivity.EXTRA_TITLE, currentTask.getTitle());
        intent.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, currentTask.getDescription());
        intent.putExtra(AddEditTaskActivity.EXTRA_DUE_DATE, currentTask.getDueDate());
        intent.putExtra(AddEditTaskActivity.EXTRA_COMPLETED, currentTask.isCompleted());
        startActivityForResult(intent, MainActivity.EDIT_TASK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainActivity.EDIT_TASK_REQUEST && resultCode == RESULT_OK && data != null) {
            int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Task can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            long dueDate = data.getLongExtra(AddEditTaskActivity.EXTRA_DUE_DATE, 0);
            boolean isCompleted = data.getBooleanExtra(AddEditTaskActivity.EXTRA_COMPLETED, false);

            Task task = new Task(title, description, dueDate);
            task.setId(id);
            task.setCompleted(isCompleted);
            taskViewModel.update(task);

            Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    taskViewModel.delete(currentTask);
                    Toast.makeText(TaskDetailActivity.this, "Task deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}