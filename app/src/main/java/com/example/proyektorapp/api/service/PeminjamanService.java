package com.example.proyektorapp.api.service;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;
import com.example.proyektorapp.model.modelfitur.Peminjaman;


public interface PeminjamanService {
    @GET("transaksi")
    Call<List<Peminjaman>> getAllTransaksi(@Header("Authorization") String token);

    @POST("transaksi")
    Call<Peminjaman>addTransaksi(@Header("Authorization") String token, @Body Peminjaman transaksi);

    @PATCH("transaksi/{kode_transaksi}")
    Call<Peminjaman> updateTransaksi(@Header("Authorization") String token, @Path("kode_transaksi") String kode_transaksi, @Body Peminjaman transaksi);

    @DELETE("transaksi/{kode_transaksi}")
    Call<Void> deleteTransaksi(@Header("Authorization") String token, @Path("kode_transaksi") String kode_transaksi);

}