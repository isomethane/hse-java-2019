package ru.hse.mnmalysheva.ftpgui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

/** FTPClient user interface. **/
public class FTPClientGUI extends Application {
    private FTPClient client = new FTPClient();
    private File path;
    private TextField hostNameField = new TextField();
    private TextField portField = new TextField();
    private TextField pathField = new TextField();
    private ListView<FileDescription> filesList;
    private Button connectButton;
    private Button disconnectButton;
    private Stage primaryStage;

    /** Starts program. **/
    public static void main(@NotNull String[] args) {
        Application.launch(args);
    }

    /** Initializes stage. **/
    @Override
    public void start(@NotNull Stage primaryStage) {
        this.primaryStage = primaryStage;

        GridPane menuGrid = initMenu();

        var root = new VBox(menuGrid, filesList);
        VBox.setVgrow(filesList, Priority.ALWAYS);

        Scene scene = new Scene(root);

        primaryStage.setTitle("FTP client GUI");
        primaryStage.setScene(scene);
        primaryStage.setHeight(500);
        primaryStage.setWidth(700);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(700);
        primaryStage.show();
    }

    private GridPane initMenu() {
        GridPane menuGrid = new GridPane();

        var host = new Label("Host: ");
        var port = new Label("Port: ");
        var path = new Label("Path: ");
        connectButton = new Button("connect");
        disconnectButton = new Button("disconnect");

        menuGrid.addRow(0, host, hostNameField, port, portField, connectButton, disconnectButton);
        menuGrid.add(path, 0, 1);
        menuGrid.add(pathField, 1, 1, 5, 1);

        menuGrid.setVgap(10);
        menuGrid.setHgap(10);

        initPathField();
        initFilesList();
        initConnectionButtons();

        return menuGrid;
    }

    private void initConnectionButtons() {
        connectButton.setOnMouseClicked(event -> {
            String hostName = hostNameField.getText();
            String port = portField.getText();
            new ConnectServerTask(hostName, port).run();
        });
        disconnectButton.setOnMouseClicked(event -> {
            new DisconnectServerTask().run();
        });
    }

    private void initPathField() {
        pathField.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                path = new File(pathField.getText());
                new ExecuteListTask(null).run();
            }
        });
    }

    private void initFilesList() {
        filesList = new ListView<>();
        filesList.setOnMouseClicked(event -> {
            var fileDescription = filesList.getSelectionModel().getSelectedItem();
            if (fileDescription == null) {
                return;
            }
            if (fileDescription.isDirectory) {
                new ExecuteListTask(fileDescription.name).run();
            } else {
                new ExecuteGetTask(fileDescription.name).run();
            }
        });
    }

    private void menuSetDisable(boolean disable) {
        hostNameField.setDisable(disable);
        portField.setDisable(disable);
        pathField.setDisable(disable);
        filesList.setDisable(disable);
        connectButton.setDisable(disable);
    }

    private void showAlert(String message) {
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(primaryStage);
        alert.show();
    }

    private class ConnectServerTask extends Task<Void> {
        String hostName;
        String port;

        private ConnectServerTask(String hostName, String port) {
            this.hostName = hostName;
            this.port = port;
            menuSetDisable(true);
        }

        @Override
        protected Void call() throws Exception {
            client.connect(hostName, Integer.parseInt(port));
            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            showAlert("Connected to server");
            menuSetDisable(false);
        }

        @Override
        protected void failed() {
            super.failed();
            showAlert("Failed to connect to server");
            menuSetDisable(false);
        }
    }

    private class DisconnectServerTask extends Task<Void> {
        private DisconnectServerTask() {
            menuSetDisable(true);
        }

        @Override
        protected Void call() throws Exception {
            client.disconnect();
            return null;
        }

        @Override
        protected void succeeded() {
            showAlert("Disconnected");
            menuSetDisable(false);
        }

        @Override
        protected void failed() {
            showAlert("Failed to disconnect");
            menuSetDisable(false);
        }
    }

    private class ExecuteListTask extends Task<Void> {
        private File newPath = path;
        private List<FileDescription> files;

        private ExecuteListTask(@Nullable String directoryName) {
            if (directoryName != null) {
                newPath = new File(path, directoryName);
            }
            menuSetDisable(true);
        }

        @Override
        protected Void call() throws IOException {
            files = client.executeList(newPath.getPath());
            if (files != null) {
                files.sort((a, b) -> {
                    if (a.isDirectory) {
                        return b.isDirectory ? a.name.compareTo(b.name) : -1;
                    }
                    return b.isDirectory ? 1 : a.name.compareTo(b.name);
                });
            }
            return null;
        }

        @Override
        protected void succeeded() {
            if (files == null) {
                showAlert("Not a directory");
            } else {
                path = newPath;
                filesList.getItems().clear();
                filesList.getItems().add(new FileDescription("..", true));
                filesList.getItems().addAll(FXCollections.observableList(files));
            }
            menuSetDisable(false);
        }

        @Override
        protected void failed() {
            showAlert("Failed to execute list task");
            menuSetDisable(false);
        }
    }

    private class ExecuteGetTask extends Task<Void> {
        private File fromFile;
        private File toFile;
        private boolean toDownload = true;

        private ExecuteGetTask(@NotNull String fileName) {
            var directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose directory to save");
            File directory = directoryChooser.showDialog(primaryStage);
            if (directory != null) {
                fromFile = new File(path, fileName);
                toFile = new File(directory, fileName);
            } else {
                toDownload = false;
            }

            menuSetDisable(true);
        }

        @Override
        protected Void call() throws IOException {
            if (toDownload) {
                if (toFile.createNewFile()) {
                    var out = new FileOutputStream(toFile);
                    client.executeGet(fromFile.getPath(), out);
                } else {
                    throw new FileAlreadyExistsException(toFile.getName());
                }
            }
            return null;
        }

        @Override
        protected void succeeded() {
            if (toDownload) {
                showAlert("Downloaded file");
            }
            menuSetDisable(false);
        }

        @Override
        protected void failed() {
            if (getException() instanceof FileAlreadyExistsException) {
                showAlert("File with such name already exists");
            } else {
                showAlert("Failed to download file");
            }
            menuSetDisable(false);
        }
    }
}
