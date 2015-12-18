package util;

import javafx.collections.ObservableList;
import models.GroupModel;
import models.StudentModel;
import java.sql.*;

public class DBConnector {

    private static DBConnector instance;

    private Connection connection;

    public static DBConnector getInstance() {
        if (instance == null) {
            instance = new DBConnector();
        }
        return instance;
    }

    DBConnector() {
        try {
            openConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/deanery", "root", "");
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResultSet(DBQueriesType dbQueriesType) throws SQLException {
        Statement statementGroups = connection.createStatement();
        return statementGroups.executeQuery(dbQueriesType.getQueryBody());
    }

    public ResultSet getResultSet(DBQueriesType dbQueriesType, String query, String end) throws SQLException {
        if (end == null) {
            end = "";
        }
        Statement statementGroups = connection.createStatement();
        return statementGroups.executeQuery(dbQueriesType.getQueryBody() + query + end);
    }

    public void groupQuery(ObservableList<GroupModel> dataContainerGroups) throws SQLException, ClassNotFoundException {
        ResultSet resultGroups = getResultSet(DBQueriesType.SELECT_GROUPS);

        while (resultGroups.next()) {
            dataContainerGroups.add(queryResultToGroup(resultGroups));
        }
    }

    public void namesOfGroupsQuery(ObservableList<String> dataGroupComboBox)throws SQLException,
            ClassNotFoundException {
        ResultSet resultGroups = getResultSet(DBQueriesType.SELECT_GROUP_NAMES);

        while (resultGroups.next()) {
            dataGroupComboBox.add(resultGroups.getString("groupName"));
        }
    }

    public void namesOfEldersQuery(ObservableList<String> dataElderComboBox) throws SQLException,
            ClassNotFoundException {
        ResultSet resultElder = getResultSet(DBQueriesType.SELECT_ELDERS_INFO);

        while (resultElder.next()) {
            dataElderComboBox.add(resultElder.getString("studentName"));
        }
    }

    public GroupModel queryResultToGroup(ResultSet resultSet) {
        GroupModel groupModel = new GroupModel();
        try {
            groupModel.setGroupId(resultSet.getInt("groupId"));
            groupModel.setGroupName(resultSet.getString("groupName"));
            groupModel.setGroupYearGraduate(resultSet.getInt("groupYearGraduate"));
            groupModel.setVillageElderId(resultSet.getInt("villageElderId"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupModel;
    }

    public void studentsQuery(ObservableList<StudentModel> dataContainerStudents, int groupId)
            throws SQLException, ClassNotFoundException {
        ResultSet resultStudents = getResultSet(DBQueriesType.SELECT_STUDENTS_INFO, String.valueOf(groupId), null);

        while (resultStudents.next()) {
            dataContainerStudents.add(queryResultToStudent(resultStudents));
        }
    }

    public void studentsOfGroupByVillageElderNameQuery(ObservableList<StudentModel> dataContainerStudents, String name)
            throws SQLException {
        ResultSet resultVillageElder = getResultSet(DBQueriesType.SELECT_STUDENTS_INFO_BY_ELDER,
                String.valueOf(name), "\"))");

        while (resultVillageElder.next()) {
            dataContainerStudents.add(queryResultToStudent(resultVillageElder));
        }
    }

    public void getGroupIdByVillageElderName(ObservableList<GroupModel> dataContainerGroups, String name)
            throws SQLException {
        ResultSet resultVillageElder = getResultSet(DBQueriesType.SELECT_GROUPS_INFO_BY_ELDER, name, "\")");

        while (resultVillageElder.next()) {
            dataContainerGroups.add(queryResultToGroup(resultVillageElder));
        }
    }

    public void groupNameQuery(ObservableList<GroupModel> dataContinerGroups, String group) throws SQLException {
        ResultSet resultGroupName = getResultSet(DBQueriesType.SELECT_GROUPS_INFO_BY_GROUP_NAME, group, "\"");

        while (resultGroupName.next()) {
            dataContinerGroups.add(queryResultToGroup(resultGroupName));
        }
    }

    public StudentModel queryResultToStudent(ResultSet resultSet) {
        StudentModel studentModel = new StudentModel();
        try {
            studentModel.setStudentId(resultSet.getInt("studentId"));
            studentModel.setStudentName(resultSet.getString("studentName"));
            studentModel.setStudentGroup(resultSet.getInt("studentGroup"));
            studentModel.setNumberOfGradebook(resultSet.getInt("numberOfGradebook"));
            studentModel.setStudentSex(resultSet.getString("studentSex"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return studentModel;
    }

    public void updateGroupModel(GroupModel rowModel) {
        try {
            Statement statementGroups = connection.createStatement();
            statementGroups.executeUpdate("UPDATE groups SET groupName ='" + rowModel.getGroupName() +
                    "', groupYearGraduate ='" + rowModel.getGroupYearGraduate() + "', villageElderId ='"
                    + rowModel.getVillageElderId() + "' WHERE groupId ='" + rowModel.getGroupId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStudentModel(StudentModel rowModel) {
        try {
            Statement statementGroups = connection.createStatement();
            statementGroups.executeUpdate("UPDATE students SET studentName ='" + rowModel.getStudentName()
                    + "', numberOfGradebook ='" + rowModel.getNumberOfGradebook() + "', studentGroup ='"
                    + rowModel.getStudentGroup() + "', studentSex ='" + rowModel.getStudentSex()
                    + "' WHERE studentId ='" + rowModel.getStudentId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}