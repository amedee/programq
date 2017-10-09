package skill;

import algorithm.BKTree;
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
public class TypoCorrectionSkill implements ISkill{

    private static int MAX_RELATIVE_DISTANCE = 10;
    private BKTree<BotLogKeeper.Entry> bkTree = new BKTree<>(new EntryMetric());

    public TypoCorrectionSkill()
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
            bkTree.addAll(BotLogKeeper.loadEntries(xmlFile));
        }
        System.out.println("loaded " + bkTree.size() + " log entries.");
    }

    @Override
    public boolean isExampleUtterance(String s) {
        BotLogKeeper.Entry tmp = new BotLogKeeper.Entry(s, "", "", 0);
        return !bkTree.get(tmp,MAX_RELATIVE_DISTANCE).isEmpty();
    }

    @Override
    public String process(Bot bot, String s) {
        BotLogKeeper.Entry tmp = new BotLogKeeper.Entry(s, "", "", 0);

        BotLogKeeper.Entry bestEntry = null;
        for(BotLogKeeper.Entry e : bkTree.get(tmp, MAX_RELATIVE_DISTANCE))
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

class EntryMetric implements BKTree.Metric<BotLogKeeper.Entry>
{
    @Override
    public int distance(BotLogKeeper.Entry obj0, BotLogKeeper.Entry obj1) {
        double rDist = Levenshtein.relativeDistance(obj0.getInput().toUpperCase(),
                                                    obj1.getInput().toUpperCase());
        return (int) java.lang.Math.round(rDist * 100);
    }
}
