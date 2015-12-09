package util;


import interfaces.DBUpdatable;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import models.StudentModel;

public class DoubleClickEditStudentsTable implements EventHandler<MouseEvent> {

    private TableView<StudentModel> studentsTable;
    private DBUpdatable dbUpdatable;

    public DoubleClickEditStudentsTable(TableView<StudentModel> studentsTable, DBUpdatable listener) {
        this.studentsTable = studentsTable;
        dbUpdatable = listener;
    }

    @Override
    public void handle(MouseEvent click) {
        TablePosition pos = studentsTable.getSelectionModel().getSelectedCells().get(0);
        if (click.getClickCount() == 2) {
            studentsTable.getSelectionModel().setCellSelectionEnabled(true);
            if (pos.getColumn() == 1 || pos.getColumn() == 4) {
                try {
                    TableColumn<StudentModel, String> firstNameCol = pos.getTableColumn();
                    firstNameCol.setCellFactory(TextFieldTableCell.<StudentModel>forTableColumn());
                    firstNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StudentModel, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<StudentModel, String> event) {
                            int row = event.getTablePosition().getRow();
                            int column = event.getTablePosition().getColumn();
                            StudentModel rowModel = ((StudentModel) event.getTableView().getItems().get(row));
                            String rowNewValue = event.getNewValue().toString();
                            switch (column) {
                                case 1:
                                    rowModel.setStudentName(rowNewValue);
                                    break;
                                case 4:
                                    rowModel.setStudentSex(rowNewValue);
                                    break;
                            }
                            DBConnector.getInstance().updateStudentModel(rowModel);

                            dbUpdatable.onDBUpdated();
                        }
                    });
                } catch (ClassCastException exc) {
                    System.out.println("ClassCastException ");
                }
            } else if (pos.getColumn() == 2 || pos.getColumn() == 3) {
                TableColumn<StudentModel, Integer> firstNameCol = pos.getTableColumn();
                firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {

                    @Override
                    public String toString(Integer object) {
                        return object.toString();
                    }

                    @Override
                    public Integer fromString(String string) {
                        return Integer.parseInt(string);
                    }

                }));
                try {
                    firstNameCol.setOnEditCommit(
                            new EventHandler<TableColumn.CellEditEvent<StudentModel, Integer>>() {
                                @Override
                                public void handle(TableColumn.CellEditEvent<StudentModel, Integer> tt) {
                                    int row = tt.getTablePosition().getRow();
                                    int column = tt.getTablePosition().getColumn();
                                    StudentModel rowModel = ((StudentModel) tt.getTableView().getItems().get(row));
                                    Integer rowNewValue = tt.getNewValue().intValue();
                                    switch (column) {
                                        case 2:
                                            rowModel.setStudentGroup(rowNewValue);
                                            break;
                                        case 3:
                                            rowModel.setNumberOfGradebook(rowNewValue);
                                            break;
                                    }
                                    DBConnector.getInstance().updateStudentModel(rowModel);

                                    dbUpdatable.onDBUpdated();
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
