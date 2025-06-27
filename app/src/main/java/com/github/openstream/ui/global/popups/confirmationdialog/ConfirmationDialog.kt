package com.github.openstream.ui.global.popups.confirmationdialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.openstream.R
import com.github.openstream.ui.designsystem.components.OpenStreamDialog
import com.github.openstream.ui.global.popups.PopupController
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ConfirmationDialog(
    type: Confirmation,
) {
    val viewModel = koinViewModel<ConfirmationDialogViewModel>(
        parameters = { parametersOf(type) },
        key = type.hashCode().toString(),
    )

    OpenStreamDialog(
        dismiss = { PopupController.dismissConfirmationDialog() },
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(
                4.dp,
                alignment = Alignment.CenterVertically,
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(type.confirmationText)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
            ) {
                Button(
                    onClick = { PopupController.dismissConfirmationDialog() },
                ) {
                    Text(stringResource(R.string.dismiss))
                }
                Button(
                    onClick = viewModel::confirm,
                ) {
                    Text(stringResource(type.confirmButton))
                }
            }
        }
    }
}
