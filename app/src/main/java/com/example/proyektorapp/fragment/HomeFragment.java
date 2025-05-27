package com.example.proyektorapp.fragment;

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

import com.example.proyektorapp.activity.fitur.PeminjamanActivity;
import com.example.proyektorapp.navcol.BottomNavColorProvider;
import com.example.proyektorapp.activity.fitur.KegiatanActivity;
import com.example.proyektorapp.activity.fitur.PenanggungJawabActivity;
import com.example.proyektorapp.activity.fitur.ProyektorActivity;
import com.example.proyektorapp.R;

public class HomeFragment extends Fragment implements BottomNavColorProvider {

    private LinearLayout btnProyektor, btnPeminjaman, btnPenanggungJawab, btnKegiatan;
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
        btnPeminjaman = view.findViewById(R.id.btnPeminjaman);
        btnPenanggungJawab = view.findViewById(R.id.btnPenanggungJawab);
        btnKegiatan = view.findViewById(R.id.btnKegiatan);
        ethotdog = view.findViewById(R.id.ethotdog);
        itsearch = view.findViewById(R.id.itsearch);
        etsearch = view.findViewById(R.id.etsearch);

        // Navigation to activities
        btnProyektor.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ProyektorActivity.class)));

        btnPeminjaman.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), PeminjamanActivity.class)));

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
        EditText etKodePeminjaman = dialogView.findViewById(R.id.etKodePeminjaman);
        EditText etNIK = dialogView.findViewById(R.id.etNIK);
        Spinner spinnerStatus = dialogView.findViewById(R.id.spinnerStatus);

        // Setup spinner options
        String[] statusOptions = {"belum dikembalikan", "sudah dikembalikan"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, statusOptions);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        new AlertDialog.Builder(getActivity())
                .setTitle("Filter Pencarian")
                .setView(dialogView)
                .setPositiveButton("Terapkan", (dialog, which) -> {
                    String kodeInfokus = etKodeInfokus.getText().toString().trim();
                    String kodePeminjaman = etKodePeminjaman.getText().toString().trim();
                    String nik = etNIK.getText().toString().trim();
                    String status = spinnerStatus.getSelectedItem().toString();

                    // Send filter data to PeminjamanActivity via Intent
                    Intent intent = new Intent(getActivity(), PeminjamanActivity.class);
                    intent.putExtra("filter_kodeInfokus", kodeInfokus);
                    intent.putExtra("filter_kodePeminjaman", kodePeminjaman);
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