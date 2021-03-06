import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Vivek
 */
public class nb {
    static double ONE = 1;
    public static void main(String[] args)throws FileNotFoundException, IOException {
        BufferedReader trainReader = new BufferedReader(new FileReader(args[0]));
        BufferedReader testReader = new BufferedReader(new FileReader(args[1]));
        BufferedReader blogReader;  
        String blog = "";
        String word = "";
        double trainTotal = 0, trainLib = 0, trainCons = 0;
        // Reading training frequencies into vocab Maps
        Map <String,Double> libVocabFreq = new HashMap<String,Double>();
        Map <String,Double> consVocabFreq = new HashMap<String,Double>();
        while((blog = trainReader.readLine()) != null){
            trainTotal++;
           if(blog.matches("con(.*)")){
               trainCons++;
               blogReader = new BufferedReader(new FileReader(blog));
               while((word = blogReader.readLine()) != null){
                   word = word.toLowerCase();
                   if(consVocabFreq.get(word) == null){
                       consVocabFreq.put(word,ONE);
                   }else{
                       consVocabFreq.put(word,consVocabFreq.get(word)+1);
                   }
               }
           }else{
               trainLib++;
               blogReader = new BufferedReader(new FileReader(blog));
               while((word = blogReader.readLine()) != null){
                   word = word.toLowerCase();
                   if(libVocabFreq.get(word) == null){
                       libVocabFreq.put(word,ONE);
                   }else{
                       libVocabFreq.put(word,libVocabFreq.get(word)+1);
                   }
               }
           }            
        }
        double libProb = (double)(trainLib/trainTotal);
        double consProb = (double)(trainCons/trainTotal);
        
        Double[] libFreq =  libVocabFreq.values().toArray(new Double[0]);
        double libCorpusSize = 0;
        for(int x=0;x<libFreq.length;x++){
            libCorpusSize = libCorpusSize+libFreq[x];
        }
        Double[] consFreq =  consVocabFreq.values().toArray(new Double[0]);
        double consCorpusSize = 0;
        for(int x=0;x<consFreq.length;x++){
            consCorpusSize = consCorpusSize+consFreq[x];
        }
        Set<String> vocab = new HashSet<String>(libVocabFreq.keySet());
        vocab.addAll(consVocabFreq.keySet());
        double vocabSize = vocab.size();
        
        // Modifying lib and cons freq to prob
        double value;
        Map <String,Double> libVocabProb = new HashMap<String,Double>();
        Map <String,Double> consVocabProb = new HashMap<String,Double>();
        double d;
        
        for (Map.Entry<String, Double> pair : libVocabFreq.entrySet()){
            word = (String) pair.getKey();
            value = (double) pair.getValue();
            d = ((double)(value + 1)/(libCorpusSize + vocabSize));
            libVocabProb.put(word, d);
        }
        for (Map.Entry<String, Double> pair : consVocabFreq.entrySet()){
            word = (String) pair.getKey();
            value = (double) pair.getValue();
            d = ((double)(value + 1)/(consCorpusSize + vocabSize));
            consVocabProb.put(word, d);
        }
        
        //Test Stuff
        double probEstimateLib = 0;
        double probEstimateCons = 0;
        Map <String,Double> docVocab = new HashMap<String,Double>();
        String label;
        int correctPred = 0;
        int testLength = 0;
        
        while((blog = testReader.readLine()) != null){
            blogReader = new BufferedReader(new FileReader(blog));
            testLength++;
            probEstimateLib = 0;
            probEstimateCons = 0;
            // Analysis
            while((word = blogReader.readLine()) != null){
                word = word.toLowerCase();    
                if(vocab.contains(word)){
                    if(libVocabProb.get(word) != null){
                    probEstimateLib = probEstimateLib + Math.log(libVocabProb.get(word));
                    }else{
                        probEstimateLib = probEstimateLib + Math.log((double)(ONE)/(libCorpusSize + vocabSize));
                    }
                    
                    if(consVocabProb.get(word) != null){
                    probEstimateCons = probEstimateCons + Math.log(consVocabProb.get(word));
                    }else{
                        probEstimateCons = probEstimateCons + Math.log((double)(ONE)/(consCorpusSize + vocabSize));
                    } 
                }                  
            }
            probEstimateLib = probEstimateLib + Math.log(libProb);
            probEstimateCons = probEstimateCons + Math.log(consProb);
            if(probEstimateLib > probEstimateCons){
                label = "L";
                if(blog.matches("lib(.*)")){
                    correctPred++;
                }
            }else{
                label = "C";
                if(blog.matches("con(.*)")){
                    correctPred++;
                }
            }
            out.println(label);
        }
        out.println("Accuracy: " + String.format("%.04f",((double)correctPred/(double)testLength)));
        
        
    }
    
}
