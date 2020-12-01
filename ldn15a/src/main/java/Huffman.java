/*************************************************************************
 *  Lucas Newton
 *  Compilation:        javac Huffman.java
 *  Execution:          java Huffman - uncompressed_filename compressed_filename
 *  Maven Execution:    mvn exec:java -Dexec.mainClass="Huffman" -Dexec.args="src/test/resources/abra.txt arc/test/resources/abraOut.txt"
 *  Maven Test:         mvn test
 *  Dependencies:       BinaryIn.java BinaryOut.java
 *  Data files:         http://algs4.cs.princeton.edu/55compression/abra.txt
 *************************************************************************/
import java.io.*;

public class Huffman {

    // alphabet size of extended ASCII
    private static final int R = 256;
    public static boolean logging = true;

    // Huffman trie node
    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert (left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }


    public static void err_print(String msg) {
        if (logging) {
            System.err.print(msg);
        }
    }

    public static void err_println(String msg) {
        if (logging) {
            System.err.println(msg);
        }
    }


    // compress bytes from standard input and write to standard output
    public static void compress() {
        // read the input
        String s = BinaryStdIn.readString();
        err_println("s = " + s);
        char[] input = s.toCharArray();

        // tabulate frequency counts
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        // build Huffman trie
        Node root = buildTrie(freq);

        // build code table
        String[] st = new String[R];
        buildCode(st, root, "");

        // print trie for decoder
        writeTrie(root);
        err_println("writeTrie");

        // print number of bytes in original uncompressed message
        BinaryStdOut.write(input.length);
        err_println("writing input length " + input.length);

        err_println("happily encoding... ");
        // use Huffman code to encode input
        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            err_print("Char " + input[i] + " ");
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    BinaryStdOut.write(false);
                    err_print("0");
                }
                else if (code.charAt(j) == '1') {
                    BinaryStdOut.write(true);
                    err_print("1");
                }
                else throw new RuntimeException("Illegal state");
            }
            err_println("");
        }

        // flush output stream
        BinaryStdOut.flush();
    }

    // build the Huffman trie given frequencies
    private static Node buildTrie(int[] freq) {

        // initialze priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        // merge two smallest trees
        while (pq.size() > 1) {
            Node left  = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            err_println("buildTrie parent " + left.freq + " " + right.freq);
            pq.insert(parent);
        }
        return pq.delMin();
    }


    // write bitstring-encoded trie to standard output
    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch);
        err_println("T" + x.ch);
            return;
        }
        BinaryStdOut.write(false);
        err_print("F");

        writeTrie(x.left);
        writeTrie(x.right);
    }

    // make a lookup table from symbols and their encodings
    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
            st[x.ch] = s;
        err_println("buildCode " + x.ch + " " + s);
        }
    }


    // expand Huffman-encoded input from standard input and write to standard output
    public static void expand() {

        // read in Huffman trie from input stream
        Node root = readTrie(); 

        // number of bytes to write
        int length = BinaryStdIn.readInt();

        // decode using the Huffman trie
        for (int i = 0; i < length; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = BinaryStdIn.readBoolean();
                if (bit) x = x.right;
                else     x = x.left;
            }
            BinaryStdOut.write(x.ch);
        }
        BinaryStdOut.flush();
    }


    private static Node readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
        char x = BinaryStdIn.readChar();
        err_println("t: " + x );
            return new Node(x, -1, null, null);
        }
        else {
        err_print("f");
            return new Node('\0', -1, readTrie(), readTrie());
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        try {
            if (args.length == 3)
            {
                InputStream originalIn = System.in;
                PrintStream originalOut = System.out;
                System.setIn ( new BufferedInputStream (new FileInputStream (new File ((String) args[1]))));
                System.setOut (new PrintStream (new File ((String) args[2])));
                if(args[0] == "-")
                {
                    compress();
                }
                else if(args[0] == "+")
                {
                    expand();
                }
                System.setIn(originalIn);
                System.in.mark(0);
                System.setOut(originalOut);
            }
        }
        catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
    }

}
