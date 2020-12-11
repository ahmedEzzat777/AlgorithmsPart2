import java.util.ArrayList;
import java.util.HashSet;

public class WordFinder {
    private BoggleBoard m_board;
    private TrieStringMap m_dictionary;
    private boolean[][] m_markedNodes;
    private char Q = 'Q';
    private char U = 'U';

    public WordFinder(BoggleBoard board, TrieStringMap dictionary)
    {
        m_board = board;
        m_dictionary = dictionary;
    }

    public Iterable<String> findWords()
    {
        m_markedNodes = new boolean[m_board.rows()][m_board.cols()];

        HashSet<String> set = new HashSet<>();
        for(int i = 0; i < m_board.rows(); i++)
        {
            for (int j = 0; j < m_board.cols(); j++)
                findWords(set, i, j);
        }

        m_markedNodes = null;
        return set;
    }

    private void findWords(HashSet<String> set, int i, int j)
    {
        char letter = m_board.getLetter(i, j);

        TrieStringMap.TrieNode dictionaryNode = m_dictionary.getRoot();
        dictionaryNode = m_dictionary.getNode(dictionaryNode, letter);
        dictionaryNode = handleQu(dictionaryNode, letter);
        SearchNode searchNode = new SearchNode(m_board.rows(), m_board.cols(), i, j);

        DFS(set, searchNode, dictionaryNode);
    }

    private void DFS(HashSet<String> set, SearchNode searchNode, TrieStringMap.TrieNode dictionaryNode) {
        if(dictionaryNode == null)
            return;

        dictionaryNode = handleQu(dictionaryNode, dictionaryNode.character);

        if(dictionaryNode == null)
            return;

        if(m_markedNodes[searchNode.i][searchNode.j])
            return;

        if(dictionaryNode.depth >= 3 && dictionaryNode.value != null)
            set.add(dictionaryNode.value);

        m_markedNodes[searchNode.i][searchNode.j] = true;

        for (SearchNode n: searchNode.adj()) {
            TrieStringMap.TrieNode nextNode = m_dictionary.getNode(dictionaryNode, getNextCharacter(n));
            DFS(set, n, nextNode);
        }

        m_markedNodes[searchNode.i][searchNode.j] = false;
    }

    private char getNextCharacter(SearchNode node)
    {
        return m_board.getLetter(node.i, node.j);
    }

    private TrieStringMap.TrieNode handleQu(TrieStringMap.TrieNode node, Character character)
    {
        if(character == Q)
            return m_dictionary.getNode(node, U);

        return node;
    }
}
