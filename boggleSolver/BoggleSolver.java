import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;

public class BoggleSolver
{
    private TrieStringMap m_dictionary;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        m_dictionary = new TrieStringMap();

        for(String s : dictionary)
            m_dictionary.add(s);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        WordFinder finder = new WordFinder(board, m_dictionary);
        return finder.findWords();
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if(!m_dictionary.contains(word))
            return 0;

        int length = word.length();

        if(length < 3)
            return 0;

        switch (length)
        {
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    public static void main(String[] args)
    {
        BoggleSolver solver = new BoggleSolver(new In("dictionary-yawl.txt").readAllLines());

        Iterable<String> strings = solver.getAllValidWords(new BoggleBoard("board-points2000.txt"));
        for (String s : strings)
            StdOut.println(s);

        int score = 0;

        for(String s : strings)
            score += solver.scoreOf(s);

        StdOut.println("your score is " + score);
    }
}
