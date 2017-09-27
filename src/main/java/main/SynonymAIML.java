package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by joris on 9/27/17.
 */
public class SynonymAIML {

    public static void main(String[] args) throws FileNotFoundException
    {
        File inputFile = new File("/home/joris/Documents/uk_english_to_us_english.csv");
        Scanner sc = new Scanner(inputFile);
        while(sc.hasNextLine())
        {
            String[] line = sc.nextLine().split(",");
            if(line.length != 2 || line[0].isEmpty() || line[1].isEmpty() || line[0].contains("(") || line[1].contains(")"))
                continue;
            output(line[0], line[1]);
        }
        sc.close();
    }

    private static void output(String w0, String w1)
    {
        w0 = w0.toUpperCase();
        w1 = w1.toUpperCase();
        System.out.println("    <category>\n" +
                "        <pattern>(.*) " + w0 + " ( .*)</pattern>\n" +
                "        <template>\n" +
                "            <redirect>\\1 " + w1 + " \\2</redirect>\n" +
                "        </template>\n" +
                "    </category>\n" +
                "    <category>\n" +
                "        <pattern>" + w0 + " (.*)</pattern>\n" +
                "        <template>\n" +
                "            <redirect>" + w1 + " \\1</redirect>\n" +
                "        </template>\n" +
                "    </category>\n" +
                "    <category>\n" +
                "        <pattern>(.*) " + w0 + "</pattern>\n" +
                "        <template>\n" +
                "            <redirect>\\1 " + w1 + "</redirect>\n" +
                "        </template>\n" +
                "    </category>\n" +
                "    <category>\n" +
                "        <pattern>" + w0 + "</pattern>\n" +
                "        <template>\n" +
                "            <redirect>" + w1 + "</redirect>\n" +
                "        </template>\n" +
                "    </category>");
    }

}
