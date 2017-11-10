package bot;

/**
 * Created by joris on 9/15/17.
 * This class represents a single skill for the Bot
 */
public interface ISkill {

    /**
     * Decides whether a given input should trigger this ISkill or not
     * @param s the user input
     * @return true if the user input should trigger this ISkill, false otherwise
     */
    boolean isExampleUtterance(String s);

    /**
     * Process a given input with this ISkill
     * @param bot the Bot implementation that was given the input (to allow recursion)
     * @param s the user input
     * @return the output this ISkill associates with the given user input
     */
    String process(Bot bot, String s);

}
