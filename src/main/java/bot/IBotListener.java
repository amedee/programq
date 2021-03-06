package bot;

/**
 * Created by joris on 9/25/17.
 * This class provides a listener for the Bot class
 */
public interface IBotListener {

    /**
     * This method gets called whenever the Bot was fed input
     * @param in input fed to the Bot
     */
    void onInput(String in);

    /**
     * This method gets called whenever the Bot generates output
     * @param input input fed to the Bot
     * @param output output generated by the Bot
     * @param skill the ISkill implementation that genererated the output
     */
    void onOutput(String input, String output, ISkill skill);

}
