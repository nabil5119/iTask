package com.example.itask.Controller.Home;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotest.Model.Todo;
import com.example.todotest.Model.User;
import com.example.todotest.R;
import com.example.todotest.View.Home.HomeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    private itemClickListener itemClickListner;
    TextView itemTitle, itemDescription;
    CheckBox checkBox;
    ImageView icon;


    void setItemClickListener(itemClickListener it)
    {
        this.itemClickListner=it;
    }

    ListItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        itemTitle= itemView.findViewById(R.id.item_title);
        itemDescription= itemView.findViewById(R.id.item_description);
        checkBox=itemView.findViewById(R.id.checkBox);
        icon=itemView.findViewById(R.id.todoicon);
        }

    @Override
    public void onClick(View v)
    {
        itemClickListner.onClick(v,getAdapterPosition(),false);
    }

}

public class ListItemAdapter extends  RecyclerView.Adapter<ListItemViewHolder>
{

    private HomeView homeView;
    private List<Todo> todoList;


    private int maxStreak=6;
    private int xpGained=25;
    private int pointsGained=5;

    public ListItemAdapter(HomeView homeView, List<Todo> todoList) {
        this.homeView = homeView;
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(Objects.requireNonNull(homeView.getActivity()).getBaseContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);

        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position)
    {
        holder.itemTitle.setText(todoList.get(position).getName());
        holder.itemDescription.setText(todoList.get(position).getDescription());
        holder.checkBox.setChecked(todoList.get(position).isDone());


        Drawable img= homeView.getResources().getDrawable(R.drawable.work);
        switch(todoList.get(position).getType())
        {
            case 0:  img= homeView.getResources().getDrawable(R.drawable.work); break;
            case 1:  img= homeView.getResources().getDrawable(R.drawable.social); break;
            case 2:  img= homeView.getResources().getDrawable(R.drawable.hobby); break;
            case 3:  img= homeView.getResources().getDrawable(R.drawable.other); break;
        }

        holder.icon.setImageDrawable(img);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked)
                {
                    HomeView.getDB().collection("ToDo").document(todoList.get(position).getId())
                            .update("done",holder.checkBox.isChecked());
                    todoList.get(position).setDone(true);

                    if(todoList.size()==homeView.getUser().getMax())
                    {
                        boolean done=true;
                        for(int i=0;i<todoList.size();i++)
                        {
                            if(!todoList.get(i).isDone()) done=false;
                        }
                        if(done && homeView.getUser().getStreak()<maxStreak)
                        {
                            addStreak(homeView.getUser());
                        }
                    }
                    long newXp=homeView.getUser().getXp()+xpGained;
                    homeView.getUser().setXp(newXp);
                    HomeView.getDB().collection("User").document(homeView.getUser().getId())
                            .update("xp",newXp);

                    long newPoints=homeView.getUser().getPoints()+pointsGained;
                    homeView.getUser().setPoints(newPoints);
                    HomeView.getDB().collection("User").document(homeView.getUser().getId())
                            .update("points",newPoints);

                    checkDone(homeView.getUser());
                    checkXP(homeView.getUser());
                }
                holder.checkBox.setChecked(true);
        }});


        holder.setItemClickListener(new itemClickListener()
        {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                todoOption tdc=new todoOption();
                tdc.setHomeView(homeView);
                tdc.setId(position);
                tdc.show(Objects.requireNonNull(homeView.getActivity()).getSupportFragmentManager(),"dialog0");
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    private void addStreak(final User user)
    {
        HomeView.getDB().collection("User").document(user.getId())
                .update("streak",user.getStreak()+1);
        user.setStreak(user.getStreak()+1);

        final long[] i = {0};
        HomeView.getDB().collection("AchievementUser").whereEqualTo("userId",user.getId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc: Objects.requireNonNull(task.getResult()))
                {
                    if(Objects.requireNonNull(doc.getString("achievement")).equals("Wildfire"))
                    {
                        i[0]=(long) doc.get("level");
                        if(user.getStreak()==1 && i[0]==0)
                        {
                            user.setAchievement(homeView.getContext(),"Wildfire",1);
                        }
                        else if(user.getStreak()==3 && i[0]==1)
                        {
                            user.setAchievement(homeView.getContext(),"Wildfire",2);
                        }
                        else if(user.getStreak()==6 && i[0]==2)
                        {
                            user.setAchievement(homeView.getContext(),"Wildfire",3);
                        }
                    }
                }
            }
        });
    }

    private void checkDone(final User user)
    {
        final int[] Done = {0};
        HomeView.getDB().collection("ToDo").whereEqualTo("email",user.getEmail())
            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc: Objects.requireNonNull(task.getResult()))
                {
                    if(doc.getBoolean("done"))
                    {
                        Done[0] +=1;
                    }
                }
                if(Done[0]==5 || Done[0]==10 || Done[0]==20)
                {
                    final long[] i = {0};

                    HomeView.getDB().collection("AchievementUser").whereEqualTo("userId",user.getId())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for(DocumentSnapshot doc:task.getResult())
                            {
                                if(doc.getString("achievement").equals("Champion"))
                                {
                                    i[0]=(long) doc.get("level");
                                    if(Done[0]==5 && i[0]==0)
                                    {
                                        user.setAchievement(homeView.getContext(),"Champion",1);
                                    }
                                    else if(Done[0]==10 && i[0]==1)
                                    {
                                        user.setAchievement(homeView.getContext(),"Champion",2);
                                    }
                                    else if(Done[0]==20 && i[0]==2)
                                    {
                                        user.setAchievement(homeView.getContext(),"Champion",3);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });


    }

    private void checkXP(final User user)
    {
        final long xp=user.getXp();
        Log.i("XP",xp+"");
        if(xp==50 || xp==100 || xp==200)
        {
            final long[] i = {0};

            HomeView.getDB().collection("AchievementUser").whereEqualTo("userId",user.getId())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot doc:task.getResult())
                    {
                        if(doc.getString("achievement").equals("Overachiever"))
                        {
                            i[0]=(long) doc.get("level");
                            if(xp==50 && i[0]==0)
                            {
                                user.setAchievement(homeView.getContext(),"Overachiever",1);
                            }
                            else if(xp==100 && i[0]==1)
                            {
                                user.setAchievement(homeView.getContext(),"Overachiever",2);
                            }
                            else if(xp==200 && i[0]==2)
                            {
                                user.setAchievement(homeView.getContext(),"Overachiever",3);
                            }
                        }
                    }
                }
            });
        }
    }
}
