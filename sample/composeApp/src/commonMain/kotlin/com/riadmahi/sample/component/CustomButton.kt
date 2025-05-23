
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = { },
    enabled: Boolean = true,
    fontSize: TextUnit = 16.sp,
    minHeight: Dp = 55.dp,
    cornerSize: Dp = 12.dp
) {
    Button(
        modifier = modifier.defaultMinSize(minHeight = minHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(enabled) Color(0xFF171F23) else Color(0xFF545D61) ,
        ),
        onClick = { onClick() },
        shape = RoundedCornerShape(cornerSize),
        elevation = ButtonDefaults.buttonElevation(0.dp),
        enabled = enabled
    ) {
        Text(
            text,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
            lineHeight = fontSize,
            color = Color.White,
        )
    }
}


@Composable
fun CustomGhostButton(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 16.sp,
    minHeight: Dp = 55.dp,
    onClick: () -> Unit = { }
) {
    Button(
        modifier = modifier.defaultMinSize(minHeight = minHeight),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFEAE5D8)
        ),
        border = BorderStroke(1.dp, Color(0xFFA08F63) ),
        onClick = { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Text(
            text,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium,
            lineHeight = fontSize,
            color = Color(0xFF171F23)
        )
    }
}