package com.shaukan.gabriel.caripacar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button mRegister;
    private EditText mEmail, mPassword, mName, mAge, mOccupation, mConfirmPassword;
    private RadioGroup mRadioGroup;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(RegistrationActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        //XML Object initialization
        mConfirmPassword = (EditText) findViewById(R.id.passwordConfirm);
        mRegister = (Button) findViewById(R.id.register);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mName = (EditText) findViewById(R.id.name);
        mAge = (EditText) findViewById(R.id.age);
        mOccupation = (EditText) findViewById(R.id.occupation);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        //Registering User
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectId = mRadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = findViewById(selectId);
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String confirmPassword = mConfirmPassword.getText().toString();
                final String name = mName.getText().toString();
                final String age = mAge.getText().toString();
                final String occupation = mOccupation.getText().toString();

                if (email.equals("") || password.equals("") || name.equals("") || age.equals("") || occupation.equals("") || radioButton == null) {
                    Toast.makeText(RegistrationActivity.this, "Mohon isi bagian kosong", Toast.LENGTH_LONG).show();
                } else if(!password.equals(confirmPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Password tidak sama", Toast.LENGTH_LONG).show();
                } else if(password.length() < 7) {
                    Toast.makeText(RegistrationActivity.this, "Password harus lebih dari 7 karakter", Toast.LENGTH_LONG).show();
                } else {
                    if (radioButton.getText() == null) {
                        return;
                    }

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show();
                            } else {
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                Map userInfo = new HashMap<>();
                                userInfo.put("Name", name);
                                userInfo.put("Age", age);
                                if (radioButton.getText().toString().equals("Pria")) {
                                    userInfo.put("sex", "Male");
                                } else {
                                    userInfo.put("sex", "Female");
                                }
                                userInfo.put("Occupation", occupation);
                                userInfo.put("profileImageUrl", "default");
                                currentUserDb.updateChildren(userInfo);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    //Prevents back button from crashing app
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(RegistrationActivity.this, ChooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }
}
