import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet m_wordnet;

    public Outcast(WordNet wordnet)         // constructor takes a WordNet object
    {
        m_wordnet = wordnet;
    }

    public String outcast(String[] nouns)   // given an array of WordNet nouns, return an outcast
    {
        int[] distances = new int[nouns.length];

        for (int i = 0; i < nouns.length; i++)
        {
            for(int j = 0; j < nouns.length ; j++)
            {
                if(i != j)
                    distances[i] += m_wordnet.distance(nouns[i], nouns[j]);
            }
        }

        int maxDistance = -1;
        int maxDistanceIdx = -1;

        for(int i = 0; i < distances.length; i++)
        {
            if(distances[i] > maxDistance)
            {
                maxDistance = distances[i];
                maxDistanceIdx = i;
            }

        }
        return nouns[maxDistanceIdx];
    }

    public static void main(String[] args)  // see test client below
    {
        /*WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        String[] outcasts = {"horse","zebra","cat", "bear", "table"};
        StdOut.println(outcast.outcast(outcasts));*/
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
