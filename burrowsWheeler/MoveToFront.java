import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static int R = 256;
    private static int lgR = 8;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] seq = new char[R];

        for(char i = 0; i < R; i++)
            seq[i] = i;

        while (!BinaryStdIn.isEmpty())
        {
            char c = BinaryStdIn.readChar();

            int idx = moveToFirst(seq, c);
            BinaryStdOut.write(idx, lgR);
        }

        BinaryStdOut.close();
    }

    private static int moveToFirst(char[] seq, char c)
    {
        for(int i = 0; i < seq.length; i++)
        {
            if(seq[i] == c)
            {
                moveIndexToFirst(seq, i);
                return i;
            }
        }

        return -1;
    }

    private static char moveIndexToFirst(char[] seq, int idx) {
        char c = seq[idx];

        for(int i = idx; i > 0; i--)
            seq[i] = seq[i - 1];

        seq[0] = c;

        return c;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] seq = new char[R];

        for(char i = 0; i < R; i++)
            seq[i] = i;

        while (!BinaryStdIn.isEmpty())
        {
            int i = BinaryStdIn.readChar();

            char c = moveIndexToFirst(seq, i);
            BinaryStdOut.write(c);
        }

        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if(args.length < 1)
            throw new IllegalArgumentException();

        if(args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}