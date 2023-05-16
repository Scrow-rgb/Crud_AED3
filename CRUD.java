import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

public class CRUD {

    //Sempre mudar a variável caminho de acordo com o seu caminho
    public  String caminho = "C:/Users/Wander/Documents/Caio/TP1/TP1/Crud_AED3/";
    public static Scanner leitor = new Scanner(System.in);

    public void cargaDeDados() throws IOException {
        BufferedReader conteudoCSV = null;
        String linha = "";
        String divisor = ",";
        int id = 0;
        ListaCast aux;
        Calendar dataficticia = Calendar.getInstance();
        FileOutputStream arq;
        DataOutputStream dos;
        Random rand = new Random();

        try {
            conteudoCSV = new BufferedReader(new FileReader(caminho + "movies.csv"));
            arq = new FileOutputStream(caminho + "filmes.db");
            dos = new DataOutputStream(arq);

            linha = conteudoCSV.readLine();

            while ((linha = conteudoCSV.readLine()) != null && id <= 100) {
                String[] vetor = linha.split(divisor);
                aux = new ListaCast();

                // gerador de datas aleatórias
                long dia = rand.nextLong();
                dataficticia.setTimeInMillis(dia);

                dataficticia.set(Calendar.YEAR, Integer.parseInt(vetor[3]));

                // inserindo participantes na lista Cast
                aux.inserirUltimo(vetor[8] + ",");
                aux.inserirUltimo(vetor[9] + ",");
                aux.inserirUltimo(vetor[10] + ",");

                // Guardando as variáveis do arquivo csv e escrevendo no arquivo db
                Filme f1 = new Filme(id, vetor[0], vetor[2], dataficticia, Double.parseDouble(vetor[6]), aux,
                        Double.parseDouble(vetor[15]), false);

                dos.writeInt(f1.id);
                dos.writeUTF(f1.titulo);
                dos.writeUTF(f1.genero);
                dos.writeLong(f1.data.getTimeInMillis());
                dos.writeDouble(f1.score);
                dos.writeUTF(f1.cast.imprimirNomes());
                dos.writeDouble(f1.duracao);
                dos.writeBoolean(f1.lapide);

                id++;

                // escrevendo os dados no console
                System.out.println(f1.toString());
                f1.cast.imprimirLista();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBounds: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Erro: " + e.getMessage());
        } finally {
            if (conteudoCSV != null) {
                try {
                    conteudoCSV.close();
                } catch (IOException e) {
                    System.out.println("IO Erro: " + e.getMessage());
                }
            }
        }
    }

    public void inserirFilme(Filme inserir) throws IOException {
        RandomAccessFile dos = new RandomAccessFile(caminho + "filmes.db", "rw");
        Filme inserido = inserir;

        dos.seek(dos.length());

        dos.writeInt(inserido.id);
        dos.writeUTF(inserido.titulo);
        dos.writeUTF(inserido.genero);
        dos.writeLong(inserido.data.getTimeInMillis());
        dos.writeDouble(inserido.score);
        dos.writeUTF(inserido.cast.imprimirNomes());
        dos.writeDouble(inserido.duracao);
        dos.writeBoolean(inserido.lapide);

        dos.close();
    }

    public void lerID(int x) throws IOException {
        FileInputStream arq2 = new FileInputStream(caminho + "filmes.db");
        DataInputStream dis = new DataInputStream(arq2);
        String aux;
        int cod = x;

        Filme f2 = new Filme();

        boolean variavelDeControle = false;

        while (dis.available() != 0) {
            f2.id = dis.readInt();
            f2.titulo = dis.readUTF();
            f2.genero = dis.readUTF();
            f2.data.setTimeInMillis(dis.readLong());
            f2.score = dis.readDouble();
            aux = dis.readUTF();
            f2.duracao = dis.readDouble();
            f2.lapide = dis.readBoolean();

            if (f2.id == cod && f2.lapide != true) {
                String[] vaux = aux.split(",");

                for (int i = 0; i < vaux.length; i++) {
                    f2.cast.inserirUltimo(vaux[i] + ",");
                }
                System.out.println(f2.toString());
                f2.cast.imprimirLista();
                variavelDeControle = true;
            }

            f2.cast = new ListaCast();
        }

        if (variavelDeControle == false)
            System.out.println("Registro não encontrado!");
        arq2.close();
        dis.close();

    }

    public void lerArquivo() throws IOException {
        FileInputStream arq2 = new FileInputStream(caminho + "filmes.db");
        DataInputStream dis = new DataInputStream(arq2);
        String aux;

        Filme f2 = new Filme();

        while (dis.available() != 0) {
            f2.id = dis.readInt();
            f2.titulo = dis.readUTF();
            f2.genero = dis.readUTF();
            f2.data.setTimeInMillis(dis.readLong());
            f2.score = dis.readDouble();
            aux = dis.readUTF();
            f2.duracao = dis.readDouble();
            f2.lapide = dis.readBoolean();

            if (f2.lapide != true) {
                String[] vaux = aux.split(",");

                for (int i = 0; i < vaux.length; i++) {
                    f2.cast.inserirUltimo(vaux[i] + ",");
                }
                System.out.println(f2.toString());
                f2.cast.imprimirLista();
            }

            f2.cast = new ListaCast();
        }
        arq2.close();
        dis.close();

    }

    public void atualizarRegistro(int x) throws IOException {
        RandomAccessFile dis = new RandomAccessFile(caminho + "filmes.db", "rw");
        RandomAccessFile arqAux = new RandomAccessFile(caminho + "arquivoAux.db", "rw");

        Filme f2 = new Filme();
        Filme f3 = new Filme();

        long inicio = 0;
        long fim = 0;
        int xid = x;
        String aux;

        while (dis.getFilePointer() < dis.length()) {
            inicio = dis.getFilePointer();
            f2.id = dis.readInt();
            f2.titulo = dis.readUTF();
            f2.genero = dis.readUTF();
            f2.data.setTimeInMillis(dis.readLong());
            f2.score = dis.readDouble();
            aux = dis.readUTF();
            f2.duracao = dis.readDouble();
            f2.lapide = dis.readBoolean();
            fim = dis.getFilePointer();

            if (f2.id == xid && f2.lapide != true) {
                int opcao = -1;

                f3.id = f2.id;
                f3.lapide = f2.lapide;
                f3.titulo = f2.titulo;
                f3.genero = f2.genero;
                f3.data = f2.data;
                f3.score = f2.score;
                f3.duracao = f2.duracao;

                String[] vaux = aux.split(",");

                for (int i = 0; i < vaux.length; i++) {
                    f3.cast.inserirUltimo(vaux[i] + ",");
                }

                while (opcao != 0) {
                    System.out.println(
                            "\nDigite 1 para atualizar o título" +
                                    "\nDigite 2 para atualizar o gênero" +
                                    "\nDigite 3 para atualizar a data de lançamento" +
                                    "\nDigite 4 para atualizar o score" +
                                    "\nDigite 5 para atualizar a duração do filme" +
                                    "\nDigite 6 para atualizar os participantes" +
                                    "\nDigite 0 para voltar ao menu");
                    opcao = leitor.nextInt();

                    if (opcao == 1) {
                        System.out.println("Digite o nome do filme");
                        leitor.nextLine();
                        f3.titulo = leitor.nextLine();
                    }

                    if (opcao == 2) {
                        System.out.println("Digite o gênero do filme");
                        leitor.nextLine();
                        f3.genero = leitor.nextLine();
                    }

                    if (opcao == 3) {
                        System.out.println("Digite o dia do lançamento");
                        f3.data.set(Calendar.DAY_OF_MONTH, leitor.nextInt());

                        System.out.println("Digite o mês do lançamento");
                        f3.data.set(Calendar.MONTH, (leitor.nextInt() - 1));

                        System.out.println("Digite o ano do lançamento");
                        f3.data.set(Calendar.YEAR, leitor.nextInt());
                    }

                    if (opcao == 4) {
                        System.out.println("Digite o score do filme");
                        f3.score = leitor.nextDouble();
                    }

                    if (opcao == 5) {
                        System.out.println("Digite a duração do filme em minutos");
                        f3.duracao = leitor.nextDouble();
                    }

                    if (opcao == 6) {
                        System.out.println("Participantes presentes: ");
                        f3.cast.imprimirLista();

                        int opcao2 = -1;

                        while (opcao2 != 0) {

                            System.out.println(
                                    "\nDigite 1 para deletar um nome\nDigite 2 para acrescentar um nome\nDigite 0 para voltar");
                            opcao2 = leitor.nextInt();

                            if (opcao2 == 1) {
                                System.out.println("Digite a posição do nome que deseja deletar");
                                String n = f3.cast.deletarNome(leitor.nextInt());
                                System.out.println("O seguinte participante foi deletado: " + n);
                            }
                            if (opcao2 == 2) {
                                System.out.println("Digite o nome do novo participante");
                                leitor.nextLine();
                                f3.cast.inserirUltimo(leitor.nextLine());
                            }
                        }
                    }

                }

                break;

            }

        }
        dis.seek(0);
        while (dis.getFilePointer() < inicio) {

            f2.id = dis.readInt();
            f2.titulo = dis.readUTF();
            f2.genero = dis.readUTF();
            f2.data.setTimeInMillis(dis.readLong());
            f2.score = dis.readDouble();
            aux = dis.readUTF();
            f2.duracao = dis.readDouble();
            f2.lapide = dis.readBoolean();

            if (f2.lapide != true) {
                arqAux.writeInt(f2.id);
                arqAux.writeUTF(f2.titulo);
                arqAux.writeUTF(f2.genero);
                arqAux.writeLong(f2.data.getTimeInMillis());
                arqAux.writeDouble(f2.score);
                arqAux.writeUTF(aux);
                arqAux.writeDouble(f2.duracao);
                arqAux.writeBoolean(f2.lapide);
            }
        }

        dis.seek(fim);

        arqAux.writeInt(f3.id);
        arqAux.writeUTF(f3.titulo);
        arqAux.writeUTF(f3.genero);
        arqAux.writeLong(f3.data.getTimeInMillis());
        arqAux.writeDouble(f3.score);
        arqAux.writeUTF(f3.cast.imprimirNomes());
        arqAux.writeDouble(f3.duracao);
        arqAux.writeBoolean(f3.lapide);

        while (dis.getFilePointer() < dis.length()) {

            f2.id = dis.readInt();
            f2.titulo = dis.readUTF();
            f2.genero = dis.readUTF();
            f2.data.setTimeInMillis(dis.readLong());
            f2.score = dis.readDouble();
            aux = dis.readUTF();
            f2.duracao = dis.readDouble();
            f2.lapide = dis.readBoolean();

            if (f2.lapide != true) {
                arqAux.writeInt(f2.id);
                arqAux.writeUTF(f2.titulo);
                arqAux.writeUTF(f2.genero);
                arqAux.writeLong(f2.data.getTimeInMillis());
                arqAux.writeDouble(f2.score);
                arqAux.writeUTF(aux);
                arqAux.writeDouble(f2.duracao);
                arqAux.writeBoolean(f2.lapide);
            }
        }

        dis.seek(0);
        arqAux.seek(0);
        dis.setLength(0);

        while (arqAux.getFilePointer() < arqAux.length()) {
            dis.writeInt(arqAux.readInt());
            dis.writeUTF(arqAux.readUTF());
            dis.writeUTF(arqAux.readUTF());
            dis.writeLong(arqAux.readLong());
            dis.writeDouble(arqAux.readDouble());
            dis.writeUTF(arqAux.readUTF());
            dis.writeDouble(arqAux.readDouble());
            dis.writeBoolean(arqAux.readBoolean());
        }

        arqAux.setLength(0);

        arqAux.close();
        dis.close();
    }

    public void apagarRegistro(int x) throws IOException {
        RandomAccessFile dis = new RandomAccessFile(caminho + "filmes.db",
                "rw");

        Filme f2 = new Filme();

        int xid = x;

        String aux;

        while (dis.getFilePointer() < dis.length()) {

            f2.id = dis.readInt();
            f2.titulo = dis.readUTF();
            f2.genero = dis.readUTF();
            f2.data.setTimeInMillis(dis.readLong());
            f2.score = dis.readDouble();
            aux = dis.readUTF();
            f2.duracao = dis.readDouble();
            long ponteiro = dis.getFilePointer();
            f2.lapide = dis.readBoolean();

            String[] vaux = aux.split(",");

            for (int i = 0; i < vaux.length; i++) {
                f2.cast.inserirUltimo(vaux[i] + ",");
            }

            if (f2.id == xid && f2.lapide != true) {

                dis.seek(ponteiro);

                dis.writeBoolean(true);

                System.out.println("Arquivo removido!");
                break;

            }

            f2.cast = new ListaCast();

        }

        dis.close();
    }

    public void Menu() {

        System.out.println("\nDigite 1 para fazer a carga de dados");
        System.out.println("Digite 2 para adicionar um filme ao arquivo");
        System.out.println("Digite 3 para ler id no arquivo");
        System.out.println("Digite 4 para ler o arquivo completo");
        System.out.println("Digite 5 para atualizar um registro");
        System.out.println("Digite 6 para deletar um registro");
        System.out.println("Digite 7 para compactar o arquivo");
        System.out.println("Digite 8 para descompactar o arquivo");
        System.out.println("Digite 0 para sair");

    }
}
