package com.example.proyektorapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class ProyektorActivity extends AppCompatActivity {

    EditText inputKode, inputMerk, inputNoSeri;
    Spinner inputStatus;
    Button btnTambah, btnSimpan;
    LinearLayout formLayout, containerProyektor;
    ImageView btnBackToHome;   // Tambahkan ini

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyektor);


        // Inisialisasi semua view
        inputKode = findViewById(R.id.inputKode);
        inputMerk = findViewById(R.id.inputMerk);
        inputNoSeri = findViewById(R.id.inputNoSeri);
        inputStatus = findViewById(R.id.inputStatus);
        btnTambah = findViewById(R.id.btnTambahProyektor);
        btnSimpan = findViewById(R.id.btnSubmitProyektor);
        formLayout = findViewById(R.id.formLayout);
        containerProyektor = findViewById(R.id.containerProyektor);
        btnBackToHome = findViewById(R.id.btnBackToHome);  // Inisialisasi tombol back

        // Setup Spinner Status
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Tersedia", "Dipinjam", "Rusak"});
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputStatus.setAdapter(statusAdapter);

        // Tombol Tambah Proyektor -> tampilkan form
        btnTambah.setOnClickListener(v -> formLayout.setVisibility(View.VISIBLE));

        // Tombol Simpan -> tambah item ke containerProyektor
        btnSimpan.setOnClickListener(v -> {
            String kode = inputKode.getText().toString().trim();
            String merk = inputMerk.getText().toString().trim();
            String noSeri = inputNoSeri.getText().toString().trim();
            String status = inputStatus.getSelectedItem().toString();

            if (kode.isEmpty() || merk.isEmpty() || noSeri.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            View itemView = LayoutInflater.from(this).inflate(R.layout.item_proyektor, containerProyektor, false);
            TextView tvKode = itemView.findViewById(R.id.tvKode);
            TextView tvMerk = itemView.findViewById(R.id.tvMerk);
            TextView tvNoSeri = itemView.findViewById(R.id.tvNoSeri);
            TextView tvStatus = itemView.findViewById(R.id.tvStatus);
            Button btnEdit = itemView.findViewById(R.id.btnEdit);
            Button btnDelete = itemView.findViewById(R.id.btnDelete);

            tvKode.setText("Kode: " + kode);
            tvMerk.setText("Merk: " + merk);
            tvNoSeri.setText("No Seri: " + noSeri);
            tvStatus.setText("Status: " + status);

            // Hapus item
            btnDelete.setOnClickListener(view -> containerProyektor.removeView(itemView));

            // Edit item
            btnEdit.setOnClickListener(view -> {
                // Set data ke form
                inputKode.setText(kode);
                inputMerk.setText(merk);
                inputNoSeri.setText(noSeri);
                inputStatus.setSelection(statusAdapter.getPosition(status));

                // Tampilkan form
                formLayout.setVisibility(View.VISIBLE);

                // Hapus item lama
                containerProyektor.removeView(itemView);
            });

            containerProyektor.addView(itemView);

            // Reset form
            inputKode.setText("");
            inputMerk.setText("");
            inputNoSeri.setText("");
            inputStatus.setSelection(0);
            formLayout.setVisibility(View.GONE);
        });

        // Tombol Back ke Home -> finish activity ini
        btnBackToHome.setOnClickListener(v -> finish());
    }
}
