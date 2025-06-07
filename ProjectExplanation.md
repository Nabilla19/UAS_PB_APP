# Dokumentasi Sistem Informasi Peminjaman Proyektor

## Deskripsi Aplikasi
Aplikasi Sistem Informasi Peminjaman Proyektor adalah aplikasi Android yang dirancang untuk mencatat, mengatur, dan memantau peminjaman alat proyektor di lingkungan Fakultas Sains dan Teknologi. Aplikasi ini memudahkan staf Infokom Fakultas dalam mengelola data ketersediaan, kondisi, dan peminjaman proyektor.

## Struktur Proyek dan Detail Implementasi

### Model
1. **Proyektor.java**
   - Atribut: `kode_proyektor`, `merek`, `nomor_seri`, `status`
   - Method:
     - Constructor kosong untuk Retrofit/Gson
     - Constructor dengan parameter untuk inisialisasi objek
     - Getter dan Setter untuk setiap atribut
   - Status proyektor dapat berupa: "tersedia", "sedang dipakai", atau "rusak"

2. **PJ.java** (Penanggung Jawab)
   - Atribut: `nik`, `nama`, `no_hp`
   - Method:
     - Constructor kosong untuk Retrofit/Gson
     - Constructor dengan parameter untuk inisialisasi objek
     - Getter dan Setter untuk setiap atribut
   - Digunakan untuk menyimpan data orang yang bertanggung jawab atas peminjaman

3. **Peminjaman.java**
   - Atribut: `kode_transaksi`, `kode_proyektor`, `nik`, `status`, `waktu_dikembalikan`
   - Method:
     - Constructor kosong untuk Retrofit/Gson
     - Constructor dengan parameter untuk inisialisasi objek
     - Getter dan Setter untuk setiap atribut
   - Status peminjaman: "belum dikembalikan" atau "sudah dikembalikan"
   - `waktu_dikembalikan` otomatis diisi saat status berubah menjadi "sudah dikembalikan"

4. **Riwayat.java**
   - Atribut: `kode_transaksi`, `kode_proyektor`, `nama`, `kegiatan`, `waktu`, `waktu_dikembalikan`
   - Method:
     - Constructor kosong untuk Retrofit/Gson
     - Getter dan Setter untuk setiap atribut
   - Digunakan untuk menampilkan riwayat peminjaman dengan informasi lengkap

5. **Kegiatan.java**
   - Atribut: `kode_transaksi`, `kegiatan`, `tempat`, `waktu`
   - Method:
     - Constructor kosong untuk Retrofit/Gson
     - Constructor dengan parameter untuk inisialisasi objek
     - Getter dan Setter untuk setiap atribut
   - Menyimpan informasi tentang kegiatan yang menggunakan proyektor

### API

1. **ApiClient.java**
   - Method:
     - `getClient()`: Membuat dan mengembalikan instance Retrofit dengan konfigurasi OkHttpClient
     - Menambahkan interceptor untuk menyisipkan token JWT ke header permintaan HTTP
     - Menggunakan Gson converter untuk serialisasi/deserialisasi JSON
   - Base URL API: http://10.2.30.34:3001/
   - Mengambil token dari SharedPreferences dan menambahkannya ke header Authorization

2. **AuthService.java**
   - Interface Retrofit dengan method:
     - `login(@Body LoginRequest request)`: Mengirim permintaan login
     - `register(@Body RegisterRequest request)`: Mengirim permintaan registrasi
   - Menggunakan anotasi @POST dengan endpoint yang sesuai

3. **ProyektorService.java**
   - Interface Retrofit dengan method:
     - `getAllProyektor(@Header("Authorization") String token)`: Mendapatkan semua data proyektor
     - `addProyektor(@Header("Authorization") String token, @Body Proyektor proyektor)`: Menambah proyektor baru
     - `updateProyektor(@Header("Authorization") String token, @Path("kode") String kode, @Body Proyektor proyektor)`: Memperbarui data proyektor
     - `deleteProyektor(@Header("Authorization") String token, @Path("kode") String kode)`: Menghapus proyektor
   - Menggunakan anotasi @GET, @POST, @PUT, @DELETE dengan endpoint yang sesuai

4. **PJService.java**
   - Interface Retrofit dengan method:
     - `getAllPJ(@Header("Authorization") String token)`: Mendapatkan semua data penanggung jawab
     - `addPJ(@Header("Authorization") String token, @Body PJ penanggung_jawab)`: Menambah penanggung jawab baru
     - `updatePJ(@Header("Authorization") String token, @Path("nik") String nik, @Body PJ penanggung_jawab)`: Memperbarui data penanggung jawab
     - `deletePJ(@Header("Authorization") String token, @Path("nik") String nik)`: Menghapus penanggung jawab
   - Menggunakan anotasi @GET, @POST, @PUT, @DELETE dengan endpoint yang sesuai

5. **PeminjamanService.java**
   - Interface Retrofit dengan method:
     - `getAllTransaksi(@Header("Authorization") String token)`: Mendapatkan semua data transaksi
     - `addTransaksi(@Header("Authorization") String token, @Body Peminjaman transaksi)`: Menambah transaksi baru
     - `updateTransaksi(@Header("Authorization") String token, @Path("kode_transaksi") String kode_transaksi, @Body Peminjaman transaksi)`: Memperbarui data transaksi
     - `deleteTransaksi(@Header("Authorization") String token, @Path("kode_transaksi") String kode_transaksi)`: Menghapus transaksi
   - Menggunakan anotasi @GET, @POST, @PATCH, @DELETE dengan endpoint yang sesuai

6. **RiwayatService.java**
   - Interface Retrofit dengan method:
     - `getAllRiwayat(@Header("Authorization") String token)`: Mendapatkan semua data riwayat peminjaman
   - Menggunakan anotasi @GET dengan endpoint "riwayat"

7. **KegiatanService.java**
   - Interface Retrofit dengan method:
     - `getAllKegiatan(@Header("Authorization") String token)`: Mendapatkan semua data kegiatan
     - `addKegiatan(@Header("Authorization") String token, @Body Kegiatan kegiatan)`: Menambah kegiatan baru
     - `updateKegiatan(@Header("Authorization") String token, @Path("kode_transaksi") String kode_transaksi, @Body Kegiatan kegiatan)`: Memperbarui data kegiatan
     - `deleteKegiatan(@Header("Authorization") String token, @Path("kode_transaksi") String kode_transaksi)`: Menghapus kegiatan
   - Menggunakan anotasi @GET, @POST, @PATCH, @DELETE dengan endpoint yang sesuai

### Activity

1. **LoginActivity.java**
   - Method:
     - `onCreate()`: Inisialisasi UI dan listener
     - `attemptLogin()`: Validasi input dan mengirim permintaan login
     - `handleLoginResponse()`: Menangani respons dari server setelah login
   - Menyimpan token JWT ke SharedPreferences setelah login berhasil
   - Mengarahkan pengguna ke DashboardActivity setelah login berhasil

2. **DashboardActivity.java**
   - Method:
     - `onCreate()`: Inisialisasi UI dan bottom navigation
     - `setupBottomNavigation()`: Mengatur navigasi antar fragment
     - `loadFragment()`: Memuat fragment yang dipilih
   - Menampilkan HomeFragment, HistoryFragment, atau ProfileFragment berdasarkan pilihan navigasi

3. **ProyektorActivity.java**
   - Method:
     - `onCreate()`: Inisialisasi UI, adapter, dan listener
     - `loadProyektorData()`: Memuat data proyektor dari API
     - `saveProyektor()`: Menyimpan data proyektor baru atau yang diperbarui
     - `editProyektor()`: Mengisi form dengan data proyektor yang akan diedit
     - `deleteProyektor()`: Menghapus proyektor
     - `resetForm()`: Mengosongkan form input
   - Menggunakan RecyclerView untuk menampilkan daftar proyektor
   - Menyembunyikan tombol tambah dan form jika pengguna bukan admin

4. **PenanggungJawabActivity.java**
   - Method:
     - `onCreate()`: Inisialisasi UI, adapter, dan listener
     - `loadPJData()`: Memuat data penanggung jawab dari API
     - `savePJ()`: Menyimpan data penanggung jawab baru atau yang diperbarui
     - `editPJ()`: Mengisi form dengan data penanggung jawab yang akan diedit
     - `deletePJ()`: Menghapus penanggung jawab
     - `resetForm()`: Mengosongkan form input
   - Menggunakan RecyclerView untuk menampilkan daftar penanggung jawab

5. **PeminjamanActivity.java**
   - Method:
     - `onCreate()`: Inisialisasi UI, adapter, dan listener
     - `loadPeminjamanData()`: Memuat data peminjaman dari API
     - `savePeminjaman()`: Menyimpan data peminjaman baru atau yang diperbarui
     - `editPeminjaman()`: Mengisi form dengan data peminjaman yang akan diedit
     - `deletePeminjaman()`: Menghapus peminjaman
     - `resetForm()`: Mengosongkan form input
     - `loadProyektorSpinner()`: Memuat data proyektor untuk spinner
     - `loadPJSpinner()`: Memuat data penanggung jawab untuk spinner
   - Menggunakan RecyclerView untuk menampilkan daftar peminjaman
   - Mengatur waktu pengembalian otomatis saat status berubah menjadi "sudah dikembalikan"

6. **KegiatanActivity.java**
   - Method:
     - `onCreate()`: Inisialisasi UI, adapter, dan listener
     - `loadKegiatanData()`: Memuat data kegiatan dari API
     - `saveKegiatan()`: Menyimpan data kegiatan baru atau yang diperbarui
     - `editKegiatan()`: Mengisi form dengan data kegiatan yang akan diedit
     - `deleteKegiatan()`: Menghapus kegiatan
     - `resetForm()`: Mengosongkan form input
     - `loadTransaksiSpinner()`: Memuat data transaksi untuk spinner
   - Menggunakan RecyclerView untuk menampilkan daftar kegiatan
   - Menggunakan DatePickerDialog dan TimePickerDialog untuk input waktu

### Fragment

1. **HomeFragment.java**
   - Method:
     - `onCreateView()`: Inflate layout fragment
     - `onViewCreated()`: Inisialisasi UI dan listener
     - `setupRecyclerView()`: Mengatur RecyclerView untuk menampilkan daftar proyektor
     - `loadProyektorData()`: Memuat data proyektor dari API
     - `filterProyektor()`: Memfilter daftar proyektor berdasarkan status
   - Menampilkan daftar proyektor dengan filter status
   - Menampilkan menu utama dengan CardView untuk navigasi ke activity lain

2. **HistoryFragment.java**
   - Method:
     - `onCreateView()`: Inflate layout fragment
     - `onViewCreated()`: Inisialisasi UI dan listener
     - `setupRecyclerView()`: Mengatur RecyclerView untuk menampilkan riwayat peminjaman
     - `loadRiwayatData()`: Memuat data riwayat dari API
     - `filterRiwayat()`: Memfilter riwayat berdasarkan status
   - Menampilkan riwayat peminjaman dengan filter status
   - Menampilkan detail informasi seperti nama peminjam, kegiatan, dan waktu

3. **ProfileFragment.java**
   - Method:
     - `onCreateView()`: Inflate layout fragment
     - `onViewCreated()`: Inisialisasi UI dan listener
     - `loadUserData()`: Memuat data pengguna dari SharedPreferences
     - `logout()`: Menghapus token dan mengarahkan ke LoginActivity
   - Menampilkan informasi profil pengguna
   - Menyediakan tombol logout untuk mengakhiri sesi

### Helper

1. **SharedPrefsHelper.java**
   - Method:
     - `saveToken()`: Menyimpan token JWT
     - `getToken()`: Mengambil token JWT
     - `saveEmail()`: Menyimpan email pengguna
     - `getEmail()`: Mengambil email pengguna
     - `saveName()`: Menyimpan nama pengguna
     - `getName()`: Mengambil nama pengguna
     - `saveRole()`: Menyimpan peran pengguna (admin/user)
     - `getRole()`: Mengambil peran pengguna
     - `clearAll()`: Menghapus semua data saat logout
   - Menggunakan SharedPreferences untuk menyimpan data lokal
   - Menyediakan metode untuk manajemen sesi pengguna

### Adapter

1. **ProyektorAdapter.java**
   - Method:
     - `onCreateViewHolder()`: Membuat ViewHolder baru
     - `onBindViewHolder()`: Mengisi data ke ViewHolder
     - `getItemCount()`: Mengembalikan jumlah item
     - `setProyektorList()`: Memperbarui daftar proyektor
     - `setOnItemClickListener()`: Mengatur listener untuk klik item
   - Menggunakan ViewHolder pattern untuk efisiensi
   - Menampilkan data proyektor dalam CardView

2. **PJAdapter.java**
   - Method:
     - `onCreateViewHolder()`: Membuat ViewHolder baru
     - `onBindViewHolder()`: Mengisi data ke ViewHolder
     - `getItemCount()`: Mengembalikan jumlah item
     - `setPJList()`: Memperbarui daftar penanggung jawab
     - `setOnItemClickListener()`: Mengatur listener untuk klik item
   - Menampilkan data penanggung jawab dalam CardView

3. **PeminjamanAdapter.java**
   - Method:
     - `onCreateViewHolder()`: Membuat ViewHolder baru
     - `onBindViewHolder()`: Mengisi data ke ViewHolder
     - `getItemCount()`: Mengembalikan jumlah item
     - `setPeminjamanList()`: Memperbarui daftar peminjaman
     - `setOnItemClickListener()`: Mengatur listener untuk klik item
     - `formatTimestamp()`: Memformat timestamp untuk tampilan
   - Menampilkan data peminjaman dengan informasi lengkap

4. **RiwayatAdapter.java**
   - Method:
     - `onCreateViewHolder()`: Membuat ViewHolder baru
     - `onBindViewHolder()`: Mengisi data ke ViewHolder
     - `getItemCount()`: Mengembalikan jumlah item
     - `setRiwayatList()`: Memperbarui daftar riwayat
     - `formatTimestamp()`: Memformat timestamp untuk tampilan
   - Menampilkan riwayat peminjaman dengan informasi lengkap

## Alur Kerja Aplikasi

### Autentikasi
1. Pengguna login melalui LoginActivity
   - Input email dan password
   - Validasi input (tidak boleh kosong, format email valid)
   - Kirim permintaan login ke API
   - Terima token JWT dan simpan di SharedPreferences
2. Token JWT disimpan di SharedPreferences untuk autentikasi selanjutnya
   - Token ditambahkan ke header Authorization untuk setiap permintaan API
3. Setelah login berhasil, pengguna diarahkan ke DashboardActivity
   - Bottom navigation untuk navigasi antar fragment

### Manajemen Proyektor
1. Pengguna dapat melihat daftar proyektor
   - RecyclerView dengan CardView untuk setiap item
   - Filter berdasarkan status (Semua, Tersedia, Sedang Dipinjam, Rusak)
2. Staff dapat menambah, mengedit, dan menghapus data proyektor
   - Form input dengan validasi
   - Spinner untuk pilihan status
3. Status proyektor: Tersedia, Sedang Dipinjam, Rusak
   - Status otomatis berubah saat proyektor dipinjam atau dikembalikan

### Manajemen Peminjaman
1. Staff dapat mencatat peminjaman baru
   - Form input dengan validasi
   - Spinner untuk pilihan proyektor dan penanggung jawab
2. Data yang diinput: kode transaksi, kode proyektor, NIK penanggung jawab
   - Validasi input (tidak boleh kosong, format valid)
3. Status peminjaman: Belum Dikembalikan, Sudah Dikembalikan
   - Filter berdasarkan status
4. Saat status diubah menjadi "Sudah Dikembalikan", waktu pengembalian otomatis direkam
   - Menggunakan Timestamp saat ini

### Riwayat Peminjaman
1. Menampilkan seluruh riwayat peminjaman
   - RecyclerView dengan CardView untuk setiap item
   - Filter berdasarkan status (Semua, Belum Dikembalikan, Sudah Dikembalikan)
2. Informasi yang ditampilkan: kode transaksi, nama peminjam, kode proyektor, kegiatan, waktu pengembalian
   - Format waktu: "dd MMMM yyyy HH:mm" (contoh: "01 Januari 2023 14:30")

## Teknologi yang Digunakan

### Frontend (Android)
1. Java sebagai bahasa pemrograman utama
   - Object-Oriented Programming (OOP)
   - Model-View-Controller (MVC) pattern
2. Retrofit untuk komunikasi dengan API
3. Material Design untuk UI
4. SharedPreferences untuk penyimpanan data lokal

### Backend (API)
1. RESTful API dengan endpoint untuk autentikasi dan CRUD
2. JWT untuk autentikasi
3. Endpoint: http://10.2.30.34:3001/

## Fitur UI/UX
1. Tema warna hijau (#66BB6A, #4CAF50) yang konsisten di seluruh aplikasi
2. Layout yang responsif dengan ScrollView
3. CardView untuk menampilkan item dalam daftar
4. Bottom navigation untuk navigasi utama
5. Filter status untuk proyektor dan transaksi

## Logika Bisnis Utama

### Peminjaman Proyektor
```java
// Saat membuat peminjaman baru
if (status.equalsIgnoreCase("sudah dikembalikan")) {
    waktu_dikembalikan = new Timestamp(System.currentTimeMillis());
}
```

### Format Tampilan Waktu
```java
private String formatTimestampForDisplay(String waktu) {
    try {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        Date date = inputFormat.parse(waktu);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", new Locale("id", "ID"));
        return outputFormat.format(date);
    } catch (ParseException e) {
        return waktu; // fallback
    }
}
```

### Autentikasi API
```java
OkHttpClient client = new OkHttpClient.Builder()
    .addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            SharedPreferences prefs = MyApplication.getInstance()
                    .getSharedPreferences("user_pref", Context.MODE_PRIVATE);
            String token = prefs.getString("TOKEN", null);
            Request.Builder requestBuilder = original.newBuilder();
            if (token != null) {
                requestBuilder.addHeader("Authorization", "Bearer " + token);
            }
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    })
    .build();
```

## Keamanan
1. Autentikasi berbasis token JWT
2. Penyimpanan token di SharedPreferences
3. Validasi input pada form

## Pengembangan Selanjutnya
1. Implementasi notifikasi untuk peminjaman yang belum dikembalikan
2. Fitur pencarian dan filter yang lebih lengkap
3. Dashboard statistik peminjaman
4. Fitur pemesanan proyektor untuk waktu tertentu