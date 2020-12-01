import java.io.FileNotFoundException;
import java.io.File;

public class Deschubs
{
    public static void main(String[] args) throws FileNotFoundException
    {
        try
        {
            huffmanExpandArchive(args);
            untarArchive(args);
        }
        catch (FileNotFoundException ex)
        {
            System.err.println(ex);
        }
    }

    private static void untarArchive(String[] args) throws FileNotFoundException
    {
        String[] untarArgs = {args[0] + ".unHuff"};
        Untars untarProg = new Untars();
        untarProg.main(untarArgs);

        // delete unHuffed Tar
        File unHuffedTar = new File(args[0] + ".unHuff");
        unHuffedTar.delete();
    }

    private static void huffmanExpandArchive(String[] args) throws FileNotFoundException
    {
        Huffman huffProg = new Huffman();
        String[] huffExpandArgs = null;
        if(args.length == 1)
        {
            huffExpandArgs = new String[] {"+", args[0], args[0] + ".unHuff"};
        }
        else if (args.length > 1)
        {
            huffExpandArgs = new String [args.length + 1];
            huffExpandArgs[0] = "+";
            for(int i = 0; i < args.length; i++)
            {
                huffExpandArgs[i + 1] = args[i];
            }
        }
        huffProg.main(huffExpandArgs);
    }
}