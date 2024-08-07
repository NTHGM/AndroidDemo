package com.example.todolist

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity() {
    public lateinit var adapter:ViewAdapter
    lateinit var inputText : EditText
    public lateinit var addBut:Button
    public lateinit var deleteBut:Button
    public lateinit var reloadBut:Button
    lateinit var db:DBHelper
    lateinit var my_spinner:Spinner
    lateinit var numtask:TextView
    lateinit var res:Resources
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = DBHelper(this.applicationContext,null)
        res = resources
        //set up adapter and list
        adapter = ViewAdapter(mutableListOf(), db)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //load data from storage and display on UI
        adapter.reload()

        //regist UI element
        inputText = findViewById(R.id.input)
        addBut = findViewById(R.id.addBut)
        deleteBut = findViewById(R.id.deleteBut)
        reloadBut = findViewById(R.id.refresh)
        numtask = findViewById(R.id.num_task)

        setNumTask()

        //SetupLanguagePicker
        my_spinner = findViewById(R.id.spinner)
        val spinner_adapter = ArrayAdapter.createFromResource(
            this.applicationContext,
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        )

        // Specify the layout to use when the list of choices appears
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        my_spinner.adapter = spinner_adapter

        val currentLocaleName = if (!AppCompatDelegate.getApplicationLocales().isEmpty) {
            // Fetches the current Application Locale from the list
            AppCompatDelegate.getApplicationLocales()[0]?.language
        } else {
            // Fetches the default System Locale
            Locale.getDefault().language
        }

        for(i in 0..my_spinner.count){
            if(my_spinner.getItemAtPosition(i).toString().equals(currentLocaleName)){
                my_spinner.setSelection(i)
                break
            }
        }

        // Set the item selected listener
        my_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                // Get selected item
                val selectedItem = parentView.getItemAtPosition(position).toString()
                val localeList = LocaleListCompat.forLanguageTags(selectedItem)
                AppCompatDelegate.setApplicationLocales(localeList)
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Handle case where no item is selected
            }
        }



        reloadBut.setOnClickListener{
            adapter.reload()
            setNumTask()
        }

        addBut.setOnClickListener{
            val taskName = inputText.text.toString()
            if(!taskName.trim().isEmpty()){
                val task = Task(taskName)
                adapter.addTask(task)
            }
            inputText.text.clear()
            setNumTask()
        }

        deleteBut.setOnClickListener{
            adapter.deleteDoneTasks()
            setNumTask()
        }

    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
    //android has no custom message like ios stringdict
    //it depend on the quantity of each language
    //android plural resources only natively support the quantity of that language
    //so in order to custom message we have
    //example: english only have two types of quantity: one and other
    fun setNumTask(){
        //custom zero quantity
        var x = res.getQuantityString(R.plurals.task_num,adapter.itemCount,adapter.itemCount)
        if(adapter.itemCount==0) x = getString(R.string.zero_task)
        numtask.text = x
    }
}
