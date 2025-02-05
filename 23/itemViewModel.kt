package com.example.lab23

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class City(val name: String, val number:String, val oblast: String, var picture: Int = R.drawable.kot )
class ItemViewModel : ViewModel() {
    private var CityList = mutableStateListOf(
        City("Нижневартовск", "283 тыщь","Тюменская область", R.drawable.nv),
        City("Среднеколымск", "3118","республика Саха", R.drawable.midkol),
        City("Шуя", "55225","Ивановская", R.drawable.shuya),
        City("Железногородск", "94 тыщь","Орловская", R.drawable.iron),
        City("Москва", "13 лимонов","Тверская", R.drawable.mos),
    )
    private val _CityListFlow = MutableStateFlow(CityList)
    val CityListFlow: StateFlow<List<City>> get() = _CityListFlow
    fun clearList(){
        CityList.clear()
    }
    fun addCityToHead(city: City) {
        CityList.add(0, city)
    }
    fun addCityToEnd(city: City) {
        CityList.add( city)
    }
    fun isContains(city: City): Boolean {
        return CityList.contains(city)
    }
    fun removeItem(item: City) {
        val index = CityList.indexOf(item)
        CityList.remove(CityList[index])
    }
}