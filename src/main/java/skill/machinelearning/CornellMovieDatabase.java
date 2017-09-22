package skill.machinelearning;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by joris on 9/21/17.
 */
public class CornellMovieDatabase implements IConversationProvider{

    static class Line
    {
        String[] fields;
        public Line(String text)
        {
            fields = text.split(" *\\+\\+\\+\\$\\+\\+\\+ *");
        }
    }

    private Map<String,String> linesByID;
    private Collection<String[]> conversations;

    public CornellMovieDatabase(File root)
    {
        linesByID = getMovieLines(new File(root, "movie_lines.txt"));
        conversations = getConversations(new File(root, "movie_conversations.txt"));
    }

    @Override
    public Collection<String[]> getConversations()
    {
        return conversations;
    }

    private Collection<String[]> getConversations(File f)
    {
        List<String[]> output = new ArrayList<>();
        for(Line l : getLines(f))
        {
            String[] ids = l.fields[l.fields.length - 1].replaceAll(" ","").replaceAll("(\\[|\\])","").replaceAll("'","").split(",");
            String[] conv = new String[ids.length];
            for(int i=0;i<ids.length;i++)
                conv[i] = linesByID.get(ids[i]);
            output.add(conv);
        }
        return output;
    }

    private Map<String, String> getMovieLines(File f)
    {
        Map<String, String> output = new HashMap<>();
        for(Line l : getLines(f))
        {
            output.put(l.fields[0], l.fields[l.fields.length - 1]);
        }
        return output;
    }

    private List<Line> getLines(File f)
    {
        List<Line> output = new ArrayList<>();
        Scanner sc = null;
        try {
            sc = new Scanner(f);
            while(sc.hasNextLine())
            {
                output.add(new Line(sc.nextLine()));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }
}
