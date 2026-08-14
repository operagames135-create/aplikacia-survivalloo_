package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.ApplicationInfo
import com.example.model.CommunityLink
import com.example.model.LinkIconType
import com.example.model.MinecraftServerStatus
import com.example.model.MinigameInfo
import com.example.model.ServerCommand
import com.example.model.ServerDataRepository
import com.example.model.ServerRule
import com.example.model.ServerStatusUiState
import com.example.model.TeamMember
import com.example.model.TeamRole
import com.example.ui.theme.CodeBg
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EnderPurple
import com.example.ui.theme.EnderPurpleDark
import com.example.ui.theme.EnderPurpleLight
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.RedstoneRed
import com.example.ui.theme.SleekBg
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekCard
import com.example.ui.theme.SleekCardElevated
import com.example.ui.theme.SleekCardSecondary
import com.example.ui.theme.SleekCyan
import com.example.ui.theme.SleekGold
import com.example.ui.theme.SleekHeader
import com.example.ui.theme.SleekLime
import com.example.ui.theme.SleekLimeDark
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.SleekPurpleDark
import com.example.ui.theme.SleekRed
import com.example.ui.theme.SleekRedBg
import com.example.ui.theme.SleekRedBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite
import com.example.util.ClipboardHelper

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    badgeText: String? = null,
    accentColor: Color = SleekLime,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(top = 10.dp, bottom = 4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(width = 3.dp, height = 18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(accentColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    ),
                    color = TextWhite
                )
            }
            if (badgeText != null) {
                Surface(
                    color = accentColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(100.dp),
                    border = BorderStroke(1.dp, accentColor.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = badgeText,
                        color = accentColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }
        }
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                modifier = Modifier.padding(start = 11.dp, top = 2.dp)
            )
        }
    }
}

/**
 * Real-time Server Live Status Card for mcsurvivalloo.aternos.me:56617
 */
@Composable
fun SleekServerLiveStatusCard(
    statusState: ServerStatusUiState,
    selectedAddress: String,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onSelectAddress: (String) -> Unit,
    onCopyAddress: (String) -> Unit,
    onDiscordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_trans")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    val spinTransition = rememberInfiniteTransition(label = "spin_trans")
    val spinAngle by spinTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin_angle"
    )

    val isOnline = statusState is ServerStatusUiState.Success && statusState.status.online
    val isChecking = statusState is ServerStatusUiState.Loading || isRefreshing

    val statusColor = when {
        isChecking -> SleekCyan
        isOnline -> SleekLime
        else -> SleekRed
    }

    val statusText = when {
        isChecking -> "PREVERUJEM..."
        isOnline -> "ONLINE"
        else -> "OFFLINE (ATERNOS)"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = SleekCard),
        shape = RoundedCornerShape(26.dp),
        border = BorderStroke(1.dp, if (isOnline) SleekLime.copy(alpha = 0.4f) else SleekBorderSubtle),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isOnline) 10.dp else 4.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = if (isOnline) SleekLime.copy(alpha = 0.25f) else Color.Transparent
            )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header with Pulse Dot, Title and Refresh Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Pulsing Indicator
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(statusColor.copy(alpha = pulseAlpha * 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = "STATUS SERVERA",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp,
                            color = TextMuted
                        )
                        Text(
                            text = if (isOnline) "Server je zapnutý!" else "Aternos Server",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Status Badge
                    Surface(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(100.dp),
                        border = BorderStroke(1.dp, statusColor.copy(alpha = 0.35f))
                    ) {
                        Text(
                            text = statusText,
                            color = statusColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Refresh Button
                    Surface(
                        color = SleekCardSecondary,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, SleekBorderSubtle),
                        modifier = Modifier
                            .size(34.dp)
                            .clickable(enabled = !isRefreshing) { onRefresh() }
                            .testTag("btn_refresh_server_status")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Obnoviť stav",
                                tint = if (isRefreshing) SleekCyan else TextPrimary,
                                modifier = Modifier
                                    .size(16.dp)
                                    .rotate(if (isRefreshing) spinAngle else 0f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Address display card
            Surface(
                color = SleekCardSecondary.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, SleekBorderSubtle),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCopyAddress(selectedAddress) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "SERVER ADRESA",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = TextMuted
                        )
                        Text(
                            text = selectedAddress,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhite
                        )
                    }

                    Surface(
                        color = SleekPurple.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, SleekPurple.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "KOPÍROVAŤ",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = SleekPurple,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Detailed Content Based on State
            when (statusState) {
                is ServerStatusUiState.Loading -> {
                    Surface(
                        color = Color.Black.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = SleekCyan,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Preverujem dostupnosť ${selectedAddress}...",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                is ServerStatusUiState.Success -> {
                    val status = statusState.status
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        // Players row & bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "👥", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Online hráči:",
                                    fontSize = 12.sp,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = "${status.playersOnline} / ${status.maxPlayers}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = SleekLime
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Player Progress Bar
                        val progress = if (status.maxPlayers > 0) (status.playersOnline.toFloat() / status.maxPlayers).coerceIn(0f, 1f) else 0f
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(SleekCardSecondary)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progress.coerceAtLeast(0.04f))
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(SleekLime)
                            )
                        }

                        if (status.playerList.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Hrajú: " + status.playerList.joinToString(", "),
                                fontSize = 11.sp,
                                color = TextWhite,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Stats Grid (Version & Latency)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "VERZIA", fontSize = 9.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                Text(text = status.version, fontSize = 11.sp, color = TextWhite, fontWeight = FontWeight.SemiBold)
                            }
                            if (status.latencyMs != null) {
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "PING / ODOZVA", fontSize = 9.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                    Text(text = "${status.latencyMs} ms", fontSize = 11.sp, color = SleekCyan, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        if (status.motd.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "MOTD: ${status.motd}",
                                fontSize = 11.sp,
                                color = TextMuted,
                                fontFamily = FontFamily.Monospace,
                                maxLines = 2
                            )
                        }
                    }
                }

                is ServerStatusUiState.Offline, is ServerStatusUiState.Error -> {
                    Surface(
                        color = SleekRedBg,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, SleekRedBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "⚠️", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Server je offline (Aternos šetrí zdroje)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekRed
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Aternos servery sa automaticky vypínajú, keď nikto nehrá. Stačí požiadať na Discorde a administrátori ho hneď zapnú!",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 15.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = onDiscordClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF5865F2),
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp)
                                    .testTag("btn_request_server_start")
                            ) {
                                Text(
                                    text = "👾 Požiadať o zapnutie na Discorde ↗",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action: Copy Current Active IP
            Button(
                onClick = { onCopyAddress(selectedAddress) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SleekCardSecondary,
                    contentColor = TextWhite
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .testTag("btn_copy_status_ip")
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = SleekPurple
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Kopírovať $selectedAddress",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SleekJavaIpCard(
    ipText: String,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    var copied by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = SleekPurple),
        shape = RoundedCornerShape(28.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(28.dp), ambientColor = SleekPurple.copy(alpha = 0.4f))
            .clickable {
                onCopy()
                copied = true
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Background watermark text "JAVA"
            Text(
                text = "JAVA",
                fontSize = 72.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black.copy(alpha = 0.08f),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .rotate(10f)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "JAVA EDITION IP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black.copy(alpha = 0.65f),
                        letterSpacing = 1.sp
                    )

                    Surface(
                        color = Color.Black.copy(alpha = 0.12f),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(36.dp)
                            .clickable {
                                onCopy()
                                copied = true
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                                contentDescription = "Kopírovať Java IP",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = ipText,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Klikni pre okamžité skopírovanie",
                    fontSize = 11.sp,
                    color = Color.Black.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SleekBedrockIpCard(
    ipText: String,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    var copied by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = SleekCard),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, SleekBorderSubtle),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "BEDROCK IP",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SleekPurple,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = ipText,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = TextWhite,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    onCopy()
                    copied = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SleekCardSecondary,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .testTag("btn_copy_bedrock_ip")
            ) {
                Icon(
                    imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = if (copied) SleekLime else TextPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (copied) "Skopírované" else "COPY IP",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SleekBedrockPortCard(
    portText: String,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    var copied by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = SleekCard),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, SleekBorderSubtle),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "BEDROCK PORT",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = SleekPurple,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = portText,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextWhite
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    onCopy()
                    copied = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SleekCardSecondary,
                    contentColor = TextPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .testTag("btn_copy_bedrock_port")
            ) {
                Icon(
                    imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = if (copied) SleekLime else TextPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (copied) "Skopírovaný" else "COPY PORT",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SleekDragonAternosSection(
    dragonStatus: String,
    world: String,
    noticeText: String,
    onDiscordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, SleekBorderSubtle),
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(SleekHeader, SleekBg)
                ),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Ender Dragon status row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🐲", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ENDER DRAGON ($world)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp,
                        color = TextWhite
                    )
                }

                Surface(
                    color = SleekRedBg,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, SleekRedBorder)
                ) {
                    Text(
                        text = dragonStatus,
                        color = SleekRed,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Aternos notice inner card
            Surface(
                color = Color.Black.copy(alpha = 0.35f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, SleekBorderSubtle),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDiscordClick() }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(SleekLime.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "⚠️", fontSize = 15.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Server beží na Aternose. Ak je offline,",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "požiadaj o zapnutie na Discorde! ↗",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SleekLime
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SleekQuickActionTiles(
    onNavigateTab: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SleekTileItem(
            iconEmoji = "⚔️",
            title = "Minigames",
            onClick = { onNavigateTab("MINIGAMES") },
            modifier = Modifier.weight(1f)
        )
        SleekTileItem(
            iconEmoji = "📜",
            title = "Pravidlá",
            onClick = { onNavigateTab("RULES") },
            modifier = Modifier.weight(1f)
        )
        SleekTileItem(
            iconEmoji = "💬",
            title = "Discord Chat",
            onClick = { onNavigateTab("DISCORD") },
            modifier = Modifier.weight(1f)
        )
        SleekTileItem(
            iconEmoji = "📋",
            title = "Nábor",
            onClick = { onNavigateTab("TEAM") },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SleekTileItem(
    iconEmoji: String,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SleekCardSecondary.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, SleekBorderSubtle),
        modifier = modifier
            .height(76.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = iconEmoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextWhite
            )
        }
    }
}

@Composable
fun SleekCommandPill(
    command: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SleekCardSecondary,
        shape = RoundedCornerShape(100.dp),
        border = BorderStroke(1.dp, SleekBorderSubtle),
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            text = command,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun CommandItemRow(
    commandItem: ServerCommand,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isCopied by remember { mutableStateOf(false) }

    Surface(
        color = SleekCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, if (isCopied) SleekLime else SleekBorderSubtle),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                ClipboardHelper.copyToClipboard(
                    context = context,
                    label = "Príkaz",
                    text = commandItem.command,
                    customToastMessage = "Príkaz skopírovaný: ${commandItem.command}"
                )
                isCopied = true
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = commandItem.command,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = SleekPurple
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = SleekCardSecondary,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = commandItem.category.label,
                            fontSize = 10.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = commandItem.description,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }

            IconButton(
                onClick = {
                    ClipboardHelper.copyToClipboard(
                        context = context,
                        label = "Príkaz",
                        text = commandItem.command,
                        customToastMessage = "Príkaz skopírovaný do schránky!"
                    )
                    isCopied = true
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isCopied) SleekLime.copy(alpha = 0.2f) else SleekCardSecondary)
                    .testTag("copy_cmd_${commandItem.command.replace(" ", "_").replace("/", "")}")
            ) {
                Icon(
                    imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                    contentDescription = "Kopírovať príkaz",
                    tint = if (isCopied) SleekLime else TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun RuleCard(
    rule: ServerRule,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (rule.isSevere) Color(0xFF221417) else SleekCard
        ),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            1.dp,
            if (rule.isSevere) SleekRedBorder else SleekBorderSubtle
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (rule.isSevere) SleekRed else SleekCardSecondary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${rule.number}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (rule.isSevere) Color.White else SleekPurple
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = rule.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (rule.isSevere) SleekRed else TextWhite
                    )
                    if (rule.isSevere) {
                        Surface(
                            color = SleekRedBg,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "PERM BAN",
                                color = SleekRed,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = rule.description,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
fun TeamMemberCard(
    member: TeamMember,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SleekCard),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, SleekBorderSubtle),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when (member.role) {
                            TeamRole.OWNER -> SleekGold.copy(alpha = 0.15f)
                            TeamRole.ADMIN -> SleekPurple.copy(alpha = 0.15f)
                            TeamRole.BUILDER -> SleekLime.copy(alpha = 0.15f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (member.role) {
                        TeamRole.OWNER -> "👑"
                        TeamRole.ADMIN -> "🛡️"
                        TeamRole.BUILDER -> "🔨"
                    },
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = member.username,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextWhite
                    )
                    Surface(
                        color = when (member.role) {
                            TeamRole.OWNER -> SleekGold.copy(alpha = 0.2f)
                            TeamRole.ADMIN -> SleekPurple.copy(alpha = 0.2f)
                            TeamRole.BUILDER -> SleekLime.copy(alpha = 0.2f)
                        },
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text(
                            text = member.role.title,
                            color = when (member.role) {
                                TeamRole.OWNER -> SleekGold
                                TeamRole.ADMIN -> SleekPurple
                                TeamRole.BUILDER -> SleekLime
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
                if (member.note != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = member.note,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}

@Composable
fun MinigameCard(
    minigame: MinigameInfo,
    onCopyCommand: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SleekCard),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, SleekBorderSubtle),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = when (minigame.title) {
                            "BedWars" -> "🛏️"
                            "KitPVP" -> "⚔️"
                            "SkyWars" -> "☁️"
                            else -> "🌲"
                        },
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = minigame.title,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = TextWhite
                    )
                }
                Surface(
                    color = SleekPurple.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(
                        text = minigame.tag,
                        color = SleekPurple,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = minigame.description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 17.sp
            )

            if (minigame.joinCommand != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onCopyCommand(minigame.joinCommand) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SleekCardSecondary,
                        contentColor = TextWhite
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Kopírovať príkaz",
                        modifier = Modifier.size(14.dp),
                        tint = SleekLime
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = minigame.joinCommand,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ApplicationCard(
    application: ApplicationInfo,
    onOpenUrl: () -> Unit,
    onCopyUrl: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SleekCard),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, SleekBorderSubtle),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (application.roleTitle.contains("Helper", ignoreCase = true)) "🛡️" else "⚙️",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nábor: ${application.roleTitle}",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = SleekLime
                    )
                }
                Surface(
                    color = SleekLime.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(
                        text = "OTVORENÉ",
                        color = SleekLime,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = application.description,
                fontSize = 12.sp,
                color = TextSecondary,
                lineHeight = 17.sp
            )

            if (application.requirements.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "POŽIADAVKY:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                application.requirements.forEach { req ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "•", color = SleekPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = req, fontSize = 11.sp, color = TextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Netlify Web Link Pill
            Surface(
                color = SleekCardSecondary.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, SleekBorderSubtle),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCopyUrl?.invoke(application.url) ?: onOpenUrl() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = application.url.removePrefix("https://").removeSuffix("/"),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = SleekCyan
                    )
                    Text(
                        text = "📋 Kopírovať",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onOpenUrl,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SleekPurple,
                    contentColor = TextWhite
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("apply_for_${application.roleTitle.lowercase()}")
            ) {
                Text(
                    text = "📝 Otvoriť prihlášku pre ${application.roleTitle} ↗",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CommunityLinkCard(
    link: CommunityLink,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = SleekCard,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, SleekBorderSubtle),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Text(
                    text = when (link.iconType) {
                        LinkIconType.DISCORD -> "👾"
                        LinkIconType.VOTE -> "🗳️"
                        LinkIconType.YOUTUBE -> "▶️"
                        LinkIconType.WEB -> "🌐"
                    },
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = link.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextWhite
                    )
                    Text(
                        text = link.subtitle,
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = link.actionLabel,
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
