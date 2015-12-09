package sample;

import interfaces.AlertDialogConstants;
import interfaces.DBUpdatable;
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
import util.DoubleClickEditGroupTable;
import util.DoubleClickEditStudentsTable;

import java.sql.SQLException;
import java.util.Optional;

public class Controller implements AlertDialogConstants {

    @FXML
    public void closeAction() {
        Optional<ButtonType> result = showDialog(Alert.AlertType.CONFIRMATION, AlertDialogConstants.alertConfirmation,"Do you want to close me?","Really?" );
        if (result.get() == ButtonType.OK) {
            DBConnector.getInstance().closeConnection();
            System.exit(0);
        }
        return;
    }

    @FXML
    public void helpAction() {
        showDialog(Alert.AlertType.INFORMATION, "Information Dialog", "DataBaseProject ver 1.5", "Author maystrovoy");
    }

    public static Optional<ButtonType> showDialog(Alert.AlertType information, String title, String headerText, String contentText) {
        Alert alert = new Alert(information);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        Stage helpStage = (Stage) alert.getDialogPane().getScene().getWindow();
        helpStage.getIcons().add(new Image(Main.iconPath));

        return alert.showAndWait();
    }

    private ObservableList<GroupModel> usersDataGroups = FXCollections.observableArrayList();
    private ObservableList<StudentModel> usersDataStudents = FXCollections.observableArrayList();

    private ObservableList<String> dataElders = FXCollections.observableArrayList();
    private ObservableList<String> dataGroups = FXCollections.observableArrayList();

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
    public ComboBox searchByNameField;
    @FXML
    public ComboBox searchByGroupNameField;

    public void fillingComboBoxFields() throws SQLException, ClassNotFoundException {
        dataElders.clear();
        dataGroups.clear();

        DBConnector.getInstance().namesOfGroupsQuery(dataGroups);
        DBConnector.getInstance().namesOfEldersQuery(dataElders);

        searchByGroupNameField.getItems().clear();
        searchByNameField.getItems().clear();

        searchByNameField.getItems().addAll(dataElders);
        searchByGroupNameField.getItems().addAll(dataGroups);
    }

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        fillingComboBoxFields();
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

        studentsTable.setEditable(true);
        studentsTable.setOnMouseClicked(new DoubleClickEditStudentsTable(studentsTable, new DBUpdatable() {
            @Override
            public void onDBUpdated() {
                try {
                    fillingComboBoxFields();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }));

        groupsTable.setEditable(true);
        groupsTable.setOnMouseClicked(new DoubleClickEditGroupTable(groupsTable, new DBUpdatable() {
            @Override
            public void onDBUpdated() {
                try {
                    fillingComboBoxFields();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }));

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
    public void searchByNameAction() {
        try {
            String name = searchByNameField.getEditor().getText();
            searchByGroupNameField.getEditor().clear();
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
            Optional<ButtonType> result = showDialog(Alert.AlertType.ERROR, AlertDialogConstants.alertError, "Description:", "The elder " + name + " is not found! \nPlease, input correct name!");
            if (result.get() == ButtonType.OK) {
                searchByNameField.requestFocus();
            }
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
    public void searchByGroupAction() {
        try {
            String group = searchByGroupNameField.getEditor().getText();
            searchByNameField.getEditor().clear();
            usersDataGroups.clear();
            usersDataStudents.clear();
            DBConnector.getInstance().groupNameQuery(usersDataGroups, group);
            groupsTable.setItems(usersDataGroups);
            if (!usersDataGroups.isEmpty()) {
                studentsRequire(usersDataGroups.get(0).getGroupId());
            }else{
                Optional<ButtonType> result = showDialog(Alert.AlertType.ERROR, AlertDialogConstants.alertError, "Description:", "The group " + group + " is not found! \nPlease, input correct group!");
                if (result.get() == ButtonType.OK) {
                    searchByGroupNameField.requestFocus();
                }
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
            searchByGroupNameField.getEditor().clear();
            searchByNameField.getEditor().clear();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}