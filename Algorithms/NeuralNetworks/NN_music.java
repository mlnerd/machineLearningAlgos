/**
 *
 * @author Vivek
*
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import static java.lang.System.out;
import java.util.concurrent.TimeUnit;

public class NN_music {

    // number of neurons in the hidden layer
    static int neuron_num = 4;
    // number of inputs in train data (assuming both test & train have same number of columns)
    static int input_num;
    // Min and Max value given randomly to weights
    static double max = 0.05;
    static double min = -0.05;
    // Learning rate
    static double n = 0.5;

    public static void main(String[] args) throws IOException {
        // Extracting values from CSV
        String aa = args[0];
        String bb = args[1];
        // Extracting TRAIN set
        BufferedReader reader = new BufferedReader(new FileReader(aa));
        BufferedReader dummy = new BufferedReader(new FileReader(aa));
        BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
        String oneLine = "";
        String[] split = null;
        int numberOfRecordsTrain = 0;
        while ((oneLine = dummy.readLine()) != null) {
            numberOfRecordsTrain++;
            split = oneLine.split("\\s*,\\s*");
        }
        input_num = split.length - 1;
        String[][] arrTrain = new String[input_num + 1][numberOfRecordsTrain];
        int col = 0;
        while ((oneLine = reader.readLine()) != null) {
            split = oneLine.split("\\s*,\\s*");
            for (int x = 0; x < split.length; x++) {
                arrTrain[x][col] = split[x];
            }
            col++;
        }

        // Extracting TEST set
        reader = new BufferedReader(new FileReader(bb));
        dummy = new BufferedReader(new FileReader(bb));
        int numberOfRecordsTest = 0;
        while ((oneLine = dummy.readLine()) != null) {
            numberOfRecordsTest++;
            split = oneLine.split("\\s*,\\s*");
        }
        String[][] arrTest = new String[input_num + 1][numberOfRecordsTest];
        col = 0;
        while ((oneLine = reader.readLine()) != null) {
            split = oneLine.split("\\s*,\\s*");
            for (int x = 0; x < split.length; x++) {
                arrTest[x][col] = split[x];
            }
            col++;
        }

        // Calculating normalizing metrics (TRAIN)
        double[] arrYearTrain = new double[numberOfRecordsTrain - 1];
        double[] arrLengthTrain = new double[numberOfRecordsTrain - 1];
        for (int x = 1; x < numberOfRecordsTrain; x++) {
            for (int y = 0; y < input_num + 1; y++) {
                if (y == 0) {
                    arrYearTrain[x - 1] = Double.parseDouble(arrTrain[y][x]);
                } else if (y == 1) {
                    arrLengthTrain[x - 1] = Double.parseDouble(arrTrain[y][x]);
                }
            }
        }

        // Calculating normalizing metrics (TEST)
        double[] arrYearTest = new double[numberOfRecordsTest - 1];
        double[] arrLengthTest = new double[numberOfRecordsTest - 1];
        for (int x = 1; x < numberOfRecordsTest; x++) {
            for (int y = 0; y < input_num + 1; y++) {
                if (y == 0) {
                    arrYearTest[x - 1] = Double.parseDouble(arrTest[y][x]);
                } else if (y == 1) {
                    arrLengthTest[x - 1] = Double.parseDouble(arrTest[y][x]);
                }
            }
        }

        // Storing weights from inputs to neurons mapI [from_input][to_neuron]
        double[][] mapI = new double[input_num][neuron_num];
        for (int x = 0; x < input_num; x++) {
            for (int y = 0; y < neuron_num; y++) {
                mapI[x][y] = min + (double) (Math.random() * (max - min));
            }
        }
        // Storing weights from neurons to output mapO [from_neuron]
        double[] mapO = new double[neuron_num];
        for (int x = 0; x < neuron_num; x++) {
            mapO[x] = min + (double) (Math.random() * (max - min));
        }


//        // Normalizing TRAIN
//        Double[][] arrNumTrain = new Double[input_num+1][numberOfRecordsTrain-1];
//        for(int x=1;x<numberOfRecordsTrain;x++){
//            for(int y=0;y<input_num+1;y++){
//                if(y == 0){
//                    arrNumTrain[y][x-1] = (Double.parseDouble(arrTrain[y][x])-Exp(arrYearTrain))/Math.sqrt(Var(arrYearTrain));
//                }else if(y == 1){
//                    arrNumTrain[y][x-1] = (Double.parseDouble(arrTrain[y][x])-Exp(arrLengthTrain))/Math.sqrt(Var(arrLengthTrain));
//                }else {
//                    if(arrTrain[y][x].compareTo("yes") == 0){
//                        arrNumTrain[y][x-1] = (double)1;
//                    }else{
//                        arrNumTrain[y][x-1] = (double)0;
//                    }
//                }                     
//            }
//        }
        // Normalizing TRAIN
        Double[][] arrNumTrain = new Double[input_num + 1][numberOfRecordsTrain - 1];
        for (int x = 1; x < numberOfRecordsTrain; x++) {
            for (int y = 0; y < input_num + 1; y++) {
                if (y == 0) {
                    arrNumTrain[y][x - 1] = (Double.parseDouble(arrTrain[y][x]) - 1900) / 100;
                } else if (y == 1) {
                    arrNumTrain[y][x - 1] = (Double.parseDouble(arrTrain[y][x]) - 0) / 7;
                } else {
                    if (arrTrain[y][x].compareTo("yes") == 0) {
                        arrNumTrain[y][x - 1] = (double) 1;
                    } else {
                        arrNumTrain[y][x - 1] = (double) 0;
                    }
                }
            }
        }

//        // Normalizing TEST
//        Double[][] arrNumTest = new Double[input_num+1][numberOfRecordsTest-1];
//        for(int x=1;x<numberOfRecordsTest;x++){
//            for(int y=0;y<input_num;y++){
//                if(y == 0){
//                    arrNumTest[y][x-1] = (Double.parseDouble(arrTest[y][x])-Exp(arrYearTest))/Math.sqrt(Var(arrYearTest));
//                }else if(y == 1){
//                    arrNumTest[y][x-1] = (Double.parseDouble(arrTest[y][x])-Exp(arrLengthTest))/Math.sqrt(Var(arrLengthTest));
//                }else {
//                    if(arrTest[y][x].compareTo("yes") == 0){
//                        arrNumTest[y][x-1] = (double)1;
//                    }else{
//                        arrNumTest[y][x-1] = (double)0;
//                    }
//                }                 
//            }
//        }
        // Normalizing TEST
        Double[][] arrNumTest = new Double[input_num + 1][numberOfRecordsTest - 1];
        for (int x = 1; x < numberOfRecordsTest; x++) {
            for (int y = 0; y < input_num; y++) {
                if (y == 0) {
                    arrNumTest[y][x - 1] = (double)((Double.parseDouble(arrTest[y][x]) - 1900) / 100);
                } else if (y == 1) {
                    arrNumTest[y][x - 1] = (double)((Double.parseDouble(arrTest[y][x]) - 0) / 7);
                } else {
                    if (arrTest[y][x].compareTo("yes") == 0) {
                        arrNumTest[y][x - 1] = (double) 1;
                    } else {
                        arrNumTest[y][x - 1] = (double) 0;
                    }
                }
            }
        }

        double val;
        double output;
        double deltaK;
        double[] deltaH = new double[input_num];
        // Storing values from inputs to neurons [from_input][to_neuron] (TRAIN)
        double[][] hiddenI = new double[input_num][neuron_num];
        // Storing values from inputs to neurons [from_input][to_neuron] (TEST)
        double[][] hiddenITest = new double[input_num][neuron_num];
        double[] hiddenO = new double[neuron_num];
        double ssePrev = 99999;
        double sseNew = 100;
        // Jai Shree Ganesh
//        while(sseNew < ssePrev)
        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(20);stop > System.nanoTime();) {
            ssePrev = sseNew;
            for (int x = 0; x < numberOfRecordsTrain - 1; x++) {
                // calculating output for every thread going to neuron from input nodes
                for (int y = 0; y < input_num; y++) {
                    for (int q = 0; q < neuron_num; q++) {
                        hiddenI[y][q] = (arrNumTrain[y][x] * mapI[y][q]);
                    }
                }
                output = 0;
                for (int z = 0; z < neuron_num; z++) {
                    val = 0; // Linear sum of inputs to every neuron
                    for (int t = 0; t < input_num; t++) {
                        val = val + hiddenI[t][z];
                    }
                    hiddenO[z] = sigmoid(val); // Stores sigmoid output for every neuron
                    output = output + (sigmoid(val) * mapO[z]); // Summing weighted output of neuron to output node
                }
                output = sigmoid(output); // Total Output
                // Output calculated
                // 2
                deltaK = output * (1 - output) * (arrNumTrain[input_num][x] - output);
                // 3
                for (int d = 0; d < neuron_num; d++) {
                    deltaH[d] = hiddenO[d] * (1 - hiddenO[d]) * deltaK * mapO[d];
                }
            // 4
                // Updating weights to hidden layer
                for (int y = 0; y < input_num; y++) {
                    for (int q = 0; q < neuron_num; q++) {
                        mapI[y][q] = mapI[y][q] + (n * deltaH[q] * arrNumTrain[y][x]);
                    }
                }
                // Updating weights to output
                for (int z = 0; z < neuron_num; z++) {
                    mapO[z] = mapO[z] + (n * deltaK * hiddenO[z]);
                }
            }
            // Calculating SSE (Error)
            sseNew = 0;
            for (int g = 0; g < numberOfRecordsTrain - 1; g++) {
                // calculating output for every thread going to neuron from input nodes
                for (int y = 0; y < input_num; y++) {
                    for (int q = 0; q < neuron_num; q++) {
                        hiddenITest[y][q] = (arrNumTrain[y][g] * mapI[y][q]);
                    }
                }
                output = 0;
                for (int z = 0; z < neuron_num; z++) {
                    val = 0; // Linear sum of inputs to every neuron
                    for (int t = 0; t < input_num; t++) {
                        val = val + hiddenITest[t][z];
                    }
                    output = output + (sigmoid(val) * mapO[z]); // Summing weighted output of neuron to output node
                }
                // Calculating total (output)
                output = sigmoid(output); // Total Output
                // Calculating SSE
                sseNew = sseNew + Math.pow((output - arrNumTrain[input_num][g]), 2);
            }
            if(sseNew > ssePrev){
                break;
            }
            out.println(sseNew);
        }

        String pred = null;
        // Now, predicting test outputs
        out.println("TRAINING COMPLETED! NOW PREDICTING.");
        for (int g = 0; g < numberOfRecordsTest - 1; g++) {
            // calculating output for every thread going to neuron from input nodes
            for (int y = 0; y < input_num; y++) {
                for (int q = 0; q < neuron_num; q++) {
                    hiddenITest[y][q] = (arrNumTest[y][g] * mapI[y][q]);
                }
            }
            output = 0;
            for (int z = 0; z < neuron_num; z++) {
                val = 0; // Linear sum of inputs to every neuron
                for (int t = 0; t < input_num; t++) {
                    val = val + hiddenITest[t][z];
                }
                output = output + (sigmoid(val) * mapO[z]); // Summing weighted output of neuron to output node
            }
            // Calculating total (output)
            output = sigmoid(output); // Test Output
            if (output >= 0.5) {
                pred = "yes";
            } else {
                pred = "no";
            }
            out.println(pred);
        }

    }

    public static double Var(double[] arr) {
        int len = arr.length;
        double[] sqarr = new double[len];
        for (int x = 0; x < len; x++) {
            sqarr[x] = (double) Math.pow(arr[x], 2);
        }
        return (Exp(sqarr) - (double) Math.pow(Exp(arr), 2));
    }

    public static double Min(double[] arr) {
        int len = arr.length;
        double min = arr[1];
        for (int x = 1; x < len; x++) {
            if (arr[x] < min) {
                min = arr[x];
            }
        }
        return min;
    }

    public static double Max(double[] arr) {
        int len = arr.length;
        double max = arr[1];
        for (int x = 1; x < len; x++) {
            if (arr[x] > max) {
                max = arr[x];
            }
        }
        return max;
    }

    public static double Exp(double[] arr) {
        int len = arr.length;
        double sum = 0;
        for (int x = 0; x < len; x++) {
            sum = sum + arr[x];
        }
        return sum / len;
    }

    // Sigmiod function
    public static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

}
