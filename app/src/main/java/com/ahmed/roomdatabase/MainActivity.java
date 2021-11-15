 package com.ahmed.roomdatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ahmed.roomdatabase.adapter.RecyclerViewAdapter;
import com.ahmed.roomdatabase.model.Contact;
import com.ahmed.roomdatabase.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

 public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {

     private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
     private static final String TAG = "Clicked";
     public static final String CONTACT_ID = "contact_id";
     private ContactViewModel contactViewModel;
     private TextView textView;
     private RecyclerView recyclerView;
     private RecyclerViewAdapter recyclerViewAdapter;
     private LiveData<List<Contact>> contactList;

     @Override
     protected void onCreate(Bundle savedInstanceState) {

         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         recyclerView = findViewById(R.id.recycler_view);

         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));


         contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
                 .getApplication())
                 .create(ContactViewModel.class);

         contactViewModel.getAllContacts().observe(this, contacts -> {

             recyclerViewAdapter = new RecyclerViewAdapter(contacts, MainActivity.this, this);
             recyclerView.setAdapter(recyclerViewAdapter);


         });


         FloatingActionButton fab = findViewById(R.id.add_contact_fab);
         fab.setOnClickListener(view -> {
             Intent intent = new Intent(MainActivity.this, NewContact.class);
             startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
         });

     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
             assert data != null;
             String name = data.getStringExtra(NewContact.NAME_REPLY);
             String occupation = data.getStringExtra(NewContact.OCCUPATION);

             assert name != null;
             Contact contact = new Contact(name, occupation);

             ContactViewModel.insert(contact);
         }
     }

     @Override
     public void onContactClick(int position) {
         Contact contact = Objects.requireNonNull(contactViewModel.allContacts.getValue()).get(position);
         Log.d(TAG, "onContactClick: " + contact.getId());

         Intent intent = new Intent(MainActivity.this, NewContact.class);
         intent.putExtra(CONTACT_ID, contact.getId());
         startActivity(intent);


     }

 }
