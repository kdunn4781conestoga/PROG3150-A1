package ca.kdunn4781.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.kdunn4781.assignment1.databinding.ActivityMainBinding;

public class ShowSavedTrips extends AppCompatActivity {
    Button back;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_trips);

//    ActivityMainBinding binding
//            = DataBindingUtil.setContentView(this, R.layout.);
      back=findViewById(R.id.btbBack);
      delete=findViewById(R.id.btnDelete);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wc = new Intent(ShowSavedTrips.this,WelcomeScreen.class);
                startActivity(wc);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need to delete from DB
            }
        });
    }
}