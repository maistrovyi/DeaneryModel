package util;

import interfaces.DBUpdatable;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import models.GroupModel;

public class DoubleClickEditGroupTable implements EventHandler<MouseEvent> {

    private TableView<GroupModel> groupsTable;
    private DBUpdatable dbUpdatable;

    public DoubleClickEditGroupTable(TableView<GroupModel> groupsTable, DBUpdatable listener) {
        this.groupsTable = groupsTable;
        dbUpdatable = listener;
    }

    @Override
    public void handle(MouseEvent click) {
        @SuppressWarnings("rawtypes")
        TablePosition pos = groupsTable.getSelectionModel().getSelectedCells().get(0);
        if (click.getClickCount() == 2) {
            groupsTable.getSelectionModel().setCellSelectionEnabled(true);
            if (pos.getColumn() == 1) {
                try {
                    TableColumn<GroupModel, String> firstNameCol = pos.getTableColumn();
                    firstNameCol.setCellFactory(TextFieldTableCell.<GroupModel>forTableColumn());
                    firstNameCol.setOnEditCommit(
                            (TableColumn.CellEditEvent<GroupModel, String> t) -> {
                                GroupModel model = ((GroupModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                                model.setGroupName(t.getNewValue());
                                DBConnector.getInstance().updateGroupModel(model);

                                dbUpdatable.onDBUpdated();
                            });
                } catch (ClassCastException exc) {
                    System.out.println("ClassCastException ");
                }
            } else if (pos.getColumn() == 2 || pos.getColumn() == 3) {
                TableColumn<GroupModel, Integer> firstNameCol = pos.getTableColumn();
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
                            new EventHandler<TableColumn.CellEditEvent<GroupModel, Integer>>() {
                                @Override
                                public void handle(TableColumn.CellEditEvent<GroupModel, Integer> tt) {
                                    int row = tt.getTablePosition().getRow();
                                    int column = tt.getTablePosition().getColumn();
                                    GroupModel rowModel = ((GroupModel) tt.getTableView().getItems().get(row));
                                    Integer rowNewValue = tt.getNewValue().intValue();
                                    switch (column) {
                                        case 2:
                                            rowModel.setGroupYearGraduate(rowNewValue);
                                            break;
                                        case 3:
                                            rowModel.setVillageElderId(rowNewValue);
                                            break;
                                    }
                                    DBConnector.getInstance().updateGroupModel(rowModel);

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
