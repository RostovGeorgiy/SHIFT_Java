import java.io.*;
import java.math.BigInteger;
import java.util.List;

public class ProcessFileFullStat {
    static Object[] processFile(String filename) {
        int intCount = 0, floatCount = 0, stringCount = 0, maxString = 0, minString = Integer.MAX_VALUE;
        double maxFloat = Double.MIN_VALUE, minFloat = Double.MAX_VALUE, sumFloat = 0;
        BigInteger maxInt = BigInteger.valueOf(Integer.MIN_VALUE), minInt = BigInteger.valueOf(Integer.MAX_VALUE),
                sumInt = BigInteger.valueOf(0);

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            try (BufferedWriter writerInt = new BufferedWriter(new FileWriter(Main.outputPath + File.separator +
                    Main.prefix + "integers.txt", true))) {
                try (BufferedWriter writerFloat = new BufferedWriter(new FileWriter(Main.outputPath + File.separator +
                        Main.prefix + "floats.txt", true))) {
                    try (BufferedWriter writerString = new BufferedWriter(new FileWriter(Main.outputPath + File.separator +
                            Main.prefix + "strings.txt", true))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            try {
                                if (line.matches("-?\\d+")) {
                                    BigInteger number = (new BigInteger(line));
                                    intCount++;
                                    sumInt = sumInt.add(number);
                                    maxInt = maxInt.max(number);
                                    minInt = minInt.min(number);
                                    writerInt.write(number.toString());
                                    writerInt.newLine();
                                } else if (line.matches("-?\\d*\\.?\\d+[eE]-?\\d+|-?\\d+\\.?\\d*")) {
                                    double number = Double.parseDouble(line);
                                    floatCount++;
                                    maxFloat = Double.max(maxFloat, number);
                                    minFloat = Double.min(minFloat, number);
                                    sumFloat += number;
                                    writerFloat.write(Double.toString(number));
                                    writerFloat.newLine();
                                } else if (Double.parseDouble(line) == Double.POSITIVE_INFINITY
                                        || Double.parseDouble(line) == Double.NEGATIVE_INFINITY) {
                                    System.out.printf("Error: line %s exceeds possible values of type double. It will be treated as a string\n", line);
                                }
                                else {
                                    stringCount++;
                                    minString = Math.min(minString, line.length());
                                    maxString = Math.max(maxString, line.length());
                                    writerString.write(line);
                                    writerString.newLine();
                                }
                            } catch (NumberFormatException e) {
                                stringCount++;
                                minString = Math.min(minString, line.length());
                                maxString = Math.max(maxString, line.length());
                                writerString.write(line);
                                writerString.newLine(); // Treat it as a string if it's not a valid number
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Error writing to file " + Main.outputPath + File.separator +
                                Main.prefix + "strings.txt" + ": " + e.getMessage());
                    }
                } catch (IOException e) {
                    System.out.println("Error writing to file " + Main.outputPath + File.separator +
                            Main.prefix + "floats.txt" + ": " + e.getMessage());
                }
            } catch (IOException e) {
                System.out.println("Error writing to file " + Main.outputPath + File.separator +
                        Main.prefix + "integers.txt" + ": " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error reading file " + filename + ": " + e.getMessage());
        }
    return new Object[]{intCount, minInt, maxInt, sumInt, floatCount, minFloat, maxFloat, sumFloat, stringCount, minString, maxString};
    }
}
