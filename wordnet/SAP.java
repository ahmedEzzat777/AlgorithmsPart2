import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private class ResultPair{
        int distance;
        int ancestor;
        ResultPair(int distance, int ancestor)
        {
            this.distance = distance;
            this.ancestor = ancestor;
        }
    }

    private Digraph m_g;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        if(G == null)
            throw new IllegalArgumentException();

        m_g = cloneDigraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        return getAncestorAndDistance(v, w).distance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        return getAncestorAndDistance(v, w).ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        return getAncestorAndDistance(v, w).distance;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        return getAncestorAndDistance(v, w).ancestor;
    }

    private ResultPair getAncestorAndDistance(int v, int w) {
        if(v >= m_g.V() || v < 0 || w >= m_g.V() || w < 0)
            throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(m_g, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(m_g, w);

        int minDist = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int i = 0; i < m_g.V(); i++)
        {
            int distV = bfsV.distTo(i);
            int distW = bfsW.distTo(i);

            if(distV != Integer.MAX_VALUE && distW != Integer.MAX_VALUE && distV+distW < minDist)
            {
                minDist = distV + distW;
                ancestor = i;
            }
        }

        ResultPair p = new ResultPair(minDist, ancestor);

        if(minDist == Integer.MAX_VALUE)
            p.distance = -1;

        return  p;
    }

    private ResultPair getAncestorAndDistance(Iterable<Integer> v, Iterable<Integer> w) {
        validateIterables(v,w);

        if(!v.iterator().hasNext() || !w.iterator().hasNext())
            return new ResultPair(-1,-1);

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(m_g, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(m_g, w);

        int minDist = Integer.MAX_VALUE;
        int ancestor = -1;

        for (int i = 0; i < m_g.V(); i++)
        {
            int distV = bfsV.distTo(i);
            int distW = bfsW.distTo(i);

            if(distV != Integer.MAX_VALUE && distW != Integer.MAX_VALUE && distV+distW < minDist)
            {
                minDist = distV + distW;
                ancestor = i;
            }
        }

        ResultPair p = new ResultPair(minDist, ancestor);

        if(minDist == Integer.MAX_VALUE)
            p.distance = -1;

        return  p;
    }

    private void validateIterables(Iterable<Integer> v, Iterable<Integer> w) {
        if(v == null || w == null)
            throw new IllegalArgumentException();

        for (Integer i: v) {
            if(i == null || i >= m_g.V() || i < 0)
                throw new IllegalArgumentException();
        }

        for (Integer i: w) {
            if(i == null || i >= m_g.V() || i < 0)
                throw new IllegalArgumentException();
        }
    }

    private Digraph cloneDigraph(Digraph g) {
        Digraph clonedG = new Digraph(g.V());

        for (int i = 0; i < g.V(); i++)
        {
            for (int j : g.adj(i))
                clonedG.addEdge(i, j);
        }

        return clonedG;
    }


    // do unit testing of this class
    public static void main(String[] args){
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}