package ca.kdunn4781.assignment1;
import ca.kdunn4781.assignment1.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.kdunn4781.assignment1.databinding.ActivityMainBinding;

public class WelcomeScreen extends AppCompatActivity {
    Button create;
    Button load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.welcome_screen);

//    ActivityMainBinding binding
//            = DataBindingUtil.setContentView(this, R.layout.welcome_screen);

   load=findViewById(R.id.btnLoad);
   create= findViewById(R.id.btnCreate);



        create.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent main = new Intent(WelcomeScreen.this,MainActivity.class);
            startActivity(main);
        }
    });

        load.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent main = new Intent(WelcomeScreen.this,ShowSavedTrips.class);
            startActivity(main);
        }
    });
}
}