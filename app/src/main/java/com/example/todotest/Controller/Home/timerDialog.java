package com.example.todotest.Controller.Home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.app.Dialog;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.todotest.R;
import com.example.todotest.Controller.alarm.AlarmReceiver;
import com.example.todotest.View.Home.HomeView;

import java.util.Calendar;

public class timerDialog extends DialogFragment
{

    private HomeView homeView;
    private String t,d;

    void setInfo(String t, String d)
    {
        this.t=t;
        this.d=d;
    }

    void setHomeView(HomeView homeView)
    {
        this.homeView = homeView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        AlertDialog.Builder timerBuilder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        View timerViewInflated = LayoutInflater.from(getContext()).inflate(R.layout.setup_timer, (ViewGroup) getView(), false);


        final TimePicker timePicker1 =timerViewInflated.findViewById(R.id.timePicker1);
        timePicker1.setIs24HourView(true);
        timerBuilder.setView(timerViewInflated).setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int id) {

                Calendar currentTime = Calendar.getInstance();
                int diffHour=timePicker1.getHour()-currentTime.get(Calendar.HOUR_OF_DAY);
                int diffMinute=timePicker1.getMinute()-currentTime.get(Calendar.MINUTE);

                if(diffHour<0 || diffMinute<0)
                {
                    Toast.makeText(homeView.getActivity(),"Please enter a valid time",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    setAlarm(diffHour,diffMinute);
                    String timer=timePicker1.getHour()+":"+timePicker1.getMinute();
                    homeView.setData(t,d,id,timer);
                }
            }
        });

        return timerBuilder.create();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setAlarm(int h, int m)
    {
        AlarmManager alarmManager = (AlarmManager) homeView.getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent(homeView.getActivity(), AlarmReceiver.class);
        PendingIntent broadcast = PendingIntent.getBroadcast(homeView.getActivity(), 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, h);
        cal.add(Calendar.MINUTE, m);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
    }
}