package fr.imt_atlantique.myfirstapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class DisplayPersonalInformationActivity extends AppCompatActivity {

    private User user;
    private TextView displayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_personal_information);

        displayTextView = findViewById(R.id.display_personal_info_text);
        user = getIntent().getParcelableExtra("user");

        if (user != null) {
            String info = "Family Name: " + user.getFamilyName() + "\n"
                    + "First Name: " + user.getFirstName() + "\n"
                    + "Birth Place: " + user.getBirthPlace() + "\n";
            if (user.getPhoneNumbers() != null && !user.getPhoneNumbers().isEmpty()) {
                info += "Phone Numbers:\n";
                String[] phones = user.getPhoneNumbers().split("\\|\\|");
                for (String phone : phones) {
                    info += phone.trim() + "\n";
                }
            }
            displayTextView.setText(info);
        } else {
            displayTextView.setText(R.string.warning_no_user_data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_dial_phone){
            if (user != null && user.getPhoneNumbers() != null && !user.getPhoneNumbers().isEmpty()) {
                String[] phones = user.getPhoneNumbers().split("\\|\\|");

                for (int i = 0; i < phones.length; i++) {
                    phones[i] = phones[i].trim();
                }
                if (phones.length == 1) {
                    dialPhone(phones[0]);
                }else{
                    new androidx.appcompat.app.AlertDialog.Builder(this)
                            .setTitle("Select Phone Number")
                            .setItems(phones, (dialog, which) -> dialPhone(phones[which]))
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            } else {
                Snackbar.make(displayTextView, "No phone numbers available", Snackbar.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialPhone(String phoneNumber) {
        if (!phoneNumber.isEmpty()) {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phoneNumber));
            if (dialIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(dialIntent);
            } else {
                Snackbar.make(displayTextView, "No dialer app found", Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(displayTextView, "No valid phone number", Snackbar.LENGTH_LONG).show();
        }
    }
    public void back(android.view.View view) {
        finish();
    }
}
