package ru.irinavb.todolist.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.irinavb.todolist.R
import ru.irinavb.todolist.data.models.ToDoData
import ru.irinavb.todolist.databinding.FragmentAddBinding
import ru.irinavb.todolist.viewmodels.SharedViewModel
import ru.irinavb.todolist.viewmodels.ToDoViewModel


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.prioritiesSpinner.onItemSelectedListener = sharedViewModel.listener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = binding.titleEditText.text.toString()
        val mPriority = binding.prioritiesSpinner.selectedItem.toString()
        val mDescription = binding.descriptionEditText.text.toString()

        // Hide the keyboard.
//        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view?.windowToken, 0)

        val validation = sharedViewModel.verifyDataFromUser(mTitle, mDescription)

        if(validation) {
            val newData = ToDoData (
                0,
                mTitle,
                sharedViewModel.parsePriority(mPriority),
                mDescription
            )
            toDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}











