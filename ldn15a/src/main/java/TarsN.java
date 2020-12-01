/*************************************************************************
 *  Lucas Newton
 *  Compilation:        javac TarsN.java
 *  Execution:          java -cp ./src/main/java TarsN target.tar source1.txt source2.txt sourceN.txt ...
 *  Maven Test:         mvn test
 *  Date:               Fall 2020
 *  Dependencies:       BinaryIn.java BinaryOut.java
*************************************************************************/
import java.io.*;

public class TarsN {

    public static void tarFile(File tarFile, File textFile)
    {
        BinaryIn appender = null;
        BinaryIn in = null;
        BinaryOut out = null;

        char separator = (char) 255; // all ones 11111111

        appender = new BinaryIn (tarFile.toString());
        out = new BinaryOut(tarFile.toString());

        while(!appender.isEmpty())
        {
            char nextChar = appender.readChar();
            out.write(nextChar);
        }

        // starts reading in new txt file and adds it
        try{
            long filesize = textFile.length();
            int filenamesize = textFile.toString().length();

            // archive file is at args[0]
            // layout: file-name-length, separator, filename, file-size, file
            // output
            // 4 bytes - length of file name
            // 1 byte  - all 1 bits 11111111
            // ? filename
            // 8 bytes - file size

            out.write(filenamesize);
            out.write(separator);

            out.write(textFile.toString());
            out.write(separator);

            out.write(filesize);
            out.write(separator);

            in = new BinaryIn(textFile.toString());
            while(!in.isEmpty())
            {
                char next = in.readChar();
                out.write(next);
            }
        }
        finally
        {
            if(out != null)
            {
                out.close();
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        if(args.length >= 2)
        {
            File tarOutFile = new File(args[0]);
            if (!tarOutFile.exists())
            {
                try
                {
                    File file = new File(args[0]);
                    file.createNewFile();
                }
                catch(Exception e) {
                    System.err.println(e);
                }
            }
            else if (!tarOutFile.isFile())
            {
                throw new FileNotFoundException("FileNotFoundException");
            }
            else
            {
                // File exists, time delete contents to prepare for writing
                new PrintWriter(args[0]).close();
            }

            for(int i = 1; i < args.length; i++)
            {
                File nextTxtFile = new File(args[i]);
                if (!nextTxtFile.exists() || !nextTxtFile.isFile())
                {
                    throw new FileNotFoundException("FileNotFoundException");
                }
                tarFile(tarOutFile, nextTxtFile);
            }
        }
        else
        {
            throw new FileNotFoundException("FileNotFoundException");
        }
    }

}
