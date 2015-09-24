import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author Vivek
 */
public class topwords {
    static double ONE = 1;
    public static void main(String[] args)throws FileNotFoundException, IOException {
        BufferedReader trainReader = new BufferedReader(new FileReader(args[0]));
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
        
        Set<Entry<String, Double>> dummySet = libVocabProb.entrySet();
        List<Entry<String, Double>> list = new ArrayList<Entry<String, Double>>(dummySet);
        Collections.sort( list, new Comparator<Map.Entry<String, Double>>()
        {
            public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        int i = 0;
        for(Entry entry:list){
            i++;
            if(i>20){
                break;
            }
            out.println(entry.getKey()+" "+String.format("%.04f",entry.getValue()));
        }
        
        out.println();
        
        // Sorting and printing conservative
        dummySet = consVocabProb.entrySet();
        list = new ArrayList<Entry<String, Double>>(dummySet);
        Collections.sort( list, new Comparator<Map.Entry<String, Double>>()
        {
            public int compare( Map.Entry<String, Double> o1, Map.Entry<String, Double> o2 )
            {
                return (o2.getValue()).compareTo( o1.getValue() );
            }
        } );
        i = 0;
        for(Entry entry:list){
            i++;
            out.println(entry.getKey()+" "+String.format("%.04f",entry.getValue()));
            if(i>=20){
                break;
            }
        }
        
    }
    
}
