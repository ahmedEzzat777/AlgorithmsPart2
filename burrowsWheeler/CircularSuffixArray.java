import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private char[] m_string;
    private int[] m_index;

    // circular suffix array of s
    public CircularSuffixArray(String s){
        if(s == null)
            throw new IllegalArgumentException();

        m_string = s.toCharArray();
        CircularString[] suffixs = new CircularString[m_string.length];
        m_index = new int[m_string.length];

        for (int i = 0; i < s.length(); i++)
            suffixs[i] = new CircularString(this.m_string, i);

        sort(suffixs);

        int i = 0;
        for(CircularString cs: suffixs)
            m_index[i++] = cs.getStartOfString();

        m_string = null;
    }

    private void sort(CircularString[] suffixs) {
        sort(suffixs, 0, suffixs.length - 1, 0); // 3 way radix quick sort
    }

    private void sort(CircularString[] suffixs, int lo, int hi, int d) {
        if(lo >= hi)
            return;

        int lt = lo;
        int gt = hi;
        int p = suffixs[lo].charAt(d);
        int i = lo + 1;

        while (i <= gt)
        {
            int v = suffixs[i].charAt(d);

            if(v < p)   exchange(suffixs, i++, lt++);
            else if(v > p) exchange(suffixs, i, gt--);
            else i++;
        }

        sort(suffixs, lo, lt - 1, d);
        if(p >= 0) sort(suffixs, lt, gt, d+1);
        sort(suffixs, gt + 1, hi, d);
    }

    private void exchange(CircularString[] suffixs, int i, int j) {
        CircularString temp = suffixs[i];
        suffixs[i] = suffixs[j];
        suffixs[j] = temp;
    }

    // length of s
    public int length(){
        return m_index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i){
        if(i < 0 || i >= length())
            throw new IllegalArgumentException();
        return m_index[i];
    }

    // unit testing (required)
    public static void main(String[] args){
        CircularSuffixArray c = new CircularSuffixArray("AAAAAAAAAA");

        int len = c.length();
        StdOut.println("length is " + len);
        for(int i = 0; i < len; i++)
            StdOut.println(i + ":" +c.index(i));
    }

}
