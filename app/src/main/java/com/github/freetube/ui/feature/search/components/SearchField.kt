package com.github.freetube.ui.feature.search.components

import android.annotation.SuppressLint
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arshia.freetube.R
import com.github.freetube.ui.designsystem.components.NoRippleIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    searchQuery: TextFieldValue,
    isSearchFieldFocused: Boolean,
    searchFieldInteractionSource: MutableInteractionSource,
    setSearchQuery: (TextFieldValue) -> Unit,
    search: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    TextField(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(95.dp))
            .fillMaxWidth(),
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
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                search()
                focusManager.clearFocus()
            },
        ),
        leadingIcon = {
            NoRippleIconButton(
                onClick = {
                    search()
                    focusManager.clearFocus()
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = "",
                    tint = Color(0xFF8E8E8E),
                )
            }
        },
        trailingIcon = {
            if (isSearchFieldFocused) {
                NoRippleIconButton(
                    onClick = {
                        setSearchQuery(TextFieldValue())
                        focusManager.clearFocus()
                    },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.cross),
                        contentDescription = "",
                        tint = Color(0xFF8E8E8E),
                    )
                }
            }
        }
    )
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Preview
@Composable
private fun SearchPreview() {
    MaterialTheme {
        LazyColumn {
            item {
                SearchField(
                    searchQuery = TextFieldValue("as;kgnqrh"),
                    setSearchQuery = {},
                    isSearchFieldFocused = true,
                    searchFieldInteractionSource = MutableInteractionSource(),
                    search = {},
                )
            }
        }
    }
}
