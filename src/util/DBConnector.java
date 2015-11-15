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


    public void groupQuery(ObservableList<GroupModel> dataContinerGroups) throws SQLException, ClassNotFoundException {

        Statement statementGroups = connection.createStatement();
        ResultSet resultGroups = statementGroups.executeQuery("SELECT * FROM groups");

        while (resultGroups.next()) {
            dataContinerGroups.add(queryResultToGroup(resultGroups));
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

    public void studentsQuery(ObservableList<StudentModel> dataConatinerStudents, int groupId) throws SQLException, ClassNotFoundException {

        Statement statementStudents = connection.createStatement();
        ResultSet resultStudents = statementStudents.executeQuery("SELECT studentName, studentId, numberOfGradebook, studentGroup, studentSex FROM students WHERE students.studentGroup = " + groupId);

        while (resultStudents.next()) {
            dataConatinerStudents.add(queryResultToStudent(resultStudents));
        }
    }

    public void studentsOfGroupByVillageElderNameQuery(ObservableList<StudentModel> dataContainerStudents, String name) throws SQLException {
        Statement statementVillageElder = connection.createStatement();
        ResultSet resultVillageElder = statementVillageElder.executeQuery("SELECT studentName, studentId, numberOfGradebook, studentGroup, studentSex\n" +
                "FROM students\n" +
                "WHERE studentGroup =\n" +
                "        (SELECT groupId FROM groups WHERE villageElderId =\n" +
                "                                        (SELECT studentId FROM students WHERE studentName = \"" + name + "\"))");

        while (resultVillageElder.next()) {
            dataContainerStudents.add(queryResultToStudent(resultVillageElder));
        }
    }

    public void getGroupIdByVillageElderName(ObservableList<GroupModel> dataContainerGroups, String name) throws SQLException {
        Statement statementVillageElder = connection.createStatement();
        ResultSet resultVillageElder = statementVillageElder.executeQuery("SELECT * FROM groups WHERE villageElderId =\n" +
                "                                        (SELECT studentId FROM students WHERE studentName = \"" + name + "\")");

        while (resultVillageElder.next()) {
            dataContainerGroups.add(queryResultToGroup(resultVillageElder));
        }
    }

    public void groupNameQuery(ObservableList<GroupModel> dataContinerGroups, String group) throws SQLException {
        Statement statementGroupName = connection.createStatement();
        ResultSet resultGroupName = statementGroupName.executeQuery("SELECT * FROM groups WHERE groupName = \"" + group +"\"");

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

}