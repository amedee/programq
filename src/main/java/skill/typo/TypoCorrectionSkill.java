package skill.typo;

import algorithm.BKTree;
import algorithm.Levenshtein;
import bot.Bot;
import skill.logging.BotLogKeeper;
import bot.ISkill;
import skill.logging.Entry;

import java.io.File;

/**
 * Created by joris on 9/15/17.
 * This ISkill implementation allows the Bot to handle typos
 */
public class TypoCorrectionSkill implements ISkill{

    private static int MAX_RELATIVE_DISTANCE = 10;
    private BKTree<Entry> bkTree = new BKTree<>(new EntryMetric());

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
        Entry tmp = new Entry(s, "", "", 0);
        return !bkTree.get(tmp,MAX_RELATIVE_DISTANCE).isEmpty();
    }

    @Override
    public String process(Bot bot, String s) {
        Entry tmp = new Entry(s, "", "", 0);

        Entry bestEntry = null;
        for(Entry e : bkTree.get(tmp, MAX_RELATIVE_DISTANCE))
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

/**
 * This class represents a BKTree.Metric over entries in the conversation history of the Bot
 */
class EntryMetric implements BKTree.Metric<Entry>
{
    @Override
    public int distance(Entry obj0, Entry obj1) {
        double rDist = Levenshtein.relativeDistance(obj0.getInput().toUpperCase(),
                                                    obj1.getInput().toUpperCase());
        return (int) java.lang.Math.round(rDist * 100);
    }
}
