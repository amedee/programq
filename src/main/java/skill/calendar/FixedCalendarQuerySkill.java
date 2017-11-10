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

    private static Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(new String[]{"THE","AN","A","SESSION","TALK","EVENT","ABOUT","WITH", "REGARDS","TO","WHEN","IS","ARE","WHERE"}));

    private List<Object[]> loadCalendar()
    {
        List<Object[]> output = new ArrayList<>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            Scanner sc =  new Scanner(HOME);
            while(sc.hasNextLine())
            {
                String[] line = sc.nextLine().split("\t+");
                output.add(new Object[]{format.parse(line[0]), line[1], line[2]});
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
        for(Object[] entry : calendar)
        {
            Set<String> description = new HashSet<>(Arrays.asList(entry[2].toString().toUpperCase().split(" ")));
            description.removeAll(STOP_WORDS);
            if(description.containsAll(query))
                return entry;
        }
        return null;
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
       return "This event takes place around " + date.toString() + " at " + location + ".";
    }

}
