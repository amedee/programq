package bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joris on 9/15/17.
 */
public class Bot {

    private List<IBotListener> listeners = new ArrayList<>();
    private List<ISkill> skillList = new ArrayList<>();

    public Bot addSkill(ISkill skill)
    {
        skillList.add(skill);
        return this;
    }

    public boolean addListener(IBotListener listener)
    {
        return listeners.add(listener);
    }

    public boolean removeListener(IBotListener listener)
    {
        return listeners.remove(listener);
    }

    private void fireOnInput(String input)
    {
        for(int i = listeners.size() - 1 ; i >= 0 ; i--)
        {
            listeners.get(i).onInput(input);
        }
    }

    private void fireOnOutput(String input, String output, ISkill skill)
    {
        for(int i = listeners.size() - 1 ; i >= 0 ; i--)
        {
            listeners.get(i).onOutput(input, output, skill);
        }
    }

    public String process(String in)
    {
        // notify listeners
        fireOnInput(in);

        for(ISkill skill : skillList)
        {
            if(skill.isExampleUtterance(in)) {

                // process
                String out =  skill.process(this, in);

                // notify listeners
                fireOnOutput(in, out, skill);

                // return
                return out;

            }
        }

        // default
        return null;
    }
}
