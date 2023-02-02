package ca.kdunn4781.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.TextView;

import ca.kdunn4781.assignment1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding binding
                = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }
}


