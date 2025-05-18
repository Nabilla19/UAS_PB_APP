package com.example.proyektorapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HistoryFragment extends Fragment implements BottomNavColorProvider {

    private LinearLayout containerHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        containerHistory = view.findViewById(R.id.containerHistory);

        // Contoh data riwayat transaksi
        addHistoryItem("TRX001", "PRJ001", "1234567890", "Selesai");
        addHistoryItem("TRX002", "PRJ002", "9876543210", "Selesai");

        return view;
    }

    private void addHistoryItem(String kodeTransaksi, String kodeProyektor, String nik, String status) {
        LinearLayout card = new LinearLayout(requireContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(24, 24, 24, 24);
        card.setBackgroundResource(R.drawable.bg_history_card);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);

        TextView tvKode = new TextView(requireContext());
        tvKode.setText("Kode Transaksi: " + kodeTransaksi);
        tvKode.setTextColor(getResources().getColor(R.color.green_dark));
        tvKode.setTextSize(16);
        card.addView(tvKode);

        TextView tvProyektor = new TextView(requireContext());
        tvProyektor.setText("Kode Proyektor: " + kodeProyektor);
        tvProyektor.setTextColor(getResources().getColor(R.color.green_dark));
        tvProyektor.setTextSize(16);
        card.addView(tvProyektor);

        TextView tvNIK = new TextView(requireContext());
        tvNIK.setText("NIK: " + nik);
        tvNIK.setTextColor(getResources().getColor(R.color.green_dark));
        tvNIK.setTextSize(16);
        card.addView(tvNIK);

        TextView tvStatus = new TextView(requireContext());
        tvStatus.setText("Status: " + status);
        tvStatus.setTextColor(getResources().getColor(R.color.green_success));
        tvStatus.setTextSize(16);
        tvStatus.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        tvStatus.setPadding(0, 16, 0, 0);
        card.addView(tvStatus);

        containerHistory.addView(card);
    }

    @Override
    public int getBottomNavColor() {
        // Contoh warna biru muda (ubah sesuai colors.xml)
        return getResources().getColor(R.color.colorHistoryBackground);
    }
}
