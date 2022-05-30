package com.example.atividade02

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.atividade02.entities.*
import java.lang.Exception

class FirstActivity: AppCompatActivity() {

  private var nextDisciplineCode = 0;

  private val contractLauncher = registerForActivityResult(MyContract()) { response: Response? ->
    if (response == null) {
      return@registerForActivityResult
    }

    val discipline: Discipline = response.discipline

    val itemList = findViewById<RecyclerView>(R.id.itemList)
    val adapter = itemList.adapter

    if (adapter !is ItemAdapter) return@registerForActivityResult

    if (response.type == ResponseType.UPDATED) {
      adapter.setItem(discipline)
      Toast.makeText(this, "Disciplina atualizada com sucesso!!", Toast.LENGTH_SHORT).show()
      return@registerForActivityResult
    }

    adapter.addItem(discipline)
    itemList.scrollToPosition(adapter.itemCount - 1)
    ++nextDisciplineCode

    Toast.makeText(this, "Disciplina cadastrada com sucesso!!", Toast.LENGTH_SHORT).show()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.first_activity)

    setUpRecyclerView()
    setUpListeners()
  }

  private fun setUpRecyclerView() {
    val itemList = findViewById<RecyclerView>(R.id.itemList)
    itemList.layoutManager = LinearLayoutManager(this)
    itemList.adapter = ItemAdapter()
  }

  private fun performEditDiscipline(view: View) {
    val buttonNewDiscipline = findViewById<Button>(R.id.buttonNewDiscipline)
    val buttonEditDiscipline = findViewById<Button>(R.id.buttonEditDiscipline)
    val textDisciplineCode = findViewById<TextView>(R.id.textDisciplineCode)

    val textSelectedCode: String = textDisciplineCode.text.toString()

    if (textSelectedCode.isEmpty()) {
      Toast.makeText(this, "Código da disciplina não pode estar vazio!", Toast.LENGTH_SHORT).show()
      return
    }

    var selectedCode: Int = -1
    try {
      if (textSelectedCode.isEmpty()) {
        throw NumberFormatException()
      }
      selectedCode = textSelectedCode.toInt()
      if ((selectedCode < 0) || (selectedCode >= nextDisciplineCode)) {
        throw Exception()
      }

      val itemList = findViewById<RecyclerView>(R.id.itemList)
      val adapter = itemList.adapter

      if (adapter !is ItemAdapter) return

      textDisciplineCode.text = ""
      contractLauncher.launch(Request(RequestType.UPDATE, adapter.getItem(selectedCode)))
    } catch (nfe: NumberFormatException) {
      Toast.makeText(this, "Código inválido!", Toast.LENGTH_SHORT).show()
    } catch (error: Exception) {
      Toast.makeText(this, "Código não existente!", Toast.LENGTH_SHORT).show()
    }
  }

  private fun setUpListeners() {
    val buttonNewDiscipline = findViewById<Button>(R.id.buttonNewDiscipline)
    val buttonEditDiscipline = findViewById<Button>(R.id.buttonEditDiscipline)
    val textDisciplineCode = findViewById<TextView>(R.id.textDisciplineCode)
    textDisciplineCode.setOnKeyListener { view, keyCode, keyEvent ->
      when {
        ((keyCode == KeyEvent.KEYCODE_ENTER) && (keyEvent.action == KeyEvent.ACTION_DOWN)) -> {
          performEditDiscipline(view)
          return@setOnKeyListener true
        }
        else -> false
      }
    }
    buttonNewDiscipline.setOnClickListener {
      contractLauncher.launch(Request(RequestType.CREATE, Discipline(nextDisciplineCode)))
    }
    buttonEditDiscipline.setOnClickListener {
      performEditDiscipline(it)
    }
  }
}

class MyContract: ActivityResultContract<Request, Response>() {
  override fun createIntent(context: Context, request: Request) =
    Intent(context, SecondActivity::class.java).apply {
      putExtra(SEND_CODE, request)
    }

  override fun parseResult(resultCode: Int, intent: Intent?): Response? {
    if (resultCode != Activity.RESULT_OK) {
      return null
    }

    return intent?.getParcelableExtra(RECEIVE_CODE)
  }

  companion object {
    const val SEND_CODE = "send_code"
    const val RECEIVE_CODE = "receive_code"
  }
}