/*************************************************************************
 *  Lucas Newton
 *  Compilation:        javac Untars.java
 *  Execution:          java -cp ./src/main/java Untars target.tar
 *  Maven Test:         mvn test
 *  Date:               Fall 2020
 *  Dependencies:       BinaryIn.java BinaryOut.java
*************************************************************************/
import java.io.*;

public class Untars {


    public static void untarFile(File tarFile) {
        BinaryIn in = null;
        BinaryOut out = null;
        in = new BinaryIn(tarFile.toString());
        while(!in.isEmpty())
        {
            try
            {
                int filenamesize = in.readInt();
                System.err.println(filenamesize);
                char separator = in.readChar();
                String filename = "";
                for(int i = 0; i < filenamesize; i++)
                {
                    filename += in.readChar();
                }
                separator = in.readChar();
                long filesize = in.readLong();
                separator = in.readChar();
                System.out.println("Extracting file: " + filename + " ("+ filesize +").");
                // archive file is at args[0]
                // layout: file-name-length, separator, filename, file-size, file
                // output
                // 4 bytes - length of file name
                // 1 byte  - all 1 bits 11111111
                // ? filename
                // 8 bytes - file size
                try {
                    File outFile = new File(filename);
                    boolean createdFile = outFile.createNewFile();
                    if (createdFile)
                    {
                        System.out.println("File has been created successfully");
                        out = new BinaryOut(outFile.toString());
                        for(int i = 0; i < filesize; i++)
                        {
                            out.write(in.readChar());
                        }
                    }
                    else
                    {
                        System.out.println("File already present at the specified location");
                        System.out.println("Overriding");
                        out = new BinaryOut(outFile.toString());
                        for(int i = 0; i < filesize; i++)
                        {
                            out.write(in.readChar());
                        }
                    }
                } catch (IOException e)
                {
                        System.out.println("Exception Occurred:");
                        e.printStackTrace();
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
    }

    public static void main(String[] args) throws FileNotFoundException {
        if(args.length == 1)
        {
            File inFile = new File(args[0]);
            if (!inFile.exists() || !inFile.isFile())
            {
                throw new FileNotFoundException("FileNotFoundException");
            }
            untarFile(inFile);
        }
        else
        {
            throw new FileNotFoundException("FileNotFoundException");
        }
    }

}
