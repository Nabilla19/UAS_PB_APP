package com.example.proyektorapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment implements BottomNavColorProvider {

    private LinearLayout btnProyektor, btnTransaksi, btnPenanggungJawab, btnKegiatan;
    private ImageView ethotdog, itsearch;
    private EditText etsearch;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        btnProyektor = view.findViewById(R.id.btnProyektor);
        btnTransaksi = view.findViewById(R.id.btnTransaksi);
        btnPenanggungJawab = view.findViewById(R.id.btnPenanggungJawab);
        btnKegiatan = view.findViewById(R.id.btnKegiatan);
        ethotdog = view.findViewById(R.id.ethotdog);
        itsearch = view.findViewById(R.id.itsearch);
        etsearch = view.findViewById(R.id.etsearch);

        // Navigation to activities
        btnProyektor.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ProyektorActivity.class)));

        btnTransaksi.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), TransaksiActivity.class)));

        btnPenanggungJawab.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), PenanggungJawabActivity.class)));

        btnKegiatan.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), KegiatanActivity.class)));

        // Show filter dialog when burger icon, search icon, or search EditText is clicked
        ethotdog.setOnClickListener(v -> showInputFilterDialog());
        itsearch.setOnClickListener(v -> showInputFilterDialog());
        etsearch.setOnClickListener(v -> showInputFilterDialog());

        return view;
    }

    private void showInputFilterDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);

        EditText etKodeInfokus = dialogView.findViewById(R.id.etKodeInfokus);
        EditText etKodeTransaksi = dialogView.findViewById(R.id.etKodeTransaksi);
        EditText etNIK = dialogView.findViewById(R.id.etNIK);
        Spinner spinnerStatus = dialogView.findViewById(R.id.spinnerStatus);

        // Setup spinner options
        String[] statusOptions = {"Semua", "Tersedia", "Sedang Dipinjam", "Rusak"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, statusOptions);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        new AlertDialog.Builder(getActivity())
                .setTitle("Filter Pencarian")
                .setView(dialogView)
                .setPositiveButton("Terapkan", (dialog, which) -> {
                    String kodeInfokus = etKodeInfokus.getText().toString().trim();
                    String kodeTransaksi = etKodeTransaksi.getText().toString().trim();
                    String nik = etNIK.getText().toString().trim();
                    String status = spinnerStatus.getSelectedItem().toString();

                    // Send filter data to TransaksiActivity via Intent
                    Intent intent = new Intent(getActivity(), TransaksiActivity.class);
                    intent.putExtra("filter_kodeInfokus", kodeInfokus);
                    intent.putExtra("filter_kodeTransaksi", kodeTransaksi);
                    intent.putExtra("filter_nik", nik);
                    intent.putExtra("filter_status", status);
                    startActivity(intent);

                    Toast.makeText(getActivity(), "Filter diterapkan", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Batal", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public int getBottomNavColor() {
        return R.color.green_dark;
    }
}
