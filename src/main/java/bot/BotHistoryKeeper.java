package bot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by joris on 9/25/17.
 */
public class BotHistoryKeeper implements IBotListener{

    private int LOG_FLUSH_THRESHOLD = 16;
    private File HOME = new File(System.getProperty("user.home"));

    static class Entry
    {
        String input;
        String output;
        String skill;
        long timestamp;
    }
    private List<Entry> log = new ArrayList<>();

    @Override
    public void onInput(String in) {}

    @Override
    public void onOutput(String input, String output, ISkill skill) {

       if(log.isEmpty()) {
           addToLog(input, output, skill);
       }
       else{
           Entry tuple = log.get(log.size() - 1);
           if(!tuple.output.equals(output)) {
               addToLog(input, output, skill);
           }
           else{
               long timeDiff = java.lang.Math.abs(System.currentTimeMillis() - tuple.timestamp);
               if(timeDiff > 500)
                   addToLog(input, output, skill);
           }
       }
    }

    private void addToLog(String input, String output, ISkill skill)
    {
        // create entry
        Entry e = new Entry();
        e.input = input;
        e.output = output;
        e.skill = skill.getClass().getSimpleName();
        e.timestamp = System.currentTimeMillis();

        // add to log
        log.add(e);

        // flush if needed
        if(log.size() > LOG_FLUSH_THRESHOLD) {
            try {
                flushLog();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void flushLog() throws IOException
    {
        List<Entry> tmp = new ArrayList<>(log);
        log.clear();

        // create writer
        File outFile = new File(HOME, "log_" + new SimpleDateFormat("dd_MM_yyyy").format(new Date(System.currentTimeMillis())));
        FileWriter writer = null;
        if(outFile.exists())
        {
            writer = new FileWriter(outFile, true);
        }
        else
        {
            writer = new FileWriter(outFile);
        }

        // IO
        for(Entry tuple : tmp)
        {
            writer.write(tuple.input + "\t" + tuple.output + "\t" + tuple.skill + "\n");
        }
        writer.flush();
        writer.close();
    }
}
