import java.util.Calendar;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.RandomAccessFile;

public class CRUD {
    private static Scanner leitor = new Scanner(System.in);
    // sempre mudar o caminho do arquivo para o caminho correto do seu computador
    private static String caminho = "C:/Users/Wander/Documents/Caio/Crud_AED3/";
    private static LZW compactador;

    // carrega os primeiros 100 filmes da database para o arquivo
    public void cargaDeDados() throws IOException {
        BufferedReader conteudoCSV = null;
        String linha = "";
        String divisor = ",";
        int id = 1;
        long pos;
        ListaCast aux;
        Calendar dataficticia = Calendar.getInstance();

        RandomAccessFile file1 = new RandomAccessFile(caminho + "filmes.db", "rw");
        RandomAccessFile file2 = new RandomAccessFile(caminho + "index.db", "rw");

        Random rand = new Random();

        try {
            conteudoCSV = new BufferedReader(new FileReader(caminho + "movies.csv"));

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

                pos = file1.getFilePointer();

                file1.writeInt(f1.id);
                file1.writeUTF(f1.titulo);
                file1.writeUTF(f1.genero);
                file1.writeLong(f1.data.getTimeInMillis());
                file1.writeDouble(f1.score);
                file1.writeUTF(f1.cast.imprimirNomes());
                file1.writeDouble(f1.duracao);
                file1.writeBoolean(f1.lapide);

                // preenchendo arquivo de indices
                file2.writeInt(f1.id);
                file2.writeLong(pos);
                file2.writeBoolean(false);

                System.out.println(id);

                id++;

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

        file1.close();
        file2.close();
    }

    public void inserirFilme(Filme inserir) throws IOException {
        RandomAccessFile dos = new RandomAccessFile(caminho + "filmes.db", "rw");
        RandomAccessFile index = new RandomAccessFile(caminho + "index.db", "rw");
        Filme inserido = inserir;
        long pos;

        dos.seek(dos.length());
        index.seek(index.length());
        pos = dos.length();

        dos.writeInt(inserido.id);
        dos.writeUTF(inserido.titulo);
        dos.writeUTF(inserido.genero);
        dos.writeLong(inserido.data.getTimeInMillis());
        dos.writeDouble(inserido.score);
        dos.writeUTF(inserido.cast.imprimirNomes());
        dos.writeDouble(inserido.duracao);
        dos.writeBoolean(inserido.lapide);

        // preenchendo arquivo de indices
        index.writeInt(inserido.id);
        index.writeLong(pos);
        index.writeBoolean(false);

        dos.close();
        index.close();
    }

    public void lerID(int x) throws IOException {
        RandomAccessFile dis = new RandomAccessFile(caminho + "filmes.db", "rw");
        RandomAccessFile file2 = new RandomAccessFile(caminho + "index.db", "rw");
        String aux;
        int cod = x;

        Filme f2 = new Filme();

        boolean variavelDeControle = false;

        // procura o id no arquivo de indices e pega a pos no arquivo principal
        while (file2.getFilePointer() < file2.length()) {
            int idIndex = file2.readInt();
            long posIndex = file2.readLong();
            boolean lapideIndex = file2.readBoolean();

            // puxa o filme do arquivo principal
            if (idIndex == cod && lapideIndex != true) {

                dis.seek(posIndex);

                f2.id = dis.readInt();
                f2.titulo = dis.readUTF();
                f2.genero = dis.readUTF();
                f2.data.setTimeInMillis(dis.readLong());
                f2.score = dis.readDouble();
                aux = dis.readUTF();
                f2.duracao = dis.readDouble();
                f2.lapide = dis.readBoolean();

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

        file2.close();
        dis.close();

    }

    // criado apenas para testar a funcionalidade do arquivo de indices
    public void lerArquivoIndex() throws IOException {
        RandomAccessFile dis = new RandomAccessFile(caminho + "index.db", "rw");
        int id;
        long pos;
        boolean lap;

        while (dis.getFilePointer() < dis.length()) {
            id = dis.readInt();
            pos = dis.readLong();
            lap = dis.readBoolean();

            System.out.println(id + " " + pos + " " + lap);
        }
        dis.close();

    }

    // leitura total do arquivo
    public void lerArquivo() throws IOException {
        RandomAccessFile arq = new RandomAccessFile(caminho + "filmes.db", "rw");
        String aux;
        Filme f2 = new Filme();

        while (arq.getFilePointer() < arq.length()) {
            f2.id = arq.readInt();
            f2.titulo = arq.readUTF();
            f2.genero = arq.readUTF();
            f2.data.setTimeInMillis(arq.readLong());
            f2.score = arq.readDouble();
            aux = arq.readUTF();
            f2.duracao = arq.readDouble();
            f2.lapide = arq.readBoolean();

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
        arq.close();

    }

    // atualiza registros e remove lapides

    public void atualizarRegistro(int x) throws IOException {
        RandomAccessFile dis = new RandomAccessFile(caminho + "filmes.db", "rw");
        RandomAccessFile arqAux = new RandomAccessFile(caminho + "arquivoAux.db", "rw");
        RandomAccessFile indexFile = new RandomAccessFile(caminho + "index.db", "rw");

        Filme f2 = new Filme();
        Filme f3 = new Filme();

        long inicio = 0;
        long fim = 0;
        long posAux;
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

                // inicio do loop de atualizaçao de registro
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
        indexFile.setLength(0);

        // inicio do processso de reescrita do arquivo principal e remoçao das lapides

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

        arqAux.seek(0);
        dis.setLength(0);
        int idAux;

        while (arqAux.getFilePointer() < arqAux.length()) {

            posAux = dis.getFilePointer();

            idAux = arqAux.readInt();

            dis.writeInt(idAux);
            dis.writeUTF(arqAux.readUTF());
            dis.writeUTF(arqAux.readUTF());
            dis.writeLong(arqAux.readLong());
            dis.writeDouble(arqAux.readDouble());
            dis.writeUTF(arqAux.readUTF());
            dis.writeDouble(arqAux.readDouble());
            dis.writeBoolean(arqAux.readBoolean());

            indexFile.writeInt(idAux);
            indexFile.writeLong(posAux);
            indexFile.writeBoolean(false);
        }

        arqAux.setLength(0);

        arqAux.close();
        dis.close();
        indexFile.close();
    }

    public void apagarRegistro(int x) throws IOException {
        RandomAccessFile dis = new RandomAccessFile(caminho + "filmes.db", "rw");
        RandomAccessFile file2 = new RandomAccessFile(caminho + "index.db", "rw");

        Filme f2 = new Filme();

        int xid = x;

        int idIndex;
        long posIndex, posAux;
        boolean lapideIndex;

        String aux;

        while (file2.getFilePointer() < file2.length()) {

            idIndex = file2.readInt();
            posIndex = file2.readLong();
            posAux = file2.getFilePointer();
            lapideIndex = file2.readBoolean();

            if (idIndex == xid && lapideIndex != true) {

                dis.seek(posIndex);

                f2.id = dis.readInt();
                f2.titulo = dis.readUTF();
                f2.genero = dis.readUTF();
                f2.data.setTimeInMillis(dis.readLong());
                f2.score = dis.readDouble();
                aux = dis.readUTF();
                f2.duracao = dis.readDouble();
                dis.writeBoolean(true);
                f2.lapide = dis.readBoolean();

                System.out.println("O seguinte filme foi removido do arquivo:");

                String[] vaux = aux.split(",");

                for (int i = 0; i < vaux.length; i++) {
                    f2.cast.inserirUltimo(vaux[i] + ",");
                }

                System.out.println(f2.toString());
                f2.cast.imprimirLista();

                file2.seek(posAux);
                file2.writeBoolean(true);

                break;

            }

            f2.cast = new ListaCast();

        }

        dis.close();
        file2.close();
    }

    public void removerLapides() throws IOException {
        RandomAccessFile arq1 = new RandomAccessFile(caminho + "filmes.db", "rw");
        RandomAccessFile arq2 = new RandomAccessFile(caminho + "arquivoAux.db", "rw");
        RandomAccessFile arq3 = new RandomAccessFile(caminho + "index.db", "rw");

        arq3.setLength(0);

        Filme f1 = new Filme();
        String aux;

        while (arq1.getFilePointer() < arq1.length()) {

            f1.id = arq1.readInt();
            f1.titulo = arq1.readUTF();
            f1.genero = arq1.readUTF();
            f1.data.setTimeInMillis(arq1.readLong());
            f1.score = arq1.readDouble();
            aux = arq1.readUTF();
            f1.duracao = arq1.readDouble();
            f1.lapide = arq1.readBoolean();

            if (f1.lapide != true) {
                arq2.writeInt(f1.id);
                arq2.writeUTF(f1.titulo);
                arq2.writeUTF(f1.genero);
                arq2.writeLong(f1.data.getTimeInMillis());
                arq2.writeDouble(f1.score);
                arq2.writeUTF(aux);
                arq2.writeDouble(f1.duracao);
                arq2.writeBoolean(f1.lapide);
            }

        }

        arq1.setLength(0);

        int idAux;
        long posAux;

        while (arq2.getFilePointer() < arq2.length()) {
            posAux = arq1.getFilePointer();

            idAux = arq2.readInt();

            arq1.writeInt(idAux);
            arq1.writeUTF(arq2.readUTF());
            arq1.writeUTF(arq2.readUTF());
            arq1.writeLong(arq2.readLong());
            arq1.writeDouble(arq2.readDouble());
            arq1.writeUTF(arq2.readUTF());
            arq1.writeDouble(arq2.readDouble());
            arq1.writeBoolean(arq2.readBoolean());

            arq3.writeInt(idAux);
            arq3.writeLong(posAux);
            arq3.writeBoolean(false);
        }

        arq1.close();
        arq2.close();
        arq3.close();

    }

    public void compactar() throws IOException {
        // removerLapides();

        RandomAccessFile arquivo1 = new RandomAccessFile(caminho + "filmes.db", "rw");
        RandomAccessFile arquivo2 = new RandomAccessFile(caminho + "filmesCompactado.db", "rw");

        compactador = new LZW();

        String linha = "";
        ListaLZW listaAux;
        long tamanhoArq = arquivo1.length();
        System.out.println(tamanhoArq);

        while (arquivo1.getFilePointer() < arquivo1.length()) {

            arquivo1.readInt();
            linha += arquivo1.readUTF() + "#";
            linha += arquivo1.readUTF() + "#";
            linha += arquivo1.readLong() + "#";
            linha += arquivo1.readDouble() + "#";
            linha += arquivo1.readUTF() + "#";
            linha += arquivo1.readDouble() + "#" + "@";
            arquivo1.readBoolean();
        }

        listaAux = compactador.compress(linha);

        for (int i = 0; i < listaAux.tamanho; i++) {
            arquivo2.writeShort((short) listaAux.procurarValor(i));
        }

        long tamanhoArq2 = arquivo2.length();

        System.out.println("Arquivo compactado! Eficiência de: " + Math.round(tamanhoArq2 * 100) / tamanhoArq + "%");

        arquivo1.setLength(0);

        arquivo1.close();
        arquivo2.close();

    }

    public void descompactar() throws IOException {
        RandomAccessFile arquivo1 = new RandomAccessFile(caminho + "filmes.db", "rw");
        RandomAccessFile arquivo2 = new RandomAccessFile(caminho + "filmesCompactado.db", "rw");

        // Filme f1 = new Filme();

        ListaLZW listaAux = new ListaLZW();
        String aux = "";
        String[] linhaFilme;
        String[] filme;
        int i = 1;

        while (arquivo2.getFilePointer() < arquivo2.length()) {
            listaAux.inserirUltimo((int) arquivo2.readShort());
        }

        aux = compactador.decompress(listaAux);

        linhaFilme = aux.split("@");

        for (int j = 0; j < linhaFilme.length; j++) {
            filme = linhaFilme[j].split("#");

            arquivo1.writeInt(i);
            arquivo1.writeUTF(filme[0]);
            arquivo1.writeUTF(filme[1]);
            arquivo1.writeLong(Long.parseLong(filme[2]));
            arquivo1.writeDouble(Double.parseDouble(filme[3]));
            arquivo1.writeUTF(filme[4]);
            arquivo1.writeDouble(Double.parseDouble(filme[5]));
            arquivo1.writeBoolean(false);

            i++;
        }

        arquivo2.setLength(0);

        arquivo1.close();
        arquivo2.close();

    }

    public void Criptografar() throws IOException {

        RandomAccessFile arq = new RandomAccessFile(caminho + "filmes.db", "rw");
        String chave = "sorvete";
        AES crip = new AES();

        String tituloCrip;
        String aux;
        Filme f1 = new Filme();

        while (arq.getFilePointer() < arq.length()) {
            f1.id = arq.readInt();
            f1.titulo = arq.readUTF();
            tituloCrip = crip.encrypt(f1.titulo, chave);
            f1.genero = arq.readUTF();
            f1.data.setTimeInMillis(arq.readLong());
            f1.score = arq.readDouble();
            aux = arq.readUTF();
            f1.duracao = arq.readDouble();
            f1.lapide = arq.readBoolean();

            f1.setTitulo(tituloCrip);

            if (f1.lapide != true) {
                String[] vaux = aux.split(",");

                for (int i = 0; i < vaux.length; i++) {
                    f1.cast.inserirUltimo(vaux[i] + ",");
                }
                System.out.println(f1.toString());
                f1.cast.imprimirLista();
            }

            f1.cast = new ListaCast();
        }
        arq.close();

    }

    public void Descriptografar() throws IOException {
        RandomAccessFile arq = new RandomAccessFile(caminho + "filmes.db", "rw");
        String chave = "sorvete";
        AES crip = new AES();

        String tituloCrip;
        String tituloDecript;
        String aux;
        Filme f1 = new Filme();

        while (arq.getFilePointer() < arq.length()) {
            f1.id = arq.readInt();
            f1.titulo = arq.readUTF();
            tituloCrip = crip.encrypt(f1.titulo, chave);
            f1.genero = arq.readUTF();
            f1.data.setTimeInMillis(arq.readLong());
            f1.score = arq.readDouble();
            aux = arq.readUTF();
            f1.duracao = arq.readDouble();
            f1.lapide = arq.readBoolean();

            tituloDecript = crip.decrypt(tituloCrip, chave);

            f1.setTitulo(tituloDecript);

            if (f1.lapide != true) {
                String[] vaux = aux.split(",");

                for (int i = 0; i < vaux.length; i++) {
                    f1.cast.inserirUltimo(vaux[i] + ",");
                }
                System.out.println(f1.toString());
                f1.cast.imprimirLista();
            }

            f1.cast = new ListaCast();
        }
        arq.close();

    }

    public void Menu() {
        System.out.println("\nDigite 1 para fazer a carga de dados");
        System.out.println("Digite 2 para adicionar um filme ao arquivo");
        System.out.println("Digite 3 para ler id no arquivo");
        System.out.println("Digite 4 para ler o arquivo completo");
        System.out.println("Digite 5 para atualizar um registro");
        System.out.println("Digite 6 para deletar um registro");
        System.out.println("Digite 7 para compactar");
        System.out.println("Digite 8 para descompactar");
        System.out.println("Digite 9 para fazer a criptografia");
        System.out.println("Digite 10 para descriptografar");
        System.out.println("Digite 11 para ler o arquivo index");
        System.out.println("Digite 0 para sair");
    }

}
