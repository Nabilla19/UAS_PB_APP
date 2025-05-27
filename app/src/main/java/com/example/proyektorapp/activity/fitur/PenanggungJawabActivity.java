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
import com.example.proyektorapp.api.service.PJService;
import com.example.proyektorapp.model.modelfitur.PJ;
import com.example.proyektorapp.api.ApiClient;
import com.example.proyektorapp.R;
import com.example.proyektorapp.helper.SharedPrefsHelper;

public class PenanggungJawabActivity extends AppCompatActivity {

    private ImageView btnBackToHome;
    private LinearLayout formLayout, containerPJ;
    private Button btnTambahPJ, btnSubmitPJ;
    private EditText inputNIK, inputNama,inputNoHP;
    private PJService pjservice;
    private String token;
    private boolean isAdmin = true;
    private String kodeYangDiedit = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penanggung_jawab);


        SharedPrefsHelper prefsHelper = new SharedPrefsHelper(this);
        token = "Bearer " + prefsHelper.getToken();

        String userRole = prefsHelper.getRole();
        isAdmin = userRole != null && userRole.equalsIgnoreCase("ADMIN");


        btnBackToHome = findViewById(R.id.btnBackToHome);
        formLayout = findViewById(R.id.formLayout);
        containerPJ = findViewById(R.id.containerPJ);
        btnTambahPJ = findViewById(R.id.btnTambahPJ);
        btnSubmitPJ = findViewById(R.id.btnSubmitPJ);
        inputNama = findViewById(R.id.inputNamaPJ);
        inputNIK = findViewById(R.id.inputNIKPJ);
        inputNoHP = findViewById(R.id.inputNoHPPJ);

        btnBackToHome.setOnClickListener(v -> finish());


        pjservice = ApiClient.getClient().create(PJService.class);

        if (!isAdmin) {
            btnTambahPJ.setVisibility(View.GONE);
            formLayout.setVisibility(View.GONE);
        }

        btnTambahPJ.setOnClickListener(v -> {
            resetForm();
            kodeYangDiedit = null;
            formLayout.setVisibility(View.VISIBLE);
        });

        btnSubmitPJ.setOnClickListener(v -> {
            String nik = inputNIK.getText().toString().trim();
            String nama = inputNama.getText().toString().trim();
            String nohp = inputNoHP.getText().toString().trim();
            if (nik.isEmpty() || nama.isEmpty() || nohp.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }
            PJ pj = new PJ(nik, nama, nohp);

            if (kodeYangDiedit == null) {
                pjservice.addPJ(token, pj).enqueue(new Callback<PJ>() {
                    @Override
                    public void onResponse(Call<PJ> call, Response<PJ> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PenanggungJawabActivity.this, "Berhasil ditambah", Toast.LENGTH_SHORT).show();
                            resetForm();
                            formLayout.setVisibility(View.GONE);
                            kodeYangDiedit = null;
                            loadPjList();
                        } else {
                            // Server merespon error, misal 400, 401, 500
                            String errorMsg = "Gagal Menambah: " + response.code() + " " + response.message();
                            Toast.makeText(PenanggungJawabActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PJ> call, Throwable t) {
                        Log.e("TAMBAH_PJ", "Gagal menambah Penanggung Jawab", t);
                        Toast.makeText(PenanggungJawabActivity.this, "Gagal tambah", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Edit PJ
                pjservice.updatePJ(token, kodeYangDiedit, pj).enqueue(new Callback<PJ>() {
                    @Override
                    public void onResponse(Call<PJ> call, Response<PJ> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PenanggungJawabActivity.this, "Berhasil diupdate", Toast.LENGTH_SHORT).show();
                            resetForm();
                            formLayout.setVisibility(View.GONE);
                            kodeYangDiedit = null;
                            loadPjList();
                        } else {
                            // Server merespon error, misal 400, 401, 500
                            String errorMsg = "Gagal update: " + response.code() + " " + response.message();
                            Toast.makeText(PenanggungJawabActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PJ> call, Throwable t) {
                        Log.e("UPDATE_PROYEKTOR", "Gagal update proyektor", t);
                        Toast.makeText(PenanggungJawabActivity.this, "Gagal update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        loadPjList();
        Log.d("PenanggungJawabActivity", "Token: " + token);

    }
    private void loadPjList() {
        containerPJ.removeAllViews();

        pjservice.getAllPJ(token).enqueue(new Callback<List<PJ>>() {
            @Override
            public void onResponse(Call<List<PJ>> call, Response<List<PJ>> response) {
                if (response.isSuccessful()) {
                    for (PJ p : response.body()) {
                        addPJview(p);
                    }
                } else {
                    Toast.makeText(PenanggungJawabActivity.this, "Gagal ambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<PJ>> call, Throwable t) {
                Log.e("AMBIL_PROYEKTOR", "Gagal mengambil proyektor", t);
                Toast.makeText(PenanggungJawabActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPJview(PJ p) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.item_penanggung_jawab, containerPJ, false);

        TextView tvnik = itemView.findViewById(R.id.tvNIKPJ);
        TextView tvnamaPJ = itemView.findViewById(R.id.tvNamaPJ);
        TextView tvNoHP = itemView.findViewById(R.id.tvNoHP);
        Button btnEdit = itemView.findViewById(R.id.btnEdit);
        Button btnDelete = itemView.findViewById(R.id.btnDelete);

        tvnik.setText("Kode: " + p.getnik());
        tvnamaPJ.setText("Merek: " + p.getnama());
        tvNoHP.setText("No Seri: " + p.getNo_hp());


        if (isAdmin) {
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);

            btnEdit.setOnClickListener(v -> {
                kodeYangDiedit = p.getnik();
                inputNIK.setText(p.getnik());
                inputNama.setText(p.getnama());
                inputNoHP.setText(p.getNo_hp());
                formLayout.setVisibility(View.VISIBLE);
            });

            btnDelete.setOnClickListener(v -> {
                pjservice.deletePJ(token, p.getnik()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(PenanggungJawabActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                        loadPjList();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("DELETE_PROYEKTOR", "Gagal hapus proyektor", t);
                        Toast.makeText(PenanggungJawabActivity.this, "Gagal hapus", Toast.LENGTH_SHORT).show();
                    }
                });
            });

        } else {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }

        containerPJ.addView(itemView);
    }

    private void resetForm() {
        inputNIK.setText("");
        inputNama.setText("");
        inputNoHP.setText("");
        kodeYangDiedit = null;
    }
}