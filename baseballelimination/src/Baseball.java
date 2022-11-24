/**
 * -------------------------------------------
 *                  Baseball.java
 * -------------------------------------------
 *
 * Year : 2022
 * Course : Graphs and networks
 * Authors : Juanfer MERCIER, Adrien PICHON
 **/

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.System;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class Baseball {
    /* ANSI escape codes to print text with a certain color
     *
     * DISCLAIMER : The following constants where borrowed from :
     * https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
     *
     * author   : WhiteFang34, shakram02
     * editor   : SergeyB
     */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\033[1;31m";
    public static final String ANSI_GREEN = "\033[1;32m";
    public static final String ANSI_PURPLE = "\033[1;35m";
    public static final String ANSI_CYAN = "\u001B[36m";

    /* Returns the extension of the filename
     *
     * author   : Juanfer MERCIER
     * date     : last modified December 27th 2022
     */
    public static /*Optional<String>*/ String getExtension(String filename) {
        if(filename.contains("."))
            return filename.substring(filename.lastIndexOf(".") + 1);
        else
            return "";
    }

    /* Displays the program's manual */
    private static void usage() {
        System.err.println("Usage: java Baseball file.txt");
        System.err.println("Solve the image binarization problem on instance file.txt");
    }

    /* Return next int from file when possible, -1 otherwise */
    private static int getInt(Scanner file) {
        if(file.hasNextInt())
            return file.nextInt();
        return -1;
    }

    // TODO change parameters
    /* Construct flow network using the data provided
     *
     * n    : number of rows
     * m    : number of columns
     * A    : foreground likelihood score
     * B    : background likelihood score
     * Ph   : horizontal penalties
     * Pv   : vertical penalties
     */
    public static FlowNetwork ConstructionReseau(int n, int m, int[][] A, int[][] B, int[][] Ph, int[][] Pv) {
        return new FlowNetwork(n, m, A, B, Ph, Pv);
    }

    // TODO change parameters
    /* Do test for team k
     *
     * R    : the flow network for team k
     */
    public static TestEliminationEquipe() {
        // TODO implement
    }


    // TODO change parameters
    /* Solve the baseball elimination problem on the data provided
     *
     * n    : number of rows
     * m    : number of columns
     * A    : foreground likelihood score
     * B    : background likelihood score
     * Ph   : horizontal penalties
     * Pv   : vertical penalties
     */
    public static void TestEliminationToutes(int n, int m, int[][] A, int[][] B, int[][] Ph, int[][] Pv) {
        // TODO loop to construct FlowNetwork of team 1, do the test,
        // eliminate other teams according to lemma and repeat with
        // remaining teams

        // Construct the network from file
        FlowNetwork R = ConstructionReseau(n, m, A, B, Ph, Pv);

    }

    public static void main(String args[]) {
        // Check if length of args array is
        // greater than 0
        if(args.length == 1) {
            // Check if filename has extension .txt
            String ext = getExtension(args[0]);

            if("txt".equals(ext)){
                // CHECKS PASSED. Now we read the file...
                try {
                    Scanner file = new Scanner(new File(args[0]));

                    // TODO modify loading structures

                    // Loop variables
                    int i = 0, j = 0, k = 0;

                    // Number of rows
                    int n = getInt(file);
                    // Number of columns
                    int m = getInt(file);

                    // Set A
                    int[][] A = new int[n][m];
                    // Set B
                    int[][] B = new int[n][m];
                    // Horizontal and vertical penalties
                    int[][] Ph = new int[n][m-1];
                    int[][] Pv = new int[n-1][m];

                    // Read values of A and B from file
                    for(k = 0; k < 2; k++)
                        for(i = 0; i < n; i++)
                            for(j = 0; j < m; j++)
                                if(k == 0)
                                    A[i][j] = getInt(file);
                                else
                                    B[i][j] = getInt(file);

                    // Read values of Ph from file
                    for(i = 0; i < n; i++)
                        for(j = 0; j < m-1; j++)
                            Ph[i][j] = getInt(file);

                    // Read values of Pv from file
                    for(i = 0; i < n-1; i++)
                        for(j = 0; j < m; j++)
                            Pv[i][j] = getInt(file);

                    // TODO replace call with correct arguments
                    // Solve BinIm problem
                    TestEliminationToutes(n, m, A, B, Ph, Pv);

                    // Close input stream
                    file.close();
                } catch(FileNotFoundException e) { // File not found
                    System.err.println(ANSI_RED + "File \"" + args[0] + "\" not found\n" + ANSI_RESET);
                    usage();
                }
            } else { // File extension not ".txt"
                System.err.println(ANSI_RED + "File extension shall be \".txt\" (without quotes)\n" + ANSI_RESET);
                usage();
            }
        } else { // No file specified
            System.err.println(ANSI_RED + "Filename not specified\n" + ANSI_RESET);
            usage();
        }
    }
}