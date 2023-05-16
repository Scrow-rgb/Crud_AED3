import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LZW {
    Map<String, Integer> dictionary = new HashMap<>();

    private List<Integer> compressedData;

    public LZW() {
        dictionary = new HashMap<>();
        compressedData = new ArrayList<>();


       

        // Inicializar o dicionário com os valores iniciais
        char[] iniciarDicionario = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                'V', 'W', 'X', 'Y', 'Z', ' ', '(', ')', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ',', '/',
                '|', ';', '"', '.', '!', ':', '\n', '\'', 'Ö', 'ö', '&', 'é', 'É', '?'};



        // Inicializar o dicionário com os caracteres ASCII
        for (int i = 0; i < iniciarDicionario.length; i++) {
            dictionary.put(Character.toString(iniciarDicionario[i]), i);

        }
    }

    public List<Integer> compress(String input) {
        String current = "";
        for (char c : input.toCharArray()) {
            String updated = current + c;
            if (dictionary.containsKey(updated)) {
                current = updated;
            } else {
                compressedData.add(dictionary.get(current));
                dictionary.put(updated, dictionary.size());
                current = Character.toString(c);
            }
        }

        if (!current.equals("")) {
            compressedData.add(dictionary.get(current));
        }

        return compressedData;
    }

    public String decompress(List<Integer> compressedData) {
        StringBuilder decompressed = new StringBuilder();
        Map<Integer, String> reverseDictionary = new HashMap<>();

        // Inverter o dicionário
        for (String key : dictionary.keySet()) {
            reverseDictionary.put(dictionary.get(key), key);
        }

        int previousCode = compressedData.get(0);
        String previousString = reverseDictionary.get(previousCode);
        decompressed.append(previousString);

        for (int i = 1; i < compressedData.size(); i++) {
            int currentCode = compressedData.get(i);
            String currentString;

            if (reverseDictionary.containsKey(currentCode)) {
                currentString = reverseDictionary.get(currentCode);
            } else {
                currentString = previousString + previousString.charAt(0);
            }

            decompressed.append(currentString);

            reverseDictionary.put(dictionary.size(), previousString + currentString.charAt(0));
            dictionary.put(previousString + currentString.charAt(0), dictionary.size());

            previousString = currentString;
        }

        return decompressed.toString();
    }

    public static void main(String[] args) throws IOException {
        LZW lzw = new LZW();

        Scanner leitor = new Scanner(System.in);
        String arquivoCompactado = "compact.db";
        String arquivoDescompactado = "decompress.db";

        System.out.println("Informe as frases a serem compactadas (digite 'fim' para encerrar):");

        List<String> frases = new ArrayList<>();
        String input;
        while (!(input = leitor.nextLine()).equalsIgnoreCase("fim")) {
            frases.add(input);
        }

        StringBuilder sb = new StringBuilder();
        for (String frase : frases) {
            sb.append(frase).append("\n");
        }
        String textoCompactar = sb.toString().trim();

        List<Integer> compressed = lzw.compress(textoCompactar);

        OutputStream saida = new FileOutputStream(arquivoCompactado);
        DataOutputStream dataOutputStream = new DataOutputStream(saida);
        for (int i : compressed) {
            dataOutputStream.writeByte(i);
        }
        dataOutputStream.close();

        OutputStream out = new FileOutputStream(arquivoDescompactado);
        String textoDescompactado = lzw.decompress(compressed);
        byte[] bytes = textoDescompactado.getBytes();
        out.write(bytes);
        out.close();

        System.out.println();
        System.out.println("Frases Compactadas: " + compressed);

        System.out.println();
        System.out.println("Texto Descompactado: " + textoDescompactado);

        leitor.close();
    }   
}