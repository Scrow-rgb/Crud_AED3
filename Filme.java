

import java.util.Calendar;

public class Filme {
    protected int id;
    protected String titulo; // 0
    



    protected String genero; // 2
    protected Calendar data; // 3 e 4
    protected double score; // 6
    protected ListaCast cast; // 8 9 10
    protected double duracao; // 15
    protected boolean lapide;

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

  
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
}
