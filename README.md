
# ⏰ Beep App – Hourly Foreground Beep Service

**Beep App** is a simple Android application written in Kotlin that runs a foreground service to play a beep sound exactly at the top of every hour (e.g., 09:00, 10:00, etc.). The app keeps the user informed via a persistent notification showing the next scheduled beep time.

---

## 🚀 Features

- ✅ Beeps **every full hour** (aligned to the clock)
- 🔔 Uses a **foreground service** to stay alive in the background
- 🕓 Shows a **notification** with the time of the next beep
- ✅ Compatible with Android 6.0+ (API 23+)
- 🔐 Handles **runtime notification permission** on Android 13+

---

## 🛠 Tech Stack

- Kotlin
- Android Foreground Service
- ToneGenerator
- NotificationManager
- Handler & Handler.postDelayed
- Calendar time math

---

## 📄 License

MIT License – feel free to use, modify, and share.

---

## 👨‍💻 Author

[Abdelmuhaimen Seaudi](https://github.com/aseaudi)
