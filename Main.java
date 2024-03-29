import java.util.Calendar;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        int x = -1;
        int auxID = 100;
        System.out.println("");
        Scanner leitor = new Scanner(System.in);
        CRUD crud = new CRUD();
        boolean sinal = false;

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
                    crud.Criptografar();

                    break;

                case 2: {

                    auxID++;
                    Filme f1 = new Filme();

                    f1.id = auxID;

                    System.out.println("Digite o nome do filme");
                    leitor.nextLine();
                    f1.titulo = leitor.nextLine();

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

                    crud.Descriptografar();
                    crud.inserirFilme(f1);

                    crud.lerID(f1.id);

                    crud.Criptografar();

                }

                    break;

                case 3: {
                    Filme f1 = new Filme();

                    System.out.println("Digite o id que deseja buscar");
                    f1.id = leitor.nextInt();

                    System.out.println("Buscando registro...");

                    crud.Descriptografar();
                    crud.lerID(f1.id);
                    crud.Criptografar();

                }
                    break;

                case 4:

                    System.out.println("Lendo arquivo completo...");

                    crud.lerArquivo();
                    break;

                case 5:

                    System.out.println("Digite o id que deseja atualizar");

                    crud.Descriptografar();
                    crud.atualizarRegistro(leitor.nextInt());
                    crud.Criptografar();

                    break;

                case 6:

                    System.out.println("Digite o id do registro que deseja apagar");

                    crud.Descriptografar();
                    crud.apagarRegistro(leitor.nextInt());
                    crud.Criptografar();

                    break;

                case 7: {

                    if (sinal == false) {
                        crud.Descriptografar();
                        crud.compactar();
                        sinal = true;
                    } else
                        System.out.println("Arquivo ja compactado!");

                }
                    break;

                case 8: {

                    if (sinal == true) {
                        crud.descompactar();
                        crud.Criptografar();
                        sinal = false;
                    } else
                        System.out.println("O arquivo não está compactado!");

                }

                    break;

                case 9:
                    System.out.println("Digite o padrão que deseja procurar:");
                    leitor.nextLine();
                    crud.Descriptografar();
                    crud.KMP(leitor.nextLine());
                    crud.Criptografar();
                    break;

                case 10:
                    crud.Criptografar();
                    break;

                case 11:

                    crud.Descriptografar();

                    break;

                case 12:
                    crud.lerArquivoIndex();

                    break;

                default:

                    System.out.println("Opcao invalida!!");
            }

        } while (x != 0);

        leitor.close();
    }
}
