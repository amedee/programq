package main;

import bot.Bot;
import skill.aiml.AIMLSkill;

import java.io.File;
import java.util.Scanner;

/**
 * Created by joris on 9/15/17.
 */
public class Main01 {

    public static void main(String[] args)
    {

        Bot bot = new Bot();
        bot.addSkill(new AIMLSkill());

        Scanner sc = new Scanner(System.in);
        String line = "";
        while(!line.equals("STOP"))
        {
            line = sc.nextLine();
            System.out.println(bot.process(line));
        }
    }

}
