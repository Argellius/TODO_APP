package com.example.todo_app

import CategoryEntity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.todo_app.database.ObjectBox
import com.example.todo_app.databinding.ActivityMainBinding
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor

class MainActivity : AppCompatActivity() {


    private lateinit var categoryBox: Box<CategoryEntity>
    private lateinit var boxStore: BoxStore
    private lateinit var binding : ActivityMainBinding
    private lateinit var spinner : Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ObjectBox.init(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Need_to_do_task())


        // Inicializace ObjectBox
        categoryBox = ObjectBox.store.boxFor(CategoryEntity::class.java)

        // Zkontrolujte, zda databáze obsahuje záznamy kategorie
        if (categoryBox.count() == 0L) {
            // Založení kategorie při prvním spuštění
            val category = CategoryEntity()
            category.description = getString(R.string.unknown)

            // Uložení kategorie do databáze
            categoryBox.put(category)
        }



        binding.bottomNavigationView.setOnItemSelectedListener{
            when (it.itemId) {

                R.id.home -> replaceFragment(Need_to_do_task())
                R.id.home2 -> replaceFragment(Task_view())
                R.id.settings -> replaceFragment(Settings())

                else -> {

                }

            }
            true
        }
    }

    private fun replaceFragment (fragment : Fragment)
    {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
        fragmentTransaction.commit()

    }

}