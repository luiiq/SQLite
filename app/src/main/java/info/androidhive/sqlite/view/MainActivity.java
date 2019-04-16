package info.androidhive.sqlite.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.DatabaseHelper;
import info.androidhive.sqlite.database.model.Note;
import info.androidhive.sqlite.database.model.Student;
import info.androidhive.sqlite.utils.MyDividerItemDecoration;
import info.androidhive.sqlite.utils.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity {
    private NotesAdapter mAdapter;
    private List<Student> studentsList = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private TextView noStudentsView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.student_toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = findViewById(R.id.student_coordinator_layout);
        recyclerView = findViewById(R.id.student_recycler_view);
        noStudentsView = findViewById(R.id.empty_students_view);

        db = new DatabaseHelper(this);

        studentsList.addAll(db.getAllStudents());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStudentDialog(false, null, -1);
            }
        });

        mAdapter = new NotesAdapter(this, studentsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyStudents();


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));
    }

    private void createStudent(String name, int grade, String course) {

        long id = db.insertStudent(name, grade, course);


        Student n = db.getStudent(id);

        if (n != null) {

            studentsList.add(0, n);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyStudents();
        }
    }

    private void updateStudent(String name, int grade, String course, int position) {
        Student n = studentsList.get(position);

        n.setName(name);
        n.setGrade(grade);
        n.setCourse(course);

        // updating student in db
        db.updateStudent(n);

        // refreshing the list
        studentsList.set(position, n);
        mAdapter.notifyItemChanged(position);

        toggleEmptyStudents();
    }


    private void deleteStudent(int position) {
        // deleting the student from db
        db.deleteStudent(studentsList.get(position));


        studentsList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyStudents();
    }


    private void showActionsDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showStudentDialog(true, studentsList.get(position), position);
                } else {
                    deleteStudent(position);
                }
            }
        });
        builder.show();
    }



    private void showStudentDialog(final boolean shouldUpdate, final Student student, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText studentName = view.findViewById(R.id.student_name);
        final EditText studentGrade = view.findViewById(R.id.student_grade);
        final EditText studentCourse = view.findViewById(R.id.course);

        TextView dialogTitle = view.findViewById(R.id.student_dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_student_title) : getString(R.string.lbl_edit_student_title));

        if (shouldUpdate && student != null) {
            studentName.setText(student.getName());
            studentCourse.setText(student.getCourse());
            studentGrade.setText(Integer.toString(student.getGrade()));
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (
                        TextUtils.isEmpty(studentName.getText().toString()) &&
                                TextUtils.isEmpty(studentGrade.getText().toString()) &&
                                TextUtils.isEmpty(studentCourse.getText().toString())
                ) {
                    Toast.makeText(MainActivity.this, "Enter student details!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating student
                if (shouldUpdate && student != null) {
                   
                    updateStudent(studentName.getText().toString(),Integer.parseInt(studentGrade.getText().toString()), studentCourse.getText().toString(), position);
                } else {
                    // create new student
                    createStudent(studentName.getText().toString(),Integer.parseInt(studentGrade.getText().toString()), studentCourse.getText().toString());
                }
            }
        });
    }


    private void toggleEmptyStudents() {


        if (db.getStudentsCount() > 0) {
            noStudentsView.setVisibility(View.GONE);
        } else {
            noStudentsView.setVisibility(View.VISIBLE);
        }
    }
}
