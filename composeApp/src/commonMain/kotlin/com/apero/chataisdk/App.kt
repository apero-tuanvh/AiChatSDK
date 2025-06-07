package com.apero.chataisdk

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import chataisdk.composeapp.generated.resources.Res
import chataisdk.composeapp.generated.resources.compose_multiplatform
import com.apero.service.AiChatSDK
import com.apero.service.data.remote.model.ApiResult
import com.apero.service.domain.usecase.GetTimestampUseCase
import com.apero.service.domain.usecase.SignUpUseCase
import com.apero.service.domain.usecase.RefreshTokenUseCase
import com.apero.service.domain.model.AuthResult
import com.apero.service.domain.model.GenImageStyleModel

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

            SignUpScreen(AiChatSDK.signupUseCase)
            TimestampScreen(AiChatSDK.timestampUseCase)
            RefreshTokenScreen(AiChatSDK.refreshTokenUseCase)
            FetchStylesScreen(AiChatSDK.aiChatRepository)
            ChatSSEScreen(AiChatSDK.conversationRepository, AiChatSDK.aiChatRepository)
        }
    }
}

@Composable
fun ChatSSEScreen(
    conversationRepository: com.apero.service.domain.repository.ConversationRepository,
    aiChatRepository: com.apero.service.domain.repository.AiChatRepository
) {
    var conversationId by remember { mutableStateOf<String?>(null) }
    var isCreatingConversation by remember { mutableStateOf(false) }
    var questionText by remember { mutableStateOf("") }
    var chatText by remember { mutableStateOf("") }
    var isChatting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Chat SSE Test",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Create Conversation Section
        if (conversationId == null) {
            Button(
                onClick = {
                    isCreatingConversation = true
                    errorMessage = null
                },
                enabled = !isCreatingConversation
            ) {
                Text(if (isCreatingConversation) "Creating Conversation..." else "Create Conversation")
            }

            LaunchedEffect(isCreatingConversation) {
                if (isCreatingConversation) {
                    try {
                        when (val result =
                            conversationRepository.createConversation("ai-virtu")) {
                            is ApiResult.Success -> {
                                conversationId = result.data.data.id
                                errorMessage = null
                            }

                            is ApiResult.Error -> {
                                errorMessage = result.message
                            }
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message
                    } finally {
                        isCreatingConversation = false
                    }
                }
            }
        } else {
            Text("Conversation ID: $conversationId", style = MaterialTheme.typography.bodySmall)
        }

        errorMessage?.let {
            println(it)
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }

        // Chat Section
        if (conversationId != null) {
            androidx.compose.material3.TextField(
                value = questionText,
                onValueChange = { questionText = it },
                label = { Text("Enter your question") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                enabled = !isChatting
            )

            Button(
                onClick = {
                    if (questionText.isNotBlank()) {
                        isChatting = true
                        chatText = ""
                        errorMessage = null
                    }
                },
                enabled = !isChatting && questionText.isNotBlank()
            ) {
                Text(if (isChatting) "Chatting..." else "Send Message")
            }

            // Cancel Button
            if (isChatting) {
                Button(
                    onClick = {
                        aiChatRepository.cancelSendSseMessage()
                        isChatting = false
                        chatText += "\n\n[Message cancelled by user]"
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Cancel Message")
                }
            }

            // Chat Flow Effect
            LaunchedEffect(isChatting) {
                if (isChatting && conversationId != null) {
                    try {
                        aiChatRepository.chatSeeAsFlow(
                            botCode = "ai-virtu",
                            question = questionText,
                            fileUrls = emptyList(),
                            persist = true,
                            conversationId = conversationId!!
                        ).collect { chatData ->
                            when (chatData) {
                                is com.apero.service.domain.model.ChatAnswerData.Init -> {
                                    chatText = "Chat initialized...\n"
                                }

                                is com.apero.service.domain.model.ChatAnswerData.Answering -> {
                                    chatText = chatData.message
                                }

                                is com.apero.service.domain.model.ChatAnswerData.Completed -> {
                                    isChatting = false
                                    chatData.error?.let { error ->
                                        errorMessage = error.message
                                    } ?: run {

                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message
                        isChatting = false
                    }
                }
            }

            // Display Single Chat Text
            if (chatText.isNotEmpty()) {
                val scrollState = rememberScrollState()

                LaunchedEffect(chatText) {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }

                Card(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text(
                        text = chatText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .verticalScroll(scrollState),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
fun FetchStylesScreen(aiChatRepository: com.apero.service.domain.repository.AiChatRepository) {
    var stylesList by remember { mutableStateOf<List<GenImageStyleModel>?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                // Reset state
                stylesList = null
                errorMessage = null
                isLoading = true
            },
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Fetching Styles..." else "Fetch Image Styles")
        }

        LaunchedEffect(isLoading) {
            if (isLoading) {
                try {
                    when (val result = aiChatRepository.fetchGenImageStyleModels("ai-virtu")) {
                        is ApiResult.Success -> {
                            stylesList = result.data
                            errorMessage = null
                        }

                        is ApiResult.Error -> {
                            errorMessage = result.message
                            stylesList = null
                        }
                    }
                } catch (e: Exception) {
                    errorMessage = e.message
                    stylesList = null
                } finally {
                    isLoading = false
                }
            }
        }

        when {
            isLoading -> Text("Loading...")
            errorMessage != null -> {
                Text("Error: $errorMessage")
            }

            stylesList != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Styles Found: ${stylesList?.size}")
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        items(stylesList.orEmpty()) { style ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        "ID: ${style.idStyle}",
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        "Name: ${style.nameStyle}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        "Image URL: ${style.image}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "Prompt: ${style.prompt}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SignUpScreen(signUpUseCase: SignUpUseCase) {
    var authResult by remember { mutableStateOf<AuthResult?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                // Reset state
                authResult = null
                errorMessage = null
                isLoading = true
            },
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Signing Up..." else "Sign Up")
        }

        LaunchedEffect(isLoading) {
            if (isLoading) {
                try {
                    when (val result = signUpUseCase()) {
                        is ApiResult.Success -> {
                            authResult = result.data
                            errorMessage = null
                        }

                        is ApiResult.Error -> {
                            errorMessage = result.message
                            authResult = null
                        }
                    }
                } catch (e: Exception) {
                    errorMessage = e.message
                    authResult = null
                } finally {
                    isLoading = false
                }
            }
        }

        when {
            isLoading -> Text("Loading...")
            errorMessage != null -> {
                Text("Error: $errorMessage")
            }

            authResult != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Sign Up Success!")
                    Text("User ID: ${authResult!!.userId}")
                    Text("Email: ${authResult!!.email ?: "N/A"}")
                    Text("Username: ${authResult!!.userName ?: "N/A"}")
                }
            }
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

@Composable
fun RefreshTokenScreen(refreshTokenUseCase: RefreshTokenUseCase) {
    var authResult by remember { mutableStateOf<AuthResult?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                // Reset state
                authResult = null
                errorMessage = null
                isLoading = true
            },
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Refreshing Token..." else "Refresh Token")
        }

        LaunchedEffect(isLoading) {
            if (isLoading) {
                try {
                    when (val result = refreshTokenUseCase()) {
                        is ApiResult.Success -> {
                            authResult = result.data
                            errorMessage = null
                        }

                        is ApiResult.Error -> {
                            errorMessage = result.errorCodeEnum?.code
                            authResult = null
                        }
                    }
                } catch (e: Exception) {
                    errorMessage = e.message
                    authResult = null
                } finally {
                    isLoading = false
                }
            }
        }

        when {
            isLoading -> Text("Loading...")
            errorMessage != null -> {
                Text("Error: $errorMessage")
            }

            authResult != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Token Refresh Success!")
                    Text("User ID: ${authResult!!.userId}")
                    Text("Email: ${authResult!!.email ?: "N/A"}")
                    Text("Username: ${authResult!!.userName ?: "N/A"}")
                }
            }
        }
    }
}