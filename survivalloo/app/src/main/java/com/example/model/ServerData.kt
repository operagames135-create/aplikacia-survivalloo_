package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.DiamondCyan
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EnderPurple
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.RedstoneRed

data class ServerCommand(
    val command: String,
    val description: String,
    val category: CommandCategory,
    val example: String? = null
)

enum class CommandCategory(val label: String, val color: Color) {
    BASIC("Základné", EmeraldGreen),
    TELEPORT("Teleportácia", EnderPurple),
    MINIGAMES("Minihry", DiamondCyan),
    COMMUNITY("Komunita", GoldYellow)
}

data class ServerRule(
    val number: Int,
    val title: String,
    val description: String,
    val isSevere: Boolean = false
)

data class TeamMember(
    val username: String,
    val role: TeamRole,
    val note: String? = null
)

enum class TeamRole(val title: String, val badgeColor: Color, val priority: Int) {
    OWNER("Majiteľ", GoldYellow, 1),
    ADMIN("Admin", EnderPurple, 2),
    BUILDER("Builder", EmeraldGreen, 3)
}

data class MinigameInfo(
    val title: String,
    val tag: String,
    val description: String,
    val joinCommand: String? = null,
    val features: List<String>
)

data class ApplicationInfo(
    val roleTitle: String,
    val description: String,
    val requirements: List<String>,
    val url: String
)

data class CommunityLink(
    val title: String,
    val subtitle: String,
    val url: String,
    val actionLabel: String,
    val iconType: LinkIconType
)

enum class LinkIconType {
    DISCORD, VOTE, YOUTUBE, WEB
}

data class MinecraftServerStatus(
    val online: Boolean = false,
    val host: String = "mcsurvivalloo.aternos.me",
    val port: Int = 56617,
    val playersOnline: Int = 0,
    val maxPlayers: Int = 20,
    val playerList: List<String> = emptyList(),
    val version: String = "1.8 – 1.26.2",
    val motd: String = "SurvivalLoo Minecraft Server",
    val latencyMs: Long? = null,
    val lastCheckedMillis: Long = System.currentTimeMillis(),
    val errorMessage: String? = null
)

sealed interface ServerStatusUiState {
    data object Loading : ServerStatusUiState
    data class Success(val status: MinecraftServerStatus) : ServerStatusUiState
    data class Offline(val address: String, val message: String = "Server je momentálne offline.") : ServerStatusUiState
    data class Error(val message: String) : ServerStatusUiState
}

object ServerDataRepository {
    const val SERVER_NAME = "SurvivalLoo"
    const val SERVER_HOST_NAME = "mcsurvivalloo.aternos.me"
    const val SERVER_PORT = 56617
    const val JAVA_IP = "mcsurvivalloo.aternos.me:56617"
    const val BEDROCK_IP = "mcsurvivalloo.aternos.me"
    const val BEDROCK_PORT = "56617"
    const val JAVA_VERSIONS = "1.8 – 1.26.2"
    const val BEDROCK_VERSIONS = "Najnovšia verzia"
    const val DRAGON_STATUS = "STÁLE NEZABITÝ"
    const val DRAGON_WORLD = "world_1"
    const val ATERNOS_NOTICE = "Server beží na Aternos hostingu. Ak je server vypnutý, napíš do Discord kanála alebo označ Staff tím so žiadosťou o zapnutie!"

    const val DISCORD_URL = "https://discord.gg/VZrD3WKFB6"
    const val VOTE_URL = "https://minecraft-list.cz/server/survivalloo"
    const val YT_OPERA_URL = "https://www.youtube.com/@operagames777"
    const val YT_SERVER_URL = "https://www.youtube.com/@serversurvivalloo"
    const val OFFICIAL_WEB_URL = "https://mcsurvivalloo.macaly.app/"
    const val HELPER_APPLICATION_URL = "https://fabulous-concha-3c08b9.netlify.app/"
    const val TECHNICIAN_APPLICATION_URL = "https://willowy-mooncake-c9b885.netlify.app/"
    const val DISCORD_WEBHOOK_URL = "https://discord.com/api/webhooks/1537786954240888862/0IbUiya5QzPvb7qZaiLOrC0iP5oneL1MeocWcjsx3gxHUce2kSH_ebRprkj6h9JNms5Y"

    val commands = listOf(
        ServerCommand(
            command = "/spawn",
            description = "Návrat na hlavný spawn servera.",
            category = CommandCategory.BASIC
        ),
        ServerCommand(
            command = "/sethome",
            description = "Nastavenie tvojho aktuálneho miesta ako domov.",
            category = CommandCategory.BASIC
        ),
        ServerCommand(
            command = "/home",
            description = "Okamžitý teleport do uloženého domova.",
            category = CommandCategory.BASIC
        ),
        ServerCommand(
            command = "/tpa",
            description = "Odoslanie žiadosti o teleport k vybranému hráčovi.",
            category = CommandCategory.TELEPORT,
            example = "/tpa <meno_hráča>"
        ),
        ServerCommand(
            command = "/tpaccept",
            description = "Prijatie žiadosti o teleport od iného hráča.",
            category = CommandCategory.TELEPORT
        ),
        ServerCommand(
            command = "bw join bedwarsvoid",
            description = "Okamžité pripojenie do BedWars arény (Void).",
            category = CommandCategory.MINIGAMES
        ),
        ServerCommand(
            command = "/warp",
            description = "Otvorenie ponuky warpov a teleportačných bodov.",
            category = CommandCategory.TELEPORT
        ),
        ServerCommand(
            command = "/discord",
            description = "Zobrazenie oficiálneho odkazu na SurvivalLoo Discord v chate.",
            category = CommandCategory.COMMUNITY
        ),
        ServerCommand(
            command = "/kit",
            description = "Zoznam a vyzdvihnutie dostupných herných kitov.",
            category = CommandCategory.BASIC
        ),
        ServerCommand(
            command = "/msg",
            description = "Odoslanie súkromnej správy inému hráčovi na serveri.",
            category = CommandCategory.COMMUNITY,
            example = "/msg <meno_hráča> <správa>"
        )
    )

    val rules = listOf(
        ServerRule(
            number = 1,
            title = "Negriefovať",
            description = "Prísny zákaz ničenia cudzích stavieb, kradnutia z truhlíc, zaplavovania lávou a akéhokoľvek poškodzovania cudzích pozemkov."
        ),
        ServerRule(
            number = 2,
            title = "Necheatovať",
            description = "Zákaz používania hackovaných klientov, módov poskytujúcich neférovú výhodu (Fly, KillAura, AutoClicker, Speed, Jesus)."
        ),
        ServerRule(
            number = 3,
            title = "Zákaz X-Ray",
            description = "Prísny zákaz X-Ray texture packov, X-Ray módov, Freecamu a softvérov na odhaľovanie rúd a podzemných lokácií."
        ),
        ServerRule(
            number = 4,
            title = "Pravidlo o čísle 67",
            description = "Špeciálne komunitné pravidlo servera: Rešpektuj ostatných hráčov, nehádaj sa, správaj sa slušne a udržuj priateľskú atmosféru."
        ),
        ServerRule(
            number = 5,
            title = "Permanentný BAN",
            description = "Porušenie ktoréhokoľvek z vyššie uvedených pravidiel má za následok okamžité udelenie trvalého BANU bez predošlého varovania!",
            isSevere = true
        )
    )

    val teamMembers = listOf(
        TeamMember(
            username = "opera_025",
            role = TeamRole.OWNER,
            note = "Vedenie servera & YouTube tvorca"
        ),
        TeamMember(
            username = "Tometo",
            role = TeamRole.OWNER,
            note = "Vedenie servera & Správa komunity"
        ),
        TeamMember(
            username = "Mario_632",
            role = TeamRole.ADMIN,
            note = "Dohľad nad pravidlami & Podpora"
        ),
        TeamMember(
            username = "Marekorl",
            role = TeamRole.ADMIN,
            note = "Serverový administrátor & Staviteľ"
        ),
        TeamMember(
            username = "jindra05",
            role = TeamRole.BUILDER,
            note = "Dizajn máp & Serverové stavby"
        )
    )

    val minigames = listOf(
        MinigameInfo(
            title = "KitPVP",
            tag = "PvP Aréna",
            description = "Vyber si svoj bojový kit, vstúp do arény a bojuj proti ostatným hráčom v dynamických súbojoch bez straty vlastných vecí.",
            features = listOf("Rôzne kity a herné štýly", "Killstreak odmeny", "Dynamické PvP arény")
        ),
        MinigameInfo(
            title = "BedWars",
            tag = "Tímová stratégia",
            description = "Chráň svoju posteľ s tímom, zbieraj suroviny zo spawnerov, vylepšuj zbrane a znič postele všetkých súperiacich tímov!",
            joinCommand = "bw join bedwarsvoid",
            features = listOf("Void aréna (bedwarsvoid)", "Generátory surovín", "Tímový boj a obrana postele")
        ),
        MinigameInfo(
            title = "SkyWars",
            tag = "Lietajúce ostrovy",
            description = "Začínaš na vlastnom ostrove na oblohe. Rýchlo prehľadaj truhlice, postav most do stredu a zostaň posledným prežívajúcim!",
            features = listOf("Rýchly loot v truhliciach", "PvP na oblohe", "Stredové bonusové truhlice")
        ),
        MinigameInfo(
            title = "Survival world_1",
            tag = "Hlavný svet",
            description = "Klasický prepracovaný Minecraft survival svet s ekonomikou, warpami, komunitnými stavbami a nezabitým Ender Dragonom!",
            features = listOf("Svet: world_1", "Ender Dragon: STÁLE NEZABITÝ", "Ochrana pozemkov a domovy")
        )
    )

    val applications = listOf(
        ApplicationInfo(
            roleTitle = "Helper",
            description = "Hľadáme aktívnych a trpezlivých hráčov, ktorí radi pomáhajú nováčikom, odpovedajú na otázky v chate a udržujú slušné správanie na serveri.",
            requirements = listOf(
                "Vek minimálne 13 rokov (alebo zodpovedné správanie)",
                "Výborná znalosť serverových príkazov a pravidiel",
                "Aktívny Discord a mikrofón",
                "Trpezlivosť a slušná komunikácia bez vulgarizmov"
            ),
            url = HELPER_APPLICATION_URL
        ),
        ApplicationInfo(
            roleTitle = "Technik / Developer",
            description = "Máš skúsenosti s konfiguráciou Minecraft pluginov, optimalizáciou servera a riešením technických chýb? Pridaj sa do nášho tímu!",
            requirements = listOf(
                "Skúsenosti s pluginmi (Paper/Spigot/Aternos)",
                "Znalosť konfigurácie YAML súborov a permissions",
                "Spoľahlivosť a schopnosť tímovej práce",
                "Aktívny prístup na Discorde"
            ),
            url = TECHNICIAN_APPLICATION_URL
        )
    )

    val communityLinks = listOf(
        CommunityLink(
            title = "Discord Komunita",
            subtitle = "discord.gg/VZrD3WKFB6",
            url = DISCORD_URL,
            actionLabel = "Pripojiť sa",
            iconType = LinkIconType.DISCORD
        ),
        CommunityLink(
            title = "Hlasovanie za Server",
            subtitle = "minecraft-list.cz/server/survivalloo",
            url = VOTE_URL,
            actionLabel = "Hlasovať",
            iconType = LinkIconType.VOTE
        ),
        CommunityLink(
            title = "YouTube @operagames777",
            subtitle = "Videá a streamy zo servera",
            url = YT_OPERA_URL,
            actionLabel = "Otvoriť kanál",
            iconType = LinkIconType.YOUTUBE
        ),
        CommunityLink(
            title = "YouTube @serversurvivalloo",
            subtitle = "Oficiálny kanál SurvivalLoo",
            url = YT_SERVER_URL,
            actionLabel = "Otvoriť kanál",
            iconType = LinkIconType.YOUTUBE
        ),
        CommunityLink(
            title = "Oficiálny Web Servera",
            subtitle = "mcsurvivalloo.macaly.app",
            url = OFFICIAL_WEB_URL,
            actionLabel = "Navštíviť web",
            iconType = LinkIconType.WEB
        )
    )
}
