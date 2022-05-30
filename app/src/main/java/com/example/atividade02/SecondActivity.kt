package com.example.atividade02

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.atividade02.entities.*

class SecondActivity: AppCompatActivity() {
  var disciplineCode: Int = 0
  var mode: RequestType = RequestType.CREATE

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.second_activity)

    setUpListeners()

    val request: Request? = intent.getParcelableExtra(MyContract.SEND_CODE)
    mode = request!!.type

    disciplineCode = request.discipline?.code!!

    val textViewDisciplineCode = findViewById<TextView>(R.id.textViewDisciplineCode)
    val text = "Código da disciplina: $disciplineCode"
    textViewDisciplineCode.text = text

    if (request.type == RequestType.UPDATE) {
      val textDisciplineName = findViewById<EditText>(R.id.textDisciplineName)
      val textProfesor = findViewById<EditText>(R.id.textProfesor)
      val textWorkload = findViewById<EditText>(R.id.textWorkload)

      val discipline: Discipline = request.discipline!!

      textDisciplineName.setText(discipline.name)
      textProfesor.setText(discipline.profesor)
      textWorkload.setText(discipline.workload.toString())
    }
  }

  private fun setUpListeners() {
    val buttonSave = findViewById<Button>(R.id.buttonSave)
    val buttonCancel = findViewById<Button>(R.id.buttonCancel)
    val textDisciplineName = findViewById<EditText>(R.id.textDisciplineName)
    val textProfesor = findViewById<EditText>(R.id.textProfesor)
    val textWorkload = findViewById<EditText>(R.id.textWorkload)
    val textViewDisciplineCode = findViewById<TextView>(R.id.textViewDisciplineCode)

    fun performSave(view: View) {
      if (textDisciplineName.text.isEmpty() || textProfesor.text.isEmpty() || textWorkload.text.isEmpty() || textViewDisciplineCode.text.isEmpty()) {
        Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
        return
      }

      setResult(Activity.RESULT_OK, Intent().apply {
        val discipline = Discipline(disciplineCode,
          textDisciplineName.text.toString(),
          textProfesor.text.toString(),
          textWorkload.text.toString().toInt()
        )
        putExtra(MyContract.RECEIVE_CODE, Response(if (mode == RequestType.CREATE) ResponseType.SAVED else ResponseType.UPDATED, discipline))
      })

      finish()
    }

    textWorkload.setOnKeyListener{ view, keyCode, keyEvent ->
      when {
        ((keyCode == KeyEvent.KEYCODE_ENTER) && (keyEvent.action == KeyEvent.ACTION_DOWN)) -> {
          performSave(view)
          return@setOnKeyListener true
        }
        else -> false
      }
    }

    buttonSave.setOnClickListener {
      performSave(it)
    }
    buttonCancel.setOnClickListener {
      setResult(Activity.RESULT_CANCELED, null)
      finish()
    }
  }
}