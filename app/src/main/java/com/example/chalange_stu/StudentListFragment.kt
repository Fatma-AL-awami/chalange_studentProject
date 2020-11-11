package com.example.chalange_stu

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentListFragment : Fragment(),inputDailogStudent.Callbacks  {


    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var bt_deleted: Button

    private lateinit var newStudentButton: ImageButton
    private lateinit var emptyTextView: TextView


    private val studentViewModel by lazy {
        ViewModelProviders.of(this).get(StudentViewModel::class.java)
    }

    override fun addnewstudent(student: Student) {

        studentViewModel.addnewstudent(student)
       updateUI()
    }

    private var adapter: StudentAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_student_list, container, false)
        studentRecyclerView = view.findViewById(R.id.Student_recycler_view) as RecyclerView
        studentRecyclerView.layoutManager = LinearLayoutManager(context)
        newStudentButton = view.findViewById(R.id.newStudent) as ImageButton
        emptyTextView = view.findViewById(R.id.empty_view) as TextView
        newStudentButton.setOnClickListener {
            val student = Student()
            inputDailogStudent().apply{
                setTargetFragment(this@StudentListFragment,0)
                show(this@StudentListFragment.requireFragmentManager(),"Input")

            }}
        updateUI()

        return view
    }

    private inner class StudentHolder(view: View) : RecyclerView.ViewHolder(view) {
        val st_number: TextView = itemView.findViewById(R.id.Student_number_tv)
        val st_name: TextView = itemView.findViewById(R.id.Student_name_tv)
        val st_passed: TextView = itemView.findViewById(R.id.Student_isPassed_tv)
        val bt_del: ImageButton = itemView.findViewById(R.id.bt_deleted)

        private lateinit var student: Student
        fun bind(item: Student) {

            this.student = item
            st_number.text = this.student.number.toString()
            st_name.text = this.student.name
            if (this.student.passed)
                st_passed.text = "true "
            else
                st_passed.text = "false "

        }


        init {
            bt_del.setOnClickListener {
                onStudentDeleted(adapterPosition)
            }
        }
    }

    private inner class StudentAdapter(var students: List<Student>) :
        RecyclerView.Adapter<StudentHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentHolder {
            val view = layoutInflater.inflate(R.layout.student_list, parent, false)
            return StudentHolder(view)
        }

        override fun onBindViewHolder(holder: StudentHolder, position: Int) {
            val student = students[position]
            holder.bind(student)


        }

        override fun getItemCount(): Int {
            //return studentViewModel.Students.size
            if(students.isEmpty()){
                emptyTextView.visibility=View.VISIBLE
                newStudentButton.visibility=View.VISIBLE
            }else{
                emptyTextView.visibility=View.GONE
                newStudentButton.visibility=View.GONE
            }

            return students.size
        }


    }
    private fun updateUI() {
        val students =
            studentViewModel.Students
        adapter = StudentAdapter(students)
        studentRecyclerView.adapter = adapter
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            StudentListFragment()

    }

    fun onStudentDeleted(item : Int){
        studentViewModel.deleted(item)
        updateUI()
    }
}