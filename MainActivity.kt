package com.example.lab23


import android.content.Context
import android.content.res.Resources
import android.os.Bundle

import android.widget.Toast

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab23.ui.theme.Lab23Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel = ItemViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val lazyListState = rememberLazyListState()
            Lab23Theme  {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Gray,
                    contentColor = Color.Black
                ) {
                    Column(Modifier.fillMaxSize()) {
                        MakeInputPart(viewModel, lazyListState)
                        MakeList(viewModel, lazyListState)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeInputPart(model: ItemViewModel, lazyListState: LazyListState) {
    var cityName by remember {
        mutableStateOf("")
    }
    var showNumber by remember{
        mutableStateOf("")
    }
    var cityOblast by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TextField(
            label = { Text("Город") },
            value = cityName.toString(),
            onValueChange = { newText ->
                cityName = newText
            },
            modifier = Modifier.fillMaxWidth(),
//            singleLine = true,
            maxLines = 2
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextField(
                label = { Text("Население") },
                value = showNumber.toString(),
                onValueChange = { newText ->
                    showNumber = newText
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            TextField(
                label = { Text("Область") },
                value = cityOblast.toString(),
                onValueChange = { newText ->
                    cityOblast = newText
                },
                modifier = Modifier.weight(1f)
            )
        }
        Button(
            onClick = {
                if(!model.isContains(City(cityName, showNumber, cityOblast))){
                    println("added $cityName $showNumber $cityOblast")
                    model.addCityToHead(City(cityName, showNumber, cityOblast))
                    scope.launch {
                        lazyListState.scrollToItem(0)
                    }
                }else{
                    Toast.makeText(context, "В списке уже есть этот город! 😔", Toast.LENGTH_LONG).show()
                }
                cityName = ""
                showNumber = ""
                cityOblast = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add")
        }
    }
}
@Composable
fun MakeList(viewModel: ItemViewModel, lazyListState: LazyListState) {
    LazyColumn(
//        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        state = lazyListState
    ) {
        items(
            items = viewModel.CityListFlow.value,
            key = { city -> city.name },
            itemContent = { item ->
                ListColumn(item)
            }
        )
    }
}
@Composable
fun MakeAlertDialog(context: Context, dialogTitle: String, openDialog: MutableState<Boolean>) {
    var strValue = remember{ mutableStateOf("") }
    val strId = context.resources.getIdentifier(dialogTitle.replace(" ", ""), "string", context.packageName)
    try{
        if (strId != 0) strValue.value = context.getString(strId)
    } catch (e: Resources.NotFoundException) {
    }
    AlertDialog(
        onDismissRequest = { openDialog.value = false },
        title = { Text(text = dialogTitle) },
        text = { Text(text = strValue.value, fontSize = 14.sp) },
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { openDialog.value = false })
            { Text(text = "OK") }
        },

        )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListColumn(item: City){
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false)}
    val langSelected = remember { mutableStateOf("") }
    if (openDialog.value)
        MakeAlertDialog(context, langSelected.value, openDialog)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .border(BorderStroke(2.dp, Color.DarkGray))
            .background(Color(0xFFd7d7d7))
            .combinedClickable (
                onClick = {
                    println("item = ${item.name}")
                    langSelected.value = item.name
//                    Toast.makeText(context, "item = ${item.name}", Toast.LENGTH_LONG).show()
                    openDialog.value = true
                }
            )
    ) {
        Text(
            text = item.name,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp),
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ){
            Image(
                painter = painterResource(id = item.picture),
                contentDescription = "",
//            contentScale = ContentScale.FillWidth,
                modifier = Modifier.padding(bottom = 10.dp).clip(RoundedCornerShape(25.dp)).width(150.dp)

            )
            Column (
                horizontalAlignment = Alignment.Start,
//                verticalArrangement = Arrangement.spacedBy(50.dp)
                modifier = Modifier.width(80.dp),

                ){
                Text(
                    text = "Население:",
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = item.number.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom =  10.dp),
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = "Область:",
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = item.oblast.toString(),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom =  10.dp),
                    fontStyle = FontStyle.Italic
                )
            }
        }


    }
}
