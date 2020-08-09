package com.velichkomarija.testpuzzle.main.ui

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.velichkomarija.testpuzzle.R
import com.velichkomarija.testpuzzle.main.ui.viewmodel.MainViewModel
import com.velichkomarija.testpuzzle.main.utils.Render
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * Главный фрагмент приложения.
 */
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        const val IMG_PATH = "/images"
        const val IMG_EXTENSION = ".jpg"
        const val IMG_PREFIX = "temp_"
        const val DATA_TYPE = "image/jpeg"
    }

    private lateinit var viewModel: MainViewModel

    /**
     * Реализация базового метода onCreate.
     *
     * @param savedInstanceState объект типа Bundle для сохранения состояния.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /**
     * Реализация базового метода onActivityCreated.
     * Инициализация ViewModel.
     *
     * @param savedInstanceState объект типа Bundle для сохранения состояния.
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel(resources.configuration.orientation)
    }

    /**
     * Реализация базового метода onCreateView.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    /**
     * Метод, инициализирующий объект viewModel.
     * Настраивает вид адаптера, исходя из значения @param configuration.
     */
    @Suppress("DEPRECATION")
    private fun initViewModel(configuration: Int) {

        viewModel =
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getImages()
            .observe(viewLifecycleOwner, Observer {
                result_image.setImageBitmap(Render.loadBitmapFromAssets(it))
            })

        image_grid_view.adapter = viewModel.getAdapter()

        var columnCount = 3
        if (configuration == Configuration.ORIENTATION_LANDSCAPE) {
            columnCount = 6
        }

        image_grid_view.layoutManager = GridLayoutManager(context, columnCount)
    }

    /**
     * Метод, составляющий картинку и преобразующий ее для дальнейшей отправки.
     * Реализован механизм выбора отправки в разные приложения,
     * которые поддерживают работу с изображениями.
     */
    private fun sendImage() {

        val bitmap = Render.getBitmap()
        val outputDir = File(context!!.cacheDir.absolutePath.toString() + IMG_PATH)

        if (!File(outputDir.absolutePath).exists()) {
            val file = File(outputDir.absolutePath)
            file.mkdir()
        }

        var outputFile: File? = null
        var fileOutputStream: FileOutputStream?

        try {
            outputFile = File.createTempFile(
                IMG_PREFIX,
                IMG_EXTENSION, outputDir
            )
            fileOutputStream = FileOutputStream(outputFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val shareIntent = Intent()
        shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        shareIntent.action = Intent.ACTION_SEND
        val outputUri: Uri =
            FileProvider.getUriForFile(
                context!!,
                resources.getString(R.string.package_name),
                outputFile!!
            )
        shareIntent.putExtra(Intent.EXTRA_STREAM, outputUri)
        shareIntent.setDataAndType(
            outputUri,
            DATA_TYPE
        )

        startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.send_via)))
    }

    /**
     * Реализация метода для инициализации контекстного меню.
     *
     * @param menu объект типа Menu.
     * @param inflater объект типа MenuInflater.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Метод-обработчик выбора элемента контекстного меню.
     *
     * @param item выбранный элемент меню.
     * @return булева переменнная, обозначающая соверщение нажатия на элемент.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_send -> {
                sendImage()
                true
            }
            R.id.action_reset -> {
                viewModel.handleInvalidate()
                true
            }
            R.id.action_select_random -> {
                viewModel.handleSelectRandomItem()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
