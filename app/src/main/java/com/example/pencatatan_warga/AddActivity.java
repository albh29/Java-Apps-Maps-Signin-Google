package com.example.pencatatan_warga;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    private WargaDAO wargaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        wargaDAO = new WargaDAO(this);
        wargaDAO.open();

        final EditText editNama = findViewById(R.id.editNama);
        final EditText editPekerjaan = findViewById(R.id.editPekerjaan);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnBack = findViewById(R.id.btnBack);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = editNama.getText().toString();
                String pekerjaan = editPekerjaan.getText().toString();
                wargaDAO.createWarga(nama, pekerjaan);
                finish();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onPause() {
        wargaDAO.close();
        super.onPause();
    }

    @Override
    protected void onResume() {
        wargaDAO.open();
        super.onResume();
    }
}
