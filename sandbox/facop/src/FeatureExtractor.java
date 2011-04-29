
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author semanticvoid
 */
public class FeatureExtractor {
    
    private static final double TEST_PERCENT = 0.1;
    
    public Corpus training;
    public Corpus test;
    
    public FeatureExtractor() {
        training = new Corpus();
        test = new Corpus();
    }

    public void readInstances(String corpusPath) {
        try {
            BufferedReader rdr = new BufferedReader(new FileReader(corpusPath));
            int trainId = 0;
            int testId = 0;
            
            String line = null;
            while((line = rdr.readLine()) != null) {
                String[] tokens = line.split("\t");
                
                if(Math.random() > TEST_PERCENT) {
                    training.addInstance(new Instance(trainId++, tokens[0], tokens[1]));
                } else {
                    test.addInstance(new Instance(testId++, tokens[0], tokens[1]));
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(FeatureExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Extract only Personal Pronouns
     * @param instances 
     */
    public void extractFeaturesI(List<Instance> instances) {
        String[] ppronouns = {
            "i", "you", "he", "she", "it", "me", "him", "her", "myself", "yourself",
            "himself", "herself", "itself", "we", "they", "us", "them", "mine",
            "your", "yourself", "our", "ours", "yours"
        };
        
        for(Instance i : instances) {
            String text = i.line;
            String[] tokens = text.toLowerCase().split("[!@#$%^&*(),.:'\"; ]+");
            
            HashMap<String, Integer> ppFeatures = new HashMap<String, Integer>();
            for(String t : tokens) {
                for(String pp : ppronouns) {
                    if(t.equals(pp)) {
                        if(!ppFeatures.containsKey(pp)) {
                            ppFeatures.put(pp, 0);
                        }
                        
                        ppFeatures.put(pp, ppFeatures.get(pp)+1);
                    }
                }
            }
            
            StringBuffer buf = new StringBuffer("|ppfeatures ");
            for(String key : ppFeatures.keySet()) {
                buf.append(key + ":" + ppFeatures.get(key) + " ");
            }
            
            i.features += buf.toString();
        }
    }
    
    /**
     * Extract only Punctuations
     * @param instances 
     */
    public void extractFeaturesII(List<Instance> instances) {
        char[] puncts = {
            '.', '!', '?', '@', '#', ','
        };
        
        
        for(Instance i : instances) {
            String text = i.line;
            char[] characters = text.toLowerCase().toCharArray();
            
            HashMap<Character, Integer> pFeatures = new HashMap<Character, Integer>();
            for(Character c : characters) {
                for(Character p : puncts) {
                    if(c == p) {
                        if(!pFeatures.containsKey(p)) {
                            pFeatures.put(p, 0);
                        }
                        
                        pFeatures.put(p, pFeatures.get(p)+1);
                    }
                }
            }
            
            StringBuffer buf = new StringBuffer("|punctfeatures ");
            for(Character key : pFeatures.keySet()) {
                buf.append(key + ":" + pFeatures.get(key) + " ");
            }
            
            i.features += buf.toString();
        }
    }
    
    public void printTrainCorpus() {
        List<Instance> trainingInstances = training.getInstances();
//        Collections.shuffle(trainingInstances);
        for(Instance i : trainingInstances) {
            System.out.println(i.toVWString());
        }
    }
    
    public void printTestCorpus() {
        List<Instance> testInstances = test.getInstances();
        for(Instance i : testInstances) {
            System.out.println(i.toVWString());
        }
    }

    public static void main(String[] args) {
        FeatureExtractor fe = new FeatureExtractor();
        fe.readInstances(args[0]);
        
        // extract personal pronoun features
        fe.extractFeaturesI(fe.training.getInstances());
        fe.extractFeaturesI(fe.test.getInstances());
        
        // extract puntuation features
        fe.extractFeaturesII(fe.training.getInstances());
        fe.extractFeaturesII(fe.test.getInstances());
        
        fe.printTrainCorpus();
        System.out.println("\n-----------------\n");
        fe.printTestCorpus();
    }
}
