package com.example.todotest.View.Session;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todotest.Home;
import com.example.todotest.R;

import com.example.todotest.Model.User;
import com.example.todotest.View.Welcome.WelcomeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class Login extends AppCompatActivity {
    FirebaseFirestore db;

    private FirebaseAuth mAuth;
    private EditText email, password;
    Button fab;
    Button userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.editText);
        password = findViewById(R.id.editText3);
        userPass=findViewById(R.id.button);

        fab = findViewById(R.id.button5);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String em=email.getText().toString();
                String pw=password.getText().toString();

                connect(em,pw);
            }
        });

        userPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().length()>0)
                {
                    mAuth.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this,
                                                "Password sent to your email", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(Login.this,
                                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(Login.this,
                            "Please insert your email", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void connect(final String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            /*if(firebaseUser.isEmailVerified())
                            {*/
                                db.collection("User")
                                        .whereEqualTo("email", email)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                                    String id= document.getString("id");
                                                    String firstname=document.getString("firstname");
                                                    String lastname=document.getString("lastname");
                                                    long xp= (long) document.get("xp");
                                                    long points=(long) document.get("points");
                                                    long deletable=(long) document.get("deletable");
                                                    long max=(long) document.get("max");
                                                    long spent=(long) document.get("spent");
                                                    long streak=(long) document.get("streak");

                                                    WelcomeView.user= new User(id,firstname,lastname,email,xp,points,deletable,max,spent,streak);
                                                    Intent intent = new Intent(getBaseContext(), Home.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                            /*}
                            else
                            {
                                Toast.makeText(Login.this,
                                        "Please Verify your email", Toast.LENGTH_LONG).show();
                            }*/
                        }
                        else{
                            Toast.makeText(Login.this,
                                    "Wrong email and password combination.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
