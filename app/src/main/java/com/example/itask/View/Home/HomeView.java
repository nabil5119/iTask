package com.example.itask.View.Home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todotest.Controller.Home.ListItemAdapter;
import com.example.todotest.Controller.Home.todoCreate;
import com.example.todotest.Model.Todo;
import com.example.todotest.Model.User;
import com.example.todotest.R;
import com.example.todotest.View.Welcome.WelcomeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomeView extends Fragment
{
    @SuppressLint("StaticFieldLeak")
    private static FirebaseFirestore db;

    private User user;
    private List<Todo> todoList=new ArrayList<>();

    private RecyclerView listItem;

    private ListItemAdapter adapter;
    private Button fab;
    private Calendar calendar;

    private TextView todoCount;


    public User getUser() {
        return user;
    }

    public static FirebaseFirestore getDB()
    {
        return db;
    }

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        if(user==null)
        {
            user= WelcomeView.user;
        }

        db = FirebaseFirestore.getInstance();

        fab = root.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(todoList.size()<user.getMax()) {
                    todoCreate tdc = new todoCreate();
                    tdc.setHomeView(HomeView.this);
                    tdc.show(getActivity().getSupportFragmentManager(), "dialog0");
                }
                else
                {
                    Toast.makeText(getActivity(), "You can't create more than "+user.getMax()+" ToDos, increase your maximum using the Shop.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        listItem = root.findViewById(R.id.listTodo);
        listItem.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listItem.setLayoutManager(layoutManager);

        TextView day= root.findViewById(R.id.day);
        TextView month= root.findViewById(R.id.month);
        TextView year= root.findViewById(R.id.year);
        TextView daystring= root.findViewById(R.id.daystring);

        todoCount= root.findViewById(R.id.textView_todocount);


        calendar  = Calendar.getInstance();

        day.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        year.setText(String.valueOf(calendar.get(Calendar.YEAR)));

        String dayName="Monday";
        switch(calendar.get(Calendar.DAY_OF_WEEK))
        {
            case Calendar.TUESDAY: dayName="Tuesday" ;break;
            case Calendar.WEDNESDAY: dayName="Wednesday" ;break;
            case Calendar.THURSDAY: dayName="Thursday" ;break;
            case Calendar.FRIDAY: dayName="Friday" ;break;
            case Calendar.SATURDAY: dayName="Saturday" ;break;
            case Calendar.SUNDAY: dayName="Sunday" ;break;
        }
        String monthName="JAN";
        switch(calendar.get(Calendar.MONTH ))
        {
            case 2: monthName="FEB";break;
            case 3: monthName="MAR";break;
            case 4: monthName="APR";break;
            case 5: monthName="MAY";break;
            case 6: monthName="JUN";break;
            case 7: monthName="JUL";break;
            case 8: monthName="AUG";break;
            case 9: monthName="SEPT";break;
            case 10: monthName="OCT";break;
            case 11: monthName="NOV";break;
            case 12: monthName="DEC";break;
        }

        month.setText(monthName);
        daystring.setText(dayName);
        loadData();

        return root;
    }

    public void deleteItem(int order) {
        if(user.getDeletable()>0)
        {
            if(!todoList.get(order).isDone())
            {
                db.collection("ToDo")
                        .document(todoList.get(order).getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadData();
                            }
                        });
                db.collection("User").document(user.getId()).update("deletable",user.getDeletable()-1);
                user.setDeletable((int) (user.getDeletable()-1));
            }
            else
            {
                Toast.makeText(getActivity(), "You can't delete ToDos you've already done.",Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(getActivity(), "You need to buy the ability to delete from the shop",Toast.LENGTH_SHORT).show();
        }

    }

    public void setData(String t, String d, int catId, String timer)
    {

            int day=calendar.get(Calendar.DAY_OF_MONTH);
            int month=calendar.get(Calendar.MONTH)+1;
            int year=calendar.get(Calendar.YEAR);
            String date=day+"/"+month+"/"+year;

            String id= UUID.randomUUID().toString();
            Map<String,Object> todo = new HashMap<>();
            todo.put("title",t);
            todo.put("description",d);
            todo.put("categoryId",catId);
            todo.put("timer",timer);
            todo.put("done",false);
            todo.put("email",user.getEmail());
            todo.put("date",date);

            db.collection("ToDo").document(id)
                    .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    loadData();
                }
            });


    }

    private void loadData()
    {
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);
        String date=day+"/"+month+"/"+year;

        if(todoList.size()>0)
        {
            todoList.clear();
        }
        db.collection("ToDo").whereEqualTo("date",date).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc:task.getResult())
                        {
                            String docEmail=doc.getString("email");
                            boolean docDone =doc.getBoolean("done");

                            if(docEmail!=null && docEmail.equals(user.getEmail()))
                            {
                                Todo todo = new Todo(doc.getId(),doc.getString("title"),doc.getString("description"),docDone);
                                String type=doc.getLong("categoryId")+"";
                                todo.setType(Integer.valueOf(type));
                                todoList.add(todo);
                            }
                        }
                        adapter=new ListItemAdapter(HomeView.this, todoList);
                        listItem.setAdapter(adapter);
                        if(todoList.size()==10)
                        {
                            fab.setAlpha(0.5f);
                            fab.setEnabled(false);
                        }
                        else
                        {
                            fab.setAlpha(1f);
                            fab.setEnabled(true);
                        }
                        todoCount.setText(todoList.size()+"/"+user.getMax());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
}