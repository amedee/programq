package skill.aiml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by joris on 9/15/17.
 */
public class AIMLInterpreter {

    private static Random RND = new Random(System.currentTimeMillis());
    private List<AIMLNode> aimlNodeList = new ArrayList<>();

    public AIMLInterpreter addAIMLNodes(Collection<AIMLNode> aimlNodeCollection) {
        aimlNodeList.addAll(aimlNodeCollection);
        return this;
    }

    public int countNodes()
    {
        return aimlNodeList.size();
    }

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
            String redirect = process(n.firstChild(AIMLNodeType.TEXT), m, s);
            // System.out.println("[" + redirect + "]");
            return process(redirect);
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

        // default
        return null;
    }

}
