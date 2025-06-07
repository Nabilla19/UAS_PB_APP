package com.example.proyektorapp.model.modelfitur;

import java.sql.Timestamp;

public class Peminjaman {
    private String kode_transaksi;
    private String kode_proyektor;
    private String nik;
    private String status;
    private Timestamp waktu_dikembalikan;

    public Peminjaman() {}

    public Peminjaman(String kode_transaksi, String kode_proyektor, String nik, String status, Timestamp waktu_dikembalikan) {
        this.kode_transaksi = kode_transaksi;
        this.kode_proyektor = kode_proyektor;
        this.nik = nik;
        this.status = status;
        this.waktu_dikembalikan = waktu_dikembalikan;
    }

    public String getKode_transaksi() {
        return kode_transaksi;
    }

    public void setKode_transaksi(String kode_transaksi) {
        this.kode_transaksi = kode_transaksi;
    }

    public String getKode_proyektor() {
        return kode_proyektor;
    }

    public void setKode_proyektor(String kode_proyektor) {
        this.kode_proyektor = kode_proyektor;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getWaktu_dikembalikan() {
        return waktu_dikembalikan;
    }

    public void setWaktu_dikembalikan(Timestamp waktu_dikembalikan) {
        this.waktu_dikembalikan = waktu_dikembalikan;
    }
}
