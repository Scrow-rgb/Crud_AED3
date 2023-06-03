import java.util.HashMap;
import java.util.Map;

public class LZW {
    Map<String, Integer> dictionary = new HashMap<>();

    private ListaLZW compressedData;

    public LZW() {
        dictionary = new HashMap<>();
        compressedData = new ListaLZW();

        // Inicializar o dicionário com os valores iniciais

        // Inicializar o dicionário com os caracteres ASCII
        for (int i = 0; i < 256; i++) {
            dictionary.put(Character.toString((char) i), i);

        }
    }

    public ListaLZW compress(String input) {
        String current = "";
        for (char c : input.toCharArray()) {
            String updated = current + c;
            if (dictionary.containsKey(updated)) {
                current = updated;
            } else {
                compressedData.inserirUltimo(dictionary.get(current));
                dictionary.put(updated, dictionary.size());
                current = Character.toString(c);
            }
        }

        if (!current.equals("")) {
            compressedData.inserirUltimo(dictionary.get(current));
        }

        return compressedData;
    }

    public String decompress(ListaLZW compressedData) {
        StringBuilder decompressed = new StringBuilder();
        Map<Integer, String> reverseDictionary = new HashMap<>();

        // Inverter o dicionário
        for (String key : dictionary.keySet()) {
            reverseDictionary.put(dictionary.get(key), key);
        }

        int previousCode = compressedData.procurarValor(0);
        String previousString = reverseDictionary.get(previousCode);
        decompressed.append(previousString);

        for (int i = 0; i <= compressedData.imprimirTamanho(); i++) {
            int currentCode = compressedData.procurarValor(i);
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

}