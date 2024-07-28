package com.example.pencatatan_warga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private WargaDAO wargaDAO;
    private Warga warga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        wargaDAO = new WargaDAO(this);
        wargaDAO.open();

        long wargaId = getIntent().getLongExtra("WARGA_ID", 0);
        warga = wargaDAO.getWarga(wargaId);

        TextView nama = findViewById(R.id.nama);
        TextView pekerjaan = findViewById(R.id.pekerjaan);
        Button btnEdit = findViewById(R.id.btnEdit);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnBack = findViewById(R.id.btnBack);

        nama.setText(warga.getNama());
        pekerjaan.setText(warga.getPekerjaan());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, EditActivity.class);
                intent.putExtra("WARGA_ID", warga.getId());
                startActivity(intent);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wargaDAO.deleteWarga(warga);
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

