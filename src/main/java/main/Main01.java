package main;

import skill.aiml.AIMLInterpreter;
import skill.aiml.AIMLReader;
import skill.aiml.AIMLNode;

import java.util.Collection;
import java.util.Scanner;

/**
 * Created by joris on 9/15/17.
 */
public class Main01 {

    public static void main(String[] args)
    {
        AIMLInterpreter interpreter = new AIMLInterpreter();
        interpreter.addAIMLNodes(AIMLReader.interpretXML(Main01.class.getClassLoader().getResourceAsStream("salutations.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(Main01.class.getClassLoader().getResourceAsStream("countries.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(Main01.class.getClassLoader().getResourceAsStream("x11colors.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(Main01.class.getClassLoader().getResourceAsStream("abbreviations.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(Main01.class.getClassLoader().getResourceAsStream("famous.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(Main01.class.getClassLoader().getResourceAsStream("jokes.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(Main01.class.getClassLoader().getResourceAsStream("nerdy.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(Main01.class.getClassLoader().getResourceAsStream("coinflip.xml")));

        System.out.println("Currently " + interpreter.countNodes() + " nodes in the AIML system.");

        Scanner sc = new Scanner(System.in);
        String line = "";
        while(!line.equals("STOP"))
        {
            line = sc.nextLine();
            System.out.println(interpreter.process(line));
        }
    }

}
