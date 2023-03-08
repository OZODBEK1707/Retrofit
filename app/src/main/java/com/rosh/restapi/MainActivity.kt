package com.rosh.restapi

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.rosh.restapi.adapters.MyTodoAdapter
import com.rosh.restapi.databinding.ActivityMainBinding
import com.rosh.restapi.databinding.MyDialogBinding
import com.rosh.restapi.models.MyTodo
import com.rosh.restapi.models.MyTodoRequest
import com.rosh.restapi.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), MyTodoAdapter.RvClick {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var myTodoAdapter: MyTodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        myTodoAdapter = MyTodoAdapter(this)
        binding.rv.adapter = myTodoAdapter
        loadData()


        binding.mySwipe.setOnClickListener {
            addToDo()
        }
        binding.btnAddTodo.setOnClickListener {
            addToDo()
        }

        ApiClient.getApiService().getAllTodo()
            .enqueue(object : Callback<List<MyTodo>>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<List<MyTodo>>,
                    response: Response<List<MyTodo>>
                ) {
                    if (response.isSuccessful){
                        binding.myProgress.visibility = View.GONE
                        myTodoAdapter.list.addAll(response.body()!!)
                        myTodoAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<List<MyTodo>>, t: Throwable) {
                    binding.myProgress.visibility = View.GONE
                    Toast.makeText(this@MainActivity,
                        "Internetga bog'lanishni tekshiring",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun addToDo() {
            val dialog= AlertDialog.Builder(this).create()
            val  dialogBinding=MyDialogBinding.inflate(layoutInflater)
            dialogBinding.progresBar.visibility=View.GONE
            dialog.setView(dialogBinding.root)
            dialogBinding.btnSaveDialog .setOnClickListener {
                val mytodoRequest= MyTodoRequest(
                    dialogBinding.tvTitle.text.toString(),
                    dialogBinding.tvText.text.toString(),
                    "eski",
                    dialogBinding.tvDeadline.text.toString()


                )
                dialogBinding.progresBar.visibility=View.VISIBLE
                ApiClient.getApiService().addToDo(mytodoRequest)
                    .enqueue(object :Callback<MyTodo>{
                        override fun onResponse(call: Call<MyTodo>, response: Response<MyTodo>) {

                            if (response.isSuccessful){
                                dialogBinding.progresBar.visibility=View.GONE
                                Toast.makeText(this@MainActivity, "${response.body()?.id} id bilan saqlandi", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                        }
                        override fun onFailure(call: Call<MyTodo>, t: Throwable) {
                            dialogBinding.progresBar.visibility=View.GONE
                            Toast.makeText(this@MainActivity, "qo'shishda hatolik, internetni tekshiring", Toast.LENGTH_SHORT).show()
                        }
                    })
                loadData()
            }
            dialog.show()
    }

    private fun loadData() {

        ApiClient.getApiService().getAllTodo()
            .enqueue(object :Callback<List<MyTodo>>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<List<MyTodo>>,
                    response: Response<List<MyTodo>>
                ) {
                    if (response.isSuccessful){
                        binding.myProgress.visibility= View.GONE
                        myTodoAdapter.list.clear()
                        myTodoAdapter.list.addAll(response.body()!!)
                        myTodoAdapter.notifyDataSetChanged()
                        binding.mySwipe.isRefreshing=false

                    }
                }

                override fun onFailure(call: Call<List<MyTodo>>, t: Throwable) {
                    myTodoAdapter.list.clear()
                    binding.myProgress.visibility= View.GONE

                    Toast.makeText(this@MainActivity, "Internetga ulanishni tekshiring", Toast.LENGTH_SHORT).show()
                    binding.mySwipe.isRefreshing=false
                }
            })
    }

    override fun deleteTodo(myTodo: MyTodo) {
        ApiClient.getApiService().deleteTodo(myTodo.id)
            .enqueue(object :Callback<Int>{
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if (response.isSuccessful){
                        Toast.makeText(this@MainActivity, "${myTodo.id} id dagi ochirildi", Toast.LENGTH_SHORT).show()
                        loadData()
                    }
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "xatolik boldi", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun updateTodo(myTodo: MyTodo) {
        val dialog= AlertDialog.Builder(this).create()
        val  dialogBinding=MyDialogBinding.inflate(layoutInflater)
        dialogBinding.progresBar.visibility=View.GONE
        dialog.setView(dialogBinding.root)
        dialogBinding.btnSaveDialog.setOnClickListener {
            myTodo.sarlavha = dialogBinding.tvTitle.text.toString()
            myTodo.matn = dialogBinding.tvText.text.toString()
            myTodo.holat = dialogBinding.spinnerStatus.selectedItem.toString()
            myTodo.oxirgi_muddat = dialogBinding.tvDeadline.text.toString()


            ApiClient.getApiService().updateTodo(
                myTodo.id,
                MyTodoRequest(myTodo.sarlavha, myTodo.matn, myTodo.holat, myTodo.oxirgi_muddat)
            ).enqueue (object : Callback<MyTodo> {
                override fun onResponse(call: Call<MyTodo>, response: Response<MyTodo>) {
                    Toast.makeText(this@MainActivity, "${response.body()?.id}", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<MyTodo>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "internetni tekshirib ko'ring, o'zgaritishda muammo", Toast.LENGTH_SHORT).show()
                }
            })

        }
    }
}