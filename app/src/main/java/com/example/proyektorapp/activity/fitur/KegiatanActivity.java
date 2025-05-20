package com.example.proyektorapp.activity.fitur;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyektorapp.R;

public class KegiatanActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout formLayout, container;
    private Button btnTambah, btnSimpan;
    private EditText etKode, etNama, etTempat, etWaktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kegiatan);

        btnBack    = findViewById(R.id.btnBackToHome);
        formLayout = findViewById(R.id.formLayout);
        container  = findViewById(R.id.containerKegiatan);
        btnTambah  = findViewById(R.id.btnTambahKegiatan);
        btnSimpan  = findViewById(R.id.btnSubmitKegiatan);
        etKode     = findViewById(R.id.inputKodePeminjaman);
        etNama     = findViewById(R.id.inputNamaKegiatan);
        etTempat   = findViewById(R.id.inputTempatKegiatan);
        etWaktu    = findViewById(R.id.inputWaktuKegiatan);

        // Back
        btnBack.setOnClickListener(v -> finish());

        // Dummy data
        String[][] data = {
                {"KG001","Pelatihan Android","Lab A","2025-05-12, 08:00-10:00"},
                {"KG002","Rapat Proyek","Ruang Meeting","2025-05-15, 09:00-11:00"}
        };
        for (String[] d : data) {
            addKegiatanItem(d[0], d[1], d[2], d[3]);
        }

        // Tambah → show form
        btnTambah.setOnClickListener(v -> formLayout.setVisibility(View.VISIBLE));

        // Simpan → add item + hide form
        btnSimpan.setOnClickListener(v -> {
            String kode   = etKode.getText().toString().trim();
            String nama   = etNama.getText().toString().trim();
            String tempat = etTempat.getText().toString().trim();
            String waktu  = etWaktu.getText().toString().trim();
            if (kode.isEmpty()||nama.isEmpty()||tempat.isEmpty()||waktu.isEmpty()) {
                Toast.makeText(this,"Lengkapi semua data!",Toast.LENGTH_SHORT).show();
                return;
            }
            addKegiatanItem(kode,nama,tempat,waktu);
            etKode.setText("");
            etNama.setText("");
            etTempat.setText("");
            etWaktu.setText("");
            formLayout.setVisibility(View.GONE);
        });
    }

    private void addKegiatanItem(String kode, String nama, String tempat, String waktu) {
        View item = LayoutInflater.from(this)
                .inflate(R.layout.item_kegiatan, container, false);

        TextView tvKode   = item.findViewById(R.id.tvKodePeminjaman);
        TextView tvNama   = item.findViewById(R.id.tvNamaKegiatan);
        TextView tvTempat = item.findViewById(R.id.tvTempatKegiatan);
        TextView tvWaktu  = item.findViewById(R.id.tvWaktuKegiatan);
        Button btnEdit    = item.findViewById(R.id.btnEdit);
        Button btnDelete  = item.findViewById(R.id.btnDelete);

        tvKode.setText("Kode: " + kode);
        tvNama.setText("Nama: " + nama);
        tvTempat.setText("Tempat: " + tempat);
        tvWaktu.setText("Waktu: " + waktu);

        // Tombol Hapus
        btnDelete.setOnClickListener(v -> container.removeView(item));

        // Tombol Edit
        btnEdit.setOnClickListener(v -> {
            // Isi kembali form dengan data item
            etKode.setText(kode);
            etNama.setText(nama);
            etTempat.setText(tempat);
            etWaktu.setText(waktu);

            // Tampilkan form untuk edit
            formLayout.setVisibility(View.VISIBLE);

            // Hapus item lama (nanti akan diganti dengan versi baru)
            container.removeView(item);
        });

        container.addView(item);
    }

}
