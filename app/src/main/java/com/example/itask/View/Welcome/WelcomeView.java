package com.example.itask.View.Welcome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todotest.Home;
import com.example.todotest.Model.User;
import com.example.todotest.R;
import com.example.todotest.View.Session.Login;
import com.example.todotest.View.Session.Signup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class WelcomeView extends AppCompatActivity
{


    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            /*if(currentUser.isEmailVerified())
            {*/
                final FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("User")
                        .whereEqualTo("email", currentUser.getEmail())
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
                                    Log.i("MAX",max+"");
                                    user=new User(id,firstname,lastname,currentUser.getEmail(),xp,points,deletable,max,spent,streak);

                                    Intent intent = new Intent(getBaseContext(), Home.class);
                                    startActivity(intent);
                                }
                            }
                        });
            /*}
            else
            {
                Intent i = new Intent(WelcomeView.this, Login.class);
                startActivity(i);
            }*/
        }
        else
        {
            setContentView(R.layout.newuser);

            Button createAccount= findViewById(R.id.create_account);
            Button logIn=findViewById(R.id.log_in);

            createAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), Signup.class);
                    startActivity(intent);
                }
            });

            logIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getBaseContext(), Login.class);
                    startActivity(intent);
                }
            });
        }
    }

}
