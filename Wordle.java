import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Wordle {

    // Reads all words from dictionary filename into a String array.
    public static String[] readDictionary(String filename) throws IOException {
        Path filePath = Paths.get(filename);
        return Files.readAllLines(filePath).toArray(new String[0]);
    }

    // Choose a random secret word from the dictionary.
    // Hint: Pick a random index between 0 and dict.length (not including) using
    // Math.random()
    public static String chooseSecretWord(String[] dict) throws IOException {
        int index = (int) (Math.random() * dict.length);
        return dict[index];
    }

    // Simple helper: check if letter c appears anywhere in secret (true), otherwise
    // return false.
    public static boolean containsChar(String secret, char c) {
        // secret = secret.toLowerCase();
        // c = Character.toLowerCase(c);

        Boolean isContains = false;
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == c) {
                isContains = true;
            }
        }
        return isContains;
    }

    // Compute feedback for a single guess into resultRow.
    // G for exact match, Y if letter appears anywhere else, _ otherwise.
    public static void computeFeedback(String secret, String guess, char[] resultRow) {
        // you may want to use containsChar in your implementation
        secret = secret.toLowerCase();
        guess = guess.toLowerCase();
        for (int i = 0; i < secret.length(); i++) {
            char result = '_';
            if (secret.charAt(i) == guess.charAt(i)) {
                result = 'G';
            } else if (containsChar(secret, guess.charAt(i))) {
                result = 'Y';
            }
            resultRow[i] = result;
        }
    }

    // Store guess string (chars) into the given row of guesses 2D array.
    // For example, of guess is HELLO, and row is 2, then after this function
    // guesses should look like:
    // guesses[2][0] // 'H'
    // guesses[2][1] // 'E'
    // guesses[2][2] // 'L'
    // guesses[2][3] // 'L'
    // guesses[2][4] // 'O'
    public static void storeGuess(String guess, char[][] guesses, int row) {
        for (int i = 0; i < guess.length(); i++) {
            guesses[row][i] = guess.charAt(i);
        }
    }

    // Prints the game board up to currentRow (inclusive).
    public static void printBoard(char[][] guesses, char[][] results, int currentRow) {
        System.out.println("Current board:");
        for (int row = 0; row <= currentRow; row++) {
            System.out.print("Guess " + (row + 1) + ": ");
            for (int col = 0; col < guesses[row].length; col++) {
                System.out.print(guesses[row][col]);
            }
            System.out.print("   Result: ");
            for (int col = 0; col < results[row].length; col++) {
                System.out.print(results[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    // Returns true if all entries in resultRow are 'G'.
    public static boolean isAllGreen(char[] resultRow) {
        Boolean isAllTrue = true;
        for (int i = 0; i < resultRow.length; i++) {
            if (resultRow[i] != 'G') {
                isAllTrue = false;
            }
        }
        return isAllTrue;
    }

    public static void main(String[] args) throws IOException {

        int WORD_LENGTH = 5;
        int MAX_ATTEMPTS = 6;

        // Read dictionary
        String[] dict = readDictionary("dictionary.txt");

        // Choose secret word
        String secret = chooseSecretWord(dict);

        // Prepare 2D arrays for guesses and results
        char[][] guesses = new char[WORD_LENGTH][WORD_LENGTH];
        char[][] results = new char[WORD_LENGTH][WORD_LENGTH];

        // Prepare to read from the standart input
        In inp = new In();

        int attempt = 0;
        boolean won = false;

        while (attempt < MAX_ATTEMPTS && !won) {

            String guess = "";
            boolean valid = false;

            // Loop until you read a valid guess
            while (!valid) {
                System.out.print("Enter your guess (5-letter word): ");
                guess = inp.readLine();

                if (guess.length() != WORD_LENGTH || !guess.matches("^[a-zA-Z]+$")) {
                    System.out.println("Invalid word. Please try again.");
                } else {
                    valid = true;
                }
            }

            // Store guess and compute feedback
            // ... use storeGuess and computeFeedback
            storeGuess(guess, guesses, attempt);
            computeFeedback(secret, guess, null);

            // Print board
            printBoard(guesses, results, attempt);

            // Check win
            if (isAllGreen(results[attempt])) {
                System.out.println("Congratulations! You guessed the word in " + (attempt + 1) + " attempts.");
                won = true;
            }

            attempt++;
        }

        if (!won) {
            // ... follow the assignment examples for how the printing should look like
            System.out.println(String.format("Sorry, you did not guess the word.\r\n The secret word was: %s",
                    secret));
        }

        inp.close();
    }
}