# Deploy Dapur MBG ke Firebase Hosting

File konfigurasi sudah siap di folder `web/`:

| File | Fungsi |
|---|---|
| `firebase.json` | Konfigurasi hosting: folder publik `.`, rewrite SPA ke `index.html`, header cache |
| `.firebaserc` | Menunjuk project default `dapur-mbg` (ganti dengan ID project Anda) |

## Langkah deploy (butuh akun Google Anda)

Firebase CLI sudah terinstal di lingkungan ini (v15.33.0). Login interaktif tidak bisa
dilakukan otomatis, jadi jalankan 3 langkah berikut di terminal Anda sendiri:

### 1. Login
```bash
firebase login
```
(buka URL yang ditampilkan di browser, izinkan akses, lalu kembali ke terminal)

Atau tanpa browser (headless/CI):
```bash
firebase login --no-localhost
```

### 2. (Opsional) Buat / tentukan project
Jika belum punya project Firebase, buat di https://console.firebase.google.com
dengan nama misal `dapur-mbg`, lalu sesuaikan `.firebaserc`:
```json
{ "projects": { "default": "ID_PROJECT_ANDA" } }
```

### 3. Deploy
```bash
cd web
firebase deploy --only hosting
```

## Hasil
Setelah deploy selesai, website tampil permanen di:

```
https://dapur-mbg.web.app        ← (format: <project-id>.web.app)
https://dapur-mbg.firebaseapp.com
```

## Alternatif token (untuk CI/serverside)
Simpan token dari `firebase login:ci` lalu:
```bash
export FIREBASE_TOKEN=<token>
firebase deploy --only hosting --project dapur-mbg
```
