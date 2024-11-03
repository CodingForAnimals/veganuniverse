package org.codingforanimals.veganuniverse.commons.ui.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import org.codingforanimals.veganuniverse.commons.ui.icon.Icon

object VUTextFieldDefaults {

    @Composable
    fun colors() = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        errorContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
fun VUTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    isError: Boolean = false,
    leadingIcon: Icon? = null,
    onTrailingIconClick: () -> Unit = {},
    trailingIcon: Int? = null,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        placeholder = placeholder?.let {
            { Text(text = placeholder) }
        },
        shape = ShapeDefaults.Medium,
        colors = VUTextFieldDefaults.colors(),
        leadingIcon = leadingIcon?.let {
            {
                VUIcon(
                    icon = leadingIcon,
                    contentDescription = ""
                )
            }
        },
        trailingIcon = trailingIcon?.let {
            {
                IconButton(onClick = onTrailingIconClick) {
                    Icon(
                        painter = painterResource(id = trailingIcon),
                        contentDescription = null,
                    )
                }
            }
        },
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun VUNormalTextField(
    modifier: Modifier = Modifier,
    label: String? = null,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    isError: Boolean = false,
    leadingIcon: Icon? = null,
    maxLines: Int = Int.MAX_VALUE,
    maxChars: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    TextField(
        modifier = modifier,
        label = label?.let { { Text(label) } },
        value = value,
        onValueChange = {
            if (it.length <= maxChars) {
                onValueChange(it)
            }
        },
        isError = isError,
        placeholder = placeholder?.let { { Text(text = placeholder) } },
        colors = VUTextFieldDefaults.colors(),
        leadingIcon = leadingIcon?.let {
            {
                VUIcon(
                    icon = leadingIcon,
                    contentDescription = ""
                )
            }
        },
        singleLine = maxLines == 1,
        maxLines = maxLines,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}