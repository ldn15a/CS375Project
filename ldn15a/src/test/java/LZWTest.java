/*************************************************************************
 *  Lucas Newton
 *  Compilation:        javac TarsN.java
 *  Execution:          java -cp ./src/main/java TarsN target.tar source1.txt source2.txt sourceN.txt ...
 *  Maven Test:         mvn test
 *  Date:               Fall 2020
 *  Dependencies:       BinaryIn.java BinaryOut.java
*************************************************************************/
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.After;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.PrintStream;
import java.io.FileWriter;
import java.io.File;
import java.io.*;    // because I'm lazy
import java.nio.file.Files;
import java.nio.file.Paths;

public class LZWTest
{
    @Test
    public void noArgsTest() throws FileNotFoundException
    {
        boolean throwValue = false;
        LZW lzwProg = new LZW();
        try
        {
            lzwProg.main(new String[] {""});
        }
        catch(Exception e)
        {
            throwValue = true;
        }
        assert(throwValue);
    }

    @Test
    public void OneArgsTest() throws FileNotFoundException
    {
        boolean throwValue = false;
        LZW lzwProg = new LZW();
        try
        {
            lzwProg.main(new String[] {"bad arg list"});
        }
        catch(Exception e)
        {
            throwValue = true;
        }
        assert(throwValue);
    }

    @Test
    public void sourceDoesntExist() throws FileNotFoundException
    {
        boolean throwValue = false;
        LZW lzwProg = new LZW();
        try
        {
            String sourceFile = "." + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "doesntExist.txt";
            lzwProg.main(new String[] {"-", sourceFile});
        }
        catch(Exception e)
        {
            throwValue = true;
        }
        assert(throwValue);
    }

    @Test
    public void LZWCompressTest() throws FileNotFoundException
    {
        boolean threwException = false;
        LZW lzwProg = new LZW();
        try
        {
            String sourceFile = "." + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "source.txt";
            String filesContent = Files.readString(Paths.get(sourceFile));

            PrintStream originalOut = new PrintStream(System.out);

            ByteArrayInputStream myIn = new ByteArrayInputStream(filesContent.getBytes());
            ByteArrayOutputStream myOut = new ByteArrayOutputStream();

            System.setIn(myIn);
            System.setOut(new PrintStream(myOut));

            lzwProg.main(new String[] {"-"});

            String compressedResult = myOut.toString();

            myIn.reset();
            myOut.reset();

            System.setIn(System.in);
            System.setOut(originalOut);

            String sourceFileCompressed = "." + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "sourceCompressed.txt";
            String fileContent = Files.readString(Paths.get(sourceFileCompressed));

            if(fileContent.length() != compressedResult.length())
            {
                throw new RuntimeException("outputs did not match");
            }

            for(int i = 0 ; i < compressedResult.length(); i++)
            {
                if(compressedResult.charAt(i) != fileContent.charAt(i))
                {
                    throw new RuntimeException("outputs did not match");
                }
            }
        }
        catch(Exception e)
        {
            threwException = true;
            System.err.println(e);
        }

        // if we made it this far then just make sure things didn't break along the way
        assert(!threwException);
    }
}
