package ru.poe.trade.parser.tradeparser.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class IOUtil {
    public static String getStringFromFile(File file) {
        StringBuilder sb = new StringBuilder();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
