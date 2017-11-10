package skill.aiml;

import jdk.nashorn.internal.runtime.Context;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by joris on 9/15/17.
 * AIML (artificial intelligence markup language) is a format that allows
 * a bot-creator to essentially match regular expressions and have the bot output some text accordingly.
 * AIML adds abstraction, scripting, variables as well as redirects to this paradigm.
 *
 * This class acts as a simple interpreter for an AIML subset.
 */
public class AIMLInterpreter {

    // random to be used when an answer triggers multiple alternatives and one is chosen at random
    private static Random RND = new Random(System.currentTimeMillis());

    // all nodes that are known to the interpreter
    private List<AIMLNode> aimlNodeList = new ArrayList<>();

    /**
     * Construct a new AIMLInterpreter with a given collection of AIMLNode objects
     * @param aimlNodeCollection the collection of AIMLNode objects
     * @return
     */
    public AIMLInterpreter addAIMLNodes(Collection<AIMLNode> aimlNodeCollection) {
        aimlNodeList.addAll(aimlNodeCollection);
        return this;
    }

    /**
     * Get the number of AIMLNodes known by this AIMLInterpreter
     * @return
     */
    public int size()
    {
        return aimlNodeList.size();
    }

    /**
     *
     * @param s
     * @return
     */
    public boolean isExampleUtterance(String s) {
        for (AIMLNode aimlNode : aimlNodeList) {
            Matcher matcher = isExampleUtterance(aimlNode, s);
            if (matcher == null)
                return false;
            if(matcher.matches())
                return true;
        }
        return false;
    }

    /**
     * Determine whether a given AIMLNode should be executed against an input String
     * @param node the AIMLNode being tested
     * @param s the input String
     * @return a Matcher object if the AIMLNode was successfully matched against the input, null otherwise
     */
    private Matcher isExampleUtterance(AIMLNode node, String s)
    {
        if(node.getType() != AIMLNodeType.CATEGORY)
            return null;

        AIMLNode patternNode = node.firstChild(AIMLNodeType.PATTERN);
        if(patternNode == null)
            return null;

        AIMLNode textNode = patternNode.firstChild(AIMLNodeType.TEXT);
        if(textNode == null)
            return null;

        Pattern pattern = Pattern.compile(textNode.getText(), Pattern.CASE_INSENSITIVE);
        return pattern.matcher(s);
    }

    /**
     * Process a given piece of text according to all known AIMLNodes that are relevant for that input
     * @param s the input String
     * @return the output String
     */
    public String process(String s)
    {
        AIMLNode node = null;
        Matcher matcher = null;
        for(AIMLNode aimlNode : aimlNodeList) {
            Matcher tmp = isExampleUtterance(aimlNode, s);
            if(tmp != null && tmp.matches())
            {
                node = aimlNode;
                matcher = tmp;
                break;
            }
        }
        if(matcher == null)
            return null;
        return process(node, matcher, s);
    }

    /**
     * Execute a redirect.
     * Redirects are like synonyms to the bots.
     * For instance 'thank you' might be redirected to 'thanks'
     * @param s the input String
     * @return the output String
     */
    public String redirect(String s)
    {
        return process(s);
    }

    /**
     * Execute an AIMLNode
     * @param n the AIMLNode to be executed
     * @param m the Matcher object (useful when the AIMLNode contained a regular expression)
     * @param s the String that was matched
     * @return the output String
     */
    private String process(AIMLNode n, Matcher m, String s)
    {
        // CATEGORY
        if(n.getType() == AIMLNodeType.CATEGORY)
        {
            return process(n.firstChild(AIMLNodeType.TEMPLATE), m, s);
        }

        // TEMPLATE
        else if(n.getType() == AIMLNodeType.TEMPLATE || n.getType() == AIMLNodeType.LIST_ITEM)
        {
            String outputTxt = "";
            for(AIMLNode tmp : n.getChildren())
            {
                outputTxt += process(tmp, m, s);
            }
            return outputTxt;
        }

        // REDIRECT
        else if(n.getType() == AIMLNodeType.REDIRECT)
        {
            String targetTxt = process(n.firstChild(AIMLNodeType.TEXT), m, s);
            return redirect(targetTxt);
        }

        // TEXT
        else if(n.getType() == AIMLNodeType.TEXT) {
            String tmp = n.getText();
            for(int i=0;i<=m.groupCount();i++)
                tmp = tmp.replaceAll("\\\\" + i, m.group(i));
            return tmp;
        }

        // RANDOM
        else if(n.getType() == AIMLNodeType.RANDOM)
        {
            List<AIMLNode> childNodes = new ArrayList<>(n.getChildren());
            int rndIndex = RND.nextInt(childNodes.size());
            return process(childNodes.get(rndIndex), m, s);
        }

        // SCRIPT
        else if(n.getType() == AIMLNodeType.SCRIPT)
        {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
            try {
                String command = n.firstChild(AIMLNodeType.TEXT).getText();
                return engine.eval(command).toString();
            } catch (ScriptException e) {
                e.printStackTrace();
            }
        }

        // default
        return null;
    }

}
