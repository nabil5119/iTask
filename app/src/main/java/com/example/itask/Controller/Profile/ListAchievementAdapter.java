package com.example.itask.Controller.Profile;

import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotest.Model.Achievement;
import com.example.todotest.R;
import com.example.todotest.View.Profile.ProfileView;

import java.util.List;

class ListAchievementViewHolder extends RecyclerView.ViewHolder
{
    TextView AchName, AchDescription;
    Button bar;
    ImageView image;

    ListAchievementViewHolder(View itemView) {
        super(itemView);
        AchName= itemView.findViewById(R.id.ach_name);
        AchDescription= itemView.findViewById(R.id.ach_description);
        bar=itemView.findViewById(R.id.bar);
        image=itemView.findViewById(R.id.ach_image);
    }
}

public class ListAchievementAdapter extends  RecyclerView.Adapter<ListAchievementViewHolder>
{

    private ProfileView profileView;
    private List<Achievement> achievementList;

    public ListAchievementAdapter(ProfileView profileView, List<Achievement> achievementList) {
        this.profileView = profileView;
        this.achievementList = achievementList;
    }

    @NonNull
    @Override
    public ListAchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(profileView.getActivity().getBaseContext());
        View view = inflater.inflate(R.layout.list_achievement, parent, false);

        return new ListAchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAchievementViewHolder holder, final int position)
    {
        holder.AchName.setText(achievementList.get(position).getName());
        holder.AchDescription.setText(achievementList.get(position).getDescription());
        Drawable img= profileView.getResources().getDrawable(R.drawable.master);
        switch(achievementList.get(position).getName())
        {
            case "Wildfire":  img= profileView.getResources().getDrawable(R.drawable.wildfire); break;
            case "Overachiever":  img= profileView.getResources().getDrawable(R.drawable.overachiever); break;
            case "Big spender":  img= profileView.getResources().getDrawable(R.drawable.bigspender); break;
            case "Champion":  img= profileView.getResources().getDrawable(R.drawable.champion); break;
        }

        holder.image.setImageDrawable(img);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        profileView.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float width = 200*displayMetrics.widthPixels/1080f;

        holder.bar.getLayoutParams().width= (int) ((achievementList.get(position).getLevel())*width+40);
        holder.bar.getLayoutParams().height= 55;
    }

    @Override
    public int getItemCount()
    {
        return achievementList.size();
    }
}
