package lumina.ui;

/** A class to handle UI related tasks */
public class Ui {
    // configs
    private static final int indentWidth = 2;

    /**
     * Returns an indented message where each line indented by `indentWidth`
     * number of spaces.
     *
     * @param msg The message to be indented.
     * @return The indented message.
     */
    public String indentMessage(String msg) {
        String[] lines = msg.split("\n");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < indentWidth; j++) {
                builder.append(" ");
            }
            builder.append(lines[i]);
            if (i < lines.length - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    private void printSeparator() {
        System.out.println("__________________________________________________________________");
    }

    /**
     * Prints a formatted message to the console with separators before and after the message.
     *
     * @param msg The message to be printed.
     */
    public void printMessage(String msg) {
        this.printSeparator();
        System.out.println(indentMessage(msg));
        this.printSeparator();
    }

    /**
     * Greets the user with a welcome message when the program starts.
     */
    public void greet() {
        this.printMessage("Hello! I'm Lumina\n"
                + "What can I do for you?");
    }

    /**
     * Exits the program after printing a farewell message and saving the data.
     */
    public void exit() {
        System.exit(0);
    }

}
