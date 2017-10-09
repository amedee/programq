package main;

import bot.Bot;
import bot.BotLogKeeper;
import skill.MathSkill;
import skill.TypoCorrectionSkill;
import skill.aiml.AIMLSkill;

import javax.script.ScriptException;
import java.util.Scanner;

/**
 * Created by joris on 9/15/17.
 */
public class Main01 {

    public static void main(String[] args) throws ScriptException {

        Bot bot = new Bot();
        bot.addSkill(new AIMLSkill());              // AIML
        bot.addSkill(new MathSkill());              // simple mathematical operations
        bot.addSkill(new TypoCorrectionSkill());    // levenshtein based corrections
        bot.addListener(new BotLogKeeper());        // logging

        Scanner sc = new Scanner(System.in);
        String line = "";
        while(!line.equals("STOP"))
        {
            line = sc.nextLine();
            System.out.println(bot.process(line));
        }
    }

}
