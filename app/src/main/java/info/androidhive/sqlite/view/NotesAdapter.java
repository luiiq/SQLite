package info.androidhive.sqlite.view;

/**
 * Created by ravi on 20/02/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import info.androidhive.sqlite.R;
import info.androidhive.sqlite.database.model.Note;
import info.androidhive.sqlite.database.model.Student;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context context;
    private List<Student> studentsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView course;
        public TextView studentName;
        public TextView grade;

        public MyViewHolder(View view) {
            super(view);
            course = view.findViewById(R.id.student_course);
            studentName = view.findViewById(R.id.student_dot);
            grade = view.findViewById(R.id.grade);
        }
    }


    public NotesAdapter(Context context, List<Student> studentsList) {
        this.context = context;
        this.studentsList = studentsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Student student = studentsList.get(position);

        holder.course.setText(student.getCourse());


        holder.studentName.setText(student.getName());

        // Display grade
        holder.grade.setText(Integer.toString(student.getGrade()));
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }

}

