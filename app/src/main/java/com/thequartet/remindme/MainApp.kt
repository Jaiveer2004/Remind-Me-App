package com.thequartet.remindme

import android.graphics.Paint.Style
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp


data class ShoppingListItems(
    val id : Int,
    var name : String,
    var qty : Int,
    var isEditing : Boolean = false
)





@Composable
fun App(modifier : Modifier = Modifier){

    var sItems by remember {mutableStateOf(listOf<ShoppingListItems>())}
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQty by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
            ){
            Text(text = "+")
        }

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .padding(8.dp)
        ){
            items(sItems){
                item ->
                if(item.isEditing){
                    ListItemEditor(
                        item = item,
                        onEditComplete = { editedName, editedQty ->
                            sItems = sItems.map{it.copy(isEditing = false)}
                            val editedItem = sItems.find{it.id == item.id}
                            editedItem?.let{
                                it.name = editedName
                                it.qty = editedQty
                            }
                        })
                } else {
                    ListItem(
                        items = item,
                        onEditClick = {
                            sItems = sItems.map{it.copy(isEditing = it.id == item.id)}
                        },
                        onDeleteClick = {
                            sItems = sItems - item
                        })
                }
            }
        }
    }

    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {
                        if(itemName.isNotBlank()){
                            val newItem = ShoppingListItems(
                                id = sItems.size + 1,
                                name = itemName,
                                qty = itemQty.toInt()
                            )
                            sItems += newItem
                            showDialog = false
                            itemName = ""
                            itemQty = ""
                        }

                    }) {
                    Text("Add")
                }

                Button(
                    onClick = { showDialog = false },
                ){
                    Text("Cancel")
                }

            }
                            },
            title = {Text("Add Shopping Items")},
            text = {

                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange = {itemName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = {Text("Item")}
                    )

                    OutlinedTextField(
                        value = itemQty,
                        onValueChange = {itemQty = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = {Text("Qty")}

                    )


                }

            }
        )
    }
}



@Composable
fun ListItem(
    items : ShoppingListItems,
    onEditClick : () -> Unit,
    onDeleteClick : () -> Unit

){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(2.dp, Color(0XFFC70039), RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = items.name, modifier = Modifier.padding(8.dp))
        
        Text(text = "Qty: ${items.qty.toString()}", modifier = Modifier.padding(8.dp))

        Row(modifier = Modifier.padding(8.dp)){
            IconButton(onClick = { onEditClick() }) {
                Icon(Icons.Default.Edit, "")
            }

            IconButton(onClick = { onDeleteClick() }) {
                Icon(Icons.Default.Delete, "")
            }

        }
    }
}

@Composable
fun ListItemEditor(
    item:ShoppingListItems,
    onEditComplete : (String, Int) -> Unit
){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQty by remember { mutableStateOf(item.qty.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                textStyle = TextStyle(color = Color.White)
                )

            BasicTextField(value = editedQty,
                onValueChange = {editedQty = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                textStyle = TextStyle(color = Color.White)
            )
        }

        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedQty.toIntOrNull() ?: 1)
        }) {
            Text(text = "Save")
        }
    }
}