package com.velichkomarija.testpuzzle.ui.main

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.children
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
    private val mutableCheckImage = mutableMapOf<String, Drawable>()

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
                    ImageAdapter(
                        context,
                        list as ArrayList<ImageSource>
                    ) { viewModel.handleSelectedItem(it.sourceString) }
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

        image_grid_view.adapter = adapter
    }

    private fun initViewModel() {

        viewModel =
            ViewModelProviders.of(this, MainViewModelFactory(list)).get(MainViewModel::class.java)
        viewModel.getImages()
            .observe(viewLifecycleOwner, Observer {
                if (it.size > mutableCheckImage.size) {
                    for (item in it) {
                        // можно было бы заменить putIfAbsent,
                        // но минимальную  версию необходмо будет поднять до 24
                        if (!mutableCheckImage.containsKey(item.sourceString)) {
                            val drawable = Drawable.createFromStream(
                                context?.assets?.open("img/" + item.sourceString),
                                null
                            )
                            mutableCheckImage[item.sourceString] = drawable
                        }
                    }
                } else {
                    mutableCheckImage.clear()
                    for (item in it) {
                        val drawable = Drawable.createFromStream(
                            context?.assets?.open("img/" + item.sourceString),
                            null
                        )
                        mutableCheckImage[item.sourceString] = drawable
                    }
                }

                val layer = LayerDrawable(mutableCheckImage.values.toTypedArray())
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
                image_grid_view.adapter = adapter
                mutableCheckImage.clear()
                result_image.setImageDrawable(LayerDrawable(mutableCheckImage.values.toTypedArray()))
                viewModel.handleInvalidate()
                true
            }
            R.id.action_select_random -> {
                adapter.selectRandomItem()
                image_grid_view.invalidate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

}
