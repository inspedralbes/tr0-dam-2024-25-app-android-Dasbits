package davidsalvador.dam.tr0.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import davidsalvador.dam.tr0.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button per començar el quiz
        val startQuizButton: Button = findViewById(R.id.startQuizButton)
        // EditText on s'escriu el nom
        val nameEditText: EditText = findViewById(R.id.nameEditText)

        startQuizButton.setOnClickListener {

            val userName = nameEditText.text.toString()

            if (userName.isEmpty()) {
                Toast.makeText(this, "Por favor, escribe tu nombre", Toast.LENGTH_SHORT).show()
            } else {
                // Crea un Intent para iniciar la actividad QuizActivity y pasa el nombre del usuario
                val intent = Intent(this, QuizActivity::class.java)
                intent.putExtra("USER_NAME", userName)
                startActivity(intent)
            }

        }

    }
}