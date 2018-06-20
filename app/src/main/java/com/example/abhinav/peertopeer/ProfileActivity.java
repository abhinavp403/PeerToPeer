package com.example.abhinav.peertopeer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "LOG WINDOW";
    Button button, button2;
    EditText Name, Email, Phone, School, Strength, Weakness;
    ArrayList<String> StrengthList = new ArrayList<>();
    ArrayList<String> WeaknessList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Integer> Physics = new HashMap<>();
    Map<String, Integer> Chemistry = new HashMap<>();
    Map<String, Integer> Biology = new HashMap<>();
    //DocumentReference docRef= FirebaseFirestore.getInstance().document("users/sample");
    //DocumentReference docRef= FirebaseFirestore.getInstance().collection("users").document();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*FirebaseApp.initializeApp(this);
        //final FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setTimestampsInSnapshotsEnabled(true).build();
        db.setFirestoreSettings(settings);*/
        Name = findViewById(R.id.firstname);
        Phone = findViewById(R.id.phone);
        School = findViewById(R.id.institute);
        Strength = findViewById(R.id.strength);
        Weakness = findViewById(R.id.weakness);
        Email = findViewById(R.id.email);
        button = findViewById(R.id.button);
        button2 = findViewById(R.id.end);

        //click on Submit button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String name = Name.getText().toString();
                String phone = Phone.getText().toString();
                String email = Email.getText().toString();
                String school = School.getText().toString();
                String strength = Strength.getText().toString();
                String weakness = Weakness.getText().toString();

                Physics.put("Light-Reflection and Refraction", (int) (11*Math.random()));
                Physics.put("Human Eye and Colorful World", (int) (11*Math.random()));
                Physics.put("Electricity", (int) (11*Math.random()));
                Physics.put("Magnetic Effects of Electric Current", (int) (11*Math.random()));
                Physics.put("Sources of Energy", (int) (11*Math.random()));
                Physics.put("Management of Natural Resources", (int) (11*Math.random()));
                Chemistry.put("Chemical Reactions and Equations", (int) (11*Math.random()));
                Chemistry.put("Acids, Bases and Salts", (int) (11*Math.random()));
                Chemistry.put("Metals and Non-Metals", (int) (11*Math.random()));
                Chemistry.put("Carbon and its Compounds", (int) (11*Math.random()));
                Chemistry.put("Periodic Classification of Elements", (int) (11*Math.random()));
                Chemistry.put("Our Environment", (int) (11*Math.random()));
                Biology.put("Life Processes", (int) (11*Math.random()));
                Biology.put("Control and Coordination", (int) (11*Math.random()));
                Biology.put("How do Organisms", (int) (11*Math.random()));
                Biology.put("Heredity and Evolution", (int) (11*Math.random()));

                for (String key : Physics.keySet()) {
                    Integer value = Physics.get(key);
                    if (value <= 5)
                        WeaknessList.add(key);
                    else if(value > 5 && value <= 10)
                        StrengthList.add(key);
                }
                for (String key : Chemistry.keySet()) {
                    Integer value = Chemistry.get(key);
                    if (value <= 5)
                        WeaknessList.add(key);
                    else if(value > 5 && value <= 10)
                        StrengthList.add(key);
                }
                for (String key : Biology.keySet()) {
                    Integer value = Biology.get(key);
                    if (value <= 5)
                        WeaknessList.add(key);
                    else if(value > 5 && value <= 10)
                        StrengthList.add(key);
                }
                if (name.length() == 0) {
                    Name.setError("Name not entered");
                    Name.requestFocus();
                }
                if (!email.contains("@") || !email.contains(".com")) {
                    Email.setError("Incorrect Email ID");
                    Email.requestFocus();
                }
                if (phone.length() != 10) {
                    Phone.setError("Phone Number must be 10 digits");
                    Phone.requestFocus();
                }
                if (school.length() == 0) {
                    School.setError("School not entered");
                    School.requestFocus();
                }
                if (strength.length() == 0) {
                    Strength.setError("Subject not entered");
                    Strength.requestFocus();
                }
                if (weakness.length() == 0) {
                    Weakness.setError("Subject not entered");
                    Weakness.requestFocus();
                }
                if (name.length() > 0 && school.length() > 0 && strength.length() > 0 && weakness.length() > 0 && email.contains("@") && email.contains(".com") && phone.length() == 10) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("name", name);
                    user.put("email", email);
                    user.put("phone", phone);
                    user.put("school", school);
                    user.put("strength", StrengthList);
                    user.put("weakness", WeaknessList);
                    //user.put("strength", strength);
                    //user.put("weakness", weakness);
                    db.collection("users").document(name)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void a) {
                                    Log.d(TAG, "Success with Adding");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                }
                            });
                }
                Name.setText("");
                Phone.setText("");
                Email.setText("");
                School.setText("");
                Strength.setText("");
                Weakness.setText("");
                StrengthList.clear();
                WeaknessList.clear();
            }
        });

        //click on done button
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(ProfileActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}