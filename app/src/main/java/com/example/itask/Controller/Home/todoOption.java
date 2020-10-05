package com.example.itask.Controller.Home;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.todotest.R;
import com.example.todotest.View.Home.HomeView;

public class todoOption extends DialogFragment
{

    private HomeView homeView;
    private int id;

    void setHomeView(HomeView homeView)
    {
        this.homeView = homeView;
    }

    public void setId(int id)
    {
        this.id=id;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);

        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.option_dialog, (ViewGroup) getView(), false);
        Button delete=viewInflated.findViewById(R.id.delete);
        builder.setView(viewInflated);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeView.deleteItem(id);
                todoOption.this.getDialog().cancel();
            }
        });
        return builder.create();
    }
}