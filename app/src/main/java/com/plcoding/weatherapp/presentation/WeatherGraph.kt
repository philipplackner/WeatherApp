package com.plcoding.weatherapp.presentation

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.plcoding.weatherapp.domain.weather.WeatherData
import com.plcoding.weatherapp.presentation.ui.theme.CoolColor
import com.plcoding.weatherapp.presentation.ui.theme.HotColor
import kotlin.math.abs

@Composable
fun WeatherGraph(
    modifier: Modifier = Modifier,
    data: List<WeatherData>
) {
    val arcColor = MaterialTheme.colorScheme.tertiary
    val circleColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
    val max = data.maxOf { weatherData ->
        weatherData.temperatureCelsius
    }.toFloat()
    val min = data.minOf { it.temperatureCelsius }.toFloat()
    val diff = max - min + 1
    Row(
        modifier = modifier,
    ) {
        Column {
            val width = with(LocalDensity.current) {
                120.dp.toPx()
            }
            val depth = with(LocalDensity.current) {
                8.dp.toPx()
            }
            BoxWithConstraints(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .height(120.dp)
                    .width(120.dp * data.size)
            ) {
                val height = constraints.maxHeight
                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    drawPath(
                        getMainGraphCurve(
                            data,
                            width, height,
                        ),
                        color = arcColor,
                        style = Stroke(width = 8f)
                    )
                    data.forEachIndexed { index, weatherData ->
                        val x = index * (width) + width / 2f
                        val y =
                            height * (max - weatherData.temperatureCelsius.toFloat()) / diff - depth * 2
                        val yCenter = height * (max - weatherData.temperatureCelsius.toFloat()) / diff
                        drawCircle(
                            color = circleColor,
                            radius = depth / 2f,
                            center = Offset(x, yCenter)
                        )
                        if (weatherData.temperatureCelsius.toFloat() == max) {
                            drawContext.canvas.nativeCanvas.apply {
                                drawText("${weatherData.temperatureCelsius}°C",
                                    x, y,
                                    Paint().apply {
                                        textSize = 50F
                                        color = HotColor.hashCode()
                                        textAlign = Paint.Align.CENTER
                                    }
                                )
                            }
                        }
                        if (weatherData.temperatureCelsius.toFloat() == min) {
                            drawContext.canvas.nativeCanvas.apply {
                                drawText("${weatherData.temperatureCelsius}°C",
                                    x, y,
                                    Paint().apply {
                                        textSize = 50F
                                        color = CoolColor.hashCode()
                                        textAlign = Paint.Align.CENTER
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .width(120.dp * data.size)
                    .height(120.dp)
            ) {
                data.forEach { weatherData ->
                    HourlyWeatherDisplay(
                        weatherData = weatherData,
                        modifier = Modifier
                            .height(100.dp)
                            .width(120.dp)
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

fun Path.standardQuadFromTo(from: Offset, to: Offset) {
    quadraticBezierTo(
        from.x,
        from.y,
        abs(from.x + to.x) / 2f,
        abs(from.y + to.y) / 2f
    )
}

fun getMainGraphCurve(data: List<WeatherData>, width: Float, height: Int): Path {
    val path = Path()
    val max = data.maxOf { weatherData ->
        weatherData.temperatureCelsius
    }.toFloat()
    val min = data.minOf { it.temperatureCelsius }.toFloat()
    val diff = max - min + 1
    var prevPoint = Offset(width / 2f, (max - data[0].temperatureCelsius.toFloat()) / diff)
    data.forEachIndexed { index, weatherData ->
        val x = index * (width) + width / 2f
        val y = height * (max - weatherData.temperatureCelsius.toFloat()) / diff
        val nextPoint = Offset(x, y)
        when (index) {
            0 -> {
                path.moveTo(x, y)
            }
            data.size - 1 -> {
                path.lineTo(nextPoint.x, nextPoint.y)
            }
            else -> {
                path.standardQuadFromTo(prevPoint, nextPoint)
            }
        }
        prevPoint = nextPoint
    }
    return path
}