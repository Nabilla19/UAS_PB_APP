package com.example.proyektorapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class TransaksiActivity extends AppCompatActivity {

    private ImageView btnBackToHome;
    private LinearLayout formLayout, containerTransaksi;
    private Button btnTambahTransaksi, btnSubmitTransaksi;
    private EditText inputKodeTransaksi, inputKodeProyektor, inputNIK;
    private Spinner inputStatus;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        btnBackToHome        = findViewById(R.id.btnBackToHome);
        formLayout           = findViewById(R.id.formLayout);
        containerTransaksi   = findViewById(R.id.containerTransaksi);
        btnTambahTransaksi   = findViewById(R.id.btnTambahTransaksi);
        btnSubmitTransaksi   = findViewById(R.id.btnSubmitTransaksi);
        inputKodeTransaksi   = findViewById(R.id.inputKodeTransaksi);
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

        // Contoh data transaksi (bisa kamu ganti dengan data asli)
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

        // Hapus dulu semua transaksi lama (jika ada)
        containerTransaksi.removeAllViews();

        // Tampilkan transaksi hasil filter
        for (String[] d : filteredList) {
            addTransaksiItem(d[0], d[1], d[2], d[3]);
        }

        btnTambahTransaksi.setOnClickListener(v -> formLayout.setVisibility(View.VISIBLE));

        btnSubmitTransaksi.setOnClickListener(v -> {
            String kode  = inputKodeTransaksi.getText().toString().trim();
            String proy  = inputKodeProyektor.getText().toString().trim();
            String nikInput   = inputNIK.getText().toString().trim();
            String stat  = inputStatus.getSelectedItem().toString();

            if (kode.isEmpty() || proy.isEmpty() || nikInput.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            addTransaksiItem(kode, proy, nikInput, stat);

            inputKodeTransaksi.setText("");
            inputKodeProyektor.setText("");
            inputNIK.setText("");
            inputStatus.setSelection(0);
            formLayout.setVisibility(View.GONE);
        });
    }

    private void addTransaksiItem(String kode, String proyektor, String nik, String status) {
        View item = LayoutInflater.from(this)
                .inflate(R.layout.item_transaksi, containerTransaksi, false);

        TextView tvKode = item.findViewById(R.id.tvKodeTransaksi);
        TextView tvProy = item.findViewById(R.id.tvKodeProyektor);
        TextView tvNIK  = item.findViewById(R.id.tvNIK);
        TextView tvStat = item.findViewById(R.id.tvStatus);

        tvKode.setText("Kode Transaksi: " + kode);
        tvProy.setText("Kode Proyektor: " + proyektor);
        tvNIK.setText("NIK: " + nik);
        tvStat.setText("Status: " + status);

        tvStat.setTextColor(Color.parseColor(
                "Sudah dikembalikan".equalsIgnoreCase(status) ? "#388E3C" : "#FBC02D"
        ));

        containerTransaksi.addView(item);
    }
}
