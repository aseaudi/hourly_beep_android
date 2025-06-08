package com.example.beep

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    Column(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()) {
                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(16.dp)
                        )
                        HyperlinkText(
                            modifier = Modifier.padding(16.dp)
                        )
                    }
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
//                "\n" +
                "**Beep App** is a simple Android application written in Kotlin that runs a foreground service to play a beep sound exactly at the top of every hour (e.g., 09:00, 10:00, etc.). The app keeps the user informed via a persistent notification showing the next scheduled beep time.\n" +
//                "\n" +
                "---\n" +
//                "\n" +
                "## \uD83D\uDE80 Features\n" +
//                "\n" +
                "- ✅ Beeps **every full hour** (aligned to the clock)\n" +
                "- \uD83D\uDD14 Uses a **foreground service** to stay alive in the background\n" +
                "- \uD83D\uDD53 Shows a **notification** with the time of the next beep\n" +
                "- \uD83D\uDD10 Handles **runtime notification permission** on Android 13+\n" +
//                "\n" +
                "---\n" +
//                "\n" +
                "## \uD83D\uDC68\u200D\uD83D\uDCBB Author\n" +
//                "\n" +
                "Abdelmuhaimen Seaudi\n",
        modifier = modifier
    )
}

@Composable
fun HyperlinkText(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val annotatedLinkString: AnnotatedString = buildAnnotatedString {
        val str = "https://github.com/aseaudi"
        val startIndex = str.indexOf("https")
        val endIndex = str.length

        append(str)

        // Define the span style
        addStyle(
            style = SpanStyle(
                color = Color(0xFF1E88E5),
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline
            ),
            start = startIndex,
            end = endIndex
        )

        // Add the string annotation that will be used on click
        addStringAnnotation(
            tag = "URL",
            annotation = "https://github.com/aseaudi",
            start = startIndex,
            end = endIndex
        )
    }

    ClickableText(
        text = annotatedLinkString,
        style = MaterialTheme.typography.bodyLarge,
        onClick = { offset ->
            annotatedLinkString
                .getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { stringAnnotation ->
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(stringAnnotation.item))
                    context.startActivity(browserIntent)
                }
        },
        modifier = modifier
    )
}
