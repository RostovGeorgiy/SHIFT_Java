import java.io.*;
import java.math.BigInteger;
import java.util.*;



public class Main {

    public static final String FILENAME_PATTERN = "^[^\\\\/:*?\"<>|]+$";
    public static final String FILEPATH_PATTERN = "^(?:[A-z]:)?(?:[\\\\/][^\\\\/:*?\"<>|]+)+[\\\\/]?$";
    static String outputPath = ".";
    static String prefix = "";
    static boolean append = false;
    private static boolean shortStat = false;
    private static boolean fullStat = false;

    public static boolean validateFileName(String filename) {
        if (filename == null) {
            return false;
        }
        return filename.matches(FILENAME_PATTERN);
    }

    public static boolean validateFilePath(String filepath) {
        if (filepath == null) {
            return false;
        }
        return filepath.matches(FILEPATH_PATTERN);
    }


    private static void duplicateValidation(String arg, Map<String, String> duplicates) {
        if (duplicates.containsKey(arg)) {
            System.err.println("Error: repeating file name " + arg);
            System.exit(2);
        }
        duplicates.put(arg, arg);
    }


    public static void main(String[] args) throws IOException {

        List<String> inputFiles = new ArrayList<>();
        List<String> inputFilesChecked = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            if ("-p".equals(args[i])) {
                if (i + 1 < args.length) {
                    prefix = args[i + 1];
                    i++;
                } else {
                    System.out.println("No input file and output file prefix specified!");
                    System.exit(0);
                }
            } else if ("-o".equals(args[i])) {
                if (i + 1 < args.length) {
                    outputPath = args[i + 1];
                } else {
                    System.out.println("No input file and output path specified!");
                    System.exit(0);
                }
                i++;
            } else if ("-s".equals(args[i])) {
                shortStat = true;
                if (i + 1 >= args.length) {
                    System.out.println("No input file specified!");
                    System.exit(0);
                }
            } else if ("-f".equals(args[i])) {
                fullStat = true;
                if (i + 1 >= args.length) {
                    System.out.println("No input file specified!");
                    System.exit(0);
                }
            } else if ("-a".equals(args[i])) {
                append = true;
                if (i + 1 >= args.length) {
                    System.out.println("No input file specified!");
                    System.exit(0);
                }
            } else {
                inputFiles.add(args[i]);
            }

        }


        if (!shortStat && !fullStat) {
            System.out.println("No statistics type specified. Using short statistics.");
            shortStat = true;
        } else if (shortStat && fullStat) {
            System.out.println("Both statistics types specified. Using full statistics.");
            shortStat = false;
        }

        
        Map<String, String> duplicates = new HashMap<>();

        for (String filename : inputFiles) {
            duplicateValidation(filename, duplicates);
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("File does not exist: " + file.getAbsolutePath());
            }
            else if (!filename.endsWith(".txt")) {
                System.out.println(filename + ": Incorrect file extension. This file will be ignored");
            } else if (!validateFileName(filename)) {
                System.out.println(filename + ": Incorrect file name. This file will be ignored");
            }
            else inputFilesChecked.add(filename.toLowerCase());
        }

        if (inputFilesChecked.isEmpty()) {
            System.out.println("No valid input files!");
            System.exit(2);
        }

        if (!Objects.equals(prefix, "") && !validateFileName(prefix)) {
            System.out.println(prefix + ": Incorrect file prefix. Default will be used.");
            prefix = "";
        }
        File directory = new File(outputPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Output path does not exist: " + directory.getAbsolutePath() + " Default will be used.");
            outputPath = ".";
        }
        else if (!Objects.equals(outputPath, ".") && !validateFilePath(outputPath)) {
            System.out.println(outputPath + ": Incorrect file path. Default will be used.");
            outputPath = ".";
        }

        if (!Main.append) {
            new FileOutputStream(Main.outputPath + File.separator +
                    Main.prefix + "integers.txt").close();
            new FileOutputStream(Main.outputPath + File.separator +
                    Main.prefix + "floats.txt").close();
            new FileOutputStream(Main.outputPath + File.separator +
                    Main.prefix + "strings.txt").close();
        }


        if (fullStat) {
            int intCount = 0, floatCount = 0, stringCount = 0, maxString = 0, minString = Integer.MAX_VALUE;
            double maxFloat = Double.MIN_VALUE, minFloat = Double.MAX_VALUE, sumFloat = 0;
            BigInteger maxInt = BigInteger.valueOf(Integer.MIN_VALUE), minInt = BigInteger.valueOf(Integer.MAX_VALUE),
                    sumInt = BigInteger.valueOf(0);
            for (String inputFile : inputFilesChecked) {
                Object[] result = ProcessFileFullStat.processFile(inputFile);
                intCount += (int) result[0];
                minInt = minInt.min((BigInteger) result[1]);
                maxInt = maxInt.max((BigInteger) result[2]);
                sumInt = sumInt.add((BigInteger) result[3]);
                floatCount += (int) result[4];
                minFloat = Double.min(minFloat, (double) result[5]);
                maxFloat = Double.max(maxFloat, (double) result[6]);
                sumFloat += (double) result[7];
                stringCount += (int) result[8];
                minString = Math.min(minString, (int) result[9]);
                maxString = Math.max(maxString, (int) result[10]);

            }
            if (intCount > 0)
                System.out.printf("Integers: count = %d, min = %d, max = %d, sum = %d, mean = %d\n",
                        intCount, minInt, maxInt, sumInt, sumInt.divide(BigInteger.valueOf(intCount)));
            if (floatCount > 0)
                System.out.printf("Floats: count = %d, min = %f, max = %f, sum = %f, mean = %f\n",
                        floatCount, minFloat, maxFloat, sumFloat,
                        sumFloat / floatCount);
            if (stringCount > 0)
                System.out.printf("Strings: count = %d, min = %d, max = %d",
                        stringCount, minString, maxString);

        } else if (shortStat) {
            int intCount = 0, floatCount = 0, stringCount = 0;
            for (String inputFile : inputFilesChecked) {
                ProcessFileShortStat.processFileShortStat(inputFile);

                Object[] result = ProcessFileShortStat.processFileShortStat(inputFile);
                intCount += (int) result[0];
                floatCount += (int) result[1];
                stringCount += (int) result[2];

            }
            if (intCount > 0)
                System.out.printf("Integers: count = %d\n", intCount);
            if (floatCount > 0)
                System.out.printf("Floats: count = %d\n", floatCount);
            if (stringCount > 0)
                System.out.printf("Strings: count = %d", stringCount);
        }
    }

}