package fr.imt_atlantique.myfirstapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class DateActivity extends AppCompatActivity {

    private TextView dateTextView;
    private int selectedYear, selectedMonth, selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.date_picker), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dateTextView = findViewById(R.id.date_text_view);
        Button btnConfirm = findViewById(R.id.btn_confirm);
        Button btnCancel = findViewById(R.id.btn_cancel);

        Intent intent = getIntent();
        Calendar calendar = Calendar.getInstance();
        selectedYear = intent.getIntExtra("initial_year", calendar.get(Calendar.YEAR));
        selectedMonth = intent.getIntExtra("initial_month", calendar.get(Calendar.MONTH));
        selectedDay = intent.getIntExtra("initial_day", calendar.get(Calendar.DAY_OF_MONTH));

        showDatePickerDialog();

        btnConfirm.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            resultIntent.putExtra("selected_date", formattedDate);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        btnCancel.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year, month, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = month;
                    selectedDay = dayOfMonth;
                    String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateTextView.setText(formattedDate);
                }, selectedYear, selectedMonth, selectedDay);
        datePickerDialog.show();
    }
}
