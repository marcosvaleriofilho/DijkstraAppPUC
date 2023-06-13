package com.example.dijkstraapppuc

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dijkstraapppuc.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var destination: String?
        var origin: String?

        binding.btnCalcular.setOnClickListener {
            origin = binding.etOrigem.text.toString().uppercase()
            destination = binding.etDestino.text.toString().uppercase()

            //tratamento para caso o usuário não digite corretamente os campos
            if(origin != "" && destination!= ""){
                dijkstra(origin!!, destination!!)
            } else {
                Toast.makeText(this,"Preencha os campos corretamente",Toast.LENGTH_LONG).show()
            }
        }
    }

    //função para pegar o mapa referência no Firebase
    private fun getMap(completion: (HashMap<String, HashMap<String, Number>>) -> Unit){
        val buildMap: HashMap<String, HashMap<String, Number>> = HashMap()

         db.collection("Grafo").get().addOnSuccessListener { collection ->
            if(collection != null) {

                //iterando sobre cada documento na coleção
                for( doc in collection.documents){
                    val data = doc.data
                    if(data != null){
                        //incializando um novo hashMap sempre que iterar sobre um novo documento
                        val neighborMap: HashMap<String, Number> = HashMap()
                        //iterando sobre cada campo no documento
                        for ((neighbor, value) in data) {
                            neighborMap["${neighbor as String}"] = value as Number
                        }
                        buildMap["${doc.id}"] = neighborMap
                    }
                }
            }
             completion(buildMap) //callback para retornar quando a leitura for concluída
        }
    }

    //função para achar a menor distância ainda não visitada na tabela distances
    private fun findSmallestDistance(graph: HashMap<String, Number>, processed: MutableList<String>) : String
    {
        var smallestDistance = Double.MAX_VALUE
        var nodeSmallest: String = ""

        for((node, value) in graph){
            val distance = value.toDouble()
            if(distance < smallestDistance && node !in processed)
            {
                smallestDistance = distance
                nodeSmallest = node
            }
        }
        return nodeSmallest
    }

    private fun inicializeDistances(
        origin: String,
        distances: HashMap<String, Number>,
        graphRef: HashMap<String, HashMap<String, Number>> )
    {
        //inicializando todas as distâncias como infinito
        for(nodes in graphRef.keys){
            distances[nodes] = Double.MAX_VALUE
        }
        //definindo a distância dos vizinhos com nó de origem
        for((neighbor, value) in graphRef[origin]!!){
            distances[neighbor] = value.toDouble()
        }
        distances[origin] = 0 //ou tirar o origin da lista de distances
    }

    //usando recursão para busca profunda
    private fun printPath(path: HashMap<String,String>, destination: String,pathList: MutableList<String>): MutableList<String>{
        var node: String = destination
        pathList.add(node)
        //só pode ter um destino e uma origem
        if(path.containsKey(node)) {
            node = path[node]!!
            printPath(path, node, pathList)
        }
        return  pathList.asReversed()
    }


    private fun dijkstra(origin: String, destination: String)
    {
        var processed: MutableList<String> = mutableListOf() //lista de já processados
        var queue: MutableList<String> = mutableListOf(origin) //inicializando a lista com a origem
        var distances: HashMap<String, Number> = HashMap()
        var path: HashMap<String, String> = HashMap()
        var pathList: MutableList<String> = mutableListOf()
        var stringPath: String = ""

        getMap(){graphRef ->
            if(graphRef.containsKey(origin) && graphRef.containsKey(destination)){

                var node: String = origin

                //inicializando o HashMap distances de acordo com o origin
                inicializeDistances(origin, distances, graphRef)

                //inicializando a tabela path
                for(adj in graphRef[origin]!!.keys){
                    path[adj] = origin
                }

                //adicionando os adjacentes da origem na lista de não verificados
                for (x in graphRef[origin]!!.keys){
                    queue.add(x)
                }

                node = findSmallestDistance(distances,processed)
                while(queue.isNotEmpty()){
                    var cost = distances[node]!!.toDouble()
                    for((key, value) in graphRef[node]!!){
                        var newcost = cost + value.toDouble()
                        if(distances.containsKey(key)){
                            if(distances[key]!!.toDouble() > newcost)
                            {
                                distances[key] = newcost
                                path[key] = node
                                if(key !in processed && key !in queue){
                                    queue.add(key)
                                }
                            } else if (distances[key]!!.toDouble() == newcost){
                                //aqui seria para caminhos alternativos com tempo igual
                            }
                        }
                    }
                    queue.remove(node)
                    processed.add(node) //adiciona o node na lista de processados
                    node = findSmallestDistance(distances, processed)
                }

                pathList = printPath(path, destination,pathList) //lista do caminho
                //printar caminho
                for (node in pathList)
                {
                    if (node == destination){
                        stringPath =  stringPath + node
                        break
                    } else {
                        stringPath = stringPath + node + " -> "
                    }
                }

                val result = distances[destination]!!.toDouble() //resultado final
                runOnUiThread{
                    binding.tvResultado.setText("Tempo do percurso:$result min")
                    binding.tvCaminho.setText("Caminho: $stringPath")
                }
            } else {
                Toast.makeText(this,"Por favor, verifique a existência dos prédios digitados.",Toast.LENGTH_LONG).show()
            }
        }
    }
}
