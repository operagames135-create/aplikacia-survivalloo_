package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.CommandCategory
import com.example.model.ServerDataRepository
import com.example.model.ServerStatusUiState
import com.example.ui.components.ApplicationCard
import com.example.ui.components.CommandItemRow
import com.example.ui.components.CommunityLinkCard
import com.example.ui.components.DiscordChatScreen
import com.example.ui.components.MinigameCard
import com.example.ui.components.RuleCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.SleekBedrockIpCard
import com.example.ui.components.SleekBedrockPortCard
import com.example.ui.components.SleekCommandPill
import com.example.ui.components.SleekDragonAternosSection
import com.example.ui.components.SleekJavaIpCard
import com.example.ui.components.SleekQuickActionTiles
import com.example.ui.components.SleekServerLiveStatusCard
import com.example.ui.components.TeamMemberCard
import com.example.ui.theme.SleekBg
import com.example.ui.theme.SleekBorder
import com.example.ui.theme.SleekBorderSubtle
import com.example.ui.theme.SleekCard
import com.example.ui.theme.SleekCardSecondary
import com.example.ui.theme.SleekCyan
import com.example.ui.theme.SleekGold
import com.example.ui.theme.SleekGreenToast
import com.example.ui.theme.SleekHeader
import com.example.ui.theme.SleekLime
import com.example.ui.theme.SleekPurple
import com.example.ui.theme.SleekRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextWhite
import com.example.util.ClipboardHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class SleekTab(val title: String, val iconEmoji: String) {
    HOME("Home", "🏠"),
    STATUS("Status", "📡"),
    DISCORD("Discord Chat", "💬"),
    RULES("Pravidlá", "📜"),
    MINIGAMES("Minihry", "⚔️"),
    TEAM("Nábor & Tím", "📋"),
    VOTE("Vote", "🗳️")
}

/**
 * Reusable subtle staggered entrance animation component.
 * Provides a graceful fade-in combined with an upward slide transition.
 */
@Composable
fun SleekEntrance(
    delayMillis: Int = 0,
    durationMillis: Int = 420,
    offsetY: Dp = 18.dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (delayMillis > 0) {
            delay(delayMillis.toLong())
        }
        isVisible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
        label = "sleek_entrance_alpha"
    )

    val translationY by animateFloatAsState(
        targetValue = if (isVisible) 0f else offsetY.value,
        animationSpec = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing),
        label = "sleek_entrance_slide"
    )

    Box(
        modifier = modifier.graphicsLayer {
            this.alpha = alpha
            this.translationY = translationY * density
        }
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurvivalLooApp(
    viewModel: ServerStatusViewModel = viewModel(),
    chatViewModel: DiscordChatViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var activeTab by remember { mutableStateOf(SleekTab.HOME) }
    var toastMessage by remember { mutableStateOf<String?>(null) }

    val statusUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedAddress by viewModel.selectedAddress.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    fun showToast(msg: String) {
        toastMessage = msg
        scope.launch {
            delay(2400)
            if (toastMessage == msg) {
                toastMessage = null
            }
        }
    }

    val isOnline = statusUiState is ServerStatusUiState.Success && (statusUiState as ServerStatusUiState.Success).status.online
    val isChecking = statusUiState is ServerStatusUiState.Loading || isRefreshing

    val badgeColor = when {
        isChecking -> SleekCyan
        isOnline -> SleekLime
        else -> SleekRed
    }

    val badgeText = when {
        isChecking -> "Preverujem..."
        isOnline -> "Online ${(statusUiState as ServerStatusUiState.Success).status.playersOnline}p"
        else -> "Aternos Offline"
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SleekHeader)
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .border(BorderStroke(1.dp, SleekBorder))
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "MINECRAFT SERVER",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp,
                            color = SleekLime
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = buildAnnotatedString {
                                append("Survival")
                                withStyle(SpanStyle(color = SleekPurple)) {
                                    append("Loo")
                                }
                            },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                            color = TextWhite
                        )
                    }

                    // Live Status Pill Badge
                    Surface(
                        color = SleekCardSecondary,
                        shape = RoundedCornerShape(100.dp),
                        border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.4f)),
                        modifier = Modifier.clickable {
                            viewModel.refreshStatus()
                            showToast("Obnovujem stav pre $selectedAddress...")
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(badgeColor)
                                    .shadow(4.dp, CircleShape, ambientColor = badgeColor)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = badgeText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = SleekHeader,
                border = BorderStroke(1.dp, SleekBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SleekTab.values().forEach { tab ->
                        val isSelected = activeTab == tab

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    if (tab == SleekTab.VOTE) {
                                        ClipboardHelper.openUrl(context, ServerDataRepository.VOTE_URL)
                                    } else {
                                        activeTab = tab
                                        scope.launch { listState.animateScrollToItem(0) }
                                    }
                                }
                                .padding(horizontal = 4.dp, vertical = 4.dp)
                                .testTag("nav_tab_${tab.name.lowercase()}")
                        ) {
                            if (isSelected) {
                                Surface(
                                    color = SleekPurple.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(100.dp),
                                    modifier = Modifier.padding(bottom = 2.dp)
                                ) {
                                    Text(
                                        text = tab.iconEmoji,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = tab.iconEmoji,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }

                            Text(
                                text = tab.title,
                                fontSize = 9.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                color = if (isSelected) SleekPurple else TextMuted,
                                letterSpacing = 0.2.sp
                            )
                        }
                    }
                }
            }
        },
        containerColor = SleekBg,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (activeTab == SleekTab.DISCORD) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = innerPadding.calculateTopPadding(),
                            bottom = innerPadding.calculateBottomPadding()
                        )
                ) {
                    DiscordChatScreen(
                        chatViewModel = chatViewModel,
                        onShowToast = { showToast(it) }
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = innerPadding.calculateTopPadding() + 12.dp,
                        bottom = innerPadding.calculateBottomPadding() + 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                // 1. LIVE SERVER STATUS CARD (mcsurvivalloo.aternos.me:56617)
                if (activeTab == SleekTab.HOME || activeTab == SleekTab.STATUS) {
                    item {
                        SleekEntrance(delayMillis = 20) {
                            SleekServerLiveStatusCard(
                                statusState = statusUiState,
                                selectedAddress = selectedAddress,
                                isRefreshing = isRefreshing,
                                onRefresh = {
                                    viewModel.refreshStatus()
                                    showToast("Obnovujem stav servera...")
                                },
                                onSelectAddress = { addr ->
                                    viewModel.selectAddress(addr)
                                    showToast("Preverujem $addr...")
                                },
                                onCopyAddress = { addr ->
                                    ClipboardHelper.copyToClipboard(
                                        context = context,
                                        label = "Server IP",
                                        text = addr,
                                        customToastMessage = "IP skopírovaná: $addr"
                                    )
                                    showToast("IP skopírovaná: $addr")
                                },
                                onDiscordClick = {
                                    ClipboardHelper.openUrl(context, ServerDataRepository.DISCORD_URL)
                                }
                            )
                        }
                    }
                }

                // 2. JAVA EDITION PROMINENT HERO CARD
                if (activeTab == SleekTab.HOME) {
                    item {
                        SleekEntrance(delayMillis = 50) {
                            SleekJavaIpCard(
                                ipText = ServerDataRepository.JAVA_IP,
                                onCopy = {
                                    ClipboardHelper.copyToClipboard(
                                        context = context,
                                        label = "SurvivalLoo Java IP",
                                        text = ServerDataRepository.JAVA_IP,
                                        customToastMessage = "Java IP skopírovaná!"
                                    )
                                    showToast("Skopírované do schránky!")
                                }
                            )
                        }
                    }

                    // 3. BEDROCK EDITION SPLIT CARDS (IP & PORT)
                    item {
                        SleekEntrance(delayMillis = 80) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                SleekBedrockIpCard(
                                    ipText = ServerDataRepository.BEDROCK_IP,
                                    onCopy = {
                                        ClipboardHelper.copyToClipboard(
                                            context = context,
                                            label = "SurvivalLoo Bedrock IP",
                                            text = ServerDataRepository.BEDROCK_IP,
                                            customToastMessage = "Bedrock IP skopírovaná!"
                                        )
                                        showToast("Bedrock IP skopírovaná!")
                                    },
                                    modifier = Modifier.weight(1f)
                                )

                                SleekBedrockPortCard(
                                    portText = ServerDataRepository.BEDROCK_PORT,
                                    onCopy = {
                                        ClipboardHelper.copyToClipboard(
                                            context = context,
                                            label = "SurvivalLoo Port",
                                            text = ServerDataRepository.BEDROCK_PORT,
                                            customToastMessage = "Port ${ServerDataRepository.BEDROCK_PORT} skopírovaný!"
                                        )
                                        showToast("Port skopírovaný!")
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // 4. ENDER DRAGON STATUS & ATERNOS INFO
                    item {
                        SleekEntrance(delayMillis = 130) {
                            SleekDragonAternosSection(
                                dragonStatus = ServerDataRepository.DRAGON_STATUS,
                                world = ServerDataRepository.DRAGON_WORLD,
                                noticeText = ServerDataRepository.ATERNOS_NOTICE,
                                onDiscordClick = {
                                    activeTab = SleekTab.DISCORD
                                }
                            )
                        }
                    }

                    // 5. QUICK COMMANDS HORIZONTAL LIST
                    item {
                        SleekEntrance(delayMillis = 180) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "RÝCHLE PRÍKAZY",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 2.sp,
                                    color = TextMuted,
                                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val quickCommands = listOf("/spawn", "/sethome", "/tpa", "/warp", "/home", "/kit", "/discord")
                                    quickCommands.forEach { cmd ->
                                        SleekCommandPill(
                                            command = cmd,
                                            onClick = {
                                                ClipboardHelper.copyToClipboard(
                                                    context = context,
                                                    label = "Príkaz",
                                                    text = cmd,
                                                    customToastMessage = "Príkaz $cmd skopírovaný!"
                                                )
                                                showToast("Príkaz $cmd skopírovaný!")
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 6. QUICK NAVIGATION 4-TILES GRID (Minigames, Pravidlá, Discord Chat, Nábor)
                    item {
                        SleekEntrance(delayMillis = 230) {
                            SleekQuickActionTiles(
                                onNavigateTab = { tabName ->
                                    when (tabName) {
                                        "MINIGAMES" -> activeTab = SleekTab.MINIGAMES
                                        "RULES" -> activeTab = SleekTab.RULES
                                        "DISCORD" -> activeTab = SleekTab.DISCORD
                                        "TEAM" -> activeTab = SleekTab.TEAM
                                    }
                                }
                            )
                        }
                    }

                    // 7. DISCORD CHAT SHORTCUT BANNER CARD
                    item {
                        SleekEntrance(delayMillis = 250) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = SleekCard),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.dp, SleekPurple.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { activeTab = SleekTab.DISCORD }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Surface(
                                            color = Color(0xFF5865F2).copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(14.dp),
                                            modifier = Modifier.size(40.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(text = "💬", fontSize = 20.sp)
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "Discord Chat & Komunita",
                                                fontWeight = FontWeight.Black,
                                                fontSize = 14.sp,
                                                color = TextWhite
                                            )
                                            Text(
                                                text = "Zadaj meno a píš správy na server",
                                                fontSize = 11.sp,
                                                color = TextSecondary
                                            )
                                        }
                                    }

                                    Surface(
                                        color = SleekPurple,
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = "Písať ↗",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = TextWhite,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // DETAILED SECTIONS BASED ON SELECTED TAB OR ACCESSIBLE VIA TABS

                // SECTION: COMMANDS
                if (activeTab == SleekTab.HOME) {
                    item {
                        SleekEntrance(delayMillis = 280) {
                            SectionHeader(
                                title = "Zoznam Herných Príkazov",
                                subtitle = "Klepni na príkaz pre okamžité skopírovanie",
                                badgeText = "${ServerDataRepository.commands.size} PRÍKAZOV",
                                accentColor = SleekPurple
                            )
                        }
                    }

                    itemsIndexed(ServerDataRepository.commands) { index, cmd ->
                        SleekEntrance(delayMillis = (290 + index * 20).coerceAtMost(550)) {
                            CommandItemRow(commandItem = cmd)
                        }
                    }
                }

                // SECTION: RULES
                if (activeTab == SleekTab.HOME || activeTab == SleekTab.RULES) {
                    item {
                        SleekEntrance(delayMillis = if (activeTab == SleekTab.HOME) 340 else 40) {
                            SectionHeader(
                                title = "Pravidlá Servera",
                                subtitle = "Dodržiavanie pravidiel je povinné pre všetkých hráčov",
                                badgeText = "STRIKTNÉ",
                                accentColor = SleekRed
                            )
                        }
                    }

                    itemsIndexed(ServerDataRepository.rules) { index, rule ->
                        SleekEntrance(delayMillis = (if (activeTab == SleekTab.HOME) 360 else 60) + index * 30) {
                            RuleCard(rule = rule)
                        }
                    }
                }

                // SECTION: MINIGAMES
                if (activeTab == SleekTab.HOME || activeTab == SleekTab.MINIGAMES) {
                    item {
                        SleekEntrance(delayMillis = if (activeTab == SleekTab.HOME) 400 else 40) {
                            SectionHeader(
                                title = "Minihry a Módy",
                                subtitle = "Zábava na serveri SurvivalLoo",
                                badgeText = "${ServerDataRepository.minigames.size} MÓDY",
                                accentColor = SleekCyan
                            )
                        }
                    }

                    itemsIndexed(ServerDataRepository.minigames) { index, minigame ->
                        SleekEntrance(delayMillis = (if (activeTab == SleekTab.HOME) 420 else 60) + index * 30) {
                            MinigameCard(
                                minigame = minigame,
                                onCopyCommand = { cmd ->
                                    ClipboardHelper.copyToClipboard(
                                        context = context,
                                        label = "Príkaz minihry",
                                        text = cmd,
                                        customToastMessage = "Príkaz skopírovaný: $cmd"
                                    )
                                    showToast("Príkaz skopírovaný!")
                                }
                            )
                        }
                    }
                }

                // SECTION: TEAM & RECRUITMENT
                if (activeTab == SleekTab.HOME || activeTab == SleekTab.TEAM) {
                    item {
                        SleekEntrance(delayMillis = if (activeTab == SleekTab.HOME) 460 else 40) {
                            SectionHeader(
                                title = "Staff Tím Servera",
                                subtitle = "Vedenie a administrátori SurvivalLoo",
                                badgeText = "${ServerDataRepository.teamMembers.size} ČLENOV",
                                accentColor = SleekGold
                            )
                        }
                    }

                    itemsIndexed(ServerDataRepository.teamMembers) { index, member ->
                        SleekEntrance(delayMillis = (if (activeTab == SleekTab.HOME) 480 else 60) + index * 25) {
                            TeamMemberCard(member = member)
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        SleekEntrance(delayMillis = if (activeTab == SleekTab.HOME) 520 else 100) {
                            SectionHeader(
                                title = "Nábor do Tímu (Prihlášky)",
                                subtitle = "Hľadáme Helperov a Technikov",
                                badgeText = "PRIHLÁŠKY",
                                accentColor = SleekLime
                            )
                        }
                    }

                    itemsIndexed(ServerDataRepository.applications) { index, appInfo ->
                        SleekEntrance(delayMillis = (if (activeTab == SleekTab.HOME) 540 else 120) + index * 30) {
                            ApplicationCard(
                                application = appInfo,
                                onOpenUrl = {
                                    ClipboardHelper.openUrl(context, appInfo.url)
                                },
                                onCopyUrl = { url ->
                                    ClipboardHelper.copyToClipboard(
                                        context = context,
                                        label = "Prihláška URL",
                                        text = url,
                                        customToastMessage = "Odkaz na prihlášku skopírovaný!"
                                    )
                                    showToast("Odkaz na prihlášku skopírovaný!")
                                }
                            )
                        }
                    }
                }

                // SECTION: COMMUNITY LINKS
                if (activeTab == SleekTab.HOME) {
                    item {
                        SleekEntrance(delayMillis = 560) {
                            SectionHeader(
                                title = "Oficiálne Odkazy",
                                subtitle = "Pripoj sa ku komunite a sleduj novinky",
                                badgeText = "ODKAZY",
                                accentColor = SleekPurple
                            )
                        }
                    }

                    itemsIndexed(ServerDataRepository.communityLinks) { index, link ->
                        SleekEntrance(delayMillis = 580 + index * 25) {
                            CommunityLinkCard(
                                link = link,
                                onClick = {
                                    ClipboardHelper.openUrl(context, link.url)
                                }
                            )
                        }
                    }
                }

                // FOOTER BRAND
                item {
                    SleekEntrance(delayMillis = 620) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚔️ SurvivalLoo • mcsurvivalloo.macaly.app",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                        }
                    }
                }
            }
        }

            // FLOATING SLEEK GREEN TOAST
            AnimatedVisibility(
                visible = toastMessage != null,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = innerPadding.calculateBottomPadding() + 16.dp)
            ) {
                Surface(
                    color = SleekGreenToast,
                    shape = RoundedCornerShape(100.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                    modifier = Modifier.shadow(16.dp, RoundedCornerShape(100.dp))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(text = "✅", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = toastMessage ?: "Skopírované do schránky!",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
