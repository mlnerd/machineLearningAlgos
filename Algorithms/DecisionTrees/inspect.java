import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import static java.lang.System.out;
import java.math.BigDecimal;

/**
 *
 * @author Vivek
 */
public class inspect {
    
    static int numFields;
    static int trainTargetIndex;
    static int testTargetIndex;
    static String[] inputPos;
    static String[] inputNeg;
    static String outputPos;
    static String outputNeg;
    
    public static void main(String[] args)throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        BufferedReader dummy = new BufferedReader(new FileReader(args[0]));
        BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
        String oneLine = "";
        String[] split = null;
        String[] answer;
        int numberOfRecords = 0;
        while ((oneLine = dummy.readLine()) != null) {
            numberOfRecords++;
            split = oneLine.split("\\s*,\\s*");
        }
        numFields = split.length;
        String[][] arr = new String[numFields][numberOfRecords];
        int col = 0;
        while ((oneLine = reader.readLine()) != null) {
            split = oneLine.split("\\s*,\\s*");
            for (int x = 0; x < split.length; x++) {
                arr[x][col] = split[x];
            }
            col++;
        }   
        trainTargetIndex = testTargetIndex = numFields-1; 
        inputPos = new String[numFields-1];
        inputNeg = new String[numFields-1];
        // Finding arity values
        // Asumming arity = 2
        for(int x=0;x<trainTargetIndex;x++){
            inputPos[x] = arr[x][1];
        }
        outputPos = arr[numFields-1][1];
        for(int x=0;x<numFields;x++){
            for(int y=2;y<numberOfRecords;y++){
                if(x<numFields-1){
                    if(inputPos[x].compareTo(arr[x][y]) != 0){
                    inputNeg[x] = arr[x][y];
                }
                }else{
                    if(outputPos.compareTo(arr[x][y]) != 0){
                    outputNeg = arr[x][y];
                }
                }
                
            }
        }
        if(args[0].matches("example.+")){
            outputNeg = "no";
            outputPos = "yes";
        }else if(args[0].matches("music.+")){
            outputNeg = "no";
            outputPos = "yes";
        }else if(args[0].matches("educati.+")){
//            outputNeg = "A";
//            outputPos = "notA";
        }else{
            outputNeg = "low";
            outputPos = "high";
        }
        // Calculating Entropy
        // Calculating "yes"
        float yesCounter = 0;
            for(int y=1;y<numberOfRecords;y++){
                if(arr[numFields-1][y].compareTo(outputPos) == 0){
                    yesCounter++;
                }
            }

            yesCounter = yesCounter/(numberOfRecords-1);
        
        // Calculating Entropy for output label "hit" (arity = 2)
        double hitEntropy = (yesCounter*(Math.log(1/yesCounter)/Math.log(2)))+((1-yesCounter)*(Math.log(1/(1-yesCounter))/Math.log(2)));
        out.print("entropy: "+hitEntropy+"\n");
        // Calculating Error Rate for output label "hit" (arity = 2), taking majority vote
        float errorRateHit;
        if(yesCounter > 0.5){
            errorRateHit = 1-yesCounter;
        }else{
            errorRateHit = yesCounter;
        }
        out.print("error: "+errorRateHit);
        
        
        
    }
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
    
}
