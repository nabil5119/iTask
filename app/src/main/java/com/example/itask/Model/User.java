package com.example.itask.Model;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private long xp;
    private long points;
    private long deletable;
    private long max;
    private long spent;
    private long streak;

    public User(String i, String f, String l, String e, long xp, long points, long deletable, long max, long spent, long streak)
    {
        this.id=i;
        this.firstname=f;
        this.lastname=l;
        this.email=e;
        this.xp=xp;
        this.points=points;
        this.deletable=deletable;
        this.max=max;
        this.spent=spent;
        this.streak=streak;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public long getDeletable() {
        return deletable;
    }

    public void setDeletable(int deletable) {
        this.deletable = deletable;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getSpent() {
        return spent;
    }

    public void setSpent(long spent) {
        this.spent = spent;
    }

    public long getStreak() {
        return streak;
    }

    public void setStreak(long streak) {
        this.streak = streak;
    }

    public void setAchievement(Context hm, final String achievement, final int level)
    {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("AchievementUser").whereEqualTo("userId",id)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc:task.getResult())
                {
                    if(doc.getString("achievement").equals(achievement))
                    {
                        db.collection("AchievementUser").document(doc.getId()).update("level",level);
                    }
                }
            }
        });

        Toast toast= Toast.makeText(hm,"Congratulation! you unlocked '"+achievement+"' level "+level,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();
    }
}
