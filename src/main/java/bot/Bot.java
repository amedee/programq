package bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joris on 9/15/17.
 * This class represents the entire chatbot, and acts
 * as a composite around ISkill.
 */
public class Bot {

    private List<IBotListener> listeners = new ArrayList<>();
    private List<ISkill> skillList = new ArrayList<>();

    /**
     * Add a skill to this Bot
     * @param skill the skill to be added
     * @return the modified Bot
     */
    public Bot addSkill(ISkill skill)
    {
        skillList.add(skill);
        return this;
    }

    /**
     * Add an IBotListener to the Bot
     * @param listener the listener to be added
     * @return true if the listener was added successfully, false otherwise
     */
    public boolean addListener(IBotListener listener)
    {
        return listeners.add(listener);
    }

    /**
     * Remove a IBotListener from the Bot
     * @param listener the listener to be removed
     * @return true if the listener was removed successfully, false otherwise
     */
    public boolean removeListener(IBotListener listener)
    {
        return listeners.remove(listener);
    }

    /**
     * Notifies all IBotListeners that input was given to the Bot
     * @param input the input that was fed to the Bot
     */
    private void fireOnInput(String input)
    {
        for(int i = listeners.size() - 1 ; i >= 0 ; i--)
        {
            listeners.get(i).onInput(input);
        }
    }

    /**
     * Notifies all IBotListeners that output was produced
     * @param input the input that was fed to the Bot
     * @param output the output that was generated by the Bot
     * @param skill the ISkill responsible for generating the output
     */
    private void fireOnOutput(String input, String output, ISkill skill)
    {
        for(int i = listeners.size() - 1 ; i >= 0 ; i--)
        {
            listeners.get(i).onOutput(input, output, skill);
        }
    }

    /**
     * Process the input by the user
     * @param in user input text
     * @return output generated by one (or several ISkills)
     */
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
