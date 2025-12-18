package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CsvUtil {
    public static void ensureDirExists(String filePath) {
        Path p = Paths.get(filePath).toAbsolutePath().getParent();
        if (p != null && !Files.exists(p)) {
            try {
                Files.createDirectories(p);
            } catch (IOException e) {
                // ignore - caller will see IO exceptions on write
            }
        }
    }

    public static synchronized void appendLine(String filePath, String line) {
        ensureDirExists(filePath);
        Path p = Paths.get(filePath);
        try {
            if (!Files.exists(p)) {
                Files.write(p, (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            } else {
                Files.write(p, (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.err.println("Failed to write CSV line to " + filePath + ": " + e.getMessage());
        }
    }

    public static synchronized void ensureHeaderExists(String filePath, String headerLine) {
        ensureDirExists(filePath);
        Path p = Paths.get(filePath);
        try {
            if (!Files.exists(p) || Files.size(p) == 0) {
                Files.write(p, (headerLine + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
            } else {
                // check first non-empty line
                List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
                boolean hasHeader = false;
                for (String l : lines) {
                    if (l == null) continue;
                    String t = l.trim();
                    if (t.isEmpty()) continue;
                    if (t.equals(headerLine)) hasHeader = true;
                    break;
                }
                if (!hasHeader) {
                    // prepend header by rewriting file
                    List<String> newLines = new java.util.ArrayList<>();
                    newLines.add(headerLine);
                    newLines.addAll(lines);
                    Files.write(p, newLines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to ensure CSV header for " + filePath + ": " + e.getMessage());
        }
    }

    public static synchronized void overwriteLines(String filePath, List<String> lines) {
        ensureDirExists(filePath);
        Path p = Paths.get(filePath);
        try {
            Files.write(p, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Failed to overwrite CSV " + filePath + ": " + e.getMessage());
        }
    }

    public static String escape(String value) {
        if (value == null) return "";
        String v = value.replace("\"", "\"\"");
        if (v.contains(",") || v.contains("\n") || v.contains("\r") || v.contains("\"")) {
            return "\"" + v + "\"";
        }
        return v;
    }
}
