package ca.kdunn4781.assignment1;import androidx.appcompat.app.AppCompatActivity;import androidx.databinding.DataBindingUtil;import android.app.DatePickerDialog;import android.os.Bundle;import android.view.View;import android.widget.AdapterView;import android.widget.ArrayAdapter;import android.widget.Button;import android.widget.DatePicker;import android.widget.Spinner;import android.widget.TextView;import ca.kdunn4781.assignment1.databinding.ActivityMainBinding;import android.content.Intent;import android.widget.Toast;import java.util.Calendar;public class MainActivity extends AppCompatActivity {    //private ActivityMainBinding binding;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        ActivityMainBinding binding                = DataBindingUtil.setContentView(this, R.layout.activity_main);         TextView howMany_adult = binding.adultCount.howManyTv;         Button minus = binding.adultCount.minusBtn;         Button plus = binding.adultCount.plusBtn;        TextView howMany_children = binding.childrenCount.howManyTv;        Button minus2 = binding.childrenCount.minusBtn;        Button plus2 = binding.childrenCount.plusBtn;        TextView startDate = binding.startDatePicker.lblDate;        Button pick = binding.startDatePicker.btnPickDate;        TextView endDate = binding.endDatePicker.lblDate;        Button pick2 = binding.endDatePicker.btnPickDate;        Spinner from = binding.fromLocationSpinner;        Spinner to = binding.toLocationSpinner;        Button next = binding.nextBtn;        ArrayAdapter<CharSequence> adapter=                ArrayAdapter.createFromResource(this,R.array.locationsSpinner, android.R.layout.simple_spinner_item);        adapter.setDropDownViewResource(                android.R.layout.simple_spinner_dropdown_item);        from.setAdapter(adapter);        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {            @Override            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {                adapter.getItem(position);            }            @Override            public void onNothingSelected(AdapterView<?> parent) {            }        });        to.setAdapter(adapter);        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {            @Override            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {                    adapter.getItem(position);            }            @Override            public void onNothingSelected(AdapterView<?> parent) {            }        });        // Button next = binding.nextBtn;        plus.setOnClickListener(new View.OnClickListener(){            @Override            public void onClick(View v){                String s = howMany_adult.getText().toString();                int  num = Integer.valueOf(s);                num = num + 1;                String s1= String.valueOf(num);                howMany_adult.setText(s1);            }        });        minus.setOnClickListener(new View.OnClickListener(){            @Override            public void onClick(View v){                String s = howMany_adult.getText().toString();                int  num = Integer.valueOf(s);                if(num > 1) {                    num = num - 1;                }                String s1= String.valueOf(num);                howMany_adult.setText(s1);            }        });        plus2.setOnClickListener(new View.OnClickListener(){            @Override            public void onClick(View v){                String s = howMany_children.getText().toString();                int  num = Integer.valueOf(s);                num = num + 1;                String s1= String.valueOf(num);                howMany_children.setText(s1);            }        });        minus2.setOnClickListener(new View.OnClickListener(){            @Override            public void onClick(View v){                String s = howMany_children.getText().toString();                int  num = Integer.valueOf(s);                if(num > 1) {                    num = num - 1;                }                String s1= String.valueOf(num);                howMany_children.setText(s1);            }        });        pick.setOnClickListener(new View.OnClickListener(){            @Override            public void onClick(View v) {                setDate(startDate);            }        });        pick2.setOnClickListener(new View.OnClickListener(){            @Override            public void onClick(View v) {                setDate(endDate);            }        });    }    private void setDate(TextView x) {        Calendar calender = Calendar.getInstance();        int year = calender.get(Calendar.YEAR);        int month = calender.get(Calendar.MONTH);        int date = calender.get(Calendar.DATE);        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {            @Override            public void onDateSet(DatePicker view, int year, int month, int date) {                String str = year + "/" + month + "/" + date;                x.setText(str);            }        }, year, month, date);        datePickerDialog.show();    }//        next.setOnClickListener(v -> {//            // get the value which input by user in EditText and convert it to string//            String str = howMany.getText().toString();//            // Create the Intent object of this class Context() to Second_activity class//            Intent intent = new Intent(getApplicationContext(), SecondActivity.class);//            // now by putExtra method put the value in key, value pair key is//            // message_key by this key we will receive the value, and put the string//            intent.putExtra("message_key", str);//            // start the Intent//            startActivity(intent);//        });}