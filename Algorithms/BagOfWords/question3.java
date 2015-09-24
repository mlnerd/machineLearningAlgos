import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author Vivek
 */
public class question3 {

    public static void main(String args[]) throws FileNotFoundException, IOException {
        // Reading the Stop-words file and storing in HashMap
        BufferedReader reader = new BufferedReader(new FileReader(args[1]));
        String oneLine = "";
        String[] splited;
        Map <String,Integer> stopWordsMap = new HashMap<String,Integer>(); 
        while((oneLine = reader.readLine()) != null){
           splited = oneLine.split("\\s+");
           for(int x=0;x<splited.length;x++){
               splited[x] = splited[x].toLowerCase();
               stopWordsMap.put(splited[x].toLowerCase(), 0);
           }           
        }
        // Reading actual words file and processing (excluding stop-words)
        reader = new BufferedReader(new FileReader(args[0]));
        oneLine = "";
        Map <String,Integer> wordMap = new HashMap<String,Integer>(); 
        while((oneLine = reader.readLine()) != null){
           splited = oneLine.split("\\s+");
           for(int x=0;x<splited.length;x++){
               splited[x] = splited[x].toLowerCase();
               int value = 0;
               // If current word is stop-word, continue (dont add to wordMap)
               if(stopWordsMap.containsKey(splited[x])){
                   continue;
               }
               if(wordMap.containsKey(splited[x])){
                   value = wordMap.get(splited[x]);
               }
               wordMap.put(splited[x], value+1);
           }           
        }
        Set<String> set = wordMap.keySet();
        ArrayList<String> list = new ArrayList<String>();
	list.addAll(set);
	Collections.sort(list);
        String output = "";
        for(String word : list){
            output = output.concat(word + ":" + wordMap.get(word)+ ",");
        }
        output = output.replaceAll(",$", "");
        BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
        log.write(output);
        log.flush();
    }

}
