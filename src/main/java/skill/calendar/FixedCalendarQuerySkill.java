package skill.calendar;

import bot.Bot;
import bot.ISkill;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * This ISkill implementation allows the bot to query a fixed calendar
 */
public class FixedCalendarQuerySkill implements ISkill {

    // where to find calendar records
    private static File HOME = new File(System.getProperty("user.home"), FixedCalendarQuerySkill.class.getSimpleName() + ".txt");

    // how dates are expected to be formatted
    private static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm");

    // useless words
    private static Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(new String[]{"WHEN","WHERE","WHAT","TIME",
                                                                                    "IS","ARE",
                                                                                    "THE", "AN", "A","THERE",
                                                                                    "SESSION", "TALK", "EVENT",
                                                                                    "ABOUT","WITH", "REGARDS","TO"}));

    private List<Object[]> loadCalendar()
    {
        List<Object[]> output = new ArrayList<>();
        try {
            Scanner sc =  new Scanner(HOME);
            while(sc.hasNextLine())
            {
                String[] line = sc.nextLine().split("\t+");
                output.add(new Object[]{DATE_TIME_FORMAT.parse(line[0]), line[1], line[2]});
            }
            sc.close();
        } catch (FileNotFoundException | ParseException e) {
        }
        return output;
    }

    private Object[] findMatchingCalendarEvent(String txt)
    {
        List<Object[]> calendar = loadCalendar();
        Set<String> query = new HashSet<>(Arrays.asList(txt.toUpperCase().split(" ")));
        query.removeAll(STOP_WORDS);
        double queryKeywordsSize = query.size();

        Object[] bestMatchingEvent = null;
        double bestMatchingRatio = 0.0;
        for(Object[] entry : calendar)
        {
            Set<String> description = new HashSet<>(Arrays.asList(entry[2].toString().toUpperCase().split(" ")));
            description.removeAll(STOP_WORDS);

            double descriptionKeywordsSize = description.size();

            description.retainAll(query);
            double intersectKeywordsSize = description.size();

            double ratio = intersectKeywordsSize / java.lang.Math.max(descriptionKeywordsSize, queryKeywordsSize);
            if(ratio > bestMatchingRatio)
            {
                bestMatchingRatio = ratio;
                bestMatchingEvent = entry;
            }
        }
        return bestMatchingRatio > 0.75 ? bestMatchingEvent : null;
    }

    @Override
    public boolean isExampleUtterance(String s) {
        // query must start with 'WHEN IS'
        if(!s.toUpperCase().startsWith("WHEN IS"))
            return false;

        return findMatchingCalendarEvent(s) != null;
    }

    @Override
    public String process(Bot bot, String s) {
       Object[] data = findMatchingCalendarEvent(s);
       Date date = (Date) data[0];
       String location = (String) data[1];
       String description = (String) data[2];
       return "This event takes place at " + DATE_TIME_FORMAT.format(date) + " at " + location + ".";
    }

}
