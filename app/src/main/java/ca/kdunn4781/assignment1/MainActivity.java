package ca.kdunn4781.assignment1;import androidx.appcompat.app.AppCompatActivity;import androidx.databinding.DataBindingUtil;import android.os.Bundle;import android.view.View;import android.widget.Button;import android.widget.TextView;import org.w3c.dom.Text;import ca.kdunn4781.assignment1.databinding.ActivityMainBinding;public class MainActivity extends AppCompatActivity {    private ActivityMainBinding binding;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        ActivityMainBinding binding                = DataBindingUtil.setContentView(this, R.layout.activity_main);        TextView howMany_btn = binding.howMany_btn;        Button minus_btn = binding.minus_btn;        Button plus_btn = binding.plus_btn;        minus_btn.setOnClickListener(new View.OnClickListener(){            @Override            public void onClick(View v){                String s = howMany_btn.getText().toString();                int  num = Integer.parseInt(s);                num += 1;                String s1= String.valueOf(num);                howMany_btn.setText(s);            }        });    }}