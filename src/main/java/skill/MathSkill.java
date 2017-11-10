package skill;

import bot.Bot;
import bot.ISkill;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by joris on 9/15/17.
 * This ISkill implementation allows the bot to do some basic math
 */
public class MathSkill implements ISkill{

    private String[] ops = {"+","-","*","/","^","²","³"};
    private String[] bks = {"(",")","{","}","[","]"};

    @Override
    public boolean isExampleUtterance(String s) {
        List<String> tokens = Arrays.asList(s.split(" +"));
        for(String op : ops)
            tokens = split(tokens, op);
        for(String bk : bks)
            tokens = split(tokens, bk);

        Set<String> opsAndBks = new HashSet<>();
        opsAndBks.addAll(Arrays.asList(ops));
        opsAndBks.addAll(Arrays.asList(bks));

        for(String token : tokens)
        {
            if(token.isEmpty())
                continue;
            if(token.matches(" +"))
                continue;
            if(token.matches("[0123456789]+"))
                continue;
            if(token.matches("[0123456789]*\\.[0123456879]+"))
                continue;
            if(opsAndBks.contains(token))
                continue;
            // unknown token
            return false;
        }

        // default
        return true;
    }

    private List<String> split(List<String> in, String delim)
    {
        List<String> out = new ArrayList<>();
        for(String s : in)
        {
            String[] tmp = s.split(Pattern.quote(delim));
            if(tmp.length == 1)
            {
                if(s.endsWith(delim))
                {
                    out.add(tmp[0]);
                    out.add(delim);
                }
                else if(s.startsWith(delim))
                {
                    out.add(delim);
                    out.add(tmp[0]);
                }
                else
                {
                    out.add(s);
                }
            }else {
                for (int i = 0; i < tmp.length; i++)
                    if (i == 0) {
                        out.add(tmp[i]);
                    } else {
                        out.add(delim);
                        out.add(tmp[i]);
                    }
            }
        }
        return out;
    }

    @Override
    public String process(Bot bot, String s) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            return engine.eval(s).toString();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return null;
    }

}
