package com.example.proyektorapp.api.service;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;
import com.example.proyektorapp.model.modelfitur.Kegiatan;


public interface KegiatanService {
    @GET("kegiatan")
    Call<List<Kegiatan>> getAllKegiatan(@Header("Authorization") String token);

    @POST("kegiatan")
    Call<Kegiatan>addKegiatan(@Header("Authorization") String token, @Body Kegiatan kegiatan);

    @PATCH("kegiatan/{kode_transaksi}")
    Call<Kegiatan> updateKegiatan(@Header("Authorization") String token, @Path("kode_transaksi") String kode_transaksi, @Body Kegiatan kegiatan);

    @DELETE("kegiatan/{kode_transaksi}")
    Call<Void> deleteKegiatan(@Header("Authorization") String token, @Path("kode_transaksi") String kode_transaksi);

}
