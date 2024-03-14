import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 * <p>
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 * <p>
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 * <p>
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 * <p>
 * Written on March 5, 2023 for CS2 @ Menlo School
 * <p>
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);
    }

    public void makeWords(String first, String last) {
        if (last.equals("")) {
            words.add(first);
            return;
        }
        for (int i = 0; i < last.length(); i++) {
            makeWords(first + last.substring(i, i + 1), last.substring(0, i) + last.substring(i + 1));
        }

    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {

        String[] sortedList = mergeSort(0, words.size() - 1);
        for (int i = 0; i < sortedList.length; i++) {
            words.set(i, sortedList[i]);
        }
    }

    public String[] mergeSort(int low, int high) {
        if (low >= high) {
            String[] list = new String[1];
            list[0] = words.get(low);
            return list;
        }
        int mid = (low + high) / 2;
        return merge(mergeSort(low, mid), mergeSort(mid + 1, high));
    }

    public String[] merge(String[] arr1, String[] arr2) {
        String[] newArr = new String[arr1.length + arr2.length];
        int c1 = 0;
        int c2 = 0;
        int c3 = 0;

        while (c2 < arr2.length && c1 < arr1.length) {

            if (arr1[c1].compareTo(arr2[c2]) < 0) {
                newArr[c3++] = arr1[c1++];
            } else {
                newArr[c3++] = arr2[c2++];
            }
        }
        while (c1 < arr1.length) {
            newArr[c3++] = arr1[c1++];
        }
        while (c2 < arr2.length) {
            newArr[c3++] = arr2[c2++];
        }

        return newArr;
    }

    public void swap(int indexOne, int indexTwo) {

    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        for (int i = 0; i < words.size(); i++) {
            if (!binarySearch(words.get(i), 0, words.size())) {
                words.remove(i--);
            }
        }
    }

    public boolean binarySearch(String target, int first, int last) {

        int average = (first + last) / 2;

        if (target.equals(DICTIONARY[(first + last) / 2])) {
            return true;
        } else if (first == last) {
            return false;
        }

        if (target.compareTo(DICTIONARY[average]) > 0) {
            return binarySearch(target, average, last);
        } else {
            return binarySearch(target, first, average);
        }

    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while (s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
