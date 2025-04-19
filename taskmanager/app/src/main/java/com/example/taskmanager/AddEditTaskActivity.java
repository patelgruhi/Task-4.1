// File: Task 4.1/app/src/main/java/com/example/taskmanager/AddEditTaskActivity.java

package com.example.taskmanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.taskmanager.viewmodel.TaskViewModel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEditTaskActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.taskmanager.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.taskmanager.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.taskmanager.EXTRA_DESCRIPTION";
    public static final String EXTRA_DUE_DATE = "com.example.taskmanager.EXTRA_DUE_DATE";
    public static final String EXTRA_COMPLETED = "com.example.taskmanager.EXTRA_COMPLETED";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private Button buttonDatePicker;
    private TextView textViewSelectedDate;
    private CheckBox checkBoxCompleted;
    private Button buttonSave;

    private Calendar calendar;
    private long selectedDueDate;
    private TaskViewModel taskViewModel;
    private boolean isEditMode = false;
    private int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        buttonDatePicker = findViewById(R.id.button_date_picker);
        textViewSelectedDate = findViewById(R.id.text_view_selected_date);
        checkBoxCompleted = findViewById(R.id.checkbox_completed);
        buttonSave = findViewById(R.id.button_save);

        calendar = Calendar.getInstance();
        selectedDueDate = calendar.getTimeInMillis();
        updateDateText();
        // File: Task 4.1/app/src/main/java/com/example/taskmanager/AddEditTaskActivity.java (continued)

        // Set up date picker
        buttonDatePicker.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddEditTaskActivity.this,
                    (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                        calendar.set(Calendar.YEAR, selectedYear);
                        calendar.set(Calendar.MONTH, selectedMonth);
                        calendar.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth);
                        selectedDueDate = calendar.getTimeInMillis();
                        updateDateText();
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Set up save button
        buttonSave.setOnClickListener(v -> saveTask());

        // Set up TaskViewModel
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Check if we're in edit mode
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Task");
            isEditMode = true;
            taskId = intent.getIntExtra(EXTRA_ID, -1);

            taskViewModel.getTaskById(taskId).observe(this, task -> {
                if (task != null) {
                    editTextTitle.setText(task.getTitle());
                    editTextDescription.setText(task.getDescription());
                    selectedDueDate = task.getDueDate();
                    calendar.setTimeInMillis(selectedDueDate);
                    updateDateText();
                    checkBoxCompleted.setChecked(task.isCompleted());
                }
            });
        } else {
            setTitle("Add Task");
        }
    }

    private void updateDateText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date(selectedDueDate));
        textViewSelectedDate.setText(formattedDate);
    }

    private void saveTask() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        boolean isCompleted = checkBoxCompleted.isChecked();

        // Validate input
        if (title.isEmpty()) {
            editTextTitle.setError("Title cannot be empty");
            editTextTitle.requestFocus();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_DUE_DATE, selectedDueDate);
        data.putExtra(EXTRA_COMPLETED, isCompleted);

        if (isEditMode) {
            data.putExtra(EXTRA_ID, taskId);
        }

        setResult(RESULT_OK, data);
        finish();
    }
}