package com.example.pencatatan_warga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private WargaDAO wargaDAO;
    private ListView listView;
    private ArrayAdapter<Warga> adapter;
    private FirebaseAuth auth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Initialize GoogleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize UI elements
        listView = findViewById(R.id.listView);
        MaterialButton signOutWarga = findViewById(R.id.signout_warga);

        // Set up sign out button
        signOutWarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        auth.signOut();
                        Toast.makeText(ListActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                        navigateToLogin();
                    }
                }).addOnFailureListener(e -> {
                    Log.e("SignOutError", "Failed to sign out from Google: " + e.getMessage());
                    Toast.makeText(ListActivity.this, "Failed to sign out", Toast.LENGTH_SHORT).show();
                });
            }
        });

        showWargaList();
    }

    private void showWargaList() {
        wargaDAO = new WargaDAO(this);
        wargaDAO.open();

        List<Warga> values = wargaDAO.getAllWarga();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Warga warga = (Warga) listView.getItemAtPosition(position);
                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                intent.putExtra("WARGA_ID", warga.getId());
                startActivity(intent);
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(ListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (auth.getCurrentUser() != null) {
            wargaDAO.open();
            adapter.clear();
            adapter.addAll(wargaDAO.getAllWarga());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {
        if (wargaDAO != null) {
            wargaDAO.close();
        }
        super.onPause();
    }
}
