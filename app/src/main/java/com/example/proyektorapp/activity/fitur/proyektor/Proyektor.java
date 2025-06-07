package com.example.proyektorapp.activity.fitur.proyektor;

public class Proyektor {
    private String kode_proyektor;
    private String merek;
    private String nomor_seri;
    private String status;

    // Konstruktor kosong (diperlukan untuk Retrofit / Gson)
    public Proyektor() {}

    // Konstruktor lengkap
    public Proyektor(String kode_proyektor, String merek, String nomor_seri, String status) {
        this.kode_proyektor = kode_proyektor;
        this.merek = merek;
        this.nomor_seri = nomor_seri;
        this.status = status;
    }

    // Getter & Setter
    public String getKode_proyektor() {
        return kode_proyektor;
    }

    public void setKode_proyektor(String kode_proyektor) {
        this.kode_proyektor = kode_proyektor;
    }

    public String getMerek() {
        return merek;
    }

    public void setMerek(String merek) {
        this.merek = merek;
    }

    public String getNomor_seri() {
        return nomor_seri;
    }

    public void setNomor_seri(String nomor_seri) {
        this.nomor_seri = nomor_seri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
