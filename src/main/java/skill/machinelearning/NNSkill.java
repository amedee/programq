package skill.machinelearning;

import bot.Bot;
import bot.ISkill;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import skill.machinelearning.dl4j.ToVector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by joris on 9/21/17.
 */
public class NNSkill implements ISkill{

    private Word2Vec word2Vec;
    private MultiLayerNetwork conversationNN;

    private IConversationProvider conversationProvider;

    public NNSkill(File word2VecFile, File nnFile, File convFile)
    {
        try {
            word2Vec = WordVectorSerializer.readWord2VecModel(word2VecFile);
            conversationNN = ModelSerializer.restoreMultiLayerNetwork(nnFile);
            conversationProvider = new CornellMovieDatabase(convFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String findReply(String s)
    {
        double[] x = ToVector.toVector(s, word2Vec);

        String bestY = null;
        double minDistX = Double.MAX_VALUE;
        for(String[] conv : conversationProvider.getConversations())
        {
            for(int i=1;i<conv.length;i++)
            {
                String s0 = conv[i-1];
                double[] s0Vec = ToVector.toVector(s0, word2Vec);
                if(s0Vec == null)
                    continue;

                String s1 = conv[i];

                if(dist(s0Vec, x) < minDistX)
                {
                    minDistX = dist(s0Vec, x);
                    bestY = s1;
                }
            }
        }
        return bestY;
    }

    private static double dist(double[] d0, double[] d1)
    {
        if(d0 == null || d1 == null)
            return Double.MAX_VALUE;

        double out = 0;
        for(int i=0;i<d0.length;i++)
            out += java.lang.Math.pow(d0[i] - d1[i],2);
        out /= d0.length;
        out = java.lang.Math.sqrt(out);
        return out;
    }

    @Override
    public boolean isExampleUtterance(String s) {
        return true;
    }

    @Override
    public String process(Bot bot, String s) {
        return findReply(s);
    }
}
