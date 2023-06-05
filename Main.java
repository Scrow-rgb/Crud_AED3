import java.io.File;
import java.security.CryptoPrimitive;
import java.util.Calendar;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        int x = -1;
        int auxID = 100;
        System.out.println("");
        Scanner leitor = new Scanner(System.in);
        CRUD crud = new CRUD();
        Filme filmes = new Filme();
        boolean sinal = false;
        AES crip = new AES();
        final String chave = "sorvete";
        String tituloCriptografado;
        String tituloDescriptografado;

        do {

            crud.Menu();
            System.out.println("Informe a opcao desejada");
            x = leitor.nextInt();

            switch (x) {

                case 0:

                    System.out.println("Programa finalizado!! ");
                    break;
                case 1:

                    System.out.println("Fazendo a carga de dados...");

                    crud.cargaDeDados();

                    break;

                case 2: {

                    auxID++;
                    Filme f1 = new Filme();

                    f1.id = auxID;

                    System.out.println("Digite o nome do filme");
                    leitor.nextLine();
                    String titulo = leitor.nextLine();

                    System.out.println("Digite o genero");
                    f1.genero = leitor.next();

                    System.out.println("Digite o dia do lançamento");
                    f1.data.set(Calendar.DAY_OF_MONTH, leitor.nextInt());

                    System.out.println("Digite o mês do lançamento");
                    f1.data.set(Calendar.MONTH, (leitor.nextInt() - 1));

                    System.out.println("Digite o ano do lançamento");
                    f1.data.set(Calendar.YEAR, leitor.nextInt());

                    System.out.println("Digite o score do filme");
                    f1.score = leitor.nextDouble();

                    System.out.println("Digite a duração do filme em minutos");
                    f1.duracao = leitor.nextDouble();

                    f1.lapide = false;

                    int inserir = -1;

                    System.out.println("\n\nDigite nomes importantes para o filme");

                    while (inserir != 0) {
                        System.out.println("Digite 1 para adicionar um nome");
                        System.out.println("Digite 0 para sair da inserção de nomes");
                        inserir = leitor.nextInt();

                        if (inserir == 1) {
                            System.out.println("Digite o nome");
                            leitor.nextLine();
                            f1.cast.inserirUltimo(leitor.nextLine() + ",");
                        }

                    }

                    f1 = new Filme(f1.id, titulo, f1.genero, f1.data, f1.score, f1.cast, f1.duracao, f1.lapide);

                    titulo = f1.getTitulo();
                    tituloCriptografado = crip.encrypt(titulo, chave);
                    f1.setTitulo(tituloCriptografado);

                    f1 = new Filme(f1.id, tituloCriptografado, f1.genero, f1.data, f1.score, f1.cast, f1.duracao,
                            f1.lapide);

                    crud.inserirFilme(f1);

                    tituloDescriptografado = crip.decrypt(tituloCriptografado, chave);

                    f1 = new Filme(f1.id, tituloDescriptografado, f1.genero, f1.data, f1.score, f1.cast, f1.duracao,
                            f1.lapide);

                    crud.inserirFilme(f1);

                    crud.lerID(f1.id);

                }

                    break;

                case 3: {
                    Filme f1 = new Filme();

                    System.out.println("Digite o id que deseja buscar");
                    f1.id = leitor.nextInt();

                    System.out.println("Buscando registro...");

                    crud.lerID(f1.id);

                }
                    break;

                case 4:

                    System.out.println("Lendo arquivo completo...");

                    crud.lerArquivo();
                    break;

                case 5:

                    System.out.println("Digite o id que deseja atualizar");
                    crud.atualizarRegistro(leitor.nextInt());

                    break;

                case 6:

                    System.out.println("Digite o id do registro que deseja apagar");
                    crud.apagarRegistro(leitor.nextInt());

                    break;

                case 7: {

                    if (sinal == false) {
                        crud.compactar();
                        sinal = true;
                    } else
                        System.out.println("Arquivo ja compactado!");

                }
                    break;

                case 8: {

                    if (sinal == true) {
                        crud.descompactar();
                        sinal = false;
                    } else
                        System.out.println("O arquivo não está compactado!");

                }

                    break;

                case 9:

                    crud.lerArquivoIndex();

                    break;

                default:

                    System.out.println("Opcao invalida!!");
            }

        } while (x != 0);

        leitor.close();
    }
}
