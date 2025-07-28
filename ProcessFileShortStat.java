import java.io.*;


public class ProcessFileShortStat {
    static Object[] processFileShortStat(String filename) {
        int intCount = 0, floatCount = 0, stringCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            try (BufferedWriter writerInt = new BufferedWriter(new FileWriter(Main.outputPath + File.separator +
                    Main.prefix + "integers.txt", true))) {
                try (BufferedWriter writerFloat = new BufferedWriter(new FileWriter(Main.outputPath + File.separator +
                        Main.prefix + "floats.txt", true))) {
                    try (BufferedWriter writerString = new BufferedWriter(new FileWriter(Main.outputPath + File.separator +
                            Main.prefix + "strings.txt", true))) {
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            try {
                                if (line.matches("-?\\d+")) {
                                    writerInt.write(line);
                                    writerInt.newLine();
                                    intCount++;
                                } else if (line.matches("-?\\d*\\.?\\d+[eE]-?\\d+|-?\\d+\\.?\\d*")) {
                                    writerFloat.write(line);
                                    writerFloat.newLine();
                                    floatCount++;
                                } else {
                                    writerString.write(line);
                                    writerString.newLine();
                                    stringCount++;
                                }
                            } catch (NumberFormatException e) {
                                writerString.write(line);
                                writerString.newLine();
                                stringCount++;
                                // Treat it as a string if it's not a valid number
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
        return new Object[]{intCount, floatCount, stringCount};
    }
}
