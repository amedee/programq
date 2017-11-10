package skill.ml;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;
import java.io.IOException;

public class DialogTrainer {

    private static File createTemporaryData(int len)
    {
        return null;
    }

    public static void main(String[] args) throws IOException {

        // create temporary dataset
        File tmp = createTemporaryData(5000);

        SentenceIterator iter = new BasicLineIterator(tmp);

        AbstractCache<VocabWord> cache = new AbstractCache<>();

        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        /*
             if you don't have LabelAwareIterator handy, you can use synchronized labels generator
              it will be used to label each document/sequence/line with it's own label.
              But if you have LabelAwareIterator ready, you can can provide it, for your in-house labels
        */
        LabelsSource source = new LabelsSource("SENTENCE_");

        ParagraphVectors vec = new ParagraphVectors.Builder()
                .allowParallelTokenization(false)
                .minWordFrequency(3)
                .iterations(10)
                .epochs(10)
                .layerSize(32)
                .learningRate(0.025)
                .labelsSource(source)
                .windowSize(5)
                .iterate(iter)
                .trainWordVectors(false)
                .vocabCache(cache)
                .tokenizerFactory(t)
                .sampling(0)
                .build();

        vec.fit();

        // store
        File home = new File(System.getProperty("user.home"));
        File documents = new File(home, "Documents");
        File vecFile = new File(documents,"p2v.bin");
        WordVectorSerializer.writeParagraphVectors(vec, vecFile);

        // delete tmp file
        tmp.delete();
    }
}
