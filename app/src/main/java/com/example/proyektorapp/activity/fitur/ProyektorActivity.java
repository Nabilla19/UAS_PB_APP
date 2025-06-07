package com.example.proyektorapp.activity.fitur;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.proyektorapp.api.ApiClient;
import com.example.proyektorapp.api.service.ProyektorService;
import com.example.proyektorapp.model.modelfitur.Proyektor;
import com.example.proyektorapp.R;
import com.example.proyektorapp.helper.SharedPrefsHelper;


public class ProyektorActivity extends AppCompatActivity {

    private LinearLayout containerProyektor;
    private EditText inputKode, inputMerek, inputNoSeri;
    private Spinner inputStatus;
    private LinearLayout formLayout;
    private Button btnSimpan, btnTambah;

    private ProyektorService proyektorService;
    private boolean isAdmin = true; // nilai akan di-set di onCreate

    private String kodeYangDiedit = null;
    private String token;
    private ImageView btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proyektor);

        // Ambil role dari SharedPreferences (sesuaikan dengan LoginActivity)
        SharedPrefsHelper prefsHelper = new SharedPrefsHelper(this);
        token = "Bearer " + prefsHelper.getToken();

        String userRole = prefsHelper.getRole();
        isAdmin = userRole != null && userRole.equalsIgnoreCase("ADMIN");


        containerProyektor = findViewById(R.id.containerProyektor);
        inputKode = findViewById(R.id.inputKode);
        inputMerek = findViewById(R.id.inputMerk);
        inputNoSeri = findViewById(R.id.inputNoSeri);
        inputStatus = findViewById(R.id.inputStatus);
        formLayout = findViewById(R.id.formLayout);
        btnSimpan = findViewById(R.id.btnSubmitProyektor);
        btnTambah = findViewById(R.id.btnTambahProyektor);
        btnBack = findViewById(R.id.btnBackToHome);

        btnBack.setOnClickListener(v -> finish());

        String[] statusItems = {"tersedia", "rusak", "sedang dipakai"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputStatus.setAdapter(adapter);

        proyektorService = ApiClient.getClient().create(ProyektorService.class);

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
            String kode = inputKode.getText().toString().trim();
            String merk = inputMerek.getText().toString().trim();
            String noSeri = inputNoSeri.getText().toString().trim();
            String status = inputStatus.getSelectedItem().toString();
            if (kode.isEmpty() || merk.isEmpty() || noSeri.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            Proyektor proyektor = new Proyektor(kode, merk, noSeri, status);

            if (kodeYangDiedit == null) {
                // Tambah proyektor
                proyektorService.addProyektor(token, proyektor).enqueue(new Callback<Proyektor>() {
                    @Override
                    public void onResponse(Call<Proyektor> call, Response<Proyektor> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ProyektorActivity.this, "Berhasil ditambah", Toast.LENGTH_SHORT).show();
                            resetForm();
                            formLayout.setVisibility(View.GONE);
                            kodeYangDiedit = null;
                            loadProyektorList();
                        } else {
                            // Server merespon error, misal 400, 401, 500
                            String errorMsg = "Gagal Menambah: " + response.code() + " " + response.message();
                            Toast.makeText(ProyektorActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Proyektor> call, Throwable t) {
                        Log.e("TAMBAH_PROYEKTOR", "Gagal menambah proyektor", t);
                        Toast.makeText(ProyektorActivity.this, "Gagal tambah", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Edit proyektor
                proyektorService.updateProyektor(token, kodeYangDiedit, proyektor).enqueue(new Callback<Proyektor>() {
                    @Override
                    public void onResponse(Call<Proyektor> call, Response<Proyektor> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ProyektorActivity.this, "Berhasil diupdate", Toast.LENGTH_SHORT).show();
                            resetForm();
                            formLayout.setVisibility(View.GONE);
                            kodeYangDiedit = null;
                            loadProyektorList();
                        } else {
                            // Server merespon error, misal 400, 401, 500
                            String errorMsg = "Gagal update: " + response.code() + " " + response.message();
                            Toast.makeText(ProyektorActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Proyektor> call, Throwable t) {
                        Log.e("UPDATE_PROYEKTOR", "Gagal update proyektor", t);
                        Toast.makeText(ProyektorActivity.this, "Gagal update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loadProyektorList();
        Log.d("ProyektorActivity", "Token: " + token);
    }





    private void loadProyektorList() {
        containerProyektor.removeAllViews();

        proyektorService.getAllProyektor(token).enqueue(new Callback<List<Proyektor>>() {
            @Override
            public void onResponse(Call<List<Proyektor>> call, Response<List<Proyektor>> response) {
                if (response.isSuccessful()) {
                    for (Proyektor p : response.body()) {
                        addProyektorToView(p);
                    }
                } else {
                    Toast.makeText(ProyektorActivity.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Proyektor>> call, Throwable t) {
                Log.e("AMBIL_PROYEKTOR", "Gagal mengambil proyektor", t);
                Toast.makeText(ProyektorActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProyektorToView(Proyektor p) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.item_proyektor, containerProyektor, false);

        TextView tvKode = itemView.findViewById(R.id.tvKode);
        TextView tvMerk = itemView.findViewById(R.id.tvMerk);
        TextView tvNoSeri = itemView.findViewById(R.id.tvNoSeri);
        TextView tvStatus = itemView.findViewById(R.id.tvStatus);
        Button btnEdit = itemView.findViewById(R.id.btnEdit);
        Button btnDelete = itemView.findViewById(R.id.btnDelete);

        tvKode.setText("Kode: " + p.getKode_proyektor());
        tvMerk.setText("Merek: " + p.getMerek());
        tvNoSeri.setText("No Seri: " + p.getNomor_seri());
        tvStatus.setText("Status: " + p.getStatus());

        if (isAdmin) {
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);

            btnEdit.setOnClickListener(v -> {
                kodeYangDiedit = p.getKode_proyektor();
                inputKode.setText(p.getKode_proyektor());
                inputMerek.setText(p.getMerek());
                inputNoSeri.setText(p.getNomor_seri());
                inputStatus.setSelection(((ArrayAdapter<String>) inputStatus.getAdapter()).getPosition(p.getStatus()));
                formLayout.setVisibility(View.VISIBLE);
            });

            btnDelete.setOnClickListener(v -> {
                proyektorService.deleteProyektor(token, p.getKode_proyektor()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(ProyektorActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                        loadProyektorList();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("DELETE_PROYEKTOR", "Gagal hapus proyektor", t);
                        Toast.makeText(ProyektorActivity.this, "Gagal hapus", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        } else {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        containerProyektor.addView(itemView);
    }

    private void resetForm() {
        inputKode.setText("");
        inputMerek.setText("");
        inputNoSeri.setText("");
        inputStatus.setSelection(0);
        kodeYangDiedit = null;
    }
}
