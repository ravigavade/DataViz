package com.example.dataviz

import android.net.Uri
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dataviz.databinding.ActivityMainBinding
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle file upload
        val filePickerLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let { handleFile(it) }
            }

        binding.btnUpload.setOnClickListener {
            filePickerLauncher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        }
    }

    private fun handleFile(uri: Uri) {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            inputStream?.use {
                readExcelFile(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readExcelFile(inputStream: InputStream) {
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)

        // Clear previous table rows
        binding.tableLayout.removeAllViews()

        for (row in sheet) {
            val tableRow = TableRow(this)

            for (cell in row) {
                val textView = TextView(this).apply {
                    text = cell.toString()
                    setPadding(16, 16, 16, 16)
                }
                tableRow.addView(textView)
            }

            binding.tableLayout.addView(tableRow)
        }
    }
}
