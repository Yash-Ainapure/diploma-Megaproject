package com.example.mega;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    //flag set for onstart method getstatus
    String flag="1";
    private Button button;
    private EditText EditText1;
    Double lat_points,long_points;
    private EditText EditText2;
    private String username,password;
    ProgressBar pgbar;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("locations");
        pgbar=(ProgressBar) findViewById(R.id.pgbar);
        button =findViewById(R.id.button);
        EditText1=findViewById(R.id.editText1);
        EditText2=findViewById(R.id.editText2);
        mAuth=FirebaseAuth.getInstance();
        getStatus();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        int colorCodeDark = Color.parseColor("#117a11");
        window.setStatusBarColor(colorCodeDark);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException e){
            Toast.makeText(MainActivity.this,"Null Pointer Exception occurred",Toast.LENGTH_SHORT).show();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pgbar.setVisibility(ProgressBar.VISIBLE);
                username=EditText1.getText().toString();
                password=EditText2.getText().toString();

                if(username.isEmpty()){
                    pgbar.setVisibility(ProgressBar.INVISIBLE);
                    EditText1.setError("Email cannot be empty");
                    EditText1.requestFocus();
                } else if (password.isEmpty()) {
                    pgbar.setVisibility(ProgressBar.INVISIBLE);
                    EditText1.setError("Password cannot be empty");
                    EditText1.requestFocus();
                }
                else{
                    if(username.equals("Admin")&&password.equals("123456")){
                        mAuth.signInWithEmailAndPassword(username+"@gmail.com",password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Admin Login Success", Toast.LENGTH_SHORT).show();
                                    moveToUserActivity();
                                    pgbar.setVisibility(ProgressBar.INVISIBLE);
                                    //saving session of admin
                                    adminLoggedIn();
                                }else{
                                    pgbar.setVisibility(ProgressBar.INVISIBLE);
                                    Toast.makeText(MainActivity.this, "Admin login failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        //moveToUserActivity();
                    }
                    else {
                        mAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                    userLoggedIn();
                                    moveToUserActivity();
                                }else{
                                    pgbar.setVisibility(ProgressBar.INVISIBLE);
                                    Toast.makeText(MainActivity.this, "Log|Error :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
    private  void moveToUserActivity(){
        Intent intent=new Intent(MainActivity.this,user_page.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
public void getStatus(){
        if(flag.equals("1")){
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                moveToUserActivity();
                finish();
                flag="0";
            }
        }
}
public void userLoggedIn(){
    SessionManagement sessionManagement=new SessionManagement(MainActivity.this);
    sessionManagement.saveSession(""+EditText1.getText().toString());
}
public void adminLoggedIn(){
    SessionManagement sessionManagement=new SessionManagement(MainActivity.this);
    sessionManagement.saveSession("admin");
}
}