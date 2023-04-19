package com.example.todo_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.todo_app.database.ObjectBox
import com.example.todo_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ObjectBox.init(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        replaceFragment(Need_to_do_task())

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