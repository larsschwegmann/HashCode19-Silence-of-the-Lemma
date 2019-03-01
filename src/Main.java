import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {

    public static String readFileToString(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static void writeStringToFile(String filePath, String content) {
        try {
            Files.write(Paths.get(filePath), content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        String inputFileName = args[0];
        String fileContents = readFileToString(inputFileName);

        Solver solver = new Solver(fileContents);
        String out = solver.solve();

        System.out.println(out);
        String[] filenameComponents = inputFileName.split("/");
        String lastComponent = filenameComponents[filenameComponents.length - 1];

        writeStringToFile(lastComponent + ".out.txt", out);
    }

}
