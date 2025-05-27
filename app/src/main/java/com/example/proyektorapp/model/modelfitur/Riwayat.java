package com.example.proyektorapp.model.modelfitur;

public class Riwayat {

    public String kode_transaksi;
    public String kode_proyektor;
    public String nama;
    public String kegiatan;
    public String waktu;
    public String waktu_dikembalikan;

    // Constructor kosong
    public Riwayat() {}

    // Getter dan Setter
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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(String kegiatan) {
        this.kegiatan = kegiatan;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getWaktu_dikembalikan() {
        return waktu_dikembalikan;
    }

    public void setWaktu_dikembalikan(String waktu_dikembalikan) {
        this.waktu_dikembalikan = waktu_dikembalikan;
    }
}