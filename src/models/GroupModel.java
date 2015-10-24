package models;

public class GroupModel {

    private int groupId;

    private String groupName;

    private int groupYearGraduate;

    private int villageElderId;

    public int getVillageElderId() {
        return villageElderId;
    }

    public void setVillageElderId(int villageElderId) {
        this.villageElderId = villageElderId;
    }

    public int getGroupYearGraduate() {
        return groupYearGraduate;
    }

    public void setGroupYearGraduate(int groupYearGraduate) {
        this.groupYearGraduate = groupYearGraduate;
    }

    public int getGroupId() {

        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {

        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
