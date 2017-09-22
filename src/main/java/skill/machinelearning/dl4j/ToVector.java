package skill.machinelearning.dl4j;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 * Created by joris on 9/21/17.
 */
public class ToVector {

    public static double[] toVector(String s, ParagraphVectors para2Vec)
    {
        try {
            return para2Vec.inferVector(s).data().asDouble();
        }catch(Exception ex)
        {
            return null;
        }
    }

    public static double[] toVector(String s, Word2Vec word2Vec)
    {
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        double[] out = null;
        if(s== null)
            return null;

        for(String token : tokenizerFactory.create(s).getTokens())
        {
            if(out == null)
                out = word2Vec.getWordVector(token);
            else
            {
                double[] tmp = word2Vec.getWordVector(token);
                if(tmp != null) {
                    for (int i = 0; i < out.length; i++)
                        out[i] += tmp[i];
                }
            }
        }
        return out;
    }

}
