package cr.ac.una.proyecto1progra2.util;

public class Respuesta {

    private boolean estado;
    private String mensaje;
    private String error;
    private String resultadoNombre;
    private Object resultado;

    public Respuesta(boolean estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public Respuesta(boolean estado, String mensaje, String error) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.error = error;
    }

    public Respuesta(boolean estado, String mensaje, String error, String resultadoNombre, Object resultado) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.error = error;
        this.resultadoNombre = resultadoNombre;
        this.resultado = resultado;
    }

    public boolean getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getError() {
        return error;
    }

    public Object getResultado(String resultadoNombre) {
        if (this.resultadoNombre != null && this.resultadoNombre.equals(resultadoNombre)) {
            return resultado;
        }
        return null;
    }
    public boolean isSuccess() {
    return this.estado; 
}
}
