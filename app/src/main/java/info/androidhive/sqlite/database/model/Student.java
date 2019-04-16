package info.androidhive.sqlite.database.model;

public class Student {
    public static final String TABLE_NAME = "student_waiting_list";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_GRADE = "grade";
    // Course for which the course is waiting for
    public static final String COL_COURSE = "course";


    private int id;
    private String name;
    private int grade;
    private String course;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_NAME + " TEXT, "
                    + COL_GRADE + " INTEGER, "
                    + COL_COURSE + " TEXT"
                    + ")";


    public Student() {
    }

    public Student(int id, String name, int grade, String course) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}






