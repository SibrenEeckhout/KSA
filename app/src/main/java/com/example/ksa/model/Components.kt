package com.example.ksa.model

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ksa.R
import kotlinx.coroutines.flow.Flow

class Components {
    companion object {
        @Composable
        fun Error() {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error",
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(50.dp, 50.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Something went wrong", fontSize = 20.sp)
            }
        }

        @Composable
        fun NoResults() {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.SearchOff,
                    contentDescription = "No Results",
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.size(50.dp, 50.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(R.string.noResults), fontSize = 20.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(R.string.soundsLikeAYouProblem), fontSize = 15.sp)
            }
        }

        @Composable
        fun SearchBar(
            searchText: String,
            onSearchTextChanged: (String) -> Unit,
            onGroupSelected: (String) -> Unit,
            searchBarPlaceHolder: String,
            groupNames: Flow<List<String>>,
            isDropdownExpanded: Boolean,
            onDropdownListClicked: () -> Unit,
            onDropdownListDismissed: () -> Unit,
            selectedGroupName: String?,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                TextField(
                    value = searchText,
                    onValueChange = onSearchTextChanged,
                    placeholder = { Text(searchBarPlaceHolder) },
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                )
                IconButton(onClick = { onDropdownListClicked() }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                    )
                }
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { onDropdownListDismissed() },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    groupNames.collectAsState(initial = emptyList()).value.forEach { groupName ->
                        val isSelected = selectedGroupName == groupName
                        DropdownMenuItem(
                            onClick = {
                                onGroupSelected(groupName)
                                onDropdownListDismissed()
                            },
                            modifier = Modifier
                                .background(
                                    if (isSelected) MaterialTheme.colors.secondary else Color.Transparent
                                )
                        ) {
                            Text(
                                text = groupName,
                                color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
