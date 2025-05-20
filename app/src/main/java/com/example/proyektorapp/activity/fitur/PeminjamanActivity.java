package com.example.proyektorapp.activity.fitur;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyektorapp.R;

import java.util.ArrayList;
import java.util.List;

public class PeminjamanActivity extends AppCompatActivity {

    private ImageView btnBackToHome;
    private LinearLayout formLayout, containerPeminjaman;
    private Button btnTambahPeminjaman, btnSubmitPeminjaman;
    private EditText inputKodePeminjaman, inputKodeProyektor, inputNIK;
    private Spinner inputStatus;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peminjaman);

        btnBackToHome        = findViewById(R.id.btnBackToHome);
        formLayout           = findViewById(R.id.formLayout);
        containerPeminjaman   = findViewById(R.id.containerPeminjaman);
        btnTambahPeminjaman   = findViewById(R.id.btnTambahPeminjaman);
        btnSubmitPeminjaman   = findViewById(R.id.btnSubmitPeminjaman);
        inputKodePeminjaman   = findViewById(R.id.inputKodePeminjaman);
        inputKodeProyektor   = findViewById(R.id.inputKodeProyektor);
        inputNIK             = findViewById(R.id.inputNIK);
        inputStatus          = findViewById(R.id.inputStatus);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"Sudah dikembalikan", "Belum dikembalikan"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputStatus.setAdapter(adapter);

        btnBackToHome.setOnClickListener(v -> finish());

        // Contoh data Peminjaman (bisa kamu ganti dengan data asli)
        String[][] data = {
                {"TX001", "PJ001", "1234567890", "Sudah dikembalikan"},
                {"TX002", "PJ002", "9876543210", "Belum dikembalikan"},
                {"TX003", "PJ003", "1122334455", "Belum dikembalikan"},
                {"TX004", "PJ004", "2233445566", "Sudah dikembalikan"}
        };

        // Ambil data filter dari Intent (kalau ada)
        Intent intent = getIntent();
        String kodeInfokus = intent.getStringExtra("filter_kodeInfokus");
        String kodeTransaksi = intent.getStringExtra("filter_kodeTransaksi");
        String nik = intent.getStringExtra("filter_nik");
        String status = intent.getStringExtra("filter_status");

        // Filter data berdasarkan parameter filter
        List<String[]> filteredList = new ArrayList<>();

        for (String[] row : data) {
            boolean cocok = true;

            if (kodeTransaksi != null && !kodeTransaksi.isEmpty() && !row[0].toLowerCase().contains(kodeTransaksi.toLowerCase())) {
                cocok = false;
            }

            if (kodeInfokus != null && !kodeInfokus.isEmpty() && !row[1].toLowerCase().contains(kodeInfokus.toLowerCase())) {
                cocok = false;
            }

            if (nik != null && !nik.isEmpty() && !row[2].contains(nik)) {
                cocok = false;
            }

            if (status != null && !status.equals("Semua") && !row[3].equalsIgnoreCase(status)) {
                cocok = false;
            }

            if (cocok) {
                filteredList.add(row);
            }
        }

        // Hapus dulu semua Peminjaman lama (jika ada)
        containerPeminjaman.removeAllViews();

        // Tampilkan Peminjaman hasil filter
        for (String[] d : filteredList) {
            addPeminjamanItem(d[0], d[1], d[2], d[3]);
        }

        btnTambahPeminjaman.setOnClickListener(v -> formLayout.setVisibility(View.VISIBLE));

        btnSubmitPeminjaman.setOnClickListener(v -> {
            String kode  = inputKodePeminjaman.getText().toString().trim();
            String proy  = inputKodeProyektor.getText().toString().trim();
            String nikInput   = inputNIK.getText().toString().trim();
            String stat  = inputStatus.getSelectedItem().toString();

            if (kode.isEmpty() || proy.isEmpty() || nikInput.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            addPeminjamanItem(kode, proy, nikInput, stat);

            inputKodePeminjaman.setText("");
            inputKodeProyektor.setText("");
            inputNIK.setText("");
            inputStatus.setSelection(0);
            formLayout.setVisibility(View.GONE);
        });
    }

    private void addPeminjamanItem(String kode, String proyektor, String nik, String status) {
        View item = LayoutInflater.from(this)
                .inflate(R.layout.item_peminjaman, containerPeminjaman, false);

        TextView tvKode = item.findViewById(R.id.tvKodePeminjaman);
        TextView tvProy = item.findViewById(R.id.tvKodeProyektor);
        TextView tvNIK  = item.findViewById(R.id.tvNIK);
        TextView tvStat = item.findViewById(R.id.tvStatus);

        tvKode.setText("Kode Peminjaman: " + kode);
        tvProy.setText("Kode Proyektor: " + proyektor);
        tvNIK.setText("NIK: " + nik);
        tvStat.setText("Status: " + status);

        tvStat.setTextColor(Color.parseColor(
                "Sudah dikembalikan".equalsIgnoreCase(status) ? "#388E3C" : "#FBC02D"
        ));

        containerPeminjaman.addView(item);
    }
}
