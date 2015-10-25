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

//    public static Connection getConnection() throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.jdbc.Driver");
//        return DriverManager.getConnection("jdbc:mysql://localhost:3306/deanery", "root", "");
//    }

    public void groupQuery(ObservableList<GroupModel> dataContinerGroups) throws SQLException, ClassNotFoundException {

        Statement statementGroups = connection.createStatement();
        ResultSet resultGroups = statementGroups.executeQuery("SELECT * FROM groups where groupId > 0");

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

    public void studentsQuery(ObservableList<StudentModel> dataConatinerStudents, int groupID) throws SQLException, ClassNotFoundException {

        Statement statementStudents = connection.createStatement();
        ResultSet resultStudents = statementStudents.executeQuery("SELECT * FROM students WHERE students.studentGroup = " + groupID);

        while (resultStudents.next()) {
            dataConatinerStudents.add(queryResultToStudent(resultStudents));
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



