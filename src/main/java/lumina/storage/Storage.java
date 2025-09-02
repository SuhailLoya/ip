package lumina.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lumina.parser.Parser;
import lumina.task.Task;
import lumina.task.TaskList;

/**
 * Manages the saving and loading of task data to and from a file.
 */
public class Storage {
    // paths
    private static final String DATA_DIRECTORY_PATH = "./data";
    private static final String DATA_FILE_PATH = "./data/data.txt";

    private Parser parser;

    /**
     * Constructs a Storage instance with the specified parser.
     *
     * @param parser the Parser instance used for parsing dates or other data in the storage operations
     */
    public Storage(Parser parser) {
        this.parser = parser;
    }

    private void createDataDirectoryAndFileIfNotExists() {

        // get relative and absolute Path to data directory
        Path directoryPath = Paths.get(DATA_DIRECTORY_PATH).toAbsolutePath().normalize();

        // declare fileName and it's path
        Path filePath = Paths.get(DATA_FILE_PATH).toAbsolutePath().normalize();

        // create the directory and file if it does not exist
        try {
            if (Files.notExists(directoryPath)) {
                // Create the directory if it doesn't exist
                System.out.println("Creating data directory: " + directoryPath);
                Files.createDirectory(directoryPath);
            }

            if (Files.notExists(filePath)) {
                // Create the file if it doesn't exist
                System.out.println("Creating data file: " + filePath);
                Files.createFile(filePath);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Saves the data from the given TaskList to a file.
     * The data is written to a file specified by {@code DATA_FILE_PATH}.
     * If the file already exists, it is truncated before writing new data.
     *
     * @param taskList the TaskList instance containing the tasks to be saved
     */
    public void saveData(TaskList taskList) {
        Path path = Paths.get(DATA_FILE_PATH);
        ArrayList<String> lines = taskList.toLines();

        try {
            Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Loads all valid data from the data file and sets it onto the provided TaskList.
     * The data file is created if it does not already exist.
     *
     * @param taskList the TaskList instance to which the loaded tasks will be added
     */
    public void loadData(TaskList taskList) {
        // create data directory and file if not exists
        this.createDataDirectoryAndFileIfNotExists();

        // read data and load it onto tasks
        taskList.setData(this.readData());
    }

    private ArrayList<Task> readData() {
        // intialize empty array
        ArrayList<Task> retTasks = new ArrayList<>();

        // get path to data file
        Path path = Paths.get(DATA_FILE_PATH);
        assert Files.exists(path) : "File should exist";

        // get line stream and collect it into an array
        try (Stream<String> lines = Files.lines(path)) {
            retTasks = lines.map(parser::parseDataLine)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            // error handling
            System.err.println(e.getMessage());
        }

        return retTasks;
    }
}
