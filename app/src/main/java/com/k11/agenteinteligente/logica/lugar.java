package com.k11.agenteinteligente.logica;

public class lugar {

    private cuarto[] cuarto;

    public lugar(cuarto[] cuarto) {
        this.cuarto = cuarto;
    }

    public void setEstado(boolean est, int ind) {
        cuarto[ind].suciedad = est;
    }

    public boolean getEstado(int ind) {
        return cuarto[ind].suciedad;
    }

    public cuarto[] getCuarto() {
        return cuarto;
    }
}
