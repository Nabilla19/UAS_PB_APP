package com.example.proyektorapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyektorapp.R;
import com.example.proyektorapp.api.ApiClient;
import com.example.proyektorapp.api.service.RiwayatService;
import com.example.proyektorapp.helper.SharedPrefsHelper;
import com.example.proyektorapp.model.modelfitur.Riwayat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {

    private LinearLayout containerHistory;
    private RiwayatService riwayatService;
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        containerHistory = view.findViewById(R.id.containerHistory);

        SharedPrefsHelper prefsHelper = new SharedPrefsHelper(requireContext());
        token = "Bearer " + prefsHelper.getToken();

        riwayatService = ApiClient.getClient().create(RiwayatService.class);
        loadRiwayat();
        return view;
    }

    private void loadRiwayat() {
        containerHistory.removeAllViews();

        riwayatService.getAllRiwayat(token).enqueue(new Callback<List<Riwayat>>() {
            @Override
            public void onResponse(@NonNull Call<List<Riwayat>> call, @NonNull Response<List<Riwayat>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Riwayat item : response.body()) {
                        addHistoryItem(item);
                    }
                } else {
                    Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Riwayat>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addHistoryItem(Riwayat p) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_riwayat, containerHistory, false);

        TextView tvKode = itemView.findViewById(R.id.tvKodeRiwayat);
        TextView tvNama = itemView.findViewById(R.id.tvNamaRiwayat);
        TextView tvProyektor = itemView.findViewById(R.id.tvProyektorRiwayat);
        TextView tvStatus = itemView.findViewById(R.id.tvStatusRiwayat);
        TextView tvKembali = itemView.findViewById(R.id.tvWaktuKembaliRiwayat);

        tvKode.setText("Kode: " + p.getKode_transaksi());
        tvNama.setText("Nama: " + (p.getNama() != null ? p.getNama() : "-"));
        tvProyektor.setText("Proyektor: " + p.getKode_proyektor());
        tvStatus.setText("Kegiatan: " + (p.getKegiatan() != null ? p.getKegiatan() : "-"));

        String waktuKembali = p.getWaktu_dikembalikan();
        if (waktuKembali != null && !waktuKembali.isEmpty() && !waktuKembali.equals("null")) {
            String formatted = formatTimestampForDisplay(waktuKembali);
            tvKembali.setText("Dikembalikan: " + formatted);
        } else {
            tvKembali.setText("Belum dikembalikan");
        }

        containerHistory.addView(itemView);
    }

    private String formatTimestampForDisplay(String waktu) {
        try {
            // Contoh format input waktu backend: "2025-05-24T13:45:00.000Z"
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date = inputFormat.parse(waktu);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", new Locale("id", "ID"));
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return waktu; // fallback tampilkan apa adanya
        }
    }
}