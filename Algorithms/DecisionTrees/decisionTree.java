import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import static java.lang.System.out;

/**
 *
 * @author Vivek
 */
public class decisionTree {
    static String[][] treeArray = new String[3][999999];
    // Train stats
    static String[][] trainData;
    static int trainNumRecords;
    // Test stats
    static String[][] testData;
    static int testNumRecords;
    
    static boolean isTesting = false;
    static float trainError = 0;
    static float testError = 0;
    // Data Specific Stuff
    // Number of Attributes
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
        // Sending data to Global Train
        trainData = arr;
        trainNumRecords = numberOfRecords-1;
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
        // Setting initial tree attributes
        boolean[] doneAttributes = new boolean[numFields-1];
        boolean[] validDataPoints = new boolean[trainNumRecords];
        for(int x=0;x<trainNumRecords;x++){
            validDataPoints[x] = true;
        }
//        outputNeg = "yes";
//        outputPos = "no";
     //   if(args[0].matches("example.+")){
 //           outputNeg = "no";
   //         outputPos = "yes";
  //      }else if(args[0].matches("music.+")){
 //           outputNeg = "no";
            //outputPos = "yes";
 //       }else if(args[0].matches("educati.+")){
//            outputNeg = "A";
//            outputPos = "notA";
  //      }else{
  //          outputNeg = "no";
  //          outputPos = "yes";
  //      }
        
        // Starting Tree making
        makeTree(doneAttributes, validDataPoints, 0, 0);
        // Loading test data
        reader = new BufferedReader(new FileReader(args[1]));
        dummy = new BufferedReader(new FileReader(args[1]));
        oneLine = "";
        numberOfRecords = 0;
        while ((oneLine = dummy.readLine()) != null) {
            numberOfRecords++;
        }
        String[][] testArr = new String[numFields][numberOfRecords];
        col = 0;
        while ((oneLine = reader.readLine()) != null) {
            split = oneLine.split("\\s*,\\s*");
            for (int x = 0; x < split.length; x++) {
                testArr[x][col] = split[x];
            }
            col++;
        }
        // Sending data to Global Test
        testData = testArr;
        testNumRecords = numberOfRecords-1;
        // Sending test
        boolean[] testValidDataPoints = new boolean[trainNumRecords];
        for(int x=0;x<trainNumRecords;x++){
            testValidDataPoints[x] = true;
        }
        evaluateTest(testValidDataPoints, 0, 0);
        trainError = trainError/trainNumRecords;
        out.print("error(train): "+trainError+"\n");
        isTesting = true;
		testValidDataPoints = new boolean[testNumRecords];
		for(int x=0;x<testNumRecords;x++){
            testValidDataPoints[x] = true;
        }
        evaluateTest(testValidDataPoints, 0, 0);
        testError = testError/testNumRecords;
        out.print("error(test): "+testError);
        
    }
    
    static void evaluateTest(boolean[] validDataPoints, int currentNode, int level) throws IOException{
        BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
        String data[][];
        int dataNumRecords;
        int targetIndex;
        if(isTesting == true){
            data = testData;
            dataNumRecords = testNumRecords;
            targetIndex = testTargetIndex;
        }else{
            data = trainData;
            dataNumRecords = trainNumRecords;
            targetIndex = trainTargetIndex;
        }
        if(level > 3){
            return;
        }
        if (level != 0) {
            if (treeArray[0][currentNode] == null) {
                return;
            }
        }
        int negCounter = 0;
        int posCounter = 0;
        for(int x=0;x<dataNumRecords;x++){
            if(validDataPoints[x]){
                if(data[targetIndex][x+1].compareTo(outputPos) == 0){
                    posCounter++;
                }else{
                    negCounter++;
                }
            }
        }
        boolean[] leftvalidDataPoints = new boolean[validDataPoints.length];
        System.arraycopy( validDataPoints,0,leftvalidDataPoints,0,validDataPoints.length );
        boolean[] rightvalidDataPoints = new boolean[validDataPoints.length];
        System.arraycopy( validDataPoints,0,rightvalidDataPoints,0,validDataPoints.length );
        String leftVal = null;
        String rightval = null;
        int leftIndex = -1;
        int rightIndex = -1;
        for (int y = 0;y<data.length;y++){
            if(treeArray[0][2*currentNode+1] != null){
                if (treeArray[0][2*currentNode+1].compareTo(data[y][0]) == 0){
                leftVal = treeArray[1][2*currentNode+1];
                leftIndex = y;
            }
            }
            if(treeArray[0][2*currentNode+2] != null){
                if(treeArray[0][2*currentNode+2].compareTo(data[y][0]) == 0){
                rightval = treeArray[1][2*currentNode+2];
                rightIndex = y;
            }
            }
            
        }
        // Displaying results
        if(isTesting == false){
            if(level == 0){
            out.print("["+posCounter+"+"+"/"+negCounter+"-"+"]"+"\n");
        }else{
            for(int x=1;x<level;x++){
                 out.print("| ");
            }
            out.print(treeArray[0][currentNode]+" = "+treeArray[1][currentNode]+": "+"["+posCounter+"+"+"/"+negCounter+"-"+"]"+"\n");
                    
        }
        }
        // Calculating children data to pass
        for(int x=0;x<dataNumRecords;x++){
            if(validDataPoints[x]){
                if (leftVal != null) {
                    if (data[leftIndex][x + 1].compareTo(leftVal) != 0) {
                        leftvalidDataPoints[x] = false;
                    }
                }
                if (rightval != null) {
                    if (data[rightIndex][x + 1].compareTo(rightval) != 0) {
                        rightvalidDataPoints[x] = false;
                    }
                }   
            }
        }
        // Making Recursive calls
        // Left
        if(leftVal != null){
            evaluateTest(leftvalidDataPoints, 2*currentNode+1, level+1);
        }
        // Right
        if(rightval != null){
            evaluateTest(rightvalidDataPoints, 2*currentNode+2, level+1);
        }
        // Calculating Errors
        if(isTesting == false){
            if(rightval == null){
            if(leftVal == null){
                if(posCounter > negCounter){
                    treeArray[2][currentNode] = outputPos;
                    trainError = trainError + negCounter;
                }else{
                    treeArray[2][currentNode] = outputNeg;
                    trainError = trainError + posCounter;
                }
            }
        }
        }else{
            if(rightval == null){
            if(leftVal == null){
                if(treeArray[2][currentNode].compareTo(outputNeg) == 0){
                        testError = testError + posCounter;
                }else{
                        testError = testError + negCounter;
                }
            }
        }
        }
        
    
        
    }
     
    static void makeTree(boolean[] doneAttributes, boolean[] validDataPoints, int currentNode, int level){
        if(level >1){
            return;
        }
        double maxInfoGain = 0.1;
        double infoGain;
        int maxIGAttributeIndex = -1;
        for(int x=0;x<doneAttributes.length;x++){
            if(!doneAttributes[x]){
                infoGain = calculateIG(x, validDataPoints);
                if(infoGain >= 0.1){
                    if(infoGain > maxInfoGain){
                        maxInfoGain = infoGain;
                        maxIGAttributeIndex = x;
                    }
                }
            }
        }
        // If no relevent IG gain attribute found, return
        if(maxIGAttributeIndex == -1){
            return;
        }
        String leftvalue = "";
        String rightvalue = "";
        leftvalue = inputPos[maxIGAttributeIndex];
        rightvalue = inputNeg[maxIGAttributeIndex]; 
        // Left child at 2*currentNode+1 (yes)
        //Right child at 2*currentNode+2 (no)
        treeArray[0][2*currentNode+1] = trainData[maxIGAttributeIndex][0];
        treeArray[1][2*currentNode+1] = leftvalue;
        treeArray[0][2*currentNode+2] = trainData[maxIGAttributeIndex][0];
        treeArray[1][2*currentNode+2] = rightvalue;
        // Recursive sending
        boolean[] leftvalidDataPoints = new boolean[validDataPoints.length];
        System.arraycopy( validDataPoints,0,leftvalidDataPoints,0,validDataPoints.length );
        boolean[] rightvalidDataPoints = new boolean[validDataPoints.length];
        System.arraycopy( validDataPoints,0,rightvalidDataPoints,0,validDataPoints.length );
        for(int y=1;y<=trainNumRecords;y++){
            if(validDataPoints[y-1]){
                if(trainData[maxIGAttributeIndex][y].compareTo(leftvalue) == 0){
                      rightvalidDataPoints[y-1] = false;              
                }else{
                    leftvalidDataPoints[y-1] = false;
                }
            }
        }
        // Updating doneness on attribute
        doneAttributes[maxIGAttributeIndex] = true;
        boolean[] doneAttributesLeft = new boolean[doneAttributes.length];
        System.arraycopy( doneAttributes,0,doneAttributesLeft,0,doneAttributes.length );
        boolean[] doneAttributesRight = new boolean[doneAttributes.length];
        System.arraycopy( doneAttributes,0,doneAttributesRight,0,doneAttributes.length );
        // Send recursive calls
        // Left
        makeTree(doneAttributesLeft, leftvalidDataPoints, 2*currentNode+1, level+1);
        // Right
        makeTree(doneAttributesRight, rightvalidDataPoints, 2*currentNode+2, level+1);
    }
    
    static double calculateIG(int attributeIndex, boolean[] validDataPoints){
        String value = "";
        value = inputPos[attributeIndex];
        float attCounter = 0; //yes
        float targetValueCounter = 0; //yes
        float targetOtherCounter = 0;
        float targetOverallCounter = 0;
        int validSpace = 0;
        for(int x=0;x<trainNumRecords;x++){
            if(validDataPoints[x]){
                validSpace++;
            }
        }
            for(int y=1;y<=trainNumRecords;y++){
                if(validDataPoints[y-1]){
                    if(trainData[attributeIndex][y].compareTo(value) == 0){
                    attCounter++;
                    if(trainData[trainTargetIndex][y].compareTo(outputPos) == 0){
                        targetValueCounter++;
                    }
                }else{
                    if(trainData[trainTargetIndex][y].compareTo(outputPos) == 0){
                        targetOtherCounter++;
                    }
                }
                    if(trainData[trainTargetIndex][y].compareTo(outputPos) == 0){
                        targetOverallCounter++;
                    }
                }
            }
        targetValueCounter = targetValueCounter/attCounter; // target conditional prob (match, yes)
        targetOtherCounter = targetOtherCounter/(validSpace - attCounter); // target conditional prob (no match, yes)
//        targetOtherCounter = 1-targetValueCounter;
        attCounter = attCounter/validSpace; // Attribute overall prob
        targetOverallCounter = targetOverallCounter/validSpace; // overall target prob
        double outputOverallEntropy = calculateEntropy(targetOverallCounter);
        double term1 = (1-attCounter)*calculateEntropy(targetOtherCounter);
        double term2 = (attCounter)*calculateEntropy(targetValueCounter);
        double returnVal = outputOverallEntropy - term1 - term2;
        return returnVal;
    }
    
    
    
    static double calculateEntropy(float prob){
        // Taking extremeties of probability
        if(prob == 1 || prob == 0){
            return 0;
        }else{
            return (prob*(Math.log(1/prob)/Math.log(2)))+((1-prob)*(Math.log(1/(1-prob))/Math.log(2)));
        }
    }
    
}
