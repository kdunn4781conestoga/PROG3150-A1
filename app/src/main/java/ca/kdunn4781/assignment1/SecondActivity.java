package ca.kdunn4781.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import ca.kdunn4781.assignment1.databinding.ActivityMainBinding;
import ca.kdunn4781.assignment1.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ActivitySecondBinding binding
                = DataBindingUtil.setContentView(this, R.layout.activity_second);
        TextView startDate = binding.startDateTv;
        Button pick = binding.startDateBtn;
        TextView endDate = binding.endDateTv;
        Button pick2 = binding.endDateBtn;

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