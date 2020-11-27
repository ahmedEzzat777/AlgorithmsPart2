import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import edu.princeton.cs.algs4.DepthFirstOrder;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {

    private HashMap<String, ArrayList<Integer>> m_nounsMap;
    private HashMap<Integer, String> m_idsMap;

    private Digraph G;
    private SAP m_sap = null;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        if(synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        In synsetsIn = new In(synsets);
        In hypernymsIn = new In(hypernyms);

        m_nounsMap = new HashMap<>();
        m_idsMap = new HashMap<>();
        int idCount = 0;
        while(synsetsIn.hasNextLine())
        {
            String line = synsetsIn.readLine();

            String[] strings  = line.split(",");
            int id = Integer.parseInt(strings[0]);
            String synset = strings[1];

            strings = synset.split(" ");

            for (String noun : strings) {
                if(m_nounsMap.containsKey(noun))
                {
                    m_nounsMap.get(noun).add(id);
                }
                else
                {
                    ArrayList<Integer> list = new ArrayList<>();
                    list.add(id);
                    m_nounsMap.put(noun, list);
                }
            }

            m_idsMap.put(id, synset);

            idCount++;
        }

        G = new Digraph(idCount);

        while (hypernymsIn.hasNextLine())
        {
            String line = hypernymsIn.readLine();
            String[] strings  = line.split(",");
            int id = Integer.parseInt(strings[0]);

            for(int i = 1; i < strings.length; i++)
                G.addEdge(id, Integer.parseInt(strings[i]));
        }

        validateCyclic(G);
        validateRooted(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return m_nounsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        if(word == null)
            throw new IllegalArgumentException();

        return m_nounsMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        if(nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if(!m_nounsMap.containsKey(nounA) || !m_nounsMap.containsKey(nounB))
            throw new IllegalArgumentException();

        if(m_sap == null)
            m_sap = new SAP(G);

        return m_sap.length(m_nounsMap.get(nounA), m_nounsMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        if(nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if(!m_nounsMap.containsKey(nounA) || !m_nounsMap.containsKey(nounB))
            throw new IllegalArgumentException();

        if(m_sap == null)
            m_sap = new SAP(G);

        return m_idsMap.get(m_sap.ancestor(m_nounsMap.get(nounA), m_nounsMap.get(nounB)));
    }

    private void validateCyclic(Digraph g) {
        if(new DirectedCycle(g).hasCycle())
            throw new IllegalArgumentException();
    }

    private void validateRooted(Digraph g) {
        //assuming all roots are valid roots
        //for no assumptions we must use reverse
        int roots = 0;

        for(int i = 0; i < g.V(); i++) {
            if (g.outdegree(i) == 0)
                roots++;
        }

        if(roots != 1)
            throw new IllegalArgumentException();
        //implicit requirement : don't use reverse
        /*
        //since it is already acyclic,it can be topologically ordered, we can just get the post order
        // iff the first vertex can reach all the other vertices, then the digraph is rooted
        DepthFirstOrder dfs = new DepthFirstOrder(G);
        Iterable<Integer> order  = dfs.post();
        int root = order.iterator().next();
        DepthFirstDirectedPaths paths = new DepthFirstDirectedPaths(G.reverse(), root);

        for(int i = 0; i < g.V() ; i++)
        {
            if(!paths.hasPathTo(i))
                throw new IllegalArgumentException();
        }

         */
    }

    // do unit testing of this class
    public static void main(String[] args){
        WordNet n = new WordNet("synsets.txt", "hypernyms.txt");
        boolean b1 = n.isNoun("effectuality");
        boolean b2 = n.isNoun("descriptor");
        int dist = n.distance("effectuality", "descriptor");
        String sap = n.sap("effectuality", "descriptor");
        StdOut.print(b1 + " " + b2 + "\n" + dist + "\n" + sap );
    }
}