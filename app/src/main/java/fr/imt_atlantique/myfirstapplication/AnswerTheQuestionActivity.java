package fr.imt_atlantique.myfirstapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AnswerTheQuestionActivity extends AppCompatActivity{

    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_the_question);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.tp4_answer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView = findViewById(R.id.textViewQuestion);
        editText = findViewById(R.id.answer_input);

        String question = getIntent().getStringExtra(AskAQuestionActivity.KEY_QUESTION);
        if(question != null){
            textView.setText(question);
        }

        Button button = findViewById(R.id.submit_answer);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = editText.getText().toString().trim();
                Intent resultIntent = new Intent();
                resultIntent.putExtra(AskAQuestionActivity.KEY_ANSWER, answer);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
