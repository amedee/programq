package skill.ml;

import bot.Bot;
import bot.ISkill;

public class DialogSkill implements ISkill {

    @Override
    public boolean isExampleUtterance(String s) {
        return false;
    }

    @Override
    public String process(Bot bot, String s) {
        return null;
    }

}
