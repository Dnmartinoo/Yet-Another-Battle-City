package org.example.vista.menu;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.example.modelo.juego.JuegoConfig;
import org.example.vista.config.ConstantesUI;
import org.example.vista.assets.ManagerSprites;

final class MenuRenderer {

    private final MenuModel model;

    private VBox menuBox;
    private ImageView selector;

    MenuRenderer(MenuModel model) {
        this.model = model;
    }

    StackPane buildRoot() {
        var root = new StackPane(buildContent());
        root.setStyle(ConstantesUI.MENU_BG_STYLE);
        return root;
    }

    Node buildContent() {
        Label titulo = new Label("BATTLE CITY");
        titulo.setTextFill(Color.web(ConstantesUI.MENU_TITLE_COLOR));
        titulo.setFont(Font.font(ConstantesUI.MENU_TITLE_FONT, ConstantesUI.MENU_TITLE_SIZE));

        // Selector (tanque)
        selector = new ImageView(ManagerSprites.get(JuegoConfig.SPRITE_PLAYER1_0));
        selector.setFitWidth(ConstantesUI.MENU_SELECTOR_W);
        selector.setFitHeight(ConstantesUI.MENU_SELECTOR_H);
        selector.setRotate(ConstantesUI.MENU_SELECTOR_ROT);

        // Menú
        menuBox = new VBox(ConstantesUI.MENU_OPTIONS_GAP);
        menuBox.setAlignment(Pos.CENTER);
        opcionesMenu();

        // Logo con fade-in
        ImageView logoView = construirLogo();

        VBox contenedor = new VBox(ConstantesUI.MENU_VSPACING);
        contenedor.setAlignment(Pos.CENTER);
        if (logoView != null) contenedor.getChildren().add(logoView);
        contenedor.getChildren().addAll(titulo, menuBox);

        return contenedor;
    }

    void opcionesMenu() {
        menuBox.getChildren().clear();
        String[] opciones = model.opciones();
        for (int i = 0; i < opciones.length; i++) {
            Label lbl = new Label(opciones[i]);
            lbl.setTextFill(Color.web(ConstantesUI.MENU_OPTION_COLOR));
            lbl.setFont(Font.font(ConstantesUI.MENU_OPTION_FONT, ConstantesUI.MENU_OPTION_SIZE));

            HBox fila = new HBox(ConstantesUI.MENU_ROW_GAP);
            fila.setAlignment(Pos.CENTER);
            if (i == model.seleccion()) fila.getChildren().addAll(selector, lbl);
            else fila.getChildren().add(lbl);

            menuBox.getChildren().add(fila);
        }
    }

    void animar() {
        // Slide menú
        menuBox.setTranslateY(ConstantesUI.MENU_SLIDE_START_Y);
        var slide = new javafx.animation.TranslateTransition(Duration.seconds(ConstantesUI.MENU_SLIDE_SEC), menuBox);
        slide.setToY(0);
        slide.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        slide.play();

    }

    private ImageView construirLogo() {
        try {
            Image img = new Image(ConstantesUI.PATH_LOGO, true);
            if (img.isError()) return null;

            ImageView iv = new ImageView(img);
            iv.setPreserveRatio(true);
            iv.setFitWidth(240);
            iv.setSmooth(true);

            DropShadow ds = new DropShadow();
            ds.setRadius(18);
            ds.setSpread(0.15);
            ds.setColor(Color.rgb(255, 165, 0, 0.65));
            iv.setEffect(ds);

            iv.setOpacity(0);
            FadeTransition ft = new FadeTransition(Duration.seconds(ConstantesUI.MENU_FADE_LOGO_SEC), iv);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
            ft.play();

            return iv;
        } catch (Exception __) {
            return null;
        }
    }
}
