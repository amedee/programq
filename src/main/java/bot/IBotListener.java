package bot;

/**
 * Created by joris on 9/25/17.
 */
public interface IBotListener {

    void onInput(String in);

    void onOutput(String input, String output, ISkill skill);

}
