package com.github.userlist.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.userlist.R
import com.github.userlist.base.CoreActivity
import com.github.userlist.data.model.GitUser
import com.github.userlist.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : CoreActivity<ActivityMainBinding, MainViewModel>() {

    private var searchJob: Job? = null
    lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutRes(R.layout.activity_main)
        setUpAdapter()
        getUsers()
    }

    private fun getUsers() {
        binding.pbItem.isVisible = true
        binding.ivError.isVisible = false
        searchJob = lifecycleScope.launch {
            viewModel.getUsers()
                .collectLatest {
                    adapter.submitData(it)
                }
        }
    }

    private fun getUserDetail(userItem: GitUser){
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        dialogBuilder.setView(R.layout.dialog_loading)
        val dialog: Dialog = dialogBuilder.create()
        if (dialog.isShowing) dialog.show() else dialog.dismiss()
        dialog.show()
        lifecycleScope.launch {
            val user = viewModel.getUserDetail(userItem.login ?: return@launch)
            showToast(
                "Username : ${user.login}\n" +
                        "Id : ${user.id}\n" +
                        "Email : ${if (user.email.isNullOrEmpty()) "(-)" else user.email}\n" +
                        "Location : ${if (user.location.isNullOrEmpty()) "(-)" else user.location}\n" +
                        "Created At : ${if (user.createdAt.isNullOrEmpty()) "(-)" else user.createdAt}\n"
            )
            dialog.dismiss()
        }
    }

    private fun setUpAdapter() {
        binding.btnRetry.setOnClickListener {
            adapter.retry()
        }
        binding.srlItem.setOnRefreshListener {
            getUsers()
        }

        adapter = MainAdapter { userItem ->
            getUserDetail(userItem)
        }
        binding.rvItem.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
        binding.rvItem.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        adapter.addLoadStateListener { loadState ->
            val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            val isListError = loadState.refresh is LoadState.Error && adapter.itemCount == 0
            binding.srlItem.isRefreshing = false
            binding.tvError.isVisible = isListError
            binding.ivError.isVisible = isListError
            binding.rvItem.isVisible = !isListEmpty
            binding.pbItem.isVisible = loadState.source.refresh is LoadState.Loading
            binding.btnRetry.isVisible = isListError

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.refresh as? LoadState.Error

            when (errorState?.error) {
                is HttpException -> {
                    if (adapter.itemCount == 0) {
                        val error = errorState.error as HttpException
                        when (error.response()?.code()) {
                            403 -> {
                                showToast("Status: 403 Forbidden")
                            }
                            404 -> {
                                showToast("Status: 404 Not Found")
                            }
                            401 -> {
                                showToast("Status: 401 Unauthorized")
                            }
                            else -> {
                                showToast("${errorState.error.localizedMessage}")
                            }
                        }
                    }
                }
                else -> {
                    if (isListError) {
                        binding.tvError.text = "${errorState?.error?.localizedMessage}"
                    }
                }
            }
        }

    }

}
