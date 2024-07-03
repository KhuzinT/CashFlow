package ru.khuzintka.cashflow.ui.woof

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.khuzintka.cashflow.data.remote.repo.WoofRepo
import javax.inject.Inject

@HiltViewModel
class WoofViewModel
@Inject constructor(
    private val repo: WoofRepo,
) : ViewModel() {

    private val _url = MutableStateFlow("")
    val url: StateFlow<String> = _url

    init {
        newImage()
    }

    fun newImage() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getWoof()
                .catch {
                    Log.e("WOOF", it.message ?: "¯\\_(ツ)_/¯")
                }
                .collect { woof ->
                    _url.update { woof.url }
                    Log.d("WOOF", woof.url)
                }
        }
    }
}