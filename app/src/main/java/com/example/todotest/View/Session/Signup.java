package com.example.todotest.View.Session;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todotest.Model.CryptoHash;
import com.example.todotest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Signup extends AppCompatActivity {

    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public EditText firstname,lastname,email, password;
    Button fab;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        db = FirebaseFirestore.getInstance();
        firstname = findViewById(R.id.editText2);
        lastname = findViewById(R.id.editText4);
        email = findViewById(R.id.editText);
        password = findViewById(R.id.editText3);
        mAuth = FirebaseAuth.getInstance();

        fab = findViewById(R.id.button4);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String pw=password.getText().toString();
                setData(firstname.getText().toString(), lastname.getText().toString(),email.getText().toString(),pw);
            }
        });
    }

    private void setData(final String f, final String l, final String e, final String p )
    {
        mAuth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                        //Toast.makeText(Signup.this,"Account created, please verify your email ",Toast.LENGTH_SHORT).show();
                                        Toast.makeText(Signup.this,"Account created, please login",Toast.LENGTH_SHORT).show();

                                        CryptoHash ch=new CryptoHash();

                                        String id=UUID.randomUUID().toString();
                                        Map<String,Object> user = new HashMap<>();
                                        user.put("id",id);
                                        user.put("firstname",f);
                                        user.put("lastname",l);
                                        user.put("email",e);
                                        user.put("password",ch.hash(p));
                                        user.put("xp",0);
                                        user.put("points",0);
                                        user.put("deletable",0);
                                        user.put("max",3);
                                        user.put("spent",0);
                                        user.put("streak",0);
                                        db.collection("User").document(id).set(user);
                                        updateUI();

                                        String[] achievements={"Wildfire","Champion","Overachiever","Big spender","Master"};
                                        for(String achievement:achievements)
                                        {
                                            String achId=UUID.randomUUID().toString();
                                            Map<String,Object> achievementUser=new HashMap<>();
                                            achievementUser.put("level",0);
                                            achievementUser.put("achievement",achievement);
                                            achievementUser.put("userId",id);
                                            db.collection("AchievementUser").document(achId).set(achievementUser);
                                        }

                                }
                            });
                        }
                        else {
                            try {
                                throw task.getException();
                            } catch(Exception e) {
                                Toast.makeText(Signup.this, e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    private void updateUI()
    {
        Intent intent = new Intent(getBaseContext(), Login.class);
        startActivity(intent);
    }



}
