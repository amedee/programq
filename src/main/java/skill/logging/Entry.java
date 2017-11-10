package skill.logging;

/**
 * Created by joris on 9/25/17.
 * This class represents a single entry in the Bot's records
 */
public class Entry
    {
        // the input the Bot was given
        private String input;

        // the output the Bot gave
        private String output;

        // the ISkill implementation that provided the output
        private String skill;

        // the timestamp at which the conversation took place
        private long timestamp;

        public Entry(String input, String output, String skill, long timestamp)
        {
            this.input = input;
            this.output = output;
            this.skill = skill;
            this.timestamp = timestamp;
        }
        public String getInput(){ return input; }
        public String getOutput(){ return output; }
        public String getSkill(){ return skill; }
        public long getTimestamp(){ return timestamp; }
    }