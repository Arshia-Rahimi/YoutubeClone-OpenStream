package com.github.freetube.ui.feature.search.main.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arshia.freetube.R
import com.github.freetube.ui.designsystem.components.NoRippleIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    searchQuery: String,
    isSearchFieldFocused: Boolean,
    searchFieldInteractionSource: MutableInteractionSource,
    focusManager: FocusManager,
    setSearchQuery: (String) -> Unit,
    search: () -> Unit,
) {
    BasicTextField(
        value = searchQuery,
        modifier = Modifier
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(95.dp))
            .fillMaxWidth(),
        maxLines = 1,
        textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
        interactionSource = searchFieldInteractionSource,
        onValueChange = setSearchQuery,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                search()
                focusManager.clearFocus()
            },
        ),
        cursorBrush = SolidColor(Color.White)
    ) { innerTextField ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1D1D1D)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 4.dp),
            ) {
                innerTextField()
            }
            if (isSearchFieldFocused) {
                NoRippleIconButton(
                    onClick = {
                        setSearchQuery("")
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
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Preview
@Composable
private fun SearchPreview() {
    MaterialTheme {
        LazyColumn {
            item {
                SearchField(
                    searchQuery = "as;kgnqrh",
                    setSearchQuery = {},
                    isSearchFieldFocused = true,
                    searchFieldInteractionSource = MutableInteractionSource(),
                    search = {},
                    focusManager = LocalFocusManager.current,
                )
            }
        }
    }
}
