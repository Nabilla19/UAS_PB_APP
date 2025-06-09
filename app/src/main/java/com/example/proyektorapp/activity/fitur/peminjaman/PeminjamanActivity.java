package com.example.proyektorapp.activity.fitur.peminjaman;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyektorapp.R;
import com.example.proyektorapp.api.ApiClient;
import com.example.proyektorapp.api.service.Peminjaman.PeminjamanService;
import com.example.proyektorapp.helper.SharedPrefsHelper;

import java.text.SimpleDateFormat;
import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.sql.Timestamp;

public class PeminjamanActivity extends AppCompatActivity {

    private ImageView btnBackToHome;
    private LinearLayout formLayout, containerPeminjaman;
    private Button btnTambahPeminjaman, btnSubmitPeminjaman;
    private EditText inputKodePeminjaman, inputKodeProyektor, inputNIK;
    private Spinner inputStatus;
    private PeminjamanService peminjamanService;
    private boolean isAdmin = true;
    private String kodeYangDiedit = null;
    private String token;

    // Filter
    private String filterKodeInfokus = "";
    private String filterKodePeminjaman = "";
    private String filterNIK = "";
    private String filterStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peminjaman);

        SharedPrefsHelper prefsHelper = new SharedPrefsHelper(this);
        token = "Bearer " + prefsHelper.getToken();
        String userRole = prefsHelper.getRole();
        isAdmin = userRole != null && userRole.equalsIgnoreCase("ADMIN");

        // Ambil filter dari intent
        Intent intent = getIntent();
        filterKodeInfokus = intent.getStringExtra("filter_kodeInfokus");
        filterKodePeminjaman = intent.getStringExtra("filter_kodePeminjaman");
        filterNIK = intent.getStringExtra("filter_nik");
        filterStatus = intent.getStringExtra("filter_status");

        containerPeminjaman = findViewById(R.id.containerPeminjaman);
        inputKodePeminjaman = findViewById(R.id.inputKodePeminjaman);
        inputKodeProyektor = findViewById(R.id.inputKodeProyektor);
        inputNIK = findViewById(R.id.inputNIK);
        inputStatus = findViewById(R.id.inputStatus);
        formLayout = findViewById(R.id.formLayout);
        btnSubmitPeminjaman = findViewById(R.id.btnSubmitPeminjaman);
        btnTambahPeminjaman = findViewById(R.id.btnTambahPeminjaman);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        btnBackToHome.setOnClickListener(v -> finish());

        String[] statusItems = {"belum dikembalikan", "sudah dikembalikan"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputStatus.setAdapter(adapter);

        peminjamanService = ApiClient.getClient().create(PeminjamanService.class);

        if (!isAdmin) {
            btnTambahPeminjaman.setVisibility(View.GONE);
            formLayout.setVisibility(View.GONE);
        }

        btnTambahPeminjaman.setOnClickListener(v -> {
            resetForm();
            kodeYangDiedit = null;
            formLayout.setVisibility(View.VISIBLE);
        });

        btnSubmitPeminjaman.setOnClickListener(v -> {
            String kode_transaksi = inputKodePeminjaman.getText().toString().trim();
            String kode_proyektor = inputKodeProyektor.getText().toString().trim();
            String nik = inputNIK.getText().toString().trim();
            String status = inputStatus.getSelectedItem().toString();

            if (kode_transaksi.isEmpty() || kode_proyektor.isEmpty() || nik.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            Timestamp waktu_dikembalikan = null;
            if (status.equalsIgnoreCase("sudah dikembalikan")) {
                waktu_dikembalikan = new Timestamp(System.currentTimeMillis());
            }

            Peminjaman peminjaman = new Peminjaman(kode_transaksi, kode_proyektor, nik, status, waktu_dikembalikan);

            if (kodeYangDiedit == null) {
                peminjamanService.addTransaksi(token, peminjaman).enqueue(new Callback<Peminjaman>() {
                    @Override
                    public void onResponse(Call<Peminjaman> call, Response<Peminjaman> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PeminjamanActivity.this, "Berhasil ditambah", Toast.LENGTH_SHORT).show();
                            resetForm();
                            formLayout.setVisibility(View.GONE);
                            kodeYangDiedit = null;
                            loadPeminjamanList();
                        } else {
                            Toast.makeText(PeminjamanActivity.this, "Gagal Menambah: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Peminjaman> call, Throwable t) {
                        Log.e("TAMBAH_PEMINJAMAN", "Gagal menambah", t);
                        Toast.makeText(PeminjamanActivity.this, "Gagal tambah", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                peminjamanService.updateTransaksi(token, kodeYangDiedit, peminjaman).enqueue(new Callback<Peminjaman>() {
                    @Override
                    public void onResponse(Call<Peminjaman> call, Response<Peminjaman> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PeminjamanActivity.this, "Berhasil diupdate", Toast.LENGTH_SHORT).show();
                            resetForm();
                            formLayout.setVisibility(View.GONE);
                            kodeYangDiedit = null;
                            loadPeminjamanList();
                        } else {
                            Toast.makeText(PeminjamanActivity.this, "Gagal update: " + response.code(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Peminjaman> call, Throwable t) {
                        Log.e("UPDATE_PEMINJAMAN", "Gagal update", t);
                        Toast.makeText(PeminjamanActivity.this, "Gagal update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loadPeminjamanList();
    }

    private void loadPeminjamanList() {
        containerPeminjaman.removeAllViews();

        peminjamanService.getAllTransaksi(token).enqueue(new Callback<List<Peminjaman>>() {
            @Override
            public void onResponse(Call<List<Peminjaman>> call, Response<List<Peminjaman>> response) {
                if (response.isSuccessful()) {
                    List<Peminjaman> filteredList = new ArrayList<>();

                    for (Peminjaman p : response.body()) {
                        boolean matches = true;

                        if (filterKodeInfokus != null && !filterKodeInfokus.isEmpty() &&
                                !p.getKode_proyektor().toLowerCase().contains(filterKodeInfokus.toLowerCase())) {
                            matches = false;
                        }

                        if (filterKodePeminjaman != null && !filterKodePeminjaman.isEmpty() &&
                                !p.getKode_transaksi().toLowerCase().contains(filterKodePeminjaman.toLowerCase())) {
                            matches = false;
                        }

                        if (filterNIK != null && !filterNIK.isEmpty() &&
                                !p.getNik().toLowerCase().contains(filterNIK.toLowerCase())) {
                            matches = false;
                        }

                        if (filterStatus != null && !filterStatus.equalsIgnoreCase("Semua") &&
                                !p.getStatus().equalsIgnoreCase(filterStatus)) {
                            matches = false;
                        }

                        if (matches) {
                            filteredList.add(p);
                        }
                    }

                    for (Peminjaman p : filteredList) {
                        addPeminjamanToView(p);
                    }
                } else {
                    Toast.makeText(PeminjamanActivity.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Peminjaman>> call, Throwable t) {
                Log.e("AMBIL_PEMINJAMAN", "Gagal mengambil peminjaman", t);
                Toast.makeText(PeminjamanActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPeminjamanToView(Peminjaman p) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.item_peminjaman, containerPeminjaman, false);

        TextView tvKode = itemView.findViewById(R.id.tvKodePeminjaman);
        TextView tvMerk = itemView.findViewById(R.id.tvKodeProyektor);
        TextView tvNoSeri = itemView.findViewById(R.id.tvNIK);
        TextView tvStatus = itemView.findViewById(R.id.tvStatus);
        TextView tvWaktuDikembalikan = itemView.findViewById(R.id.tvWaktuDikembalikan);
        Button btnEdit = itemView.findViewById(R.id.btnEdit);
        Button btnDelete = itemView.findViewById(R.id.btnDelete);

        tvKode.setText("Kode: " + p.getKode_transaksi());
        tvMerk.setText("Proyektor: " + p.getKode_proyektor());
        tvNoSeri.setText("NIK: " + p.getNik());
        tvStatus.setText("Status: " + p.getStatus());

        if (p.getWaktu_dikembalikan() != null) {
            tvWaktuDikembalikan.setText("Waktu Dikembalikan: " + formatTimestampForDisplay(p.getWaktu_dikembalikan()));
        } else {
            tvWaktuDikembalikan.setText("Waktu Dikembalikan: -");
        }

        if (isAdmin) {
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);

            btnEdit.setOnClickListener(v -> {
                kodeYangDiedit = p.getKode_transaksi();
                inputKodePeminjaman.setText(p.getKode_transaksi());
                inputKodeProyektor.setText(p.getKode_proyektor());
                inputNIK.setText(p.getNik());
                inputStatus.setSelection(((ArrayAdapter<String>) inputStatus.getAdapter()).getPosition(p.getStatus()));
                formLayout.setVisibility(View.VISIBLE);
            });

            btnDelete.setOnClickListener(v -> {
                peminjamanService.deleteTransaksi(token, p.getKode_transaksi()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(PeminjamanActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                        loadPeminjamanList();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("DELETE_PEMINJAMAN", "Gagal hapus", t);
                        Toast.makeText(PeminjamanActivity.this, "Gagal hapus", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        } else {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        containerPeminjaman.addView(itemView);
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
        inputKodePeminjaman.setText("");
        inputKodeProyektor.setText("");
        inputNIK.setText("");
        inputStatus.setSelection(0);
        kodeYangDiedit = null;
    }
}
