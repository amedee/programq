package skill;

import bot.Bot;
import bot.ISkill;

/**
 * Created by joris on 9/15/17.
 */
public class TimeSkill implements ISkill{

    @Override
    public boolean isExampleUtterance(String s) {
        return false;
    }

    @Override
    public String process(Bot bot, String s) {
        return null;
    }
}
