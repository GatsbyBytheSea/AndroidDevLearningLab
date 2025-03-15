package fr.imt_atlantique.myfirstapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.model.LatLng;

public class MarkerDetailActivity extends AppCompatActivity {
    private TextView tvTitle;
    private TextView tvPosition;
    private ImageView ivMarkerImage;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.marker_detail_page), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTitle = findViewById(R.id.tv_marker_title);
        tvPosition = findViewById(R.id.tv_marker_position);
        ivMarkerImage = findViewById(R.id.iv_marker_image);
        btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> {
            finish();
        });

        Intent intent = getIntent();
        String markerTitle = intent.getStringExtra("markerTitle");
        LatLng markerLatLng = intent.getParcelableExtra("markerLatLng");
        String markerImagePath = intent.getStringExtra("markerImagePath");

        tvTitle.setText(markerTitle);
        if(markerLatLng != null) {
            String position = getString(R.string.word_latitude) + " : " + markerLatLng.latitude + "\n" +
                    getString(R.string.word_longitude) + " : " + markerLatLng.longitude;
            tvPosition.setText(position);
        }
        if(markerImagePath != null) {
            Bitmap bmp = BitmapFactory.decodeFile(markerImagePath);
            ivMarkerImage.setImageBitmap(bmp);
        }
    }
}
