package ru.irinavb.todolist.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import ru.irinavb.todolist.R
import ru.irinavb.todolist.databinding.FragmentListBinding


import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import ru.irinavb.todolist.data.models.ToDoData
import ru.irinavb.todolist.fragments.list.adapters.ToDoAdapter
import ru.irinavb.todolist.viewmodels.SharedViewModel
import ru.irinavb.todolist.viewmodels.ToDoViewModel


class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!


    private lateinit var navHostFragment: NavHostFragment
    private val adapter: ToDoAdapter by lazy { ToDoAdapter() }
    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        setUpRecyclerView()

        toDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            sharedViewModel.checkIfDatabaseEmpty(it)
            adapter.setData(it)
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager
            .VERTICAL)
        binding.recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        swipeToDelete(binding.recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                toDoViewModel.deleteData(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreDeletedData(viewHolder.itemView, deletedItem, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData, position: Int) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.title}", Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            toDoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> toDoViewModel.sortByHighPriority.observe(this, {
                adapter.setData(it)
            })
            R.id.menu_priority_low -> toDoViewModel.sortByLowPriority.observe(this, {
                adapter.setData(it)
            })
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_, _ ->
            toDoViewModel.deleteAll()
            Toast.makeText(activity, "Successfully removed everything!", Toast
                .LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") {_, _ ->}
        builder.setTitle("Delete All the Items?")
        builder.setTitle("Are you sure you want to delete all the items?")
        builder.create().show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        toDoViewModel.searchDatabase(searchQuery).observe(this, Observer {
            it?.let {
                adapter.setData(it)
            }
        })
    }
}