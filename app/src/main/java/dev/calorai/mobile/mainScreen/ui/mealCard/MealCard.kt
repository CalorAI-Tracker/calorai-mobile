package dev.calorai.mobile.mainScreen.ui.mealCard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import dev.calorai.mobile.R
import dev.calorai.mobile.mainScreen.ui.model.FoodUiModel
import dev.calorai.mobile.mainScreen.ui.model.MealUiModel
import dev.calorai.mobile.mainScreen.ui.utils.calculateItemOffset
import dev.calorai.mobile.ui.theme.CalorAiTheme
import dev.calorai.mobile.ui.theme.circleMediumSize


@Composable
fun MealCard(
    mealData: MealUiModel,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(80.dp),
    onAddClick: () -> Unit = {},
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        MealCardContent(
            title = mealData.title,
            formattedSubtitle = mealData.subtitle,
            foodList = mealData.visibleFoodList,
            onAddClick = onAddClick
        )
    }
}

@Composable
private fun MealCardContent(
    title: String,
    formattedSubtitle: String,
    foodList: List<FoodUiModel>,
    onAddClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        MealCardLeftBlock(
            title = title,
            formattedSubtitle = formattedSubtitle,
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 15.dp)
        )
        MealCardRightBlock(
            visibleFoodList = foodList,
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(end = 15.dp),
            onAddClick = onAddClick
        )
    }
}

@Composable
private fun MealCardLeftBlock(
    title: String,
    formattedSubtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Text(
            text = formattedSubtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun MealCardRightBlock(
    visibleFoodList: List<FoodUiModel>,
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd,
    ) {
        visibleFoodList.forEachIndexed { index, food ->
            val dx = calculateItemOffset(
                index = index,
                itemSize = circleMediumSize,
                itemOverlapOffset = 15.dp,
            )
            FoodThumbnail(
                food = food,
                modifier = Modifier
                    .size(circleMediumSize)
                    .align(Alignment.CenterEnd)
                    .offset(x = dx)
                    .zIndex((visibleFoodList.size - index).toFloat())
                    .clip(CircleShape)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.add_button_pink),
            contentDescription = stringResource(R.string.add),
            modifier = Modifier
                .size(circleMediumSize)
                .align(Alignment.CenterEnd)
                .zIndex((visibleFoodList.size + 1).toFloat())
                .clickable(onClick = onAddClick)
        )
    }
}

@Composable
private fun FoodThumbnail(
    food: FoodUiModel,
    modifier: Modifier = Modifier,
) {
    val painter = if (!food.urlToImage.isNullOrBlank()) {
        rememberAsyncImagePainter(model = food.urlToImage)
    } else {
        painterResource(R.drawable.fried_eggs)
    }

    Image(
        painter = painter,
        contentDescription = food.name,
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun MealCardPreview_EmptyImages() {
    val foodList = List(7) {
        FoodUiModel(
            id = it.toLong(),
            name = "Meal $it",
            urlToImage = null,
        )
    }
    val mealUiModel = MealUiModel(
        id = 1,
        title = "Завтрак",
        visibleFoodList = foodList.take(5),
        subtitle = "345 ккал",
    )
    CalorAiTheme {
        MealCard(
            mealData = mealUiModel,
            onAddClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MealCardPreview_NoFoods() {
    val mealUiModel = MealUiModel(
        id = 2,
        title = "Ужин",
        visibleFoodList = emptyList(),
        subtitle = "0 ккал",
    )
    CalorAiTheme {
        MealCard(
            mealData = mealUiModel,
            onAddClick = {},
        )
    }
}