package com.masud.admin_control3_madrasha_app_practic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class noticeActivity3 extends AppCompatActivity {
    private MaterialCardView galleryImagePicking;
    private ImageView galleryImage;
    private AppCompatButton submitBtn;
    private TextInputLayout textInputLayout;

    String postTile;

    private DatabaseReference database;


    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice3);

        database = FirebaseDatabase.getInstance().getReference().child("Notice");

        galleryImagePicking = findViewById(R.id.imagePick);
        galleryImage = findViewById(R.id.postImage);
        submitBtn = findViewById(R.id.submitBtn);
        textInputLayout = findViewById(R.id.textInputLayout);
        galleryImagePicking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryImageIntent, 1);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             postTile =   textInputLayout.getEditText().getText().toString().trim();
             if (postTile.isEmpty()){
                 textInputLayout.getEditText().setError("write here");
                 textInputLayout.getEditText().requestFocus();
             } else if (imageBitmap == null) {
                 gotoDatabase();
             }
            }
        });
    }

    private void gotoDatabase() {
        String uniqueId = database.push().getKey();

        NoticeData noticeData = new NoticeData(postTile,"45 ded 34","34:44 pm",uniqueId,"");
        database.child(uniqueId).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(noticeActivity3.this, "Thank for waitting", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK);{
            Uri imageUri = data.getData();
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            galleryImage.setImageURI(imageUri);
        }
    }
}