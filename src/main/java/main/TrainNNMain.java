package main;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.xml.PrettyPrinter;
import skill.machinelearning.CornellMovieDatabase;
import skill.machinelearning.IConversationProvider;
import skill.machinelearning.dl4j.ConversationDataSetIterator;
import skill.machinelearning.dl4j.ToVector;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by joris on 9/21/17.
 */
public class TrainNNMain {

    private static Logger log = LoggerFactory.getLogger(TrainNNMain.class);

    public static void main(String[] args) throws IOException {

        String inputFile =  new File(System.getProperty("user.home"),"par_2_vec_programq").getAbsolutePath();
        ParagraphVectors vec = WordVectorSerializer.readParagraphVectors(inputFile);
        vec.setTokenizerFactory(new DefaultTokenizerFactory());
        vec.getConfiguration().setIterations(1);

        File root = new File("/home/joris/Downloads/cornell_movie_dialogs_corpus/cornell movie-dialogs corpus");
        IConversationProvider conversationProvider = new CornellMovieDatabase(root);

        ConversationDataSetIterator mnistTrain = new ConversationDataSetIterator(conversationProvider, vec, 128);

        int seed = 123;
        int numIterations = 10;
        int numEpochs = 10;
        double learningRate = 0.01;
        int xDim = 100;

        MultiLayerNetwork net = new MultiLayerNetwork(new NeuralNetConfiguration.Builder()
                .seed(seed)
                .iterations(numIterations)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .learningRate(learningRate)
                .weightInit(WeightInit.XAVIER)
                .updater(Updater.NESTEROVS)     //To configure: .updater(new Nesterovs(0.9))
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(xDim)
                        .nOut(100)
                        .activation(Activation.SIGMOID)
                        .build())
                .layer(1, new DenseLayer.Builder()
                        .nIn(100)
                        .nOut(64)
                        .activation(Activation.SIGMOID)
                        .build())
                .layer(2, new DenseLayer.Builder()
                        .nIn(64)
                        .nOut(64)
                        .activation(Activation.SIGMOID)
                        .build())
                .layer(3, new DenseLayer.Builder()
                        .nIn(64)
                        .nOut(64)
                        .activation(Activation.SIGMOID)
                        .build())
                .layer(4, new DenseLayer.Builder()
                        .nIn(64)
                        .nOut(64)
                        .activation(Activation.SIGMOID)
                        .build())
                .layer(5, new DenseLayer.Builder()
                        .nIn(64)
                        .nOut(64)
                        .activation(Activation.SIGMOID)
                        .build())
                .layer(6, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .activation(Activation.IDENTITY)
                        .nIn(64)
                        .nOut(xDim).build())
                .pretrain(false).backprop(true).build()
        );
        net.init();
        net.setListeners(new ScoreIterationListener(1));

        log.info("Train model....");
        for( int i=0; i<numEpochs; i++ ){
            log.info("Epoch " + i);
            net.fit(mnistTrain);
        }

        try {
            ModelSerializer.writeModel(net, new File(System.getProperty("user.home"),"par2par_nn_programq"), true);
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<INDArray> ys = net.feedForward(Nd4j.create(ToVector.toVector("My name is Joris", vec)));
        double[] y = ys.get(ys.size() - 1).data().asDouble();

        double minDist = 0;
        String bestY = null;
        for(String[] conv : conversationProvider.getConversations()) {
            for (String s : conv) {
                double[] vS = ToVector.toVector(s, vec);
                if(vS == null)
                    continue;
                if(dist(vS, y) < minDist || bestY == null)
                {
                    bestY = s;
                    minDist = dist(vS, y);
                }
            }
        }
        System.out.println(bestY);

    }

    private static double dist(double[] d0, double[] d1)
    {
        double out = 0;
        for(int i=0;i<d0.length;i++)
            out += java.lang.Math.pow(d0[i] - d1[i],2);
        out /= d0.length;
        out = java.lang.Math.sqrt(out);
        return out;
    }
}
