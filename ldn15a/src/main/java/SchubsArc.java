import java.io.FileNotFoundException;
import java.io.File;

public class SchubsArc
{
    public static void main(String[] args) throws FileNotFoundException
    {
        try
        {
            createTarArchive(args);
            huffmanCompressArchive(args);
            lzwCompressArchive(args);
        }
        catch (FileNotFoundException ex)
        {
            System.err.println(ex);
        }
    }

    private static void createTarArchive(String[] args) throws FileNotFoundException
    {
        TarsN tarProg = new TarsN();
        tarProg.main(args);
    }

    private static void huffmanCompressArchive(String[] args) throws FileNotFoundException
    {
        Huffman huffProg = new Huffman();
        huffProg.main(new String[] {"-", args[0], args[0] + ".zh"});

        // delete non-compressed archive
        File expandedTar = new File (args[0]);
        expandedTar.delete();
    }

    private static void lzwCompressArchive(String[] args) throws FileNotFoundException
    {
        LZW lzwProg = new LZW();
        String[] lzwArgs = {"-", args[0] + ".zh"};
    }
}