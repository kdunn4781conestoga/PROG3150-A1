package ca.kdunn4781.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import ca.kdunn4781.assignment1.databinding.ActivitySecondBinding;
import ca.kdunn4781.assignment1.location.LocationActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ActivitySecondBinding binding
                = DataBindingUtil.setContentView(this, R.layout.activity_second);

        TextView startDate = binding.startDatePicker.lblDate;
        Button pick = binding.startDatePicker.btnPickDate;
        TextView endDate = binding.endDatePicker.lblDate;
        Button pick2 = binding.endDatePicker.btnPickDate;
        Button next2 = binding.next2Btn;

        pick.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setDate(startDate);
            }
         });

        pick2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                setDate(endDate);
            }
        });

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the Intent object of this class Context() to Second_activity class
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                // now by putExtra method put the value in key, value pair key is
                // message_key by this key we will receive the value, and put the string
                intent.putExtra("startDate", startDate.getText().toString());
                intent.putExtra("startLocation", "Waterloo");
                intent.putExtra("endLocation", "Stratford");
                // start the Intent
                startActivity(intent);
            }
        });
    }

        private void setDate(TextView x){
            Calendar calender = Calendar.getInstance();
            int year = calender.get(Calendar.YEAR);
            int month = calender.get(Calendar.MONTH);
            int date = calender.get(Calendar.DATE);

            DatePickerDialog datePickerDialog= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int date) {
                    String str = year + "/" + month + "/" + date;
                    x.setText(str);
                }
            },year,month,date);

            datePickerDialog.show();

        }


}