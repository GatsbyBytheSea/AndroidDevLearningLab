package fr.imt_atlantique.myfirstapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomePage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.welcome_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button button = findViewById(R.id.to_snackbar_fragment_page);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomePage.this, SnackbarFragmentActivity.class);
            startActivity(intent);
        });

        Button button2 = findViewById(R.id.to_personal_information_page);
        button2.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomePage.this, PersonalInformationActivity.class);
            startActivity(intent);
        });
    }

    public void toEditDisplayFragments(android.view.View view) {
        Intent intent = new Intent(this, TwoFragmentsActivity.class);
        startActivity(intent);
    }

    public void toDynamicFragmentPage(android.view.View view) {
        Intent intent = new Intent(this, DynamicallyCreateFragmentsActivity.class);
        startActivity(intent);
    }

    public void toCounterPage(android.view.View view) {
        Intent intent = new Intent(this, ClickCounterActivity.class);
        startActivity(intent);
    }

    public void toAskAndAnswer(android.view.View view) {
        Intent intent = new Intent(this, AskAQuestionActivity.class);
        startActivity(intent);
    }

    public void toPersonalInfoFragment(android.view.View view) {
        Intent intent = new Intent(this, PersonalInfoFragmentActivity.class);
        startActivity(intent);
    }
}
