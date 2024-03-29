
public class ListaLZW {

    protected LZWValor primeiro, ultimo;
    protected int tamanho;

    public ListaLZW() {
        LZWValor sentinela = new LZWValor();
        primeiro = sentinela;
        ultimo = sentinela;
        tamanho = 0;
    }

    public void inserirUltimo(int valor) {
        LZWValor aux = new LZWValor(valor);
        ultimo.setProximo(aux);
        ultimo = aux;
        tamanho++;
    }

    public int procurarValor(int pos) {
        LZWValor aux = primeiro;
        int valor;

        for (int j = 0; j < pos; j++, aux = aux.getProximo())
            ;

        valor = aux.getValor();

        return valor;
    }

    public int imprimirTamanho() {
        return tamanho;
    }
}
