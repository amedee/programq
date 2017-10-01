package skill.aiml;

import bot.Bot;
import bot.ISkill;
import main.Main01;

/**
 * Created by joris on 9/21/17.
 */
public class AIMLSkill implements ISkill {

    private IntegratedAIMLInterpreter interpreter;

    public AIMLSkill()
    {
        interpreter = new IntegratedAIMLInterpreter();
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("punctuation.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("abbreviations.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("british_to_us.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("teenspeak.xml")));

        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("salutations.xml")));

        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("what_happened.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("countries.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("famous.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("x11colors.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("cup_of.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("day.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("is_prime.xml")));

        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("coinflip.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("diceroll.xml")));

        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("jokes.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("nerdy.xml")));

        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("swearwords.xml")));
        interpreter.addAIMLNodes(AIMLReader.interpretXML(AIMLSkill.class.getClassLoader().getResourceAsStream("insults.xml")));

        System.out.println("loaded " + interpreter.countNodes() + " nodes.");
    }

    @Override
    public boolean isExampleUtterance(String s) {
        return interpreter.isExampleUtterance(s);
    }

    @Override
    public String process(Bot bot, String s) {
        interpreter.setBot(bot);
        return interpreter.process(s);
    }
}

class IntegratedAIMLInterpreter extends AIMLInterpreter
{
    private Bot bot;

    public IntegratedAIMLInterpreter()
    {}

    public IntegratedAIMLInterpreter setBot(Bot b)
    {
        this.bot = b;
        return this;
    }

    public String redirect(String s)
    {
        if(bot == null)
            return super.redirect(s);
        System.out.println("[" + s + "]");
        return bot.process(s);
    }
}