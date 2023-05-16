import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class Filme {
    protected int id;
    protected String titulo; // 0
    protected String genero; // 2
    protected Calendar data; // 3 e 4
    protected double score; // 6
    protected ListaCast cast; // 8 9 10
    protected double duracao; // 15
    protected boolean lapide;
    protected byte ba[];
    private String concatenar;
    private List<Integer> comprimido;
    private String descomprimido;

    public Filme(int id, String t, String g, Calendar c, double s, ListaCast lc, double dur, boolean l) {

        this.id = id;
        this.titulo = t;
        this.genero = g;
        this.data = c;
        this.score = s;
        this.cast = lc;
        this.duracao = dur;
        this.lapide = l;
    }

    public Filme() {
        this.id = -1;
        this.titulo = "";
        this.genero = "";
        this.data = Calendar.getInstance();
        this.score = 0.0;
        this.cast = new ListaCast();
        this.duracao = 0.0;
        this.lapide = false;

    }

    public String toString() {
        return "\nID: " + this.id +
                "\nTítulo: " + this.titulo +
                "\nData de lançamento: " + this.data.get(Calendar.DAY_OF_MONTH) + "/"
                + this.data.get(Calendar.MONTH)
                + "/"
                + this.data.get(Calendar.YEAR) +
                "\nDuração: " + this.duracao +
                "\nGênero: " + this.genero +
                "\nScore: " + this.score +
                "\nParticipantes: ";
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.titulo = dis.readUTF();
        this.genero = dis.readUTF();
        this.data.setTimeInMillis(dis.readLong());
        this.score = dis.readDouble();
        this.duracao = dis.readDouble();
    }// fim do metodo fromByteArray

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(titulo);
        dos.writeLong(data.getTimeInMillis());
        dos.writeDouble(score);
        dos.writeUTF(cast.imprimirNomes());
        dos.writeDouble(duracao);

        return baos.toByteArray();
    }// fim do metodo toByteArray

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public ListaCast getCast() {
        return cast;
    }

    public void setCast(ListaCast cast) {
        this.cast = cast;
    }

    public double getDuracao() {
        return duracao;
    }

    public void setDuracao(double duracao) {
        this.duracao = duracao;
    }

    public String getConcatenar() {
        return concatenar;
    }

    public void setConcatenar(String concatenar) {
        this.concatenar = concatenar;
    }

    public List<Integer> getComprimido() {
        return comprimido;
    }

    public void setComprimido(List<Integer> comprimido) {
        this.comprimido = comprimido;
    }

    public String getDescomprimido() {
        return descomprimido;
    }

    public void setDescomprimido(String descomprimido) {
        this.descomprimido = descomprimido;
    }

}
