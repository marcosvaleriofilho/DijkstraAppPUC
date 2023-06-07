package com.example.dijkstraapppuc

class Grafo {
    private val predios = mutableMapOf<String, Int>()
    private val arestas = mutableListOf<Aresta>()

    fun adicionarPredio(nomePredio: String) {
        if (!predios.containsKey(nomePredio)) {
            val indice = predios.size
            predios[nomePredio] = indice
        }
    }

    fun adicionarAresta(predioOrigem: String, predioDestino: String, tempo: Int) {
        val indiceOrigem = predios[predioOrigem] ?: return
        val indiceDestino = predios[predioDestino] ?: return

        arestas.add(Aresta(indiceOrigem, indiceDestino, tempo))
        arestas.add(Aresta(indiceDestino, indiceOrigem, tempo))
    }

    fun encontrarMenorCaminho(predioOrigem: String, predioDestino: String): MenorCaminho? {
        val indiceOrigem = predios[predioOrigem] ?: return null
        val indiceDestino = predios[predioDestino] ?: return null

        val numVertices = predios.size
        val temposMenores = IntArray(numVertices) { Int.MAX_VALUE }
        val visitados = BooleanArray(numVertices) { false }
        val antecessores = IntArray(numVertices) { -1 }

        temposMenores[indiceOrigem] = 0

        for (i in 0 until numVertices - 1) {
            val indiceVerticeAtual = obterVerticeMenorTempo(temposMenores, visitados)
            visitados[indiceVerticeAtual] = true

            for (aresta in arestas) {
                if (aresta.indiceOrigem == indiceVerticeAtual && !visitados[aresta.indiceDestino]
                    && temposMenores[indiceVerticeAtual] != Int.MAX_VALUE
                    && temposMenores[indiceVerticeAtual] + aresta.tempo < temposMenores[aresta.indiceDestino]
                ) {
                    temposMenores[aresta.indiceDestino] = temposMenores[indiceVerticeAtual] + aresta.tempo
                    antecessores[aresta.indiceDestino] = indiceVerticeAtual
                }
            }
        }

        val caminho = mutableListOf<Pair<Int, String>>()
        var verticeAtual = indiceDestino

        while (verticeAtual != -1) {
            caminho.add(verticeAtual to predios.entries.find { it.value == verticeAtual }?.key.orEmpty())
            verticeAtual = antecessores[verticeAtual]
        }

        if (caminho.size <= 1) {
            return null
        }

        caminho.reverse()

        return MenorCaminho(temposMenores[indiceDestino], caminho)
    }

    private fun obterVerticeMenorTempo(tempos: IntArray, visitados: BooleanArray): Int {
        var menorTempo = Int.MAX_VALUE
        var indiceMenorTempo = -1

        for (vertice in tempos.indices) {
            if (!visitados[vertice] && tempos[vertice] <= menorTempo) {
                menorTempo = tempos[vertice]
                indiceMenorTempo = vertice
            }
        }

        return indiceMenorTempo
    }
}

data class Aresta(val indiceOrigem: Int, val indiceDestino: Int, val tempo: Int)

data class MenorCaminho(val tempoTotal: Int, val caminho: List<Pair<Int, String>>)
