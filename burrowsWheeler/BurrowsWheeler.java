import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static class IndexedCharacter{
        char c;
        int idx;

        IndexedCharacter(char c, int idx){
            this.c = c;
            this.idx = idx;
        }
    }

    private static int R = 256;
    private static int lgR = 8;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform(){
        String s = getStringFromBinaryStdIn();
        char[] charArray = s.toCharArray();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        s = null;
        CircularString[] ca = new CircularString[csa.length()];
        int start = -1;

        for (int i = 0; i < csa.length(); i++)
        {
            int idx = csa.index(i);
            ca[i] = new CircularString(charArray, idx);

            if(idx == 0)
                start = i;
        }

        BinaryStdOut.write(start, 4*lgR);

        for (int i = 0; i < csa.length(); i++)
            BinaryStdOut.write(ca[i].charAt(csa.length() - 1), lgR);

        BinaryStdOut.close();
    }

    private static String getStringFromBinaryStdIn() {
        StringBuilder builder = new StringBuilder();

        while (!BinaryStdIn.isEmpty())
        {
            builder.append(BinaryStdIn.readChar());
        }

        return builder.toString();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform(){
        int start = -1;

        if(!BinaryStdIn.isEmpty())
            start = BinaryStdIn.readInt(4*lgR);

        String s = getStringFromBinaryStdIn();
        int N = s.length();

        if(start >= N)
            throw new IllegalArgumentException();

        char[] lastColumn = s.toCharArray();
        s = null;
        IndexedCharacter[] sortedLastColumn = new IndexedCharacter[N];

        for(int i = 0; i < N; i++)
            sortedLastColumn[i] = new IndexedCharacter(lastColumn[i], i);

        sortedLastColumn = sort(sortedLastColumn);

        for(int i = 0; i < N; i++)
        {
            lastColumn[i] = sortedLastColumn[start].c;
            start = sortedLastColumn[start].idx; //indexes of the sortedLastColumn are the next array, no need to construct another array
        }

        for(char c: lastColumn)
            BinaryStdOut.write(c);

        BinaryStdOut.close();
    }

    private static IndexedCharacter[] sort(IndexedCharacter[] a)
    {
        if(a == null)
            throw new IllegalArgumentException();

        int N = a.length;
        int[] count = new int[R + 1];
        IndexedCharacter[] aux = new IndexedCharacter[N];

        for(int i = 0; i < N; i++)
            count[a[i].c + 1]++;

        for(int r = 0; r < R; r++)
            count[r+1] += count[r];

        for(int i = 0; i < N; i++)
            aux[count[a[i].c]++] = new IndexedCharacter(a[i].c, a[i].idx);

        return aux;
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args){
        if(args.length < 1)
            throw new IllegalArgumentException();

        if(args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
    }
}
