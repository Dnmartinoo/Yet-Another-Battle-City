package org.example.vista.menu;

public final class MenuModel {
    private final String[] opciones = {"1 JUGADOR", "2 JUGADORES", "SALIR"};
    private int seleccion = 0;

    public String[] opciones() { return opciones; }
    public int seleccion() { return seleccion; }

    void prev() { seleccion = (seleccion - 1 + opciones.length) % opciones.length; }
    void next() { seleccion = (seleccion + 1) % opciones.length; }
}
