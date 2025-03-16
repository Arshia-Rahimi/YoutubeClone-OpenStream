package com.github.freetube.ui.feature.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arshia.freetube.R
import com.github.freetube.ui.designsystem.components.NoRippleIconButton
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen() {
    val viewModel = koinViewModel<SearchScreenViewModel>()
    var searchQuery by viewModel.searchQuery
    val searchFieldFocusRequester = remember { FocusRequester() }
    val searchFieldInteractionSource = remember { MutableInteractionSource() }
    val isSearchFieldFocused by searchFieldInteractionSource.collectIsFocusedAsState()
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        searchField(
            searchQuery = searchQuery,
            setSearchQuery = { searchQuery = it },
            focusRequester = searchFieldFocusRequester,
            isSearchFieldFocused = isSearchFieldFocused,
            searchFieldInteractionSource = searchFieldInteractionSource,
            unFocusSearchField = {
                // todo not being called
                println("aaaaawrgharh")
                searchFieldFocusRequester.freeFocus()
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun LazyListScope.searchField(
    searchQuery: TextFieldValue,
    focusRequester: FocusRequester,
    isSearchFieldFocused: Boolean,
    searchFieldInteractionSource: MutableInteractionSource,
    setSearchQuery: (TextFieldValue) -> Unit,
    unFocusSearchField: () -> Unit,
) {
    item {
        TextField(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .clip(RoundedCornerShape(95.dp))
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 16.sp,
            ),
            interactionSource = searchFieldInteractionSource,
            value = searchQuery,
            onValueChange = setSearchQuery,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1D1D1D),
                unfocusedContainerColor = Color(0xFF1D1D1D),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.White,
                errorTextColor = Color.White,
                cursorColor = Color.White,
                errorCursorColor = Color.White,
            ),
            leadingIcon = {
                NoRippleIconButton(
                    onClick = {},
                ) {
                    Icon(
                        painter = painterResource(R.drawable.search),
                        contentDescription = "",
                        tint = Color(0xFF8E8E8E),
                    )
                }
            },
            trailingIcon = {
                if(isSearchFieldFocused) {
                    NoRippleIconButton(
                        onClick = unFocusSearchField,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.cross),
                            contentDescription = "",
                            modifier = Modifier.clickable {},
                            tint = Color(0xFF8E8E8E),
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun SearchPreview() {
    MaterialTheme {
        LazyColumn {
            searchField(
                searchQuery = TextFieldValue(""),
                setSearchQuery = {},
                focusRequester = FocusRequester(),
                isSearchFieldFocused = false,
                unFocusSearchField = {},
                searchFieldInteractionSource = MutableInteractionSource(),
            )
        }
    }
}
