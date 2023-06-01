package com.example.todo_app

import CategoryEntity
import CategoryFragItemTouchHelperCallback
import com.example.todo_app.dialog.NewCategoryDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_app.database.ObjectBox
import com.example.todo_app.recyclerview.Adapters.CategoryFragmentCategoryAdapter
import com.google.android.material.imageview.ShapeableImageView

class ListCategories : Fragment() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var mCategoryNewButton: ShapeableImageView
    private lateinit var categoryListAdapter : CategoryFragmentCategoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_list_categories, container, false)
        mCategoryNewButton = v?.findViewById(R.id.button_add_category) as ShapeableImageView
        //val floating_button = v?.findViewById<FloatingActionButton>(R.id.floating_button)
        recyclerView = v.findViewById<RecyclerView>(R.id.RecyclerView_category_view)
        //Databáze
        val categoryEntity = ObjectBox.store.boxFor(CategoryEntity::class.java)
        //CategoryAdapter
        categoryListAdapter = CategoryFragmentCategoryAdapter(emptyList(), categoryEntity )
        //val ent: CategoryEntity = CategoryEntity()
        //ent.description = "KATEGORIE OSOBNÍ RŮST"
        //categoryEntity.put(ent)
        // Vytvoříme instanci adaptéru s prázdným seznamem


        // Přidáme položky do seznamu
        val tasks = categoryEntity.all
            .sortedBy { it.position }

        categoryListAdapter.setData(tasks)

        // Předáme instanci adaptéru k RecyclerView
        recyclerView.adapter = categoryListAdapter

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager

        // Create the ItemTouchHelper and attach it to the RecyclerView
        val callback = CategoryFragItemTouchHelperCallback(categoryListAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        mCategoryNewButton.setOnClickListener({
            val dialog = NewCategoryDialog()
            dialog.setAdapterRecyc(categoryListAdapter)
            dialog.setRecyclerView(recyclerView)
            dialog.show(childFragmentManager, "com.example.todo_app.dialog.NewCategoryDialog")
            // Get the Dialog object from the DialogFragment and set the OnDismissListener on it
            dialog.dialog?.setOnDismissListener {
                // Call your refreshRecyclerView() method here to update the RecyclerView
                refreshRecyclerView()
            }

        })


        /*floating_button?.setOnClickListener{
            val secondFragment = TaskNew()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, secondFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }*/


        return v
    }

    fun refreshRecyclerView() {
        // Refresh your data here
        // Update your RecyclerView adapter
        // For example:
        categoryListAdapter.notifyDataSetChanged()
    }

    fun scrollRecyclerViewToPosition(position: Int) {
        recyclerView.scrollToPosition(position)
        Toast.makeText(context, "Hello World!", Toast.LENGTH_SHORT).show()

    }
}