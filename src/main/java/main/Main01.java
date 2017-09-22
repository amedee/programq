package main;

import bot.Bot;
import skill.aiml.AIMLInterpreter;
import skill.aiml.AIMLReader;
import skill.aiml.AIMLNode;
import skill.aiml.AIMLSkill;
import skill.machinelearning.NNSkill;

import java.io.File;
import java.util.Collection;
import java.util.Scanner;

/**
 * Created by joris on 9/15/17.
 */
public class Main01 {

    public static void main(String[] args)
    {

        Bot bot = new Bot();
        bot.addSkill(new AIMLSkill());

        File wFile = new File(System.getProperty("user.home"),"par_2_vec_programq");
        File nFile = new File(System.getProperty("user.home"),"par2par_nn_programq");
        File cFile = new File("/home/joris/Downloads/cornell_movie_dialogs_corpus/cornell movie-dialogs corpus");

        bot.addSkill(new NNSkill(wFile, nFile, cFile));

        Scanner sc = new Scanner(System.in);
        String line = "";
        while(!line.equals("STOP"))
        {
            line = sc.nextLine();
            System.out.println(bot.process(line));
        }
    }

}
