import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Vivek
 */
public class beta {
    
    public static int N;
    public static int T;
    
    public static void main(String[] args)throws FileNotFoundException, IOException {
        double[][] bMatrix;
        // Contains N emission maps 
        List<HashMap<String,Double>> emitMaps = new ArrayList<HashMap<String,Double>>();
        BufferedReader priorReader = new BufferedReader(new FileReader(args[3]));
        BufferedReader dummyPriorReader = new BufferedReader(new FileReader(args[3]));
        BufferedReader transReader = new BufferedReader(new FileReader(args[1]));
        BufferedReader emitReader = new BufferedReader(new FileReader(args[2]));
        // Calculating N
        int count = 0;
        String oneLine = "";
        while ((oneLine = dummyPriorReader.readLine()) != null) {
            count++;
        }
        N = count;
        //********************************
        double[][] transMatrix = new double [N][N];
        double[] priorArray = new double[N];
        //********************************
        // Reading prior Prob into priorArray
        String[] split = null;
        count = 0;
        while ((oneLine = priorReader.readLine()) != null) {
            split = oneLine.split("\\s+");
            priorArray[count] = Double.parseDouble(split[1]);
            count++;
        }
        // Reading transition probabilities 
        count = 0;
        String po;
        while ((oneLine = transReader.readLine()) != null) {
            split = oneLine.split("\\s+");
            for(int x=0;x<split.length;x++){
                if(x>0){
                    po = split[x];
                    transMatrix[count][x-1] = Double.parseDouble(po.split(":")[1]);
                }
            }
            count++;
        }
        // Reading emission Probabilities
        HashMap<String,Double> stateEmit = new HashMap<String,Double>();
        while ((oneLine = emitReader.readLine()) != null) {
            stateEmit.clear();
            split = oneLine.split("\\s+");
            for(int x=0;x<split.length;x++){
                if(x>0){
                    po = split[x];
                    stateEmit.put(po.split(":")[0], Double.parseDouble(po.split(":")[1]));
                }
            }
            emitMaps.add((HashMap<String, Double>) stateEmit.clone());
        }
        
        // Reading sentences and implementing Forward Algorithm
        BufferedReader sentenceReader = new BufferedReader(new FileReader(args[0]));
        double value = Math.log(0);
        while ((oneLine = sentenceReader.readLine()) != null) {
            split = oneLine.split("\\s+");
            T = split.length;
            bMatrix = new double[N][T+1];
            for(int t=T;t>=1;t--){
                if(t == T){
                    for(int i=0;i<N;i++){
                        bMatrix[i][t] = Math.log(1);
                    }
                }else{
                    for(int i=0;i<N;i++){
                        value = Math.log(0);
                        for(int j=0;j<N;j++){
                            value = logsum(value, bMatrix[j][t+1] + Math.log(transMatrix[i][j]) + Math.log(emitMaps.get(j).get(split[t])));
                        }
                        bMatrix[i][t] = value;
                    }  
                }
            }
            value = Math.log(0);
            for(int x=0;x<N;x++){
                value = logsum(value , Math.log(priorArray[x]) + Math.log(emitMaps.get(x).get(split[0])) + bMatrix[x][1]);
            }
            out.println(value);
        }
        
        
        
    }
    
    public static double logsum(double left, double right){
		 if(right < left){
		  return left + Math.log1p(Math.exp(right - left));
		 }
		 else if(left < right){
		  return right + Math.log1p(Math.exp(left - right));
		 }else{
		  return left + Math.log1p(1.0);
		 }
	}
    
}
