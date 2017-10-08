package skill;

import algorithm.Levenshtein;
import bot.Bot;
import bot.BotLogKeeper;
import bot.ISkill;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joris on 9/15/17.
 */
public class NearMatchSkill implements ISkill{

    private static double MAX_RELATIVE_DISTANCE = 0.1;
    private List<BotLogKeeper.Entry> entryList = new ArrayList<>();

    public NearMatchSkill()
    {
        loadEntries();
    }

    private void loadEntries()
    {
        if(!BotLogKeeper.getLogDirectory().exists())
            return;
        for(File xmlFile : BotLogKeeper.getLogDirectory().listFiles())
        {
            if(!xmlFile.getName().endsWith(".xml"))
                continue;
            entryList.addAll(BotLogKeeper.loadEntries(xmlFile));
        }
        System.out.println("loaded " + entryList.size() + " log entries.");
    }

    @Override
    public boolean isExampleUtterance(String s) {
        for(BotLogKeeper.Entry e : entryList)
        {
            if(e.getSkill().equals(getClass().getSimpleName()))
                continue;
            if(Levenshtein.relativeDistance(e.getInput(), s) < MAX_RELATIVE_DISTANCE)
                return true;
        }
        return false;
    }

    @Override
    public String process(Bot bot, String s) {
        BotLogKeeper.Entry bestEntry = null;
        for(BotLogKeeper.Entry e : entryList)
        {
            if(bestEntry == null || Levenshtein.relativeDistance(e.getInput(), s) < Levenshtein.relativeDistance(bestEntry.getInput(), s))
            {
                bestEntry = e;
            }
        }
        System.out.println("~[" + bestEntry.getInput() + "]");
        return bot.process(bestEntry.getInput());
    }
}
