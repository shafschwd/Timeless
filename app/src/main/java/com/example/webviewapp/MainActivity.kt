package com.example.webviewapp

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.webviewapp.ui.theme.WebviewappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen() // Call this before super.onCreate()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var isLoading by mutableStateOf(true)

        // Keep the splash screen visible until the WebView is ready (optional).
        // This is a more advanced way to control when the splash screen dismisses.
        // For a simpler approach, the splash screen will dismiss automatically.
        splashScreen.setKeepOnScreenCondition { isLoading }


        setContent {
            WebviewappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MyWebView(
                            url = "https://https://timeless.ezassist.me/", // Replace with your website URL
                            onPageFinished = {
                                isLoading = false
                            },
                            onPageStarted = { s: String?, bitmap: Bitmap? ->
                                isLoading = true
                            }
                        )
                        if (isLoading) {
                            CircularProgressIndicator() // Show a progress indicator while loading
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyWebView(
    url: String,
    modifier: Modifier = Modifier,
    onPageStarted: (url: String?, favicon: Bitmap?) -> Unit = { _, _ -> },
    onPageFinished: (url: String?) -> Unit = { }
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        onPageStarted(url, favicon)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onPageFinished(url)
                    }
                }
                settings.javaScriptEnabled = true // Enable JavaScript if your website needs it
                loadUrl(url)
            }
        },
        update = { webView ->
            webView.loadUrl(url) // Reload if the URL changes, though not typical for this setup
        },
        modifier = modifier.fillMaxSize()
    )
}
