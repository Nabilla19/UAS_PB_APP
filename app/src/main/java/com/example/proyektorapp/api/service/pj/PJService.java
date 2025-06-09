package com.example.proyektorapp.api.service.pj;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;
import com.example.proyektorapp.activity.fitur.pj.PJ;


public interface PJService {
    @GET("pj")
    Call<List<PJ>> getAllPJ(@Header("Authorization") String token);

    @POST("pj")
    Call<PJ> addPJ(@Header("Authorization") String token, @Body PJ penanggung_jawab);

    @PUT("pj/{nik}")
    Call<PJ> updatePJ(@Header("Authorization") String token, @Path("nik") String nik, @Body PJ penanggung_jawab);

    @DELETE("pj/{nik}")
    Call<Void> deletePJ(@Header("Authorization") String token, @Path("nik") String nik);

}
