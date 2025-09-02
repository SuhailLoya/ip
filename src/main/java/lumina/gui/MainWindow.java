package lumina.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lumina.main.Lumina;

/**
 * Main window configuring the AnchorPane
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Image userImage = new Image(this.getClass()
            .getResourceAsStream("/images/user_image.png"));
    private Image luminaImage = new Image(this.getClass()
            .getResourceAsStream("/images/lumina_image.png"));

    private Lumina lumina;
    private boolean isAcceptingInputs = true;

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Lumina instance */
    public void setLumina(Lumina l) {
        this.isAcceptingInputs = true;
        this.lumina = l;
    }

    private void exit() {
        this.isAcceptingInputs = false;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Lumina's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        if (this.isAcceptingInputs) {
            String userText = userInput.getText();
            String luminaResponse = lumina.getResponse(userInput.getText());

            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(userText, userImage),
                    DialogBox.getLuminaDialog(luminaResponse, luminaImage)
            );

            if (luminaResponse.equals(Lumina.EXIT_RESPONSE)) {
                this.exit();
            }
            userInput.clear();
        }
    }
}
