package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ServerDataRepository
import com.example.ui.DiscordChatMessage
import com.example.ui.DiscordChatViewModel
import com.example.ui.MessageStatus
import com.example.ui.theme.SleekBg
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekCard
import com.example.ui.theme.SleekCardSecondary
import com.example.ui.theme.SleekCyan
import com.example.ui.theme.SleekGold
import com.example.ui.theme.SleekHeader
import com.example.ui.theme.SleekLime
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.SleekRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite
import com.example.util.ClipboardHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DiscordChatScreen(
    chatViewModel: DiscordChatViewModel,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val uiState by chatViewModel.uiState.collectAsStateWithLifecycle()

    // Scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    LaunchedEffect(uiState.feedbackMessage) {
        uiState.feedbackMessage?.let { msg ->
            onShowToast(msg)
            chatViewModel.clearFeedback()
        }
    }

    val quickTemplates = listOf(
        "Ahoj všetci! 👋",
        "Kedy bude zapnutý server? ⚡",
        "Hľadám hráčov na BedWars! 🛏️",
        "Mám otázku k pravidlám 📜",
        "Chcem sa prihlásiť do Staff tímu 👥",
        "Server beží parádne! 🔥"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SleekBg)
    ) {
        // TOP DISCORD BANNER CARD
        Card(
            colors = CardDefaults.cardColors(containerColor = SleekCard),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, SleekBorderSubtle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFF5865F2).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "👾", fontSize = 20.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "SurvivalLoo Discord Chat",
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                color = TextWhite
                            )
                            Text(
                                text = "Prepojené s oficiálnym Discordom",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // Open full Discord link button
                    Surface(
                        color = Color(0xFF5865F2).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(100.dp),
                        border = BorderStroke(1.dp, Color(0xFF5865F2).copy(alpha = 0.4f)),
                        modifier = Modifier.clickable {
                            ClipboardHelper.openUrl(context, ServerDataRepository.DISCORD_URL)
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "Otvoriť app",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8EA1E1)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = "Discord link",
                                tint = Color(0xFF8EA1E1),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // IDENTITY BAR (Username entered vs needs to enter)
                if (uiState.isUsernameConfirmed) {
                    Surface(
                        color = SleekCardSecondary.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, SleekLime.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(SleekLime)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Píšeš ako: ",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = uiState.username,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekLime
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { chatViewModel.editUsername() }
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Upraviť meno",
                                    tint = TextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Zmeniť meno",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextMuted
                                )
                            }
                        }
                    }
                } else {
                    // USERNAME INPUT CARD
                    Surface(
                        color = SleekPurple.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, SleekPurple.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "👤 Zadaj svoje herné meno pre písanie v chate:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = uiState.username,
                                    onValueChange = { chatViewModel.onUsernameChange(it) },
                                    placeholder = { Text("Napr. MinecraftNick", fontSize = 12.sp, color = TextMuted) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(onDone = {
                                        chatViewModel.confirmUsername()
                                        keyboardController?.hide()
                                    }),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = SleekPurple,
                                        unfocusedBorderColor = SleekBorderSubtle,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite,
                                        focusedContainerColor = SleekCardSecondary,
                                        unfocusedContainerColor = SleekCardSecondary
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .testTag("discord_username_input")
                                )

                                Button(
                                    onClick = {
                                        chatViewModel.confirmUsername()
                                        keyboardController?.hide()
                                    },
                                    enabled = uiState.username.trim().isNotEmpty(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SleekPurple,
                                        contentColor = TextWhite,
                                        disabledContainerColor = SleekCardSecondary,
                                        disabledContentColor = TextMuted
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("discord_confirm_username_btn")
                                ) {
                                    Text(
                                        text = "Uložiť",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // CHAT MESSAGE STREAM
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(uiState.messages, key = { it.id }) { msg ->
                DiscordChatMessageBubble(message = msg)
            }
        }

        // QUICK TEMPLATES HORIZONTAL LIST
        if (uiState.isUsernameConfirmed) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickTemplates.forEach { template ->
                    Surface(
                        color = SleekCardSecondary,
                        shape = RoundedCornerShape(100.dp),
                        border = BorderStroke(1.dp, SleekBorderSubtle),
                        modifier = Modifier.clickable(enabled = !uiState.isSending) {
                            chatViewModel.sendMessage(template)
                        }
                    ) {
                        Text(
                            text = template,
                            fontSize = 11.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }

        // BOTTOM MESSAGE INPUT BAR
        Surface(
            color = SleekHeader,
            border = BorderStroke(1.dp, SleekBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isUsernameConfirmed) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.messageText,
                        onValueChange = { chatViewModel.onMessageTextChange(it) },
                        placeholder = {
                            Text("Napíš správu na Discord...", fontSize = 13.sp, color = TextMuted)
                        },
                        singleLine = false,
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            chatViewModel.sendMessage()
                            keyboardController?.hide()
                        }),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SleekPurple,
                            unfocusedBorderColor = SleekBorderSubtle,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedContainerColor = SleekCardSecondary,
                            unfocusedContainerColor = SleekCardSecondary
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("discord_message_input")
                    )

                    Button(
                        onClick = {
                            chatViewModel.sendMessage()
                            keyboardController?.hide()
                        },
                        enabled = uiState.messageText.trim().isNotEmpty() && !uiState.isSending,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SleekPurple,
                            contentColor = TextWhite,
                            disabledContainerColor = SleekCardSecondary,
                            disabledContentColor = TextMuted
                        ),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier
                            .size(46.dp)
                            .testTag("discord_send_message_btn")
                    ) {
                        if (uiState.isSending) {
                            CircularProgressIndicator(
                                color = TextWhite,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Odoslať správu",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔒 Pre odosielanie správ najprv zadaj svoje meno vyššie",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SleekGold
                    )
                }
            }
        }
    }
}

@Composable
private fun DiscordChatMessageBubble(
    message: DiscordChatMessage,
    modifier: Modifier = Modifier
) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val formattedTime = remember(message.timestamp) { timeFormat.format(Date(message.timestamp)) }

    if (message.isSystemMessage) {
        Surface(
            color = SleekCardSecondary.copy(alpha = 0.5f),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, SleekBorderSubtle),
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(text = "🤖", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = message.senderName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = SleekCyan
                        )
                        Surface(
                            color = SleekCyan.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "BOT",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekCyan,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        Text(
                            text = formattedTime,
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = message.text,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = if (message.isFromSelf) Alignment.End else Alignment.Start
        ) {
            Surface(
                color = if (message.isFromSelf) SleekPurple.copy(alpha = 0.25f) else SleekCard,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (message.isFromSelf) 16.dp else 4.dp,
                    bottomEnd = if (message.isFromSelf) 4.dp else 16.dp
                ),
                border = BorderStroke(
                    1.dp,
                    if (message.isFromSelf) SleekPurple.copy(alpha = 0.5f) else SleekBorderSubtle
                ),
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = message.senderName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = if (message.isFromSelf) SleekLime else SleekPurple
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = formattedTime,
                                fontSize = 9.sp,
                                color = TextMuted
                            )
                        }

                        // Delivery status icon
                        when (message.status) {
                            MessageStatus.SENDING -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(
                                        color = SleekCyan,
                                        strokeWidth = 1.5.dp,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Odosielam", fontSize = 9.sp, color = SleekCyan)
                                }
                            }
                            MessageStatus.SENT -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Odoslané",
                                        tint = SleekLime,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(text = "Discord", fontSize = 9.sp, color = SleekLime)
                                }
                            }
                            MessageStatus.FAILED -> {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = "Chyba",
                                        tint = SleekRed,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(text = "Zlyhalo", fontSize = 9.sp, color = SleekRed)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = message.text,
                        fontSize = 13.sp,
                        color = TextWhite,
                        lineHeight = 18.sp
                    )

                    if (message.errorMessage != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = message.errorMessage,
                            fontSize = 10.sp,
                            color = SleekRed
                        )
                    }
                }
            }
        }
    }
}
