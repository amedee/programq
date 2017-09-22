package main;

import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.iterator.Word2VecDataFetcher;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import skill.machinelearning.CornellMovieDatabase;
import skill.machinelearning.IConversationProvider;
import skill.machinelearning.dl4j.ConversationSentenceIterator;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Created by joris on 9/21/17.
 */
public class TrainWord2VecMain {

    public static void main(String[] args)
    {
        // training set
        File root = new File("/home/joris/Downloads/cornell_movie_dialogs_corpus/cornell movie-dialogs corpus");
        IConversationProvider conversationProvider = new CornellMovieDatabase(root);

        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();

        SentenceIterator iter = new ConversationSentenceIterator(conversationProvider);

        /*
            CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
            So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
            Additionally it forces lower case for all tokens.
         */
        t.setTokenPreProcessor(new CommonPreprocessor());


        ParagraphVectors vec = new ParagraphVectors.Builder()
                .allowParallelTokenization(false)
                //.limitVocabularySize(5000)
                //.minWordFrequency(5)
                .iterations(1000)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        vec.fit();
        WordVectorSerializer.writeWord2VecModel(vec, new File(System.getProperty("user.home"),"par_2_vec_programq").getAbsolutePath());

        // Prints out the closest 10 words to "day". An example on what to do with these Word Vectors.
        Collection<String> lst = vec.wordsNearest("how are you", 10);
        System.out.println("10 Words closest to 'day': " + lst);

    }
}
