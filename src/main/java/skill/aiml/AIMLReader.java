package skill.aiml;

import org.jdom2.Content;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by joris on 9/15/17.
 */
public class AIMLReader {

    public static Collection<AIMLNode> interpretXML(InputStream is)
    {
        List<AIMLNode> nodes = new ArrayList<>();
        try {
            Element rootElement = new SAXBuilder().build(is).getRootElement();
            for(Element categoryElement : rootElement.getChildren("category"))
            {
                nodes.add(processElement(categoryElement));
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    private static AIMLNode processElement(Element e)
    {
        AIMLNode outputNode = new AIMLNode();
        if(e.getName().equalsIgnoreCase("category"))
            outputNode.setType(AIMLNodeType.CATEGORY);
        else if(e.getName().equalsIgnoreCase("pattern"))
            outputNode.setType(AIMLNodeType.PATTERN);
        else if(e.getName().equalsIgnoreCase("random"))
            outputNode.setType(AIMLNodeType.RANDOM);
        else if(e.getName().equalsIgnoreCase("redirect"))
            outputNode.setType(AIMLNodeType.REDIRECT);
        else if(e.getName().equalsIgnoreCase("template"))
            outputNode.setType(AIMLNodeType.TEMPLATE);
        else if(e.getName().equalsIgnoreCase("li"))
            outputNode.setType(AIMLNodeType.LIST_ITEM);
        else if(e.getName().equalsIgnoreCase("script"))
            outputNode.setType(AIMLNodeType.SCRIPT);

        for(Content child : e.getContent())
        {
            if(child instanceof Text)
            {
                if(outputNode.getType() == AIMLNodeType.CATEGORY)
                    continue;
                if(outputNode.getType() == AIMLNodeType.RANDOM)
                    continue;
                String text = ((Text) child).getText();
                if(outputNode.getType() == AIMLNodeType.TEMPLATE || outputNode.getType() == AIMLNodeType.LIST_ITEM)
                    text = normalizeOutput(text);
                if(!text.isEmpty() && !text.equals(" "))
                    outputNode.addChild(new AIMLNode().setType(AIMLNodeType.TEXT).setText(text));
            }
            else if(child instanceof Element)
            {
                outputNode.addChild(processElement((Element) child));
            }

        }

        return outputNode;
    }

    private static String normalizeOutput(String text)
    {
        while(text.contains("\n") || text.contains("\t"))
            text = text.replaceAll("[\\n\\t]+","");
        while(text.contains("  "))
            text = text.replaceAll("  ", " ");
        return text;
    }
}
