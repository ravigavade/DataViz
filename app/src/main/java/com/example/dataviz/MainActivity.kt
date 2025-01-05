package com.example.dataviz

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.dataviz.databinding.ActivityMainBinding
import com.opencsv.CSVReader
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var excelData = mutableListOf<List<String>>() // Store data temporarily

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
            filePickerLauncher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")  // Allow any file type (or specify "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" and "text/csv")
        }

    }

    private fun handleFile(uri: Uri) {
        val fileType = contentResolver.getType(uri) // Check the file type (MIME type)
        if (fileType == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") {
            // If Excel file
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(uri)
                inputStream?.use {
                    readExcelFile(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this,"wrong file type",Toast.LENGTH_LONG).show()
        }
    }

    private fun readExcelFile(inputStream: InputStream) {
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)

        // Clear previous table rows
        binding.tableLayout.removeAllViews()

        // Iterate over the sheet and add rows dynamically to TableLayout
        excelData.clear() // Reset data list
        for (row in sheet) {
            val rowData = mutableListOf<String>()
            val tableRow = TableRow(this)

            for (cell in row) {
                val textView = TextView(this).apply {
                    text = cell.toString()
                    setPadding(16, 16, 16, 16)  // Add padding for better spacing
                    setBackgroundResource(R.drawable.cell_border) // Gives cell-like appearance
                }
                rowData.add(cell.toString()) // Add cell data to rowData
                tableRow.addView(textView)
            }

            excelData.add(rowData) // Add the row data to excelData list
            binding.tableLayout.addView(tableRow)
        }
    }

}
