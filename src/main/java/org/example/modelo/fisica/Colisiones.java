package org.example.modelo.fisica;
import java.util.List;
public class Colisiones {
    public static void balasVsBalas(List<? extends Cuerpo> balas, MarcadorDestruccion marcar) {
        for(int i=0; i<balas.size(); i++){
            var bala1 = balas.get(i);
            for(int j=i + 1; j<balas.size(); j++){
                var bala2 = balas.get(j);
                if(bala1.hitbox().intersecta(bala2.hitbox())) {
                    marcar.destruir(bala1);
                    marcar.destruir(bala2);
                }
            }
        }
    }

    public static void balasVsTanques(List<? extends Cuerpo> balas, List<? extends Cuerpo> tanques, AplicarDano aplicar, MarcadorDestruccion marca) {
        for(var bala : balas) {
            for(var tanque : tanques) {
                if(bala.hitbox().intersecta(tanque.hitbox())) {
                    aplicar.aplicar(tanque, 1);
                    marca.destruir(bala);
                    break;
                }
            }
        }
    }

    public static void solidosVsSolidos(List<? extends Cuerpo> movibles, List<? extends Cuerpo> solidos){
        for(var m : movibles){
            for(var s : solidos){
                if(m == s) continue;
                if(m.hitbox().intersecta(s.hitbox())){
                    Vector corr = m.hitbox().correccion(s.hitbox());
                    if(corr != Vector.CERO){
                        m.setPosicion(m.posicion().mas(corr));
                    }
                }
            }
        }
    }

    public interface MarcadorDestruccion {
        void destruir(Cuerpo c);
    }

    public interface AplicarDano {
        void aplicar(Cuerpo objetivo, int dano);
    }
}
