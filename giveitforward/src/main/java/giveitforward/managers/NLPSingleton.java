package giveitforward.managers;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

public class NLPSingleton {
    private static StanfordCoreNLP pipelineInstance = null;

    private NLPSingleton() { System.err.println("Creating singleton for NLP pipeline..."); }

    public static StanfordCoreNLP getPipelineInstance(boolean useRegexner) {
        if (pipelineInstance == null) {
            System.out.println("creating new pipeline instance");
            pipelineInstance = createPipelineInstance(useRegexner);
        }
        return pipelineInstance;
    }

    public static StanfordCoreNLP createPipelineInstance(boolean useRegexner) {
        Throwable exception = null;
        try {
            Properties props = new Properties();
            if (useRegexner) {
                props.put("annotators", "tokenize,ssplit,pos,lemma,ner,regexner");
                props.put("regexner.mapping", "utah_mappings.txt");
            } else {
                props.put("annotators", "tokenize,ssplit,pos,lemma,ner");
            }
            return new StanfordCoreNLP(props);
        } catch (Throwable ex) {
            System.err.println("Failed to create StanfordCoreNLP pipeline. Retrying...");
            exception = ex;
        }
        throw new ExceptionInInitializerError(exception);
    }
}
