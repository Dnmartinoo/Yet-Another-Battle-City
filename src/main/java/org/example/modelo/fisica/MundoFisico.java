package org.example.modelo.fisica;

import java.util.List;
import java.util.function.Predicate;

public class MundoFisico {

    public void actualizar(double dt,
                           List<? extends Cuerpo> tanques,
                           List<? extends Cuerpo> balas,
                           List<? extends Cuerpo> bloques,
                           Colisiones.AplicarDano aplicarDano,
                           Colisiones.MarcadorDestruccion marcarDestruir,
                           Predicate<Cuerpo> esSolidoBloque) {
        tanques.forEach(t -> t.integrar(dt));
        balas.forEach(b -> b.integrar(dt));

        Colisiones.solidosVsSolidos(tanques, bloques.stream().filter(esSolidoBloque).toList());
        Colisiones.balasVsBalas(balas, marcarDestruir);
        Colisiones.balasVsTanques(balas, tanques, aplicarDano, marcarDestruir);



    }
}
