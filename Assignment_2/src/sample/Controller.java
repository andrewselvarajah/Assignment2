package sample;
//Andrew Selvarajah
//100520671
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.File;

public class Controller {

    @FXML
    private GridPane editAreaTop;
    @FXML
    private Button uploadBtn;
    @FXML
    private AnchorPane serverPane;
    @FXML
    private AnchorPane localPane;
    @FXML
    private Button downloadBtn;
    @FXML
    private BorderPane pane;
    @FXML
    private ListView<String> leftList;
    @FXML
    private ListView<String> rightList;

    final String host = "hostname";
    final int port = 8080;
    ObservableList<String> namesClient;


//gets file names from server
    @FXML
    void initialize(){

        Client client = new Client(host,port);
        namesClient = FXCollections.observableArrayList();
        File file = new File("src/Files");

        File[] filesInDir = file.listFiles();

        for (int x = 0; x < filesInDir.length; x++){
            namesClient.add(filesInDir[x].getName());
        }

        leftList.setItems(namesClient);

        client.getNames();









    }
//to download files from server
    @FXML
    void downloader(ActionEvent event) {

        Client client = new Client(host,port);

        client.recieveFiles("/src/Files");





    }
//upload files from to server
    @FXML
    void uploader(ActionEvent event) {

        File file = new File("/src/Files");

        File[] filesInDir = file.listFiles();
        Client client = new Client(host,port);



        client.sendFiles("/src/Files");





    }

    public ListView<String> getLeftList(){

        return leftList;
    }
    public ListView<String> getRightList(){

        return rightList;

    }

}
