public class ListaCast {
    private Cast primeiro, ultimo;
    private int tamanho;

    public ListaCast() {
        Cast sentinela = new Cast();
        primeiro = sentinela;
        ultimo = sentinela;
        tamanho = 0;
    }

    public void inserirUltimo(String nome) {
        Cast aux = new Cast(nome);
        ultimo.setProximo(aux);
        ultimo = aux;
        tamanho++;
    }

    public String deletarNome(int pos) {
        Cast aux = primeiro;
        String nome;

        for (int j = 0; j < pos; j++, aux = aux.getProximo())
            ;

        Cast temp = aux.getProximo();

        nome = aux.getNome();

        aux.setProximo(temp.getProximo());
        temp.setProximo(null);
        aux = temp = null;

        tamanho--;
        return nome;
    }

    public String imprimirNomes() {
        String nomes = "";
        Cast aux = primeiro.getProximo();

        for (int i = 0; i < tamanho; i++) {
            nomes = nomes + aux.getNome();
            aux = aux.getProximo();
        }

        return nomes;
    }

    public void imprimirLista() {
        Cast aux = primeiro.getProximo();
        for (; aux != null; aux = aux.getProximo()) {
            System.out.println(aux.getNome());
        }
    }

    public int imprimirTamanho() {
        return tamanho;
    }

}
