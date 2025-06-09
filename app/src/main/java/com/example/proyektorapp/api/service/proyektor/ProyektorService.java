package com.example.proyektorapp.api.service.proyektor;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;
import com.example.proyektorapp.activity.fitur.proyektor.Proyektor;


public interface ProyektorService {
    @GET("proyektor")
    Call<List<Proyektor>> getAllProyektor(@Header("Authorization") String token);

    @POST("proyektor")
    Call<Proyektor> addProyektor(@Header("Authorization") String token, @Body Proyektor proyektor);

    @PUT("proyektor/{kode}")
    Call<Proyektor> updateProyektor(@Header("Authorization") String token, @Path("kode") String kode, @Body Proyektor proyektor);

    @DELETE("proyektor/{kode}")
    Call<Void> deleteProyektor(@Header("Authorization") String token, @Path("kode") String kode);

}
