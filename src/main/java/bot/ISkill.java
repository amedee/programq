package bot;

/**
 * Created by joris on 9/15/17.
 */
public interface ISkill {

    boolean isExampleUtterance(String s);

    String process(Bot bot, String s);

}
