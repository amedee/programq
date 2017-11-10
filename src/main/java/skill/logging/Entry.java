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

        /**
         *  Get the input
         * @return the input
         */
        public String getInput(){ return input; }

        /**
         * Get the output
         * @return the output
         */
        public String getOutput(){ return output; }

        /**
         * Get the ISkill responsible for creating the output this log entry
         * @return the name of the ISkill responsible for this log entry
         */
        public String getSkill(){ return skill; }

        /**
         * Get the timestamp at which the log entry was created
         * @return
         */
        public long getTimestamp(){ return timestamp; }
    }