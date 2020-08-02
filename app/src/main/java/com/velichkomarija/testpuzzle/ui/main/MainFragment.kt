package com.velichkomarija.testpuzzle.ui.main

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.GridView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.velichkomarija.testpuzzle.ImageSource
import com.velichkomarija.testpuzzle.MainViewModelFactory
import com.velichkomarija.testpuzzle.R
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.io.IOException


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var adapter: ImageAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var list: List<ImageSource>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            val listAssets = context.assets.list("img")

            if (listAssets != null) {
                list = ArrayList(listAssets.size)
                for (item in listAssets) {
                    (list as ArrayList<ImageSource>).add(ImageSource(item))
                }
                adapter =
                    ImageAdapter(context, list) { viewModel.handleSelectedItem(it.sourceString) }
            }
        } catch (e: IOException) {
            Log.e("MainFragment", "IO Exception")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottom_sheet)
        sheetBehavior.isFitToContents = false
        sheetBehavior.halfExpandedRatio = 0.4f

        val bottomSheet = view.findViewById<LinearLayout>(R.id.bottom_sheet)
        val grid = bottomSheet.findViewById<GridView>(R.id.image_grid_view)
        grid.adapter = adapter
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, MainViewModelFactory(list)).get(MainViewModel::class.java)
        viewModel.getImages().observe(viewLifecycleOwner, Observer {

            val mutableCheckImage = mutableListOf<Drawable>()

            for (item in it) {
                val drawable = Drawable.createFromStream(
                    context?.assets?.open("img/" + item.sourceString),
                    null
                )
                mutableCheckImage.add(drawable)
            }

            val layer = LayerDrawable(mutableCheckImage.toTypedArray())
            result_image.setImageDrawable(layer)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_send -> {
                true
            }
            R.id.action_reset -> {
                result_image.invalidate()
                true
            }
            R.id.action_select_random -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}
