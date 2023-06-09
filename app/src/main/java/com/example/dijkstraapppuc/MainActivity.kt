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
                calculateSmallestWay(origin!!, destination!!)
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
                    //Log.d("DocData",data.toString())
                    if(data != null){
                        //incializando um novo hashMap sempre que iterar sobre um novo documento
                        val neighborMap: HashMap<String, Number> = HashMap()

                        //iterando sobre cada campo no documento
                        for ((neighbor, value) in data) {
                            neighborMap["${neighbor as String}"] = value as Number
                            //Log.d("NeighBorMap",neighborMap.toString())
                        }
                        buildMap["${doc.id}"] = neighborMap
                        //Log.d("BuildMap",buildMap.toString())
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
        Log.d("DistancesList",distances.toString())
    }

    private fun calculateSmallestWay(origin: String, destination: String)
    {
        var processed: MutableList<String> = mutableListOf() //lista de já processados
        var queue: MutableList<String> = mutableListOf(origin) //inicializando a lista com a origem
        var distances: HashMap<String, Number> = HashMap()

        getMap(){graphRef ->
            if(graphRef.containsKey(origin) && graphRef.containsKey(destination)){

                var node: String = origin

                //inicializando o HashMap distances de acordo com o origin
                inicializeDistances(origin, distances, graphRef)

                //adicionando os adjacentes da origem na lista de não verificados
                for (x in graphRef[origin]!!.keys){
                    queue.add(x)
                }

                node = findSmallestDistance(distances,processed)
                while(queue.isNotEmpty()){
                    var cost = distances[node]!!.toDouble()
                    //Log.d("cost","${node}: ${cost}")
                    for((key, value) in graphRef[node]!!){
                        var newcost = cost + value.toDouble()
                        /*Log.d("Value",value.toString())
                        Log.d("DistanceKey","${key}: ${distances[key].toString()}")
                        Log.d("NewCost",newcost.toString())*/
                        if(distances.containsKey(key)){
                            if(distances[key]!!.toDouble() > newcost)
                            {
                                distances[key] = newcost
                                //Log.d("DistanceNewValue","${key}: ${distances[key].toString()}")
                                if(key !in processed && key !in queue){
                                    queue.add(key)
                                }
                            }
                        }
                    }
                    queue.remove(node)
                    //Log.d("Queue",queue.toString())
                    processed.add(node) //adiciona o node na lista de processados
                    //Log.d("Processed",processed.toString())
                    node = findSmallestDistance(distances, processed)
                }

                val result = distances[destination]!!.toDouble() //resultado final
                runOnUiThread{
                    binding.tvResultado.setText("Tempo do percurso:$result min")
                }
            } else {
                Toast.makeText(this,"Por favor, verifique a existência dos prédios digitados.",Toast.LENGTH_LONG).show()
            }
        }
    }
}
