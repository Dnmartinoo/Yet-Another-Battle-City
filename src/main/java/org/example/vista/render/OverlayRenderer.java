package org.example.vista.render;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.vista.config.ConstantesUI;

public final class OverlayRenderer {

    public void render(Canvas canvas, String texto, Color color) {
        var g = canvas.getGraphicsContext2D();

        g.setGlobalAlpha(ConstantesUI.OVERLAY_ALPHA);
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setGlobalAlpha(1.0);

        g.setFill(color);
        g.setFont(Font.font(ConstantesUI.OVERLAY_FONT_FAMILY, ConstantesUI.OVERLAY_FONT_SIZE));

        Text tx = new Text(texto);
        tx.setFont(g.getFont());
        double textW = tx.getLayoutBounds().getWidth();
        double textH = tx.getLayoutBounds().getHeight();

        double x = (canvas.getWidth()  - textW) * 0.5;
        double y = (canvas.getHeight() - textH) * 0.5 + textH; // baseline
        g.fillText(texto, x, y);
    }
}
