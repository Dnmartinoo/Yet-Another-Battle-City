package org.example.vista.config;

import java.util.List;

public final class ConstantesUI {
    private ConstantesUI() {}

    // --- Ventana ---
    public static final String WINDOW_TITLE = "BATTLE CITY - Jugando";
    public static final String ICONO_APP    = "/sprites/logo.png";
    // --- Tamaño escena (menú) ---
    public static final double MENU_WIDTH  = 800;
    public static final double MENU_HEIGHT = 600;
    // --- Overlay ---
    public static final double OVERLAY_ALPHA       = 0.65;
    public static final String OVERLAY_FONT_FAMILY = "Consolas";
    public static final int    OVERLAY_FONT_SIZE   = 48;

    // --- Audio: música y SFX ---
    public static final String MUSICA_LOOP       = "/sounds/musica/tribe-drum-loop.wav";
    public static final String SFX_MUERTE_TANQUE = "/sounds/efectos/bang.wav";
    public static final String SFX_DERROTA       = "/sounds/efectos/explosion.wav";
    public static final String SFX_IMPACTO_BLIND = "/sounds/efectos/glass-cling.wav";
    public static final String SFX_IMPACTO_ACERO = "/sounds/efectos/glass-cling.wav";
    public static final String SFX_DISPARAR      = "/sounds/efectos/laser-gun.wav";
    public static final String SFX_LADRILLO_ROTO = "/sounds/efectos/wood-impact.wav";

    // --- Niveles / esquema ---
    public static final String XSD_NIVEL = "/niveles/schema/levelConfig.xsd";
    public static final List<String> NIVELES_XML = List.of(
            "/niveles/Level2.xml",
            "/niveles/Level1.xml",
            "/niveles/Level0.xml"
    );

    // Post Nivel
    public static final String SPLASH_VICTORIA = "Victoria!";
    public static final String SPLASH_DERROTA = "Derrota";

    // --- Sprites ---
    // Bloques
    public static final String PATH_BRICK   = "/sprites/BrickBlock20x20.png";
    public static final String PATH_STEEL   = "/sprites/SteelBlock20x20.png";
    public static final String PATH_WATER   = "/sprites/Water20x20.png";
    public static final String PATH_FOREST  = "/sprites/Forest20x20.png";
    public static final String PATH_BASE    = "/sprites/base20x20.png";
    public static final String PATH_EMPTY   = "/sprites/WhiteBlock20x20.png";

    // Jugadores
    public static final String PATH_P1_0    = "/sprites/Player1Tank0_20x20.png";
    public static final String PATH_P1_1    = "/sprites/Player1Tank1_20x20.png";
    public static final String PATH_P2_0    = "/sprites/Player2Tank0_20x20.png";
    public static final String PATH_P2_1    = "/sprites/Player2Tank1_20x20.png";

    // Enemigos
    public static final String PATH_E_FAST_0    = "/sprites/EnemyTankFast0_20x20.png";
    public static final String PATH_E_FAST_1    = "/sprites/EnemyTankFast1_20x20.png";
    public static final String PATH_E_HEAVY_0   = "/sprites/EnemyTankHeavy0_20x20.png";
    public static final String PATH_E_HEAVY_1   = "/sprites/EnemyTankHeavy1_20x20.png";
    public static final String PATH_E_POWER_0   = "/sprites/EnemyTankPowerful0_20x20.png";
    public static final String PATH_E_POWER_1   = "/sprites/EnemyTankPowerful1_20x20.png";
    public static final String PATH_E_REG_0     = "/sprites/EnemyTankRegular0_20x20.png";
    public static final String PATH_E_REG_1     = "/sprites/EnemyTankRegular1_20x20.png";

    // Otros
    public static final String PATH_TANK_DESTROYED = "/sprites/TankDestroyed_20x20.png";
    public static final String PATH_SHOT           = "/sprites/Shot.png";
    public static final String PATH_INVULNERABLE   = "/sprites/InvulnerableRing20x20.png";
    public static final String PATH_LOGO           = "/sprites/logo.png";

    // Powerups
    public static final String PATH_PW_GRENADE = "/sprites/PowerUp-Grenade20x20.png";
    public static final String PATH_PW_HELMET  = "/sprites/PowerUp-Helmet20x20.png";
    public static final String PATH_PW_STAR    = "/sprites/PowerUp-Star20x20.png";
    public static final String PATH_PW_SHOVEL  = "/sprites/PowerUp-Shovel20x20.png";
    // --- Estilos del menú ---
    public static final String MENU_BG_STYLE     = "-fx-background-color: black;";
    public static final String MENU_TITLE_FONT   = "Verdana";
    public static final int    MENU_TITLE_SIZE   = 40;
    public static final String MENU_OPTION_FONT  = "Verdana";
    public static final int    MENU_OPTION_SIZE  = 20;
    public static final String MENU_TITLE_COLOR  = "#FFA500"; // ORANGE
    public static final String MENU_OPTION_COLOR = "#FFFFFF"; // WHITE

    // Selector (tanque) tamaño/rotación
    public static final double MENU_SELECTOR_W   = 20;
    public static final double MENU_SELECTOR_H   = 20;
    public static final double MENU_SELECTOR_ROT = 90;

    // Spacing
    public static final double MENU_VSPACING     = 35;
    public static final double MENU_OPTIONS_GAP  = 20;
    public static final double MENU_ROW_GAP      = 10;

    // Animaciones
    public static final double MENU_FADE_LOGO_SEC = 1.4;
    public static final double MENU_SLIDE_SEC     = 2.0;
    public static final double MENU_SLIDE_START_Y = 400.0;

    // HUD
    public static final double HUD_ALTURA          = 40.0;
    public static final double HUD_PADDING_X       = 10.0;
    public static final double HUD_PADDING_Y       = 25.0;
    public static final double HUD_SEPARACION      = 150.0;
    public static final double HUD_OPACIDAD_FONDO  = 0.35;
    public static final String HUD_FONT_FAMILY     = "Consolas";
    public static final int    HUD_FONT_SIZE       = 18;
    public static final String HUD_COLOR_TEXT      = "#F5F5F5";
    public static final String HUD_COLOR_P2        = "#87CEFA";

}
