# CS375Project

- Requirements:
    Java 8 or higher with Maven to run test

- Design:
    There are two main programs here: "SchubsArc" and "Deschubs". SchubsArc is for taking one or more files and combining them all in a compressed archive file. While Deschubs will take that same compressed archive file and expand it to give you the original files used to create the it. Huffman is used for the compression and tar is used for the archive.

- Installation:
    It's as simple as downloading all the needed classe (recommended to just download my ACU username directory) and run whichever of the two program you wish to run. No other installation steps are necessary besides the (trivial) Java and Maven installation processes. (Maven only required for running the test)

- CLI and testing:
    Simply run `mvn test` from the directory where the "POM" file is located to run the maven tests. This will test for things such as wrong command line input along with making sure that compression works. To run the programs "SchubsArc" simple run `java SchubsArc <desiredName> file1.txt file2.txt fileN.txt`, where "<desiredName>" is whatever name you want the archive to be. (This will be a ".zh" file) Alternatively, you could use globbing and do something like `java SchubsArc <desiredName> file*.txt`.
   
A recommended example you can do is `java SchubsArc archive source*.txt` followed by `java Deschubs archive.zh`.
