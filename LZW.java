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
                'V', 'W', 'X', 'Y', 'Z', ' ', '(', ')'};

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

    public static void main(String[] args) {
        LZW lzw = new LZW();

        Scanner leitor = new Scanner(System.in);

        System.out.println("Informe a palavra a ser compactada");

        String input = leitor.nextLine();
        List<Integer> compressed = lzw.compress(input);

        System.out.println("Compressed Data: " + compressed);
        System.out.println("Decompressed Data: " + lzw.decompress(compressed));

        leitor.close();
    }
}
