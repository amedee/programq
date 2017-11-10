package skill.logging;

public class Entry
    {
        private String input;
        private String output;
        private String skill;
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