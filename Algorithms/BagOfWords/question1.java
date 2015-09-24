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
 * @author Vivek Sharma
 */
public class question1 {

    public static void main(String args[]) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        String oneLine = "";
        String[] splited;
        Map <String,Integer> map = new HashMap<String,Integer>(); 
        while((oneLine = reader.readLine()) != null){
           splited = oneLine.split("\\s+");
           for(int x=0;x<splited.length;x++){
               map.put(splited[x].toLowerCase(), 0);
           }           
        }
        Set<String> set = map.keySet();
        ArrayList<String> list = new ArrayList<String>();
	list.addAll(set);
	Collections.sort(list);
        String output = "";
        for(String word : list){
            output = output.concat(word + ",");
        }
        output = output.replaceAll(",$", "");
        BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
        log.write(output);
		log.flush();
    }

}
