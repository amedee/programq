package skill.machinelearning.dl4j;

import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import skill.machinelearning.IConversationProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joris on 9/21/17.
 */
public class ConversationSentenceIterator extends CollectionSentenceIterator{


    private static List<String> toList(IConversationProvider conversationProvider)
    {
        List<String> conversations = new ArrayList<>();
        for(String[] conv : conversationProvider.getConversations()) {
            for (String line : conv) {
                conversations.add(line);
            }
        }
        System.out.println(conversations.size());
        return conversations;
    }


    public ConversationSentenceIterator(IConversationProvider conversationProvider)
    {
        super(toList(conversationProvider));
    }

    public ConversationSentenceIterator(IConversationProvider conversationProvider, int len)
    {
        super(toList(conversationProvider).subList(0, len));
    }

}
