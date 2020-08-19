package com.k11.agenteinteligente.logica;

public class aspiradora {

    public void aspirar(int cuarto, lugar l) {
        if (l.getEstado(cuarto)) {
            l.setEstado(false, cuarto);
        }
    }
}
