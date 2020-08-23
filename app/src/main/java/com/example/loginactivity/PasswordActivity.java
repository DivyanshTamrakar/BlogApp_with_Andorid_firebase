package com.example.loginactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class PasswordActivity extends AppCompatActivity {
    private EditText resetEmail;
    private Button resetpassbtn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        resetEmail = findViewById(R.id.ResetEmail);
        resetpassbtn = findViewById(R.id.resetpass);
        firebaseAuth = FirebaseAuth.getInstance();

        resetpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = resetEmail.getText().toString().trim();




                if(useremail.equals("")){
                    resetEmail.setError("Field can't be empty");
                    resetEmail.requestFocus();
                   /* new AlertDialog.Builder(PasswordActivity.this).setMessage("Please Enter your registerd Email!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(true).show();*/
                }else if(!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){

                                             resetEmail.setError("Please enter a valid email address");


                }
                    else{
                    firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PasswordActivity.this, "reset Password link has been sent", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PasswordActivity.this, LoginActivity.class));
                            }else{
                                Toast.makeText(PasswordActivity.this, "Password verification link Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });
        final Pattern EMAIL_Address = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"+"\\@"+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"+" ( " +"\\."+"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"+")"





        );

    }
}
