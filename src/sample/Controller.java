package sample;

import util.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.GroupModel;
import models.StudentModel;

import java.sql.SQLException;

public class Controller {

    @FXML
    public void closeAction() {
        System.exit(0);
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

        groupsTable.setItems(usersDataGroups);
        studentsTable.setItems(usersDataStudents);
    }

    @FXML
    public void connectAction() {
        try {
            usersDataGroups.clear();
            usersDataStudents.clear();
            DBConnector.groupQuery(usersDataGroups);
            DBConnector.studentsQuery(usersDataStudents);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
