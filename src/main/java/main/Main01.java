package main;

import bot.Bot;
import bot.BotHistoryKeeper;
import skill.aiml.AIMLSkill;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.util.Scanner;

/**
 * Created by joris on 9/15/17.
 */
public class Main01 {

    public static void main(String[] args) throws ScriptException {

        Bot bot = new Bot();
        bot.addSkill(new AIMLSkill());
        bot.addListener(new BotHistoryKeeper());

        Scanner sc = new Scanner(System.in);
        String line = "";
        while(!line.equals("STOP"))
        {
            line = sc.nextLine();
            System.out.println(bot.process(line));
        }
    }

}
