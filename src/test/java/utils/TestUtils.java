package utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;

public class TestUtils {
    public static void RedirectStdin(String input) throws FileNotFoundException {
        FileInputStream is = new FileInputStream(new File(input));
        System.setIn(is);
    }
    public static void RedirectStdinToString(String input) {
        InputStream is = new ByteArrayInputStream(input.getBytes());
        System.setIn(is);
    }

    public static void RedirectStdout(String output) throws FileNotFoundException {
        PrintStream os = new PrintStream(new File(output));
        System.setOut(os);
    }

    public static ByteArrayOutputStream RedirectStdoutToString() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os));
        return os;
    }

    public static void resetAllStd() {
        System.setIn(_is);
        System.setOut(_os);
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void compareFile(String standard, String input) throws IOException {
        String standardText = readFile(standard);
        String inputText = readFile(input);
        assertEquals(inputText.replaceAll("\r\n", "\n"), standardText.replaceAll("\r\n", "\n"));
    }

    private static String readFile(String standard) throws IOException {
        return readFile(standard, Charset.defaultCharset());
    }


    public static final InputStream _is = System.in;
    public static final PrintStream _os = System.out;

}
