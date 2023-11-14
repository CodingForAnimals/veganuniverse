package org.codingforanimals.veganuniverse.ui.error

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.codingforanimals.veganuniverse.core.ui.R
import org.codingforanimals.veganuniverse.ui.Spacing_04
import org.codingforanimals.veganuniverse.ui.Spacing_06
import org.codingforanimals.veganuniverse.ui.components.VUImage
import org.codingforanimals.veganuniverse.ui.components.VeganUniverseBackground
import org.codingforanimals.veganuniverse.ui.icon.VUImages

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    @StringRes message: Int,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Spacing_04, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        VUImage(
            modifier = Modifier
                .size(200.dp)
                .padding(top = Spacing_06),
            image = VUImages.ErrorCat,
        )
        Text(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(bottom = Spacing_06),
            textAlign = TextAlign.Center,
            text = stringResource(message)
        )
    }
}

@Preview
@Composable
private fun PreviewErrorView() {
    VeganUniverseBackground {
        Surface {
            ErrorView(message = R.string.unknown_error_message)
        }
    }
}