package fr.imt_atlantique.myfirstapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ClickCounterActivity extends AppCompatActivity {
    private int clickCount = 0;
    private Button clickButton;

    private static final String PREFS_NAME = "tp3";
    private static final String KEY_CLICK_COUNT = "clickCount";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_counter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.click_counter_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        clickCount = prefs.getInt(KEY_CLICK_COUNT, 0);

        clickButton = findViewById(R.id.clickButton);
        clickButton.setText(String.valueOf(clickCount));

        clickButton.setOnClickListener(v -> {
            clickCount++;
            clickButton.setText(String.valueOf(clickCount));
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_CLICK_COUNT, clickCount);
        editor.apply();
    }

    public void close(android.view.View view) {
        finish();
    }

    public void reset(android.view.View view) {
        clickCount = 0;
        clickButton.setText(String.valueOf(clickCount));
    }
}
