package com.example.proyektorapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class PenanggungJawabActivity extends AppCompatActivity {

    private ImageView btnBackToHome;
    private LinearLayout formLayout, containerPJ;
    private Button btnTambahPJ, btnSubmitPJ;
    private EditText inputNama, inputNIK, inputNoHP;

    private View itemBeingEdited = null; // View yang sedang diedit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penanggung_jawab);

        btnBackToHome = findViewById(R.id.btnBackToHome);
        formLayout    = findViewById(R.id.formLayout);
        containerPJ   = findViewById(R.id.containerPJ);
        btnTambahPJ   = findViewById(R.id.btnTambahPJ);
        btnSubmitPJ   = findViewById(R.id.btnSubmitPJ);
        inputNama     = findViewById(R.id.inputNamaPJ);
        inputNIK      = findViewById(R.id.inputNIKPJ);
        inputNoHP     = findViewById(R.id.inputNoHPPJ);

        btnBackToHome.setOnClickListener(v -> finish());

        String[][] dataPJ = {
                {"Andi Saputra", "1234567890", "081234567890"},
                {"Rina Lestari", "9876543210", "085612345678"}
        };
        for (String[] pj : dataPJ) {
            addPJItem(pj[0], pj[1], pj[2]);
        }

        btnTambahPJ.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            itemBeingEdited = null; // mode tambah, bukan edit
            inputNama.setText("");
            inputNIK.setText("");
            inputNoHP.setText("");
        });

        btnSubmitPJ.setOnClickListener(v -> {
            String nama = inputNama.getText().toString().trim();
            String nik  = inputNIK.getText().toString().trim();
            String hp   = inputNoHP.getText().toString().trim();

            if (nama.isEmpty() || nik.isEmpty() || hp.isEmpty()) {
                Toast.makeText(this, "Semua kolom wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (itemBeingEdited != null) {
                // mode edit: update isi dari item
                TextView tvNama = itemBeingEdited.findViewById(R.id.tvNamaPJ);
                TextView tvNIK  = itemBeingEdited.findViewById(R.id.tvNIKPJ);
                TextView tvHP   = itemBeingEdited.findViewById(R.id.tvNoHP);

                tvNama.setText("Nama: " + nama);
                tvNIK.setText("NIK: " + nik);
                tvHP.setText("No. HP: " + hp);

                Toast.makeText(this, "Data diperbarui", Toast.LENGTH_SHORT).show();
                itemBeingEdited = null;
            } else {
                // mode tambah
                addPJItem(nama, nik, hp);
            }

            inputNama.setText("");
            inputNIK.setText("");
            inputNoHP.setText("");
            formLayout.setVisibility(View.GONE);
        });
    }

    private void addPJItem(String nama, String nik, String hp) {
        View item = LayoutInflater.from(this)
                .inflate(R.layout.item_penanggung_jawab, containerPJ, false);

        TextView tvNama = item.findViewById(R.id.tvNamaPJ);
        TextView tvNIK  = item.findViewById(R.id.tvNIKPJ);
        TextView tvHP   = item.findViewById(R.id.tvNoHP);

        Button btnEdit   = item.findViewById(R.id.btnEdit);
        Button btnDelete = item.findViewById(R.id.btnDelete);

        tvNama.setText("Nama: " + nama);
        tvNIK.setText("NIK: " + nik);
        tvHP.setText("No. HP: " + hp);

        btnDelete.setOnClickListener(v -> {
            containerPJ.removeView(item);
            Toast.makeText(this, "Data dihapus", Toast.LENGTH_SHORT).show();
        });

        btnEdit.setOnClickListener(v -> {
            inputNama.setText(nama);
            inputNIK.setText(nik);
            inputNoHP.setText(hp);
            formLayout.setVisibility(View.VISIBLE);
            itemBeingEdited = item; // simpan item yang sedang diedit
        });

        containerPJ.addView(item);
    }
}
