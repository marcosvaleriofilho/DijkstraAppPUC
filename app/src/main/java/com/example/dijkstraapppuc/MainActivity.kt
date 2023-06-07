package com.example.dijkstraapppuc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var editarTextoPredioOrigem: EditText
    private lateinit var editarTextoPredioDestino: EditText
    private lateinit var botaoCalcular: Button
    private lateinit var textoResultado: TextView

    private val grafo = Grafo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editarTextoPredioOrigem = findViewById(R.id.etOrigem)
        editarTextoPredioDestino = findViewById(R.id.etDestino)
        botaoCalcular = findViewById(R.id.btnCalcular)
        textoResultado = findViewById(R.id.tvResultado)

        // Exemplo de inserção dos prédios e caminhos no grafo
        grafo.adicionarPredio("A")
        grafo.adicionarPredio("B")
        grafo.adicionarPredio("C")
        grafo.adicionarPredio("D")

        grafo.adicionarAresta("A", "B", 4)
        grafo.adicionarAresta("A", "C", 2)
        grafo.adicionarAresta("B", "C", 1)
        grafo.adicionarAresta("B", "D", 5)
        grafo.adicionarAresta("C", "D", 8)

        botaoCalcular.setOnClickListener {
            val predioOrigem = editarTextoPredioOrigem.text.toString()
            val predioDestino = editarTextoPredioDestino.text.toString()

            val menorCaminho = grafo.encontrarMenorCaminho(predioOrigem, predioDestino)

            if (menorCaminho != null) {
                val tempo = menorCaminho.tempoTotal
                val caminho = menorCaminho.caminho.joinToString(" -> ")
                textoResultado.text = "Tempo: Aprox. $tempo min\n"
            }
        }
    }
}
