package Client;

import javafx.event.ActionEvent;

public class WelcomeController {
    NetworkClient nc = NetworkClient.getInstance();

    public void onPressConnect(ActionEvent actionEvent) {
        nc.connect("localhost",8189);
    }

    public void onPressAuthorize(ActionEvent actionEvent) {
        nc.authorize("login1","pass1");
    }
}
