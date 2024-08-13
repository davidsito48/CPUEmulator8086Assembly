package proyectoarqcomp;

public class ModeloMemoria {
    
    private String direccion;
    private  String valor;

    public ModeloMemoria(String direccion, String valor) {
        this.direccion = direccion;
        this.valor = valor;
    }   
    
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    
    
}
