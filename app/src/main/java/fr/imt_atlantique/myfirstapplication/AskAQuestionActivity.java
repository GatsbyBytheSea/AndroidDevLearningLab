package fr.imt_atlantique.myfirstapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class AskAQuestionActivity extends AppCompatActivity {

    public static final String KEY_QUESTION = "key_question";
    public static final String KEY_ANSWER   = "key_answer";

    private EditText editText;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_a_question);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tp4), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editText = findViewById(R.id.question_input);
        Button button = findViewById(R.id.submit_question);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String answer = data.getStringExtra(KEY_ANSWER);
                            if (answer != null && !answer.isEmpty()) {
                                Snackbar.make(findViewById(R.id.tp4), "User answer: " + answer, Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String question = editText.getText().toString();
                Intent intent = new Intent(AskAQuestionActivity.this, AnswerTheQuestionActivity.class);
                intent.putExtra(KEY_QUESTION, question);

                activityResultLauncher.launch(intent);
            }
        });
    }

        public void closeTp4(android.view.View view) {
        finish();
    }
}
