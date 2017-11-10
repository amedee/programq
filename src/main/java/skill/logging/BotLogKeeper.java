package skill.logging;

import bot.IBotListener;
import bot.ISkill;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by joris on 9/25/17.
 * This ISkill implementation ensures the Bot keeps records, which is useful for later machine learning
 */
public class BotLogKeeper implements IBotListener {

    // when to start flushing the cache
    private int LOG_FLUSH_THRESHOLD = 8;

    // where to output the records to
    private static File HOME = new File(System.getProperty("user.home"), BotLogKeeper.class.getSimpleName());

    /**
     * Get the directory at which this BotLogKeeper stores its historic records
     * @return the File at which this BotLogKeeper stores its records
     */
    public static File getLogDirectory()
    {
        return HOME;
    }

    private List<Entry> entryList = new ArrayList<>();

    @Override
    public void onInput(String in) {}

    @Override
    public void onOutput(String input, String output, ISkill skill) {

        if(entryList.isEmpty()) {
            addToLog(input, output, skill);
        }
        else{
            Entry tuple = entryList.get(entryList.size() - 1);
            if(!tuple.getOutput().equals(output)) {
                addToLog(input, output, skill);
            }
            else{
                long timeDiff = java.lang.Math.abs(System.currentTimeMillis() - tuple.getTimestamp());
                if(timeDiff > 500)
                    addToLog(input, output, skill);
            }
        }
    }

    private void addToLog(String input, String output, ISkill skill)
    {
        // create entry
        Entry e = new Entry(input, output, skill.getClass().getSimpleName(), System.currentTimeMillis());

        // add to entryList
        entryList.add(e);

        // flush if needed
        if(entryList.size() > LOG_FLUSH_THRESHOLD) {
            try {
                flushLog();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static List<Entry> loadEntries(File file)
    {
        List<Entry> retval = new ArrayList<>();
        try {
            for(Element e : new SAXBuilder().build(file).getRootElement().getChildren())
            {
                Entry entry = new Entry(e.getChildText("input"), e.getChildText("output"), e.getChildText("skill"), Long.parseLong(e.getChildText("timestamp")));
                retval.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retval;
    }

    /**
     * Flush the log to the filesystem
     * @throws IOException
     * @throws JDOMException
     */
    private void flushLog() throws IOException, JDOMException
    {
        List<Entry> tmp = new ArrayList<>(entryList);
        entryList.clear();

        // create writer
        if(!HOME.exists())
            HOME.mkdirs();
        File outFile = new File(HOME, "log_" + new SimpleDateFormat("dd_MM_yyyy").format(new Date(System.currentTimeMillis())) + ".xml");
        if(outFile.exists())
        {
            appendToXML(outFile, tmp);
        }
        else
        {
            createXML(outFile, tmp);
        }
    }

    /**
     * Append entries to an existing log file
     * @param outputFile the existing log file
     * @param entries the entries to be added to the log file
     * @throws IOException
     * @throws JDOMException
     */
    private void appendToXML(File outputFile, List<Entry> entries) throws IOException, JDOMException
    {
        Document doc = new SAXBuilder().build(outputFile);

        Element rootElement = doc.getRootElement();
        for(Entry e : entries)
        {
            Element entryElement = new Element("entry");
            entryElement.addContent(new Element("input").setText(e.getInput()));
            entryElement.addContent(new Element("output").setText(e.getOutput()));
            entryElement.addContent(new Element("skill").setText(e.getSkill()));
            entryElement.addContent(new Element("timestamp").setText(e.getTimestamp()+""));
            rootElement.addContent(entryElement);
        }

        // new XMLOutputter().output(doc, System.out);
        XMLOutputter xmlOutput = new XMLOutputter();

        // display nice nice
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(doc, new FileWriter(outputFile));
    }

    /**
     * Creates a new log file with the given entries
     * @param outputFile the log file
     * @param entries the entries to be added to the log file
     * @throws IOException
     */
    private void createXML(File outputFile, List<Entry> entries) throws IOException
    {
        Document doc = new Document();

        Element rootElement = new Element("entryList");
        doc.setRootElement(rootElement);

        for(Entry e : entries)
        {
            Element entryElement = new Element("entry");
            entryElement.addContent(new Element("input").setText(e.getInput()));
            entryElement.addContent(new Element("output").setText(e.getOutput()));
            entryElement.addContent(new Element("skill").setText(e.getSkill()));
            entryElement.addContent(new Element("timestamp").setText(e.getTimestamp()+""));
            rootElement.addContent(entryElement);
        }

        // new XMLOutputter().output(doc, System.out);
        XMLOutputter xmlOutput = new XMLOutputter();

        // display nice nice
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(doc, new FileWriter(outputFile));
    }

}
