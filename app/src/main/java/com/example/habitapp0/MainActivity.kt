package com.example.habitapp0

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Habit(
    val name: String,
    val description: String,
    val frequency: String
)

class MainActivity : AppCompatActivity() {

    // RecyclerView and Adapter setup
    private lateinit var habitRecyclerView: RecyclerView
    private lateinit var habitAdapter: HabitAdapter
    private val habitList = mutableListOf(
        Habit("Exercise", "Morning jog", "Daily"),
        Habit("Reading", "Read a book", "Everyday"),
        Habit("Drink Water", "Drink 8 glasses of water", "Daily")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a vertical linear layout for the whole activity
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setPadding(16, 16, 16, 16)
        }

        // Create a RecyclerView programmatically
        habitRecyclerView = RecyclerView(this).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Create an "Add Habit" button
        val addButton = Button(this).apply {
            text = "Add Habit"
            setOnClickListener {
                showAddHabitDialog()
            }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // Add the button and RecyclerView to the main layout
        mainLayout.addView(addButton)
        mainLayout.addView(habitRecyclerView)

        // Set the content view to the main layout
        setContentView(mainLayout)

        // Initialize the adapter with the sample data
        habitAdapter = HabitAdapter(habitList) { habit ->
            removeHabit(habit)
        }
        habitRecyclerView.adapter = habitAdapter
    }

    // Show dialog to add a new habit
    private fun showAddHabitDialog() {
        val dialogLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 16, 16, 16)
        }

        val nameInput = EditText(this).apply {
            hint = "Habit Name"
        }
        val descriptionInput = EditText(this).apply {
            hint = "Description"
        }
        val frequencyInput = EditText(this).apply {
            hint = "Frequency"
        }

        val saveButton = Button(this).apply {
            text = "Save Habit"
            setOnClickListener {
                val name = nameInput.text.toString()
                val description = descriptionInput.text.toString()
                val frequency = frequencyInput.text.toString()

                if (name.isNotBlank() && description.isNotBlank() && frequency.isNotBlank()) {
                    val newHabit = Habit(name, description, frequency)
                    habitList.add(newHabit)
                    habitAdapter.notifyItemInserted(habitList.size - 1)
                }
            }
        }

        dialogLayout.addView(nameInput)
        dialogLayout.addView(descriptionInput)
        dialogLayout.addView(frequencyInput)
        dialogLayout.addView(saveButton)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Habit")
            .setView(dialogLayout)
            .create()

        dialog.show()
    }

    // Remove habit from the list
    private fun removeHabit(habit: Habit) {
        habitList.remove(habit)
        habitAdapter.notifyDataSetChanged()
    }

    // Adapter class to handle displaying the list of habits
    class HabitAdapter(
        private val habitList: MutableList<Habit>,
        private val onHabitClick: (Habit) -> Unit
    ) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
            // Create a LinearLayout for each item in the list
            val habitLayout = LinearLayout(parent.context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(16, 16, 16, 16)
            }

            // Create TextViews for the habit's name, description, and frequency
            val nameTextView = TextView(parent.context).apply {
                textSize = 18f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            val descriptionTextView = TextView(parent.context).apply {
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            val frequencyTextView = TextView(parent.context).apply {
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }

            val deleteButton = Button(parent.context).apply {
                text = "Delete"
            }

            // Add TextViews and delete button to the layout
            habitLayout.addView(nameTextView)
            habitLayout.addView(descriptionTextView)
            habitLayout.addView(frequencyTextView)
            habitLayout.addView(deleteButton)

            val holder = HabitViewHolder(habitLayout, nameTextView, descriptionTextView, frequencyTextView)

            // Set click listener for delete button
            deleteButton.setOnClickListener {
                val position = holder.bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onHabitClick(habitList[position])
                }
            }

            return holder
        }

        override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
            // Get the habit at the current position
            val habit = habitList[position]

            // Set the text for each TextView
            holder.nameTextView.text = habit.name
            holder.descriptionTextView.text = habit.description
            holder.frequencyTextView.text = habit.frequency
        }

        override fun getItemCount(): Int {
            return habitList.size
        }

        // ViewHolder class to hold references to the views
        class HabitViewHolder(
            itemView: ViewGroup,
            val nameTextView: TextView,
            val descriptionTextView: TextView,
            val frequencyTextView: TextView
        ) : RecyclerView.ViewHolder(itemView)
    }

}
