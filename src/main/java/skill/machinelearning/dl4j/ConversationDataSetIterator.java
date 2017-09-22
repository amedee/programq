package skill.machinelearning.dl4j;

import org.deeplearning4j.datasets.iterator.DoublesDataSetIterator;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.tokenization.tokenizer.DefaultTokenizer;
import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.primitives.Pair;
import skill.machinelearning.IConversationProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by joris on 9/21/17.
 */
public class ConversationDataSetIterator extends DoublesDataSetIterator {

    public ConversationDataSetIterator(IConversationProvider provider, Word2Vec word2Vec, int batchSize)
    {
        super(generateDataSet(provider, word2Vec), batchSize);
    }

    public ConversationDataSetIterator(IConversationProvider provider, ParagraphVectors para2Vec, int batchSize)
    {
        super(generateDataSet(provider, para2Vec), batchSize);
    }

    private static List<Pair<double[], double[]>> generateDataSet(IConversationProvider provider, ParagraphVectors para2Vec)
    {
        List<Pair<double[], double[]>> dataset = new ArrayList<>();
        for(String[] conv : provider.getConversations())
        {
            for(int i=1;i<conv.length;i++)
            {
                String s0 = conv[i-1];
                String s1 = conv[i];

                double[] v0 = ToVector.toVector(s0, para2Vec);
                double[] v1 = ToVector.toVector(s1, para2Vec);
                if(v0 == null || v1 == null)
                    continue;

                dataset.add(new Pair<double[], double[]>(v0, v1));
            }
        }
        return dataset;
    }

    private static List<Pair<double[], double[]>> generateDataSet(IConversationProvider provider, Word2Vec word2Vec)
    {
        List<Pair<double[], double[]>> dataset = new ArrayList<>();
        for(String[] conv : provider.getConversations())
        {
            for(int i=1;i<conv.length;i++)
            {
                String s0 = conv[i-1];
                String s1 = conv[i];

                double[] v0 = ToVector.toVector(s0, word2Vec);
                double[] v1 = ToVector.toVector(s1, word2Vec);
                if(v0 == null || v1 == null)
                    continue;

                dataset.add(new Pair<double[], double[]>(v0, v1));
            }
        }
        return dataset;
    }

}
