import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 *
 * @author Vivek
 */
public class partA {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("9Cat-Train.labeled"));
        BufferedReader dummy = new BufferedReader(new FileReader("9Cat-Train.labeled"));
        BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
        String oneLine = "";
        String[] split;
        String[] answer;
        int numberOfRecords = 0;
        while ((oneLine = dummy.readLine()) != null) {
            numberOfRecords++;
        }
        String[][] arr = new String[10][numberOfRecords + 1];
        arr[0][0] = "Gender";
        arr[1][0] = "Age";
        arr[2][0] = "Student";
        arr[3][0] = "Previously Declined?";
        arr[4][0] = "Hair Length";
        arr[5][0] = "Employed?";
        arr[6][0] = "Type Of Colateral";
        arr[7][0] = "First Loan";
        arr[8][0] = "Life Insurance";
        arr[9][0] = "Risk";
        int col = 1;
        while ((oneLine = reader.readLine()) != null) {
            split = oneLine.split("\\t+");
            for (int x = 0; x < split.length; x++) {
                answer = split[x].split("\\s+");
                arr[x][col] = answer[answer.length - 1];
            }
            col++;
        }
        // Calculating number of unique inputs possible
        // Last one (x = 9) is the output, so ignored
        int inputDomainSize = 1;
        for (int x = 0; x < 9; x++) {
            inputDomainSize = inputDomainSize * calculateArity(arr[x]);
        }
        log.write(inputDomainSize + "\n");
        // Calculating size of output range (only one, i.e. last column)
        int outputRangeSize = calculateArity(arr[9]);

        // Calculating size of concept space
        double conceptSpaceSize;
        conceptSpaceSize = Math.pow((double) outputRangeSize, (double) inputDomainSize);
        // Number of digits in concept space
        int digitsInConcept = new BigDecimal(conceptSpaceSize).toPlainString().length();
        log.write(digitsInConcept + "\n");
        // Calculating size of Hypothesis Space, assuming that arity of each attribute is "2"
        int HypoSpaceSize = 1;
        for (int x = 0; x < 9; x++) {
            HypoSpaceSize = (calculateArity(arr[x]) + 1) * HypoSpaceSize;
        }
        // Adding "1" extra for "NULL" Hypothesis
        HypoSpaceSize = HypoSpaceSize+1;
        log.write(HypoSpaceSize + "\n");
        // Running Find-S algorithm
        numberOfRecords = 1;
        int counter = 1;
        String[] hypothesis = new String[9];
        for (int x = 0; x < 9; x++) {
            hypothesis[x] = "null";
        }
        PrintWriter writer = new PrintWriter("partA4.txt", "UTF-8");
        while (numberOfRecords < arr[1].length) {
            if (arr[9][numberOfRecords].compareTo("high") == 0) {
                if (counter == 1) {
                    for (int y = 0; y < 9; y++) {
                        hypothesis[y] = arr[y][numberOfRecords];
                    }
                    counter++;
                } else {
                    for (int y = 0; y < 9; y++) {
                        if (hypothesis[y].compareTo(arr[y][numberOfRecords]) != 0) {
                            hypothesis[y] = "?";
                        }
                    }
                }
            }
            if (numberOfRecords % 30 == 0) {
                for (int z = 0; z < 9; z++) {
                    if (z == 0 && numberOfRecords == 30) {
                        writer.print(hypothesis[z] + "\t");
                    } else if (z == 0) {
                        writer.print("\n" + hypothesis[z] + "\t");
                    } else if (z != 8) {
                        writer.print(hypothesis[z] + "\t");
                    } else {
                        writer.print(hypothesis[z]);
                    }

                }
            }
            numberOfRecords++;
        }
        writer.close();
        // Reading test records
        BufferedReader test = new BufferedReader(new FileReader("9Cat-Dev.labeled"));
        dummy = new BufferedReader(new FileReader("9Cat-Dev.labeled"));
        numberOfRecords = 0;
        while ((oneLine = dummy.readLine()) != null) {
            numberOfRecords++;
        }
        String[][] testarr = new String[10][numberOfRecords + 1];
        testarr[0][0] = "Gender";
        testarr[1][0] = "Age";
        testarr[2][0] = "Student";
        testarr[3][0] = "Previously Declined?";
        testarr[4][0] = "Hair Length";
        testarr[5][0] = "Employed?";
        testarr[6][0] = "Type Of Colateral";
        testarr[7][0] = "First Loan";
        testarr[8][0] = "Life Insurance";
        testarr[9][0] = "Risk";
        col = 1;
        while ((oneLine = test.readLine()) != null) {
            split = oneLine.split("\\t+");
            for (int x = 0; x < split.length; x++) {
                answer = split[x].split("\\s+");
                testarr[x][col] = answer[answer.length - 1];
            }
            col++;
        }
        // Using "Hypothesis to predict and calculate error-rate using test data
        numberOfRecords = 1;
        boolean match;
        int misClassified = 0;
        while (numberOfRecords < testarr[1].length) {
            match = true;
            for (int x = 0; x < 9; x++) {
                if (hypothesis[x].compareTo("?") == 0) {
                    continue;
                } else if (hypothesis[x].compareTo(testarr[x][numberOfRecords]) != 0) {
                    match = false;
                    break;
                }
            }
            if (match == true) {
                if (testarr[9][numberOfRecords].compareTo("low") == 0) {
                    misClassified++;
                }
            }
            numberOfRecords++;
        }
        float misClassifiedRate = (float) misClassified / (testarr[1].length - 1);
        log.write(misClassifiedRate + "\n");

        // Fetching data from file passed as command line argument (assuming it doesnot have output label) 
        BufferedReader cmmdFile = new BufferedReader(new FileReader(args[0]));
        dummy = new BufferedReader(new FileReader(args[0]));
        numberOfRecords = 0;
        while ((oneLine = dummy.readLine()) != null) {
            numberOfRecords++;
        }
        String[][] predarr = new String[10][numberOfRecords + 1];
        predarr[0][0] = "Gender";
        predarr[1][0] = "Age";
        predarr[2][0] = "Student";
        predarr[3][0] = "Previously Declined?";
        predarr[4][0] = "Hair Length";
        predarr[5][0] = "Employed?";
        predarr[6][0] = "Type Of Colateral";
        predarr[7][0] = "First Loan";
        predarr[8][0] = "Life Insurance";
        predarr[9][0] = "Risk";
        col = 1;
        while ((oneLine = cmmdFile.readLine()) != null) {
            split = oneLine.split("\\t+");
            for (int x = 0; x < split.length; x++) {
                answer = split[x].split("\\s+");
                predarr[x][col] = answer[answer.length - 1];
            }
            col++;
        }
        // Making predictions
        numberOfRecords = 1;
        String pred;
        while (numberOfRecords < predarr[1].length) {
            match = true;
            for (int x = 0; x < 9; x++) {
                if (hypothesis[x].compareTo("?") == 0) {
                    continue;
                } else if (hypothesis[x].compareTo(predarr[x][numberOfRecords]) != 0) {
                    match = false;
                    break;
                }
            }
            if (match == true) {
                pred = "high";
            } else {
                pred = "low";
            }              
            log.write(pred + "\n");
            numberOfRecords++;
        }
        log.flush();

    }

    public static int calculateArity(String[] row) {
        int arity = 0;
        boolean unique = true;
        for (int x = 1; x < row.length; x++) {
            for (int y = 1; y < x; y++) {
                if (row[y].compareTo(row[x]) == 0) {
                    unique = false;
                    break;
                }
            }
            if (unique == true) {
                arity++;
            }
            unique = false;
        }
        return arity + 1;
    }
}
