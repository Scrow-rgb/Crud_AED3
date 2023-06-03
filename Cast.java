

public class Cast {
    private String nome;
    private Cast proximo;

    public Cast() {
        this.nome = "";
    }

    public Cast(String n) {
        this.nome = n;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cast getProximo() {
        return proximo;
    }

    public void setProximo(Cast proximo) {
        this.proximo = proximo;
    }

}
