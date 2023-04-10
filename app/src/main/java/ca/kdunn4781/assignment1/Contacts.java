package ca.kdunn4781.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import ca.kdunn4781.assignment1.output.OutputFragment;

public class Contacts extends AppCompatActivity {

    // Declare variables
    private Button submitContacts;
    private Button phoneContactOne;
    private Button getPhoneContactTwo;
    private Button getPhoneContactThree;

    // Declare variables
    private TextView name1;
    private TextView name2;
    private TextView name3;
    private TextView email1;
    private TextView email2;
    private TextView email3;
    private TextView eventType;
    String date;
    private Boolean doneInsert = false;
    private String nameStatus;
    private String emailStatus;
    private static final int RESULT_PICK_CONTACT = 1;

//    DSConnector database;

    public String person1;
    public String person2;
    public String person3;


    public int addBtn1;
    public int addBtn2;
    public int addBtn3;


    public String stringFromContacts;


    /*
     * FUNCTION      :   onCreate()
     * DESCRIPTION   :   onCreate method.
     * PARAMETERS    :   Bundle savedInstanceState
     * RETURNS       :   void
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        submitContacts = (Button) findViewById(R.id.submitContactsBtn);
        phoneContactOne = (Button) findViewById(R.id.add_phone_Contact_1);
        getPhoneContactTwo = (Button) findViewById(R.id.add_phone_Contact_2);
        getPhoneContactThree = (Button) findViewById(R.id.add_phone_Contact_3);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PackageManager.PERMISSION_GRANTED);

        eventType = (TextView) findViewById(R.id.EventType);

        name1 = (TextView) findViewById(R.id.contactName1);
        name2 = (TextView) findViewById(R.id.contactName2);
        name3 = (TextView) findViewById(R.id.contactName3);
        email1 = (TextView) findViewById(R.id.contactEmail1);
        email2 = (TextView) findViewById(R.id.contactEmail2);
        email3 = (TextView) findViewById(R.id.contactEmail3);

        // Get data from previous page
        Intent intent = getIntent();
        String date_val = intent.getStringExtra("date");


        // Button click event handling
        submitContacts.setOnClickListener(new View.OnClickListener() {
            /*
             * FUNCTION      :   onClick()
             * DESCRIPTION   :   Click handler.
             * PARAMETERS    :   View view   :   The view being clicked
             * RETURNS       :   void
             */
            @Override
            public void onClick(View view) {
                // Start another activity here when the button is clicked.
                // Set navigating and send variables to the summary page
                Intent NextAct = new Intent(Contacts.this, OutputFragment.class);
                NextAct.putExtra("date", date_val);
                NextAct.putExtra("event", eventType.getText().toString());

                NextAct.putExtra("name1", name1.getText().toString());
                NextAct.putExtra("name2", name2.getText().toString());
                NextAct.putExtra("name3", name3.getText().toString());

                NextAct.putExtra("email1", email1.getText().toString());
                NextAct.putExtra("email2", email2.getText().toString());
                NextAct.putExtra("email3", email3.getText().toString());
                //Start the tasks


                //Names of the people entered
                try {
                    person1 = name1.getText().toString();
                    person2 = name2.getText().toString();
                    person3 = name3.getText().toString();



//                    dateForDatabase = date_val.toString();
//                    eventForDatabase = eventType.getText().toString();
                } catch (Exception e) {
                    Toast.makeText(Contacts.this, "Failed to save", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Start an AsyncTask to add an entry to the database
//                AsyncDatabaseInsert dbInserter = new AsyncDatabaseInsert(eventForDatabase, dateForDatabase, person1, person2, person3, Contacts.this);
//                dbInserter.execute();

                startActivity(NextAct);
            }
        });

        ///Phone contact
        //Add from phone contacts
        phoneContactOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkContactPermission()) {
                    //permission granted, pick contact
                    pickContactIntent();
                    addBtn1 = 1;
                } else {
                    //permission not granted, request
                    requestContactPermission();
                }

            }
        });

        getPhoneContactTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkContactPermission()) {
                    //permission granted, pick contact
                    pickContactIntent();
                    addBtn2 = 1;
                } else {
                    //permission not granted, request
                    requestContactPermission();
                }
            }
        });

        getPhoneContactThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkContactPermission()) {
                    //permission granted, pick contact
                    pickContactIntent();
                    addBtn3 = 1;
                } else {
                    //permission not granted, request
                    requestContactPermission();
                }
            }
        });
    }


    private static final int CONTACT_PERMISSION_CODE = 1;
    private static final int CONTACT_PICK_CODE = 2;


    //METHOD    : checkContactPermission
    //PARAMETER : void
    //RETURN    : result
    //DESCRIPTION: This checks to see if we have permission to open the contacts
    private boolean checkContactPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    //METHOD    : requestContactPermission
    //PARAMETER : void
    //RETURN     : void
    //DESCRIPTION: send request to see permissions
    private void requestContactPermission() {
        //permissions to request
        String[] permission = {Manifest.permission.READ_CONTACTS};

        ActivityCompat.requestPermissions(this, permission, CONTACT_PERMISSION_CODE);
    }

    //METHOD    : pickContactIntent
    //PARAMETER  : void
    //RETURN     : void
    //DESCRIPTION: This function opens up the contact screen.
    private void pickContactIntent() {
        //intent to pick contact
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICK_CODE);
    }

    //METHOD     :onRequestPermissionsResult
    //PARAMETER  : int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults
    //RETURN     : void
    //DESCRIPTION: Gives us the options to open the contact screen
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Granted
                pickContactIntent();
            } else {
                Toast.makeText(this, "DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //METHOD    :  onActivityResult
    //PARAMETER  : int requestCode, int resultCode, @Nullable Intent data
    //RETURN     : void
    //DESCRIPTION: Send the name and email address to the text box.
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            //calls when user clicks a contact from list

            if (requestCode == CONTACT_PICK_CODE) {

                Cursor cursor, emailCursor;
                Uri uri = data.getData();
                String email;

                cursor = getContentResolver().query(uri, null, null, null, null);

                emailCursor = getContentResolver().query(uri, null, null, null, null);


                if (cursor.moveToFirst()) {
                    stringFromContacts = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                    String emailAdd = emailCursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));

                    if (addBtn1 == 1) {
                        name1.append((stringFromContacts));
                        emailCursor.moveToFirst();
                        email1.append(emailAdd);

                        addBtn1 = 0;
                    } else if (addBtn2 == 1) {
                        name2.append((stringFromContacts));
                        addBtn2 = 0;
                    } else if (addBtn3 == 1) {
                        name3.append((stringFromContacts));
                        addBtn3 = 0;
                    }

                }


            }
        } else {
            //back btn
        }
    }
}

