package com.example.beep

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.beep.ui.theme.BeepTheme

class MainActivity : ComponentActivity() {
    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startBeepService()
        } else {
            // Optional: Notify user why notification is needed
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.w("MAIN", "XXXXXX MainActivity onCreate")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeepTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    startBeepService()
                }

                shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Optional: Explain why this permission is needed
                    requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }

                else -> {
                    requestNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            startBeepService()
        }


    }

    private fun startBeepService() {
        val intent = Intent(this, BeepService::class.java)
        startForegroundService(intent)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "\n" +
                "# ⏰ Beep App – Hourly Foreground Beep Service\n" +
                "\n" +
                "**Beep App** is a simple Android application written in Kotlin that runs a foreground service to play a beep sound exactly at the top of every hour (e.g., 09:00, 10:00, etc.). The app keeps the user informed via a persistent notification showing the next scheduled beep time.\n" +
                "\n" +
                "---\n" +
                "\n" +
                "## \uD83D\uDE80 Features\n" +
                "\n" +
                "- ✅ Beeps **every full hour** (aligned to the clock)\n" +
                "- \uD83D\uDD14 Uses a **foreground service** to stay alive in the background\n" +
                "- \uD83D\uDD53 Shows a **notification** with the time of the next beep\n" +
                "- \uD83D\uDD10 Handles **runtime notification permission** on Android 13+\n" +
                "\n" +
                "---\n" +
                "\n" +
                "## \uD83D\uDC68\u200D\uD83D\uDCBB Author\n" +
                "\n" +
                "Abdelmuhaimen Seaudi (https://github.com/aseaudi)\n",
        modifier = modifier
    )
}
