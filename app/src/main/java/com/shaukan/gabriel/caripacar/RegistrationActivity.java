package com.shaukan.gabriel.caripacar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Button mRegister;
    private EditText mEmail, mPassword, mName, mOccupation, mConfirmPassword;
    private RadioGroup mRadioGroup;
    private TextView mDateofBirth;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

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
        mDateofBirth = (TextView) findViewById(R.id.dateOfBirth);
        mOccupation = (EditText) findViewById(R.id.occupation);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        //Date Selection for age
        mDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegistrationActivity.this,
                        R.style.CustomDatePickerDialogTheme,
                        mDateSetListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String Age = "Umur: " + getAge(year, month, dayOfMonth);
                mDateofBirth.setText(Age);
            }
        };



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
                final String dateOfBirth = mDateofBirth.getText().toString();
                final String occupation = mOccupation.getText().toString();

                if (email.equals("") || password.equals("") || name.equals("") || dateOfBirth.equals("") || occupation.equals("") || radioButton == null) {
                    Toast.makeText(RegistrationActivity.this, "Mohon isi bagian kosong", Toast.LENGTH_SHORT).show();
                } else if(name.length() > 12) {
                    Toast.makeText(RegistrationActivity.this, "Nama maximal 12 karakter", Toast.LENGTH_SHORT).show();
                } else if(occupation.length() > 52) {
                    Toast.makeText(RegistrationActivity.this, "Nama pekerjaan maximal 52 huruf", Toast.LENGTH_SHORT).show();
                } else if(!password.equals(confirmPassword)) {
                    Toast.makeText(RegistrationActivity.this, "Password tidak sama", Toast.LENGTH_SHORT).show();
                } else if(password.length() < 7) {
                    Toast.makeText(RegistrationActivity.this, "Password harus lebih dari 7 karakter", Toast.LENGTH_SHORT).show();
                } else if (Integer.valueOf(dateOfBirth.substring(6)) < 18) {
                    Toast.makeText(RegistrationActivity.this, "Anda belum cukup umur", Toast.LENGTH_SHORT).show();
                }
                    else {
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
                                userInfo.put("Age", dateOfBirth.substring(6));
                                if (radioButton.getText().toString().equals("Pria")) {
                                    userInfo.put("sex", "Male");
                                } else {
                                    userInfo.put("sex", "Female");
                                }
                                userInfo.put("Occupation", occupation);
                                userInfo.put("profileImageUrl", "default");
                                userInfo.put("notificationKey", "default");
                                currentUserDb.updateChildren(userInfo);
                            }
                        }
                    });

                    OneSignal.setSubscription(true);
                    OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                        @Override
                        public void idsAvailable(String userId, String registrationId) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("notificationKey").setValue(userId);
                        }
                    });
                    OneSignal.setInFocusDisplaying(OneSignal.OSInFocusDisplayOption.None);
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

    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
