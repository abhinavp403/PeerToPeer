package com.example.abhinav.peertopeer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    ListView lv;
    ListAdapter adapter;
    public static final String TAG = "LOG WINDOW";
    Button button;
    EditText Name;
    TextView BestMatch;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Object> NamesList = new ArrayList<>();
    String keySchool;
    ArrayList<Object> keyStrength = new ArrayList<>();
    ArrayList<Object> keyWeakness= new ArrayList<>();
    ArrayList<Object> searchStrength = new ArrayList<>();
    ArrayList<Object> searchWeakness= new ArrayList<>();
    String storeStrength[]= new String[15];
    String storeWeakness[]= new String[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = findViewById(R.id.listview);
        lv.setOnItemClickListener(this);
        Name = findViewById(R.id.sname);
        button = findViewById(R.id.sbutton);
        BestMatch = findViewById(R.id.bestmatch);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final String keyName= Name.getText().toString();
                BestMatch.setVisibility(View.VISIBLE);
                lv.setVisibility(View.VISIBLE);

                //Get searched user's details
                DocumentReference docRef = db.collection("users").document(keyName);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            keyStrength= (ArrayList<Object>) documentSnapshot.get("strength");
                            keyWeakness= (ArrayList<Object>) documentSnapshot.get("weakness");
                            keySchool= documentSnapshot.getString("school");
                            //keyStrength= documentSnapshot.getString("strength");
                            //keyWeakness= documentSnapshot.getString("weakness");
                            Log.d(TAG, "Success in Retrieval");
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                });

                //Get all users' details and match strength to weakness and weakness to strength. Display Names
                db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        NamesList.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                searchStrength= (ArrayList<Object>) document.get("strength");
                                searchWeakness= (ArrayList<Object>) document.get("weakness");
                                for(int j=0; j<searchWeakness.size(); j++) {
                                    if(keyStrength.contains(searchWeakness.get(j)))
                                        storeStrength[j]= (String) searchWeakness.get(j);
                                    else
                                        storeStrength[j]= null;
                                }
                                for(int i=0; i<searchStrength.size(); i++) {
                                    if(keyWeakness.contains(searchStrength.get(i)))
                                        storeWeakness[i]= (String) searchStrength.get(i);
                                    else
                                        storeWeakness[i]= null;
                                }
                                if (!isEmptyStringArray(storeWeakness) && !isEmptyStringArray(storeStrength)) {
                                    NamesList.add(document.getString("name"));
                                }
                                /*else {
                                    adapter = new ArrayAdapter<>(SearchActivity.this, R.layout.customlistview,  Collections.singletonList("No Results"));
                                    lv.setAdapter(adapter);
                                }*/
                            }
                            adapter = new ArrayAdapter<>(SearchActivity.this, R.layout.customlistview, NamesList);
                            lv.setAdapter(adapter);
                            Log.d(TAG, "Success in Showing");
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
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

    public boolean isEmptyStringArray(String [] array){
        for(int i=0; i<array.length; i++){
            if(array[i]!=null)
                return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new ArrayAdapter(this, R.layout.customlistview);
        lv.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        AlertDialog.Builder box= new AlertDialog.Builder(this);
        view= getLayoutInflater().inflate(R.layout.activity_dialog_box, null);
        Button Send= view.findViewById(R.id.send);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SearchActivity.this, "Message Sent", Toast.LENGTH_SHORT);
            }
        });
        box.setView(view);
        AlertDialog dialog= box.create();
        dialog.show();
    }
}