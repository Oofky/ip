package bogos;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls the main Bogos chat window.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    private Bogos bogos;
    private final Image userImage = new Image(getClass().getResourceAsStream("/images/photosprinted.png"));
    private final Image bogosImage = new Image(getClass().getResourceAsStream("/images/bogosbinted.png"));

    /**
     * Configures scrolling after FXML fields have been injected.
     */
    @FXML
    public void initialize() {
        dialogContainer.heightProperty().addListener((observable, oldHeight, newHeight) -> {
            scrollPane.setVvalue(scrollPane.getVmax());
        });
    }

    /**
     * Supplies the command-processing application used by this window.
     *
     * @param bogos Bogos application instance.
     */
    public void setBogos(Bogos bogos) {
        this.bogos = bogos;
        dialogContainer.getChildren().add(DialogBox.getBogosDialog(bogos.getWelcomeMessage(), bogosImage));
    }

    /**
     * Adds the user's command and Bogos's reply to the dialog history.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = bogos.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBogosDialog(response, bogosImage));
        userInput.clear();

        if (bogos.isExitCommand(input)) {
            Platform.exit();
        }
    }
}
