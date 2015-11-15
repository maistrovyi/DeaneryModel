package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.GroupModel;
import models.StudentModel;
import util.DBConnector;

import java.sql.SQLException;
import java.util.Optional;

public class Controller {

    @FXML
    public void closeAction() {
        Alert alertClose = new Alert(Alert.AlertType.CONFIRMATION);
        alertClose.setTitle("Confirmation Dialog");
        alertClose.setHeaderText("Confirmation Dialog");
        alertClose.setContentText("Exit?");
        Stage closeStage = (Stage) alertClose.getDialogPane().getScene().getWindow();
        closeStage.getIcons().add(new Image(Main.iconPath));

        Optional<ButtonType> result = alertClose.showAndWait();
        if (result.get() == ButtonType.OK) {
            DBConnector.getInstance().closeConnection();
            System.exit(0);
        }
        return;
    }

    @FXML
    public void helpAction() {
        Alert alerHelp = new Alert(Alert.AlertType.INFORMATION);
        alerHelp.setTitle("Information Dialog");
        alerHelp.setHeaderText("Data Base Project ver 1.1");
        alerHelp.setContentText("Outhor maystrovoy");
        Stage helpStage = (Stage) alerHelp.getDialogPane().getScene().getWindow();
        helpStage.getIcons().add(new Image(Main.iconPath));

        alerHelp.showAndWait();
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

//        studentsTable.setEditable(true);

        studentId.setCellValueFactory(new PropertyValueFactory<StudentModel, Integer>("studentId"));
        studentName.setCellValueFactory(new PropertyValueFactory<StudentModel, String>("studentName"));
        studentGroup.setCellValueFactory(new PropertyValueFactory<StudentModel, Integer>("studentGroup"));
        numberOfGradebook.setCellValueFactory(new PropertyValueFactory<StudentModel, Integer>("numberOfGradebook"));
        studentSex.setCellValueFactory(new PropertyValueFactory<StudentModel, String>("studentSex"));


        groupsTable.setEditable(true);
         // selects cell only, not the whole row
        groupsTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                @SuppressWarnings("rawtypes")
                TablePosition pos = groupsTable.getSelectionModel().getSelectedCells().get(0);
                //if (pos.getColumn() == groupsTable.getEditingCell().getColumn() && pos.getRow() == groupsTable.getEditingCell().getRow() ){
                if (click.getClickCount() == 2 ) {


//                    groupsTable.setEditable(false);
                    groupsTable.getSelectionModel().setCellSelectionEnabled(true);

                    //TablePosition pos = groupsTable.getSelectionModel().getSelectedCells().get(0);
                    if (pos.getRow() == 1) {
                        TableColumn<GroupModel, String> firstNameCol = pos.getTableColumn();


                        firstNameCol.setCellFactory(TextFieldTableCell.<GroupModel>forTableColumn());
                        firstNameCol.setOnEditCommit(
                                (TableColumn.CellEditEvent<GroupModel, String> t) -> {

                                    ((GroupModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setGroupName(t.getNewValue());
                                });
                    } else {
                        TableColumn<GroupModel, Integer> firstNameCol = pos.getTableColumn();


                        firstNameCol.setCellFactory(TextFieldTableCell.<GroupModel, Integer>forTableColumn(new StringConverter<Integer>() {
                            @Override
                            public String toString(Integer object) {
                                return object.toString();
                            }

                            @Override
                            public Integer fromString(String string) {
                                return Integer.valueOf(string);
                            }
                        }));
                        try {


                            firstNameCol.setOnEditCommit(
                                    new EventHandler<TableColumn.CellEditEvent<GroupModel, Integer>>() {
                                        @Override
                                        public void handle(TableColumn.CellEditEvent<GroupModel, Integer> tt) {
                                            int row = tt.getTablePosition().getRow();
                                            GroupModel rowModel = ((GroupModel) tt.getTableView().getItems().get(row));
                                            Integer rowNewValue = tt.getNewValue().intValue();
                                            switch (row) {
                                                case 0:
                                                    rowModel.setGroupId(rowNewValue);
                                                    break;
                                                case 2:
                                                    rowModel.setGroupYearGraduate(rowNewValue);
                                                    break;
                                                case 3:
                                                    rowModel.setVillageElderId(rowNewValue);
                                                    break;
                                            }

                                        }
                                    });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    int row = pos.getRow();
                    int col = pos.getColumn();
                    @SuppressWarnings("rawtypes")
                    TableColumn column = pos.getTableColumn();
                    String val = column.getCellData(row).toString();
                    System.out.println("Selected Value, " + val + ", Column: " + col + ", Row: " + row);
                //}
                }
            }
        });

        groupsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            usersDataStudents.clear();
            if (groupsTable.getSelectionModel().getSelectedItem() != null) {
                try {
                    DBConnector.getInstance().studentsQuery(usersDataStudents, groupsTable.getSelectionModel().getSelectedItem().getGroupId());
                    studentsTable.setItems(usersDataStudents);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        });
        groupsTable.setItems(usersDataGroups);
    }

    @FXML
    public TextField searchByNameField;

    @FXML
    public void searchByNameAction() {
        try {
            String name = searchByNameField.getText();
            usersDataGroups.clear();
            usersDataStudents.clear();
            DBConnector.getInstance().villageElderQuery(usersDataStudents, name);
            studentsTable.setItems(usersDataStudents);


        } catch (SQLException e) {
            e.printStackTrace();
        }
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


    public static class GroupWrapper{

        private SimpleIntegerProperty id;
        private SimpleIntegerProperty value_0;
        private SimpleIntegerProperty value_1;
        private String groupName;

        public GroupWrapper(SimpleIntegerProperty id, SimpleIntegerProperty value_0, SimpleIntegerProperty value_1, String groupName) {
            this.id = id;
            this.value_0 = value_0;
            this.value_1 = value_1;
            this.groupName = groupName;
        }

        public GroupWrapper() {

        }


        public int getId() {
            return id.get();
        }

        public SimpleIntegerProperty idProperty() {
            return id;
        }

        public void setId(int id) {
            this.id.set(id);
        }

        public int getValue_0() {
            return value_0.get();
        }

        public SimpleIntegerProperty value_0Property() {
            return value_0;
        }

        public void setValue_0(int value_0) {
            this.value_0.set(value_0);
        }

        public int getValue_1() {
            return value_1.get();
        }

        public SimpleIntegerProperty value_1Property() {
            return value_1;
        }

        public void setValue_1(int value_1) {
            this.value_1.set(value_1);
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

    }
}
