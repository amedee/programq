package bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joris on 9/15/17.
 */
public class Bot {

    private List<ISkill> skillList = new ArrayList<>();

    public Bot addSkill(ISkill skill)
    {
        skillList.add(skill);
        return this;
    }

    public String process(String s)
    {
        for(ISkill skill : skillList)
        {
            if(skill.isExampleUtterance(s))
                return skill.process(this, s);
        }
        return null;
    }
}
