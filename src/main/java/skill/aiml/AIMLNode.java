package skill.aiml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by joris on 9/15/17.
 */
public class AIMLNode {

    private AIMLNodeType nodeType;
    private String text;

    private List<AIMLNode> children = new ArrayList<>();

    public AIMLNode(){}

    public AIMLNode setText(String s)
    {
        this.text = s;
        return this;
    }

    public String getText()
    {
        return text;
    }

    public AIMLNode setType(AIMLNodeType type)
    {
        this.nodeType = type;
        return this;
    }

    public AIMLNodeType getType()
    {
        return nodeType;
    }

    public AIMLNode addChild(AIMLNode childNode)
    {
        this.children.add(childNode);
        return this;
    }

    public Collection<AIMLNode> getChildren()
    {
        return children;
    }

    public AIMLNode firstChild()
    {
        return children.get(0);
    }

    public AIMLNode firstChild(AIMLNodeType nodeType)
    {
        for(AIMLNode n : children)
            if(n.getType() == nodeType)
                return n;
        return null;
    }
}
