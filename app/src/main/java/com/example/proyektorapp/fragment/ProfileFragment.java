package com.example.proyektorapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.proyektorapp.navcol.BottomNavColorProvider;
import com.example.proyektorapp.activity.auth.LoginActivity;
import com.example.proyektorapp.R;

public class ProfileFragment extends Fragment implements BottomNavColorProvider {

    private TextView tvNama, tvEmail;
    private Button btnLogout;

    // Dummy user data
    private String namaUser = "Andi Saputra";
    private String emailUser = "andi.saputra@example.com";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvNama = view.findViewById(R.id.tvNama);
        tvEmail = view.findViewById(R.id.tvEmail);
        btnLogout = view.findViewById(R.id.btnLogout);

        tvNama.setText(namaUser);
        tvEmail.setText(emailUser);

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public int getBottomNavColor() {
        // Contoh warna hijau tua (ubah sesuai colors.xml)
        return getResources().getColor(R.color.colorProfileBackground);
    }
}
