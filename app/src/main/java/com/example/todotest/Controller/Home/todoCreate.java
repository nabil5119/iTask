package com.example.todotest.Controller.Home;

import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.todotest.R;
import com.example.todotest.View.Home.HomeView;

public class todoCreate extends DialogFragment
{

    private HomeView homeView;
    private int TodoCatId=0;

    public void setHomeView(HomeView homeView)
    {
        this.homeView = homeView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.create_todo_dialog, (ViewGroup) getView(), false);

        final EditText t = viewInflated.findViewById(R.id.title);
        final EditText d = viewInflated.findViewById(R.id.description);
        RadioGroup radioGroup = viewInflated.findViewById(R.id.typegroup);
        TodoCatId=0;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButton: TodoCatId=0;break;
                    case R.id.radioButton2:TodoCatId=1;break;
                    case R.id.radioButton3:TodoCatId=2;break;
                    case R.id.radioButton4:TodoCatId=3;break;
                }
            }
        });

        final CheckBox setTimer= viewInflated.findViewById(R.id.checkBox2);

        builder.setView(viewInflated)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(t.getText().toString().length()>0 && d.getText().toString().length()>0)
                        {
                          if (setTimer.isChecked())
                          {
                              timerDialog td=new timerDialog();
                              td.setHomeView(homeView);
                              td.setInfo(t.getText().toString(),d.getText().toString());
                              td.show(homeView.getActivity().getSupportFragmentManager(),"dialog1");
                          }
                          else
                          {
                              homeView.setData(t.getText().toString(),d.getText().toString(),TodoCatId,"");
                          }
                        }
                        else
                        {
                            Toast.makeText(homeView.getActivity(),"Please insert a title and a description",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        todoCreate.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}