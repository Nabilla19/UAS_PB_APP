package com.example.proyektorapp.activity.fitur.pj;

public class PJ {
    private String nik;
    private String nama;
    private String no_hp;

    // Konstruktor kosong (diperlukan untuk Retrofit / Gson)
    public PJ() {}

    // Konstruktor lengkap
    public PJ(String nik, String nama, String no_hp) {
        this.nik = nik;
        this.nama = nama;
        this.no_hp = no_hp;
    }

    // Getter & Setter
    public String getnik() {
        return nik;
    }

    public void setnik(String nik) {
        this.nik = nik;
    }

    public String getnama() {
        return nama;
    }

    public void setnama(String nama) {
        this.nama = nama;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }
}
