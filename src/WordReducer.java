import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class WordReducer {
    private static final String WORDS_URL = "https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt";
    private static final Set<String> wordSet = new HashSet<>();

    public static void main(String[] args) throws IOException {
        long startTime = System.nanoTime();
        loadAllWords();
        List<String> validWords = findReducibleWords();
        long endTime = System.nanoTime();

        // Резултати
        validWords.forEach(System.out::println);
        System.out.println("Found " + validWords.size() + " reducible 9-letters words.");
        System.out.println("Execution time: " + (endTime - startTime) / 1000000 + " ms");

    }

    /**
     * 🚀 Зарежда думите и ги съхранява в HashSet
     */
    private static void loadAllWords() throws IOException {
        URL url = new URL(WORDS_URL);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {
            wordSet.addAll(reader.lines().map(String::trim).map(String::toLowerCase).collect(Collectors.toSet()));
        }
    }

    /**
     * 🔍 Намира всички 9-буквени думи, които могат да бъдат редуцирани
     */
    private static List<String> findReducibleWords() {
        List<String> result = new ArrayList<>();
        for (String word : wordSet) {
            if (word.length() == 9 && isReducible(word)) {
                result.add(word);
            }
        }
        return result;
    }

    /**
     * ✅ Проверява дали дадена дума може да бъде сведена до "i" или "a"
     */
    private static boolean isReducible(String word) {

        // Ако думата е само една буква, трябва да е валидна (I или A)
        if (word.length() == 1) {
            return word.equals("i") || word.equals("a");
        }

        // Премахваме всяка буква от думата и проверяваме дали остава валидна дума
        boolean validReductionFound = false;
        for (int i = 0; i < word.length(); i++) {
            String reducedWord = word.substring(0, i) + word.substring(i + 1);
            if (wordSet.contains(reducedWord) || reducedWord.equals("i") || reducedWord.equals("a")) {
                // Ако редуцираната дума е валидна, проверяваме за следващата стъпка
                if (isReducible(reducedWord)) {
                    validReductionFound = true;
                    break;  // Намерена е валидна редукция, продължаваме с друга буква
                }
            }
        }

        return validReductionFound;  // Ако намерим поне една валидна редукция
    }
}
