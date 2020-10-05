package com.example.itask.View.Shop;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.todotest.Model.User;
import com.example.todotest.R;
import com.example.todotest.View.Welcome.WelcomeView;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShopView extends Fragment {

    private User user;
    private FirebaseFirestore db;

    private Button textView_points;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_shop, container, false);

        if(user==null)
        {
            user= WelcomeView.user;
        }

        textView_points=root.findViewById(R.id.button_points);
        textView_points.setText("You have "+user.getPoints()+"                  ");
        ImageView imageView=root.findViewById(R.id.imagePoints);


        final Button buy_5=root.findViewById(R.id.shop_increase5_btn);
        final Button buy_7=root.findViewById(R.id.shop_increase7_btn);
        final Button buy_1=root.findViewById(R.id.shop_increase10_btn);
        final Button buy_d=root.findViewById(R.id.shop_delete_btn);

        if(user.getMax()>=5) {
            buy_5.setAlpha(.5f);
            buy_5.setEnabled(false);
        }
        if(user.getMax()>=7) {
            buy_7.setEnabled(false);
            buy_7.setAlpha(.5f);
        }
        if(user.getMax()==10)
        {
            buy_1.setEnabled(false);
            buy_1.setAlpha(.5f);
        }

        buy_d.setText("BUY ("+user.getDeletable()+")");
        db = FirebaseFirestore.getInstance();

        buy_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getPoints()>=30)
                {
                    if(user.getMax()==3)
                    {
                        db.collection("User").document(user.getId()).update("max",5);
                        updatePoints(30);
                        user.setMax(5);
                        buy_5.setAlpha(.5f);
                        buy_5.setEnabled(false);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "You already bought that.", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    Toast.makeText(getActivity(), "You don't have enough points.", Toast.LENGTH_LONG).show();
                }

            }
        });
        buy_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getPoints()>=50)
                {
                    if(user.getMax()==5)
                    {
                        db.collection("User").document(user.getId()).update("max",7);
                        updatePoints(50);
                        user.setMax(7);

                        buy_7.setEnabled(false);
                        buy_7.setAlpha(.5f);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "You already bought that.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "You don't have enough points.", Toast.LENGTH_LONG).show();
                }

            }
        });
        buy_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getPoints()>=100)
                {
                    if(user.getMax()==7)
                    {
                        db.collection("User").document(user.getId()).update("max",10);
                        updatePoints(100);
                        user.setMax(10);

                        buy_1.setEnabled(false);
                        buy_1.setAlpha(.5f);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "You already bought that.", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "You don't have enough points.", Toast.LENGTH_LONG).show();
                }

            }
        });

        buy_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getPoints()>=10)
                {
                    if(user.getDeletable()<9)
                    {
                        db.collection("User").document(user.getId()).update("deletable",user.getDeletable()+1);
                        updatePoints(10);
                        user.setDeletable((int) (user.getDeletable()+1));
                        buy_d.setText("BUY ("+user.getDeletable()+")");
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"You can't buy more than 9.",Toast.LENGTH_SHORT).show();
                    }
                }

                else
                {
                    Toast.makeText(getActivity(), "You don't have enough points.", Toast.LENGTH_LONG).show();
                }

            }
        });

        imageView.bringToFront();
        return root;
    }

    @SuppressLint("SetTextI18n")
    private void updatePoints(int points)
    {
        db.collection("User").document(user.getId()).update("points",user.getPoints()-points);
        db.collection("User").document(user.getId()).update("spent",user.getSpent()+points);
        user.setSpent(user.getSpent()+points);
        user.setPoints(user.getPoints()-points);

        textView_points.setText("You have "+user.getPoints()+"                  ");
        if(user.getSpent()>=100)
        {
            user.setAchievement(getContext(),"Big spender",3);
        }
        else if(user.getSpent()>=50)
        {
            user.setAchievement(getContext(),"Big spender",2);
        }
        else if(user.getSpent()>=25)
        {
            user.setAchievement(getContext(),"Big spender",1);
        }
    }
}