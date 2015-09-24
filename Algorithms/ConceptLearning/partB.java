import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

/**
 *
 * @author Vivek
 */
public class partB {

    // Boolean array, which tracks hypothesis that are still in Version-Space
    public static boolean[] stillInGame = new boolean[65536];
    // Hypothesis votes
    static int high, low;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("4Cat-Train.labeled"));
        BufferedReader dummy = new BufferedReader(new FileReader("4Cat-Train.labeled"));
        BufferedWriter log = new BufferedWriter(new OutputStreamWriter(System.out));
        String oneLine = "";
        String[] split;
        String[] answer;
        int numberOfRecords = 0;
        while ((oneLine = dummy.readLine()) != null) {
            numberOfRecords++;
        }
        String[][] arr = new String[5][numberOfRecords + 1];
        arr[0][0] = "Gender";
        arr[1][0] = "Age";
        arr[2][0] = "Student";
        arr[3][0] = "Previously Declined?";
        arr[4][0] = "Risk";
        int col = 1;
        while ((oneLine = reader.readLine()) != null) {
            split = oneLine.split("\\t+");
            for (int x = 0; x < split.length; x++) {
                answer = split[x].split("\\s+");
                arr[x][col] = answer[answer.length - 1];
            }
            col++;
        }
        int inputDomainSize = 1;
        for (int x = 0; x < 4; x++) {
            inputDomainSize = inputDomainSize * calculateArity(arr[x]);
        }
        log.write(inputDomainSize + "\n");
        // Calculating size of output range (only one, i.e. last column)
        int outputRangeSize = calculateArity(arr[4]);
        // Calculating size of concept space
        double conceptSpaceSize;
        conceptSpaceSize = Math.pow((double) outputRangeSize, (double) inputDomainSize);
        log.write((int)conceptSpaceSize + "\n");
        // List-then-Eliminate Algorithm, assuming arity of "2" for every attribute
        // Manually creating version space.... Size is 4*17 (rows*column)  
        String[][] versionSpace = {{"Gender", "Male", "Male", "Male", "Male", "Male", "Male", "Male", "Male", "Female", "Female", "Female", "Female", "Female", "Female", "Female", "Female"},
        {"Age", "Young", "Young", "Young", "Young", "Old", "Old", "Old", "Old", "Young", "Young", "Young", "Young", "Old", "Old", "Old", "Old"},
        {"Student", "No", "No", "Yes", "Yes", "No", "No", "Yes", "Yes", "No", "No", "Yes", "Yes", "No", "No", "Yes", "Yes"},
        {"Previously Declined?", "No", "Yes", "No", "Yes", "No", "Yes", "No", "Yes", "No", "Yes", "No", "Yes", "No", "Yes", "No", "Yes"}};

        // Initially, all are part of Version-Space
        for (int x = 0; x < 65536; x++) {
            stillInGame[x] = true;
        }
        // Running Algo using Train-set to reduce version sapce
        boolean match = true;
        int y, z;
        for (int x = 1; x <= numberOfRecords; x++) {
            if (arr[4][x].compareTo("high") == 0) {
                for (y = 1; y <= 16; y++) {
                    match = true;
                    for (z = 0; z < 4; z++) {
                        if (versionSpace[z][y].compareTo(arr[z][x]) != 0) {
                            match = false;
                            break;
                        }
                    }
                    if (match == true) {
                        binaryCounterCheckFuncForHigh(y - 1);
                    }
                }

            }
        }
        for (int x = 1; x <= numberOfRecords; x++) {
            if (arr[4][x].compareTo("low") == 0) {
                for (y = 1; y <= 16; y++) {
                    match = true;
                    for (z = 0; z < 4; z++) {
                        if (versionSpace[z][y].compareTo(arr[z][x]) != 0) {
                            match = false;
                            break;
                        }
                    }
                    if (match == true) {
                        binaryCounterCheckFuncForLow(y - 1);
                    }
                }

            }
        }
        // Calculating number of hypothesis that stil remain part of Version-Space
        int versionSpaceSize = 0;
        for (int x = 0; x < 65536; x++) {
            if (stillInGame[x] == true) {
                versionSpaceSize++;
            }
        }
        log.write(versionSpaceSize + "\n");
        // Taking test file, and voting for every instance in it using versiion-Space in it
        BufferedReader test = new BufferedReader(new FileReader(args[0]));
        dummy = new BufferedReader(new FileReader(args[0]));
        numberOfRecords = 0;
        while ((oneLine = dummy.readLine()) != null) {
            numberOfRecords++;
        }
        String[][] testarr = new String[5][numberOfRecords + 1];
        arr[0][0] = "Gender";
        arr[1][0] = "Age";
        arr[2][0] = "Student";
        arr[3][0] = "Previously Declined?";
        arr[4][0] = "Risk";
        col = 1;
        while ((oneLine = test.readLine()) != null) {
            split = oneLine.split("\\t+");
            for (int x = 0; x < split.length; x++) {
                answer = split[x].split("\\s+");
                testarr[x][col] = answer[answer.length - 1];
            }
            col++;
        }
        // Voting for each record in test; done by every hypothesis in version-Space
        for (int x = 1; x <= numberOfRecords; x++) {
            high = 0;
            low = 0;
            for (y = 1; y <= 16; y++) {
                match = true;
                for (z = 0; z < 4; z++) {
                    if (versionSpace[z][y].compareTo(testarr[z][x]) != 0) {
                        match = false;
                        break;
                    }
                }
                if (match == true) {
                    binaryCounterVoteHighLow(y - 1);
                    log.write(high + " " + low+"\n");
                    high = 0;
                    low = 0;
                }
            }
        }
        log.flush();

    }

    // Calculating Arity
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

    public static void binaryCounterCheckFuncForHigh(int inputNumber) {
        int base = 16;
        int[] outputSpace = new int[base];
        final boolean[] ret = new boolean[base];
        for (int funcNumber = 0; funcNumber < 65536; funcNumber++) {
            for (int x = 0; x < base; x++) {
                ret[base - 1 - x] = (1 << x & funcNumber) != 0;
            }
            for (int x = 0; x < base; x++) {
                if (ret[x]) {
                    outputSpace[x] = 1;
                }
            }
            if (stillInGame[funcNumber] == true) {
                if (outputSpace[inputNumber] == 0) {
                    stillInGame[funcNumber] = false;
                }
            }
            for (int x = 0; x < base; x++) {
                outputSpace[x] = 0;
            }
        }
    }

    public static void binaryCounterCheckFuncForLow(int inputNumber) {
        int base = 16;
        int[] outputSpace = new int[base];
        final boolean[] ret = new boolean[base];
        for (int funcNumber = 0; funcNumber < 65536; funcNumber++) {
            for (int x = 0; x < base; x++) {
                ret[base - 1 - x] = (1 << x & funcNumber) != 0;
            }
            for (int x = 0; x < base; x++) {
                if (ret[x]) {
                    outputSpace[x] = 1;
                }
            }
            if (stillInGame[funcNumber] == true) {
                if (outputSpace[inputNumber] == 1) {
                    stillInGame[funcNumber] = false;
                }
            }
            for (int x = 0; x < base; x++) {
                outputSpace[x] = 0;
            }
        }

    }

    public static boolean binaryCounterVoteHighLow(int interestAttribute) {
        int base = 16;
        int[] outputSpace = new int[base];
        final boolean[] ret = new boolean[base];
        for (int funcNumber = 0; funcNumber < 65536; funcNumber++) {
            for (int x = 0; x < base; x++) {
                ret[base - 1 - x] = (1 << x & funcNumber) != 0;
            }
            for (int x = 0; x < base; x++) {
                if (ret[x]) {
                    outputSpace[x] = 1;
                }
            }
            if (stillInGame[funcNumber]) {
                for (int x = 0; x < 16; x++) {
                    if (interestAttribute == x) {
                        if (outputSpace[x] == 1) {
                            high++;
                        } else {
                            low++;
                        }
                    }
                }
            }

            for (int x = 0; x < base; x++) {
                outputSpace[x] = 0;
            }
        }
        return false;
    }

}
