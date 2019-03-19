package com.shaukan.gabriel.caripacar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.onesignal.OneSignal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.FormatFlagsConversionMismatchException;
import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private TextView mNameField;
    private EditText mOccupationField, mEmailField, mPasswordField;

    private Button  mConfirm;

    private ImageView mProfileImage;
    private TextView mPrivacyPolicy;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    private String userId, name, age, profileImageUrl, occupation,  userSex, email, password;

    private Uri resultUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        // subscribes user to onesignal
        OneSignal.startInit(this).init();
        OneSignal.setSubscription(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("notificationKey").setValue(userId);
            }
        });
        OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.None);

        mNameField = (TextView) findViewById(R.id.name);
        mEmailField = (EditText) findViewById(R.id.email);
        mPasswordField = (EditText) findViewById(R.id.password);
        mOccupationField = (EditText) findViewById(R.id.occupation);

        mProfileImage = (ImageView) findViewById(R.id.profileImg);
        mPrivacyPolicy = (TextView) findViewById(R.id.privacyPolicy);

        mConfirm = (Button) findViewById(R.id.confirm);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        getUserInfo();

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        mPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://sites.google.com/view/cari-pacar-privacy-policy/home");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


    }

    //Listens for changes in user data and sets fields to new values
    private void getUserInfo() {
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Map<String, Object > map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("Name") != null & map.get("Age") != null) {
                        name = map.get("Name").toString();
                        age = map.get("Age").toString();
                        mNameField.setText(name + ", " + age);
                    }

                    if(map.get("Occupation") != null) {
                        occupation = map.get("Occupation").toString();
                        mOccupationField.setText(occupation);
                    }

                    if(map.get("sex") != null) {
                        userSex = map.get("sex").toString();
                    }

                    if(FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
                        email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                        mEmailField.setText(email);
                    }

                    Glide.clear(mProfileImage);
                    if(map.get("profileImageUrl") != null) {
                        profileImageUrl = map.get("profileImageUrl").toString();
                        switch (profileImageUrl) {
                            case "default":
                                Glide.with(getApplication()).load(R.drawable.default_img_1).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Gets change user data from fields and saves them to the database
    private void saveUserInformation() {
        name  = mNameField.getText().toString();
        occupation = mOccupationField.getText().toString();
        email = mEmailField.getText().toString();

        Map userInfo = new HashMap();
        userInfo.put("Occupation", occupation);
        mUserDatabase.updateChildren(userInfo);

        FirebaseAuth.getInstance().getCurrentUser().updateEmail(email);



        Toast.makeText(SettingsActivity.this, "Perubahan berhasil", Toast.LENGTH_LONG).show();

        if(resultUri != null) {
            StorageReference filePath = FirebaseStorage.getInstance().getReference().child("profileImages").child(userId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            final UploadTask uploadTask = filePath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           Uri downloadUrl = uri;
                           Map userInfo = new HashMap();
                           userInfo.put("profileImageUrl", downloadUrl.toString());
                           mUserDatabase.updateChildren(userInfo);
                           finish();
                           return;

                       }
                   });
                }
            });

        } else {
            finish();
        }

        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
        }
    }
}
