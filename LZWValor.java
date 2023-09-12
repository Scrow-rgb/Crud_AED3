//Célula dos valores gerados pelo algoritmo LZW
public class LZWValor {
    private int valor;
    private LZWValor proximo;

    public LZWValor() {
        this.valor = 0;
    }

    public LZWValor(int n) {
        this.valor = n;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public LZWValor getProximo() {
        return proximo;
    }

    public void setProximo(LZWValor proximo) {
        this.proximo = proximo;
    }

}
