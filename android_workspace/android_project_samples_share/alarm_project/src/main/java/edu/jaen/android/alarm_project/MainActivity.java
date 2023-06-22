package edu.jaen.android.alarm_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import edu.jaen.android.alarm_project.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    AlarmManager manager;

    Calendar cal = Calendar.getInstance();
    int year = 0;
    int month = 0;
    int day = 0;
    int hour = 0;
    int min = 0;

    private static String pad(int c) {
        return String.format("%02d", c);
    }

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        binding.reg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int after = Integer.parseInt(binding.elap.getText().toString());

                long now = SystemClock.elapsedRealtime();
                long atTime = now + (after * 1000);

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 101, intent, PendingIntent.FLAG_IMMUTABLE);
                manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, atTime, pendingIntent);
                Toast.makeText(MainActivity.this, after + "초 후 알람등록", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });

        binding.cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 101, intent, PendingIntent.FLAG_IMMUTABLE);
                Toast.makeText(MainActivity.this, "알람 해제", Toast.LENGTH_SHORT).show();
                manager.cancel(pendingIntent);
            }
        });

        binding.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                binding.date.setText(year + "-" + pad(monthOfYear + 1) + "-" + pad(dayOfMonth));
                                MainActivity.this.year = year;
                                MainActivity.this.month = monthOfYear;
                                MainActivity.this.day = dayOfMonth;
                            }
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        binding.time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(
                        MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                binding.time.setText(pad(hourOfDay) + ":" + pad(minute));
                                MainActivity.this.hour = hourOfDay;
                                MainActivity.this.min = minute;
                            }
                        },
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE),
                        true
                ).show();
            }
        });

        binding.reg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.this.day == 0 || MainActivity.this.min == 0){
                    Toast.makeText(MainActivity.this, "날짜와 시간을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                cal.set(Calendar.YEAR, MainActivity.this.year);
                cal.set(Calendar.MONTH, MainActivity.this.month);
                cal.set(Calendar.DAY_OF_MONTH, MainActivity.this.day);
                cal.set(Calendar.HOUR_OF_DAY, MainActivity.this.hour);
                cal.set(Calendar.MINUTE, MainActivity.this.min);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long atTime = cal.getTimeInMillis();

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 102, intent, PendingIntent.FLAG_IMMUTABLE);
                manager.set(AlarmManager.RTC_WAKEUP, atTime, pendingIntent);
                Toast.makeText(MainActivity.this, "알람등록", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });

        binding.cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 102, intent, PendingIntent.FLAG_IMMUTABLE);
                Toast.makeText(MainActivity.this, "알람 해제", Toast.LENGTH_SHORT).show();
                manager.cancel(pendingIntent);
            }
        });
    }
}
