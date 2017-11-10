package main;

import bot.Bot;
import skill.calendar.FixedCalendarQuerySkill;
import skill.logging.BotLogKeeper;
import skill.math.MathSkill;
import skill.typo.TypoCorrectionSkill;
import skill.aiml.AIMLSkill;

import javax.script.ScriptException;
import java.util.Scanner;

/**
 * Created by joris on 9/15/17.
 */
public class Main001 {

    public static void main(String[] args) throws ScriptException {

        Bot bot = new Bot();
        bot.addSkill(new AIMLSkill());
        bot.addSkill(new MathSkill());
        bot.addSkill(new FixedCalendarQuerySkill());
        bot.addSkill(new TypoCorrectionSkill());
        bot.addListener(new BotLogKeeper());

        Scanner sc = new Scanner(System.in);
        String line = "";
        while(!line.equals("STOP"))
        {
            line = sc.nextLine();
            System.out.println(bot.process(line));
        }
    }

}
