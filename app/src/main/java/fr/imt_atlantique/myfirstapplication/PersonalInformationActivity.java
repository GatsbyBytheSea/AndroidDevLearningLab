package fr.imt_atlantique.myfirstapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.Menu;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class PersonalInformationActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "UserInputPrefs";
    private static final String KEY_FAMILY_NAME = "familyName";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_BIRTH_PLACE = "birthPlace";
    private static final String KEY_BIRTH_DATE = "birthDate";
    private static final String KEY_PHONE_NUMBERS = "phoneNumbers";

    private EditText editTextFamilyName;
    private EditText editTextFirstName;
    private Spinner spinnerBirthPlace;
    private Button birthDateBtn;
    private LinearLayout personalInfo;

    private static final int STATIC_CHILD_COUNT = 8;

    private ActivityResultLauncher<Intent> dateActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.personal_information_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextFamilyName = findViewById(R.id.et_family_name);
        editTextFirstName = findViewById(R.id.et_first_name);
        spinnerBirthPlace = findViewById(R.id.select_birth_place);
        birthDateBtn = findViewById(R.id.select_birth_date);
        personalInfo = findViewById(R.id.personal_info);

        if (savedInstanceState != null) {
            editTextFamilyName.setText(savedInstanceState.getString(KEY_FAMILY_NAME, ""));
            editTextFirstName.setText(savedInstanceState.getString(KEY_FIRST_NAME, ""));
            spinnerBirthPlace.setSelection(savedInstanceState.getInt(KEY_BIRTH_PLACE, 0));
            birthDateBtn.setText(savedInstanceState.getString(KEY_BIRTH_DATE, getString(R.string.msg_select_date)));
            String phoneNumbersStr = savedInstanceState.getString(KEY_PHONE_NUMBERS, "");
            restorePhoneNumbers(phoneNumbersStr);
        } else {
            loadFromPreferences();
        }

        dateActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                (ActivityResult result) -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String selectedDate = data.getStringExtra("selected_date");
                            if (selectedDate != null && !selectedDate.isEmpty()) {
                                birthDateBtn.setText(selectedDate);
                            }
                        }
                    } else {
                        Snackbar.make(personalInfo, R.string.warning_no_date_selected, Snackbar.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void submit(View view){

        String familyName = editTextFamilyName.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        String birthPlace = spinnerBirthPlace.getSelectedItem().toString();
        String birthDate = birthDateBtn.getText().toString();

        String message = getString(R.string.title_family_name) + ": " + familyName + "\n" +
                getString(R.string.title_first_name) + ": " + firstName + "\n" +
                getString(R.string.title_birth_place) + ": " + birthPlace + "\n" +
                getString(R.string.title_birth_date) + ": " + birthDate;



        int totalChildren = personalInfo.getChildCount();
        StringBuilder phoneNumbers = new StringBuilder();
        for (int i = STATIC_CHILD_COUNT; i < totalChildren; i++) {
            LinearLayout phoneContainer = (LinearLayout) personalInfo.getChildAt(i);
            EditText phoneEditText = (EditText) phoneContainer.getChildAt(0);
            String phoneNumber = phoneEditText.getText().toString();
            if (!phoneNumber.isEmpty()) {
                phoneNumbers.append(phoneNumber).append("\n");
            }
        }
        if (phoneNumbers.length() > 0) {
            message += "\n" + getString(R.string.title_phone_number) + ": " + phoneNumbers;
        }

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(5);
        snackbar.setAction(getString(R.string.btn_close), v -> snackbar.dismiss());
        snackbar.show();
    }

    public void display(View view){
        String familyName = editTextFamilyName.getText().toString();
        String firstName = editTextFirstName.getText().toString();
        String birthPlace = spinnerBirthPlace.getSelectedItem().toString();
        String phoneNumbers = collectPhoneNumbers();

        User user = new User(familyName, firstName, birthPlace, phoneNumbers);
        Intent intent = new Intent(this, DisplayPersonalInformationActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            resetAction();
            return true;
        }
        if(id == R.id.action_wikipedia){
            String birthCity = spinnerBirthPlace.getSelectedItem().toString();
            String url = "http://fr.wikipedia.org/?search=" + birthCity;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Snackbar.make(personalInfo, getString(R.string.warning_no_browser_message), Snackbar.LENGTH_LONG).show();
            }
            return true;
        }
        if (id == R.id.action_share_birth_city) {
            String birthCity = spinnerBirthPlace.getSelectedItem().toString();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.title_my_birth_city) + " " + birthCity);
            shareIntent.setType("text/plain");

            Intent chooser = Intent.createChooser(shareIntent, getString(R.string.msg_share_via));
            if (shareIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(chooser);
            } else {
                Snackbar.make(personalInfo, getString(R.string.warning_no_share_app), Snackbar.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetAction() {
        editTextFamilyName.setText("");
        editTextFirstName.setText("");
        spinnerBirthPlace.setSelection(0);
        birthDateBtn.setText(getString(R.string.msg_select_date));
        int totalChildren = personalInfo.getChildCount();
        if (totalChildren > STATIC_CHILD_COUNT) {
            personalInfo.removeViews(STATIC_CHILD_COUNT, totalChildren - STATIC_CHILD_COUNT);
        }
    }

    public void addPhone(View view){
        LinearLayout personalInfo = findViewById(R.id.personal_info);

        LinearLayout phoneContainer = new LinearLayout(this);
        phoneContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        phoneContainer.setLayoutParams(containerParams);

        EditText phoneEditText = new EditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT,1);
        phoneEditText.setLayoutParams(params);
        phoneEditText.setHint("Phone Number");
        phoneEditText.setInputType(InputType.TYPE_CLASS_PHONE);

        Button deleteButton = new Button(this);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        deleteButton.setLayoutParams(buttonParams);
        deleteButton.setText(getString(R.string.btn_delete));

        deleteButton.setOnClickListener(v -> personalInfo.removeView(phoneContainer));

        phoneContainer.addView(phoneEditText);
        phoneContainer.addView(deleteButton);

        personalInfo.addView(phoneContainer);
    }

    public void backToWelcome(View view){
        finish();
    }

    public void selectBirthDate(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
//            String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
//            Button selectBirthDateBtn = findViewById(R.id.select_birth_date);
//            selectBirthDateBtn.setText(formattedDate);
//        }, year, month, day);
//
//        datePickerDialog.show();

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra("initial_year", year);
        intent.putExtra("initial_month", month);
        intent.putExtra("initial_day", day);

        if (intent.resolveActivity(getPackageManager()) != null) {
            dateActivityLauncher.launch(intent);
        } else {
            Snackbar.make(personalInfo, "No DateActivity found", Snackbar.LENGTH_LONG).show();
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_FAMILY_NAME, editTextFamilyName.getText().toString());
        outState.putString(KEY_FIRST_NAME, editTextFirstName.getText().toString());
        outState.putInt(KEY_BIRTH_PLACE, spinnerBirthPlace.getSelectedItemPosition());
        outState.putString(KEY_BIRTH_DATE, birthDateBtn.getText().toString());
        outState.putString(KEY_PHONE_NUMBERS, collectPhoneNumbers());
        super.onSaveInstanceState(outState);
    }

    private String collectPhoneNumbers() {
        StringBuilder sb = new StringBuilder();
        int totalChildren = personalInfo.getChildCount();
        for (int i = STATIC_CHILD_COUNT; i < totalChildren; i++) {
            LinearLayout phoneContainer = (LinearLayout) personalInfo.getChildAt(i);
            EditText phoneEditText = (EditText) phoneContainer.getChildAt(0);
            String phone = phoneEditText.getText().toString();
            if (!phone.isEmpty()) {
                if (sb.length() > 0) {
                    sb.append("||");
                }
                sb.append(phone);
            }
        }
        return sb.toString();
    }

    private void restorePhoneNumbers(String phoneNumbersStr) {
        int totalChildren = personalInfo.getChildCount();
        if (totalChildren > STATIC_CHILD_COUNT) {
            personalInfo.removeViews(STATIC_CHILD_COUNT, totalChildren - STATIC_CHILD_COUNT);
        }
        if (phoneNumbersStr != null && !phoneNumbersStr.isEmpty()) {
            String[] phones = phoneNumbersStr.split("\\|\\|");
            for (String phone : phones) {
                addPhone(null);
                int childCount = personalInfo.getChildCount();
                LinearLayout phoneContainer = (LinearLayout) personalInfo.getChildAt(childCount - 1);
                EditText phoneEditText = (EditText) phoneContainer.getChildAt(0);
                phoneEditText.setText(phone);
            }
        }
    }

    @Override
    protected void onDestroy() {
        saveToPreferences();
        super.onDestroy();
    }

    private void saveToPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_FAMILY_NAME, editTextFamilyName.getText().toString());
        editor.putString(KEY_FIRST_NAME, editTextFirstName.getText().toString());
        editor.putInt(KEY_BIRTH_PLACE, spinnerBirthPlace.getSelectedItemPosition());
        editor.putString(KEY_BIRTH_DATE, birthDateBtn.getText().toString());
        editor.putString(KEY_PHONE_NUMBERS, collectPhoneNumbers());
        editor.apply();
    }

    private void loadFromPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editTextFamilyName.setText(prefs.getString(KEY_FAMILY_NAME, ""));
        editTextFirstName.setText(prefs.getString(KEY_FIRST_NAME, ""));
        spinnerBirthPlace.setSelection(prefs.getInt(KEY_BIRTH_PLACE, 0));
        birthDateBtn.setText(prefs.getString(KEY_BIRTH_DATE, getString(R.string.msg_select_date)));
        String phoneNumbersStr = prefs.getString(KEY_PHONE_NUMBERS, "");
        restorePhoneNumbers(phoneNumbersStr);
    }
}
