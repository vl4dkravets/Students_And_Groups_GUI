package logic;

public class Group {

    private int groupId;
    private String groupName;
    private String professorName;
    private String specialityField;

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String curator) {
        this.professorName = curator;
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

    public void setGroupName(String nameGroup) {
        this.groupName = nameGroup;
    }

    public String getSpecialityField() {
        return specialityField;
    }

    public void setSpecialityField(String speciality) {
        this.specialityField = speciality;
    }

    public String toString() {
        return groupName;
    }
}