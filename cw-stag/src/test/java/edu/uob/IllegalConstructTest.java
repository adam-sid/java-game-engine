package edu.uob;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IllegalConstructTest {
    //TODO test all files to make sure no uses of illegal language features

    @Test
    public void testFilesInFolder() {
        // Replace this with the path to the folder containing your files
        String filePath = "target" + File.separator + "classes";
        File gameStateFile = new File(filePath + File.separator + "GameState");
        assertTrue(gameStateFile.exists());
        File folder = new File(filePath);

        // List all files in the directory (you can filter for specific extensions if needed)
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".java"));

        if (files != null) {
            for (File file : files) {
                // We use file.getAbsolutePath() to pass the file path as an argument
                System.out.println("Running Strange for file: " + file.getAbsolutePath());

                // Call Strange.main() with the file path as an argument
                StrangeTest.execute(new String[] { file.getAbsolutePath() });
            }
        } else {
            System.out.println("No files found in the specified directory.");
        }
    }
}

