package com.devhjs.plantdex.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.AppRadii
import com.devhjs.plantdex.presentation.designsystem.AppTextStyles
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme

/** 디자인에 메모 편집 목업이 없어서 다이얼로그로 만든다. */
@Composable
fun MemoEditorDialog(
    value: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    title: String,
    placeholder: String,
    confirmLabel: String,
    dismissLabel: String,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = AppColors.Cream,
        shape = RoundedCornerShape(AppRadii.hero),
        title = { Text(text = title, style = AppTextStyles.TitleS) },
        text = {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = placeholder, style = AppTextStyles.BodyMuted) },
                textStyle = AppTextStyles.Body.copy(color = AppColors.InkBody),
                minLines = 3,
                shape = RoundedCornerShape(AppRadii.field),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AppColors.Sand,
                    unfocusedContainerColor = AppColors.Sand,
                    focusedBorderColor = AppColors.Terracotta,
                    unfocusedBorderColor = AppColors.Border,
                    cursorColor = AppColors.Terracotta,
                ),
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmLabel, style = AppTextStyles.LabelAccent)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = dismissLabel,
                    style = AppTextStyles.Chip.copy(color = AppColors.InkPlaceholder),
                )
            }
        },
    )
}

@Preview
@Composable
private fun MemoEditorDialogPreview() {
    PlantDexTheme {
        MemoEditorDialog(
            value = "",
            onValueChange = {},
            onConfirm = {},
            onDismiss = {},
            title = "메모",
            placeholder = "이 식물에 대해 남길 말을 적어주세요",
            confirmLabel = "저장",
            dismissLabel = "취소",
        )
    }
}

@Preview
@Composable
private fun MemoEditorDialogFilledPreview() {
    PlantDexTheme {
        MemoEditorDialog(
            value = "창가로 옮긴 뒤 새잎이 두 장 났다.",
            onValueChange = {},
            onConfirm = {},
            onDismiss = {},
            title = "메모",
            placeholder = "이 식물에 대해 남길 말을 적어주세요",
            confirmLabel = "저장",
            dismissLabel = "취소",
        )
    }
}
