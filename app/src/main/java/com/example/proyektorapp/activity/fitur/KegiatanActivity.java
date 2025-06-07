package com.example.proyektorapp.activity.fitur;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyektorapp.R;
import com.example.proyektorapp.api.ApiClient;
import com.example.proyektorapp.api.service.KegiatanService;
import com.example.proyektorapp.helper.SharedPrefsHelper;
import com.example.proyektorapp.model.modelfitur.Kegiatan;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.sql.Timestamp;

public class KegiatanActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout formLayout, container;
    private Button btnTambah, btnSimpan;
    private EditText etKode, etNama, etTempat, etWaktu;

    private KegiatanService kegiatanService;
    private boolean isAdmin = true;
    private String kodeYangDiedit = null;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kegiatan);

        SharedPrefsHelper prefsHelper = new SharedPrefsHelper(this);
        token = "Bearer " + prefsHelper.getToken();
        String userRole = prefsHelper.getRole();
        isAdmin = userRole != null && userRole.equalsIgnoreCase("ADMIN");

        btnBack    = findViewById(R.id.btnBackToHome);
        formLayout = findViewById(R.id.formLayout);
        container  = findViewById(R.id.containerKegiatan);
        btnTambah  = findViewById(R.id.btnTambahKegiatan);
        btnSimpan  = findViewById(R.id.btnSubmitKegiatan);
        etKode     = findViewById(R.id.inputKodePeminjaman);
        etNama     = findViewById(R.id.inputNamaKegiatan);
        etTempat   = findViewById(R.id.inputTempatKegiatan);
        etWaktu    = findViewById(R.id.inputWaktuKegiatan);

        btnBack.setOnClickListener(v -> finish());

        kegiatanService = ApiClient.getClient().create(KegiatanService.class);

        if (!isAdmin) {
            btnTambah.setVisibility(View.GONE);
            formLayout.setVisibility(View.GONE);
        }

        btnTambah.setOnClickListener(v -> {
            resetForm();
            kodeYangDiedit = null;
            formLayout.setVisibility(View.VISIBLE);
        });

        btnSimpan.setOnClickListener(v -> {
            String kode_transaksi = etKode.getText().toString().trim();
            String namakegiatan = etNama.getText().toString().trim();
            String tempat = etTempat.getText().toString().trim();

            Timestamp waktu = new Timestamp(System.currentTimeMillis());
            etWaktu.setText(formatTimestampForDisplay(waktu)); // Set waktu ke EditText agar terlihat user

            if (kode_transaksi.isEmpty() || namakegiatan.isEmpty() || tempat.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            Kegiatan kegiatan = new Kegiatan(kode_transaksi, namakegiatan, tempat, waktu);

            if (kodeYangDiedit == null) {
                kegiatanService.addKegiatan(token, kegiatan).enqueue(new Callback<Kegiatan>() {
                    @Override
                    public void onResponse(Call<Kegiatan> call, Response<Kegiatan> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(KegiatanActivity.this, "Berhasil ditambah", Toast.LENGTH_SHORT).show();
                            resetForm();
                            formLayout.setVisibility(View.GONE);
                            kodeYangDiedit = null;
                            loadKegiatanList();
                        } else {
                            Toast.makeText(KegiatanActivity.this, "Gagal tambah: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Kegiatan> call, Throwable t) {
                        Log.e("TAMBAH_KEGIATAN", "Gagal menambah", t);
                        Toast.makeText(KegiatanActivity.this, "Gagal tambah", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                kegiatanService.updateKegiatan(token, kodeYangDiedit, kegiatan).enqueue(new Callback<Kegiatan>() {
                    @Override
                    public void onResponse(Call<Kegiatan> call, Response<Kegiatan> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(KegiatanActivity.this, "Berhasil diupdate", Toast.LENGTH_SHORT).show();
                            resetForm();
                            formLayout.setVisibility(View.GONE);
                            kodeYangDiedit = null;
                            loadKegiatanList();
                        } else {
                            Toast.makeText(KegiatanActivity.this, "Gagal update: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Kegiatan> call, Throwable t) {
                        Log.e("UPDATE_KEGIATAN", "Gagal update", t);
                        Toast.makeText(KegiatanActivity.this, "Gagal update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loadKegiatanList();
    }

    private void loadKegiatanList() {
        container.removeAllViews();

        kegiatanService.getAllKegiatan(token).enqueue(new Callback<List<Kegiatan>>() {
            @Override
            public void onResponse(Call<List<Kegiatan>> call, Response<List<Kegiatan>> response) {
                if (response.isSuccessful()) {
                    for (Kegiatan kegiatan : response.body()) {
                        addKegiatanToView(kegiatan);
                    }
                } else {
                    Toast.makeText(KegiatanActivity.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Kegiatan>> call, Throwable t) {
                Log.e("AMBIL_KEGIATAN", "Gagal mengambil kegiatan", t);
                Toast.makeText(KegiatanActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addKegiatanToView(Kegiatan p) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.item_kegiatan, container, false);

        TextView etKode = itemView.findViewById(R.id.tvKodePeminjaman);
        TextView etNama = itemView.findViewById(R.id.tvNamaKegiatan);
        TextView etTempat = itemView.findViewById(R.id.tvTempatKegiatan);
        TextView etWaktu = itemView.findViewById(R.id.tvWaktuKegiatan);
        Button btnEdit = itemView.findViewById(R.id.btnEdit);
        Button btnDelete = itemView.findViewById(R.id.btnDelete);

        etKode.setText("Kode: " + p.getKode_transaksi());
        etNama.setText("Nama Kegiatan: " + p.getKegiatan());
        etTempat.setText("Tempat: " + p.getTempat());
        etWaktu.setText("Waktu: " + formatTimestampForDisplay(p.getWaktu()));

        if (isAdmin) {
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);

            btnEdit.setOnClickListener(v -> {
                kodeYangDiedit = p.getKode_transaksi();
                etKode.setText(p.getKode_transaksi());
                etNama.setText(p.getKegiatan());
                etTempat.setText(p.getTempat());
                etWaktu.setText(formatTimestampForDisplay(p.getWaktu()));
                formLayout.setVisibility(View.VISIBLE);
            });

            btnDelete.setOnClickListener(v -> {
                kegiatanService.deleteKegiatan(token, p.getKode_transaksi()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(KegiatanActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                        loadKegiatanList();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("DELETE_KEGIATAN", "Gagal hapus", t);
                        Toast.makeText(KegiatanActivity.this, "Gagal hapus", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        } else {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        container.addView(itemView);
    }

    private String formatTimestampForDisplay(Timestamp timestamp) {
        try {
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", new Locale("id", "ID"));
            return outputFormat.format(timestamp);
        } catch (Exception e) {
            return timestamp.toString();
        }
    }

    private void resetForm() {
        etKode.setText("");
        etNama.setText("");
        etTempat.setText("");
        etWaktu.setText("");
        kodeYangDiedit = null;
    }
}
