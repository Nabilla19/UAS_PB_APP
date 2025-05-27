package com.example.proyektorapp.model.modelfitur;

import java.sql.Timestamp;

public class Kegiatan {
    private String kode_transaksi;
    private String kegiatan;
    private String tempat;
    private Timestamp waktu;

    public Kegiatan() {}

    public Kegiatan(String kode_transaksi, String kegiatan, String tempat, Timestamp waktu) {
        this.kode_transaksi = kode_transaksi;
        this.kegiatan = kegiatan;
        this.tempat = tempat;
        this.waktu = waktu;
    }

    public String getKode_transaksi() {
        return kode_transaksi;
    }

    public void setKode_transaksi(String kode_transaksi) {
        this.kode_transaksi = kode_transaksi;
    }

    public String getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(String kegiatan) {
        this.kegiatan = kegiatan;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat =tempat;
    }


    public Timestamp getWaktu() {
        return waktu;
    }


}