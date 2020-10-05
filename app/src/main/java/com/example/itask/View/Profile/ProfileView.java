package com.example.itask.View.Profile;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotest.Controller.Profile.ListAchievementAdapter;
import com.example.todotest.Model.Achievement;
import com.example.todotest.Model.User;
import com.example.todotest.R;
import com.example.todotest.View.Welcome.WelcomeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileView extends Fragment {

    private RecyclerView listAchievement;

    private User user;

    private FirebaseFirestore db;
    private List<Achievement> achievementList = new ArrayList<>();
    private ListAchievementAdapter adapter;

    @SuppressLint({"SetTextI18n", "CutPasteId"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        if(user==null)
        {
            user= WelcomeView.user;
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = 635*displayMetrics.heightPixels/1080;
        RecyclerView rv=root.findViewById(R.id.listAch);
        rv.getLayoutParams().height= (int) (height);


        TextView textView_name=root.findViewById(R.id.textview_name);
        TextView textView_xp=root.findViewById(R.id.textview_xp);
        TextView textView_points=root.findViewById(R.id.textview_points);
        TextView textView_streak=root.findViewById(R.id.textview_streak);

        textView_name.setText(user.getFirstname()+" "+user.getLastname());
        textView_xp.setText(user.getXp()+"");
        textView_points.setText(user.getPoints()+"");
        textView_streak.setText(user.getStreak()+"");
        db = FirebaseFirestore.getInstance();

        listAchievement = root.findViewById(R.id.listAch);
        listAchievement.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listAchievement.setLayoutManager(layoutManager);
        loadData();

        return root;
    }

    private void loadData() {
        final List<Achievement> achList = new ArrayList<>();
        db.collection("Achievement").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            final Achievement achievement;

                            final String name = doc.getString("name");
                            String description = doc.getString("description");

                            if (!name.equals("Master")) {
                                List<Long> steps = (List<Long>) doc.get("steps");
                                achievement = new Achievement(doc.getId(), name, description);
                                achievement.setSteps(steps);
                            } else {
                                achievement = new Achievement(doc.getId(), name, description);
                            }
                            achList.add(achievement);
                        }
                        updateUI(achList);
                    }
                });
    }

    private void updateUI(final List<Achievement> achList) {
        db.collection("AchievementUser").whereEqualTo("userId", user.getId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc : task.getResult()) {
                            int level = Integer.valueOf(doc.getLong("level") + "");
                            Achievement achievement = getAchievement(achList,doc.getString("achievement"));
                            if (!achievement.getName().equals("Master"))
                            {
                                if(level<3)
                                achievement.setDescription(achievement.getDescription()
                                        .replace("xx", achievement.getSteps().get(level) + ""));
                                else
                                {
                                    achievement.setDescription("You earned this achievement. Well played!");
                                }
                            }
                            achievement.setLevel(level);
                            achievementList.add(achievement);
                        }
                        adapter = new ListAchievementAdapter(ProfileView.this, achievementList);
                        listAchievement.setAdapter(adapter);
                    }
                });
    }

    private Achievement getAchievement(List<Achievement> achList, String name)
    {
        for(Achievement ach:achList)
        {
            if(ach.getName().equals(name))
            return ach;
        }
        return null;
    }
}