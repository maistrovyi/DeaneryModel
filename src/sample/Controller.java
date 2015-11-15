package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.GroupModel;
import models.StudentModel;
import util.DBConnector;

import java.sql.SQLException;
import java.util.Optional;

public class Controller {

    @FXML
    public void closeAction() {
        Optional<ButtonType> result = showDialog(Alert.AlertType.CONFIRMATION, "Confirmation Dialog","Confirmation Dialog","Exit?" );
        if (result.get() == ButtonType.OK) {
            DBConnector.getInstance().closeConnection();
            System.exit(0);
        }
        return;
    }

    @FXML
    public void helpAction() {
        showDialog(Alert.AlertType.INFORMATION, "Information Dialog", "Data Base Project ver 1.1", "Outhor maystrovoy");
    }

    public static Optional<ButtonType> showDialog(Alert.AlertType information, String title, String headerText, String contentText) {
        Alert alerHelp = new Alert(information);
        alerHelp.setTitle(title);
        alerHelp.setHeaderText(headerText);
        alerHelp.setContentText(contentText);
        Stage helpStage = (Stage) alerHelp.getDialogPane().getScene().getWindow();
        helpStage.getIcons().add(new Image(Main.iconPath));

        return alerHelp.showAndWait();
    }

    private ObservableList<GroupModel> usersDataGroups = FXCollections.observableArrayList();

    private ObservableList<StudentModel> usersDataStudents = FXCollections.observableArrayList();

    @FXML
    private TableView<GroupModel> groupsTable;
    @FXML
    private TableColumn<GroupModel, Integer> groupId = new TableColumn();
    @FXML
    private TableColumn<GroupModel, String> groupName = new TableColumn();
    @FXML
    private TableColumn<GroupModel, Integer> groupYearGraduate = new TableColumn();
    @FXML
    private TableColumn<GroupModel, Integer> villageElderId = new TableColumn();


    @FXML
    private TableView<StudentModel> studentsTable;
    @FXML
    private TableColumn<StudentModel, Integer> studentId = new TableColumn();
    @FXML
    private TableColumn<StudentModel, String> studentName = new TableColumn();
    @FXML
    private TableColumn<StudentModel, Integer> studentGroup = new TableColumn();
    @FXML
    private TableColumn<StudentModel, Integer> numberOfGradebook = new TableColumn();
    @FXML
    private TableColumn<StudentModel, String> studentSex = new TableColumn();

    @FXML
    private void initialize() {
        groupId.setCellValueFactory(new PropertyValueFactory<GroupModel, Integer>("groupId"));
        groupName.setCellValueFactory(new PropertyValueFactory<GroupModel, String>("groupName"));
        groupYearGraduate.setCellValueFactory(new PropertyValueFactory<GroupModel, Integer>("groupYearGraduate"));
        villageElderId.setCellValueFactory(new PropertyValueFactory<GroupModel, Integer>("villageElderId"));

        studentId.setCellValueFactory(new PropertyValueFactory<StudentModel, Integer>("studentId"));
        studentName.setCellValueFactory(new PropertyValueFactory<StudentModel, String>("studentName"));
        studentGroup.setCellValueFactory(new PropertyValueFactory<StudentModel, Integer>("studentGroup"));
        numberOfGradebook.setCellValueFactory(new PropertyValueFactory<StudentModel, Integer>("numberOfGradebook"));
        studentSex.setCellValueFactory(new PropertyValueFactory<StudentModel, String>("studentSex"));

        groupsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            retrieveStudents();
        });
        groupsTable.setItems(usersDataGroups);


    }

    private void retrieveStudents() {
        usersDataStudents.clear();
        if (groupsTable.getSelectionModel().getSelectedItem() != null) {
            studentsRequire(groupsTable.getSelectionModel().getSelectedItem().getGroupId());
        }
    }

    private void studentsRequire(int id) {
        try {
            DBConnector.getInstance().studentsQuery(usersDataStudents, id );
            studentsTable.setItems(usersDataStudents);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public TextField searchByNameField;

    @FXML
    public void searchByNameAction() {
        try {
            String name = searchByNameField.getText();
            getStudentsByName(name);
            getGroupByName(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getStudentsByName(String name) throws SQLException {
        usersDataStudents.clear();
        DBConnector.getInstance().studentsOfGroupByVillageElderNameQuery(usersDataStudents, name);
        if (usersDataStudents.isEmpty()) {
            showDialog(Alert.AlertType.ERROR, "Error", "Test", "Test1");
        } else {
            studentsTable.setItems(usersDataStudents);
        }
    }

    private void getGroupByName(String name) throws SQLException {
        usersDataGroups.clear();
        DBConnector.getInstance().getGroupIdByVillageElderName(usersDataGroups, name);
        groupsTable.setItems(usersDataGroups);
    }

    @FXML
    public TextField groupNameField;

    @FXML
    public void searchByGroupAction() {
        try {
            String group = groupNameField.getText().trim();
            usersDataGroups.clear();
            usersDataStudents.clear();
            DBConnector.getInstance().groupNameQuery(usersDataGroups, group);
            groupsTable.setItems(usersDataGroups);
            if (!usersDataGroups.isEmpty()) {
                studentsRequire(usersDataGroups.get(0).getGroupId());
            }else{
                showDialog(Alert.AlertType.ERROR, "Error", "Test", "Test1");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void connectAction() {
        try {
            usersDataGroups.clear();
            usersDataStudents.clear();
            DBConnector.getInstance().groupQuery(usersDataGroups);


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
