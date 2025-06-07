package com.example.proyektorapp.api.service;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;
import com.example.proyektorapp.model.modelfitur.Riwayat;


public interface RiwayatService {
    @GET("riwayat")
    Call<List<Riwayat>> getAllRiwayat(@Header("Authorization") String token);
}


