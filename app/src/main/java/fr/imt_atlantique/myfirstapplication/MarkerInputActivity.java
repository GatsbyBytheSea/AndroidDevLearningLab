package fr.imt_atlantique.myfirstapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;

public class MarkerInputActivity extends AppCompatActivity {

    private EditText etMarker;
    private ImageView ivMarker;
    private Button btnAddPhoto, btnSubmit;
    private String markerImagePath=null;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_input);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.marker_input_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etMarker = findViewById(R.id.marker_name_input);
        ivMarker = findViewById(R.id.image_view_marker);
        btnAddPhoto = findViewById(R.id.btn_add_image);
        btnSubmit = findViewById(R.id.btn_submit);

        cameraLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        Bitmap bmp = (Bitmap) result.getData().getExtras().get("data");
                        markerImagePath = saveBitmapToFile(bmp);
                        ivMarker.setImageBitmap(bmp);
                    }
                }
        );

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode() == RESULT_OK && result.getData() != null){
                        Uri selectedImage = result.getData().getData();
                        try {
                            Bitmap bmp = android.provider.MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            markerImagePath = saveBitmapToFile(bmp);
                            ivMarker.setImageBitmap(bmp);
                        } catch(Exception e){
                            e.printStackTrace();
                            Toast.makeText(MarkerInputActivity.this, getString(R.string.error_image_load_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        btnAddPhoto.setOnClickListener(v -> {
            new AlertDialog.Builder(MarkerInputActivity.this)
                    .setTitle(getString(R.string.msg_choose_image_source))
                    .setItems(new String[]{getString(R.string.word_camera), getString(R.string.word_gallery)}, (dialog, which) -> {
                        if(which == 0){
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                                cameraLauncher.launch(cameraIntent);
                            }else{
                                new AlertDialog.Builder(MarkerInputActivity.this)
                                        .setTitle(getString(R.string.error_no_camera))
                                        .setMessage(getString(R.string.error_no_camera))
                                        .setPositiveButton(getString(R.string.btn_close), (dialog1, which1) -> {
                                            dialog1.dismiss();
                                        }).show();
                            }
                        }else if(which == 1){
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            galleryLauncher.launch(galleryIntent);
                        }
                    }).show();
        });

        btnSubmit.setOnClickListener(v -> {
            String markerText = etMarker.getText().toString();
            Intent reslutIntent = new Intent();
            reslutIntent.putExtra("markerText", markerText);
            reslutIntent.putExtra("markerImagePath", markerImagePath);
            setResult(RESULT_OK, reslutIntent);
            finish();
        });
    }

    private String saveBitmapToFile(Bitmap bitmap){
        try{
            File cacheDir = getCacheDir();
            String fileName = "marker_image_" + System.currentTimeMillis() + ".png";
            File file = new File(cacheDir, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
