package com.apero.chataisdk

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import chataisdk.composeapp.generated.resources.Res
import chataisdk.composeapp.generated.resources.compose_multiplatform
import com.apero.service.AiChatSDK
import com.apero.service.domain.usecase.GetTimestampUseCase

@Composable
@Preview
fun App() {

    AiChatSDK.install(
        baseUrl = "https://api-chatbot-ai.dev.apero.vn/",
        bundleId = "com.aichat.chatbot.aiassistant",
        apiKey = "sk-MFBX2yDGuYZygMN97ArFIe00u8jdmaCtWzpKlDT5hd6nHGIKgN",
        publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz+zgKEmqyK5HHtvryJ3pEIjaK+gXMYw/CEBZIzRmis7pxFoHTq9eMZHwPLohKiCJKvXZdtXJltkIW8glyVmgw0Fh6apRV/pvd8VYnphz1v+5pKgDIltCrbNrLKCuxX/bo6/3Z2Gz5xsm0xyB3c3sEtdpRmUK19W2hRK46/c5PKeNbn/OD5Ike5go4gTLaFwEC8XpvxrAfyxqO7ahbJlLqkO8DUwEK7y+NhRTgu+1/n2vxSsmoynwPA18ZSsSBqC+lta+E0dxqlvbeuz736aGbu4EVvRr/WGxg6/mbvb7kfBcyz/OwSQhrjd5lAsKOpP1VVbBSxDSU0f/TecipUzIuQIDAQAB\n" +
                "-----END PUBLIC KEY-----",
        isDebug = true
    )

    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }

            TimestampScreen(AiChatSDK.timestampUseCase)
        }
    }
}

@Composable
fun TimestampScreen(getTimestampUseCase: GetTimestampUseCase) {
    var timestamp by remember { mutableStateOf<Long?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {  // Chạy 1 lần khi Composable load
        try {
            isLoading = true
            timestamp = getTimestampUseCase()
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> Text("Loading...")
        errorMessage != null -> Text("Error: $errorMessage")
        timestamp != null -> Text("Timestamp: $timestamp")
        else -> Text("No data")
    }
}