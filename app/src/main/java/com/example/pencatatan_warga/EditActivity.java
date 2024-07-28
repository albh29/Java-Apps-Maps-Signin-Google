package com.example.pencatatan_warga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private WargaDAO wargaDAO;
    private Warga warga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        wargaDAO = new WargaDAO(this);
        wargaDAO.open();

        long wargaId = getIntent().getLongExtra("WARGA_ID", 0);
        warga = wargaDAO.getWarga(wargaId);

        final EditText editNama = findViewById(R.id.editNama);
        final EditText editPekerjaan = findViewById(R.id.editPekerjaan);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnBack = findViewById(R.id.btnBack);

        editNama.setText(warga.getNama());
        editPekerjaan.setText(warga.getPekerjaan());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                warga.setNama(editNama.getText().toString());
                warga.setPekerjaan(editPekerjaan.getText().toString());
                wargaDAO.updateWarga(warga);
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
