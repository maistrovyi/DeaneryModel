package DBConnect;

import javafx.collections.ObservableList;
import models.GroupModel;
import models.StudentModel;

import java.sql.*;

public class DBClass {


    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/deanery", "root", "");
    }

    public static void groupQuery(ObservableList<GroupModel> dataContinerGroups) throws SQLException, ClassNotFoundException {

        Statement statementGroups = null;
        statementGroups = getConnection().createStatement();
        ResultSet resultGroups = statementGroups.executeQuery("SELECT * FROM groups where groupId > 0");

        while (resultGroups.next()) {
            dataContinerGroups.add(queryResultToGroup(resultGroups));
        }
    }

    public static GroupModel queryResultToGroup(ResultSet resultSet) {
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

    public static void studentsQuery(ObservableList<StudentModel> dataConatinerStudents) throws SQLException, ClassNotFoundException {

        Statement statementStudents = null;
        statementStudents = getConnection().createStatement();
        ResultSet resultStudents = statementStudents.executeQuery("SELECT * FROM students where studentId > 0");

        while (resultStudents.next()) {
            dataConatinerStudents.add(queryResultToStudent(resultStudents));
        }
    }

    public static StudentModel queryResultToStudent(ResultSet resultSet) {
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



