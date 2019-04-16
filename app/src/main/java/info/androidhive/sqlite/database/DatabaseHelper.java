package info.androidhive.sqlite.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.database.model.Note;
import info.androidhive.sqlite.database.model.Student;

/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "student_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_NAME);
        // create students table
        db.execSQL(Student.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Student.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertStudent(String name, int grade, String course) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `grade` will be inserted automatically.
        // no need to add them
        values.put(Student.COL_NAME, name);
        values.put(Student.COL_GRADE, grade);
        values.put(Student.COL_COURSE, course);

        // insert row
        long id = db.insert(Student.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Student getStudent(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Student.TABLE_NAME,
                new String[]{Student.COL_ID, Student.COL_NAME, Student.COL_GRADE, Student.COL_COURSE},
                Student.COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare course object
        Student student = new Student(
                cursor.getInt(cursor.getColumnIndex(Student.COL_ID)),
                cursor.getString(cursor.getColumnIndex(Student.COL_NAME)),
                cursor.getInt(cursor.getColumnIndex(Student.COL_GRADE)),
                cursor.getString(cursor.getColumnIndex(Student.COL_COURSE)));

        // close the db connection
        cursor.close();

        return student;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Student.TABLE_NAME + " ORDER BY " +
                Student.COL_GRADE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setId(cursor.getInt(cursor.getColumnIndex(Student.COL_ID)));
                student.setName(cursor.getString(cursor.getColumnIndex(Student.COL_NAME)));
                student.setGrade(cursor.getInt(cursor.getColumnIndex(Student.COL_GRADE)));
                student.setCourse(cursor.getString(cursor.getColumnIndex(Student.COL_COURSE)));

                students.add(student);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return students list
        return students;
    }

    public int getStudentsCount() {
        String countQuery = "SELECT  * FROM " + Student.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        return count;
    }

    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Student.COL_NAME, student.getName());
        values.put(Student.COL_GRADE, student.getGrade());
        values.put(Student.COL_COURSE, student.getCourse());

        // updating row
        return db.update(Student.TABLE_NAME, values, Student.COL_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
    }

    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Student.TABLE_NAME, Student.COL_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
        db.close();
    }
}