package util;


public enum DBQueriesType {

    SELECT_GROUP_NAMES("SELECT groupName FROM groups"),

    SELECT_GROUPS("SELECT * FROM groups"),

    SELECT_STUDENTS_INFO("SELECT studentName, studentId, numberOfGradebook, studentGroup, studentSex FROM students WHERE students.studentGroup = "),

    SELECT_STUDENTS_INFO_BY_ELDER("SELECT studentName, studentId, numberOfGradebook, studentGroup, studentSex\n" +
            "FROM students\n" +
            "WHERE studentGroup =\n" +
            "        (SELECT groupId FROM groups WHERE villageElderId =\n" +
            "                                        (SELECT studentId FROM students WHERE studentName = \""),

    SELECT_GROUPS_INFO_BY_ELDER("SELECT * FROM groups WHERE villageElderId =\n" +
            "                                        (SELECT studentId FROM students WHERE studentName = \""),

    SELECT_GROUPS_INFO_BY_GROUP_NAME("SELECT * FROM groups WHERE groupName = \""),

//    UPDATE_GROUPS_INFO_BY_GROUP_ID(),

    SELECT_ELDERS_INFO("SELECT studentName\n" +
            "FROM students\n" +
            "WHERE studentId >= 4 AND studentId <= 5");

    String queryBody;

    DBQueriesType(String queryBodyValue) {
        queryBody = queryBodyValue;
    }

    public String getQueryBody() {
        return queryBody;
    }
}
