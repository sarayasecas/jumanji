package com.example.jumanji

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jumanji.ui.theme.JumanjiTheme
import com.example.jumanji.TipoCasilla.*
import com.example.jumanji.ui.theme.Arena
import com.example.jumanji.ui.theme.AzulRio
import com.example.jumanji.ui.theme.Crema
import com.example.jumanji.ui.theme.Dorado
import com.example.jumanji.ui.theme.MaderaOscura
import com.example.jumanji.ui.theme.NaranjaLeon
import com.example.jumanji.ui.theme.VerdeJungla
import com.example.jumanji.ui.theme.VerdeLiana

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JumanjiTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), containerColor = VerdeJungla) { innerPadding ->
                    PantallaJumanji(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaJumanji(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VerdeJungla)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Escapa de la jungla",
                fontSize = 40.sp,
                color = Arena,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Tablero()
            Leyenda()
        }
    }
}

@Composable
fun Leyenda() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaderaOscura.copy(alpha = 0.6f),
        border = BorderStroke(2.dp, Dorado)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            listOf(SALIDA, RIO, LIANA, LEON, META).forEach { tipo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(colorDe(tipo)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = iconoDe(tipo), fontSize = 16.sp)
                    }
                    Text(
                        text = reglaDe(tipo),
                        color = Crema,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
enum class TipoCasilla {
    NORMAL, SALIDA, META, RIO, LIANA, LEON
}
val mapa = listOf(
    NORMAL, NORMAL, RIO, NORMAL, NORMAL,
    LIANA, NORMAL, NORMAL, LEON, NORMAL,
    NORMAL, RIO, META, NORMAL, LIANA,
    LEON, NORMAL, NORMAL, NORMAL, NORMAL,
    SALIDA, NORMAL, LIANA, NORMAL, RIO

)


fun colorDe(tipo: TipoCasilla): Color = when (tipo) {
    RIO -> AzulRio
    NORMAL -> Arena
    LIANA -> VerdeLiana
    LEON -> NaranjaLeon
    META -> Dorado
    SALIDA -> Crema
}

fun iconoDe(tipo: TipoCasilla): String = when (tipo) {
    RIO -> "🌊"      // 🌊
    LIANA -> "🌿"    // 🌿
    LEON -> "🦁"     // 🦁
    META -> "💎"     // 💎
    SALIDA -> "🏁"   // 🏁
    NORMAL -> ""
}

fun reglaDe(tipo: TipoCasilla): String = when (tipo) {
    SALIDA -> "Salida: todos empiezan aquí"
    RIO -> "Río: la corriente te arrastra, pierdes un turno"
    LIANA -> "Liana: te columpias y avanzas 3 casillas"
    LEON -> "León: ¡huye! vuelves a la salida"
    META -> "Meta: el primero en llegar escapa de la jungla"
    NORMAL -> ""
}


val numeros = listOf(
    5,  6,  7,  8,  9,
    4, 19, 20, 21, 10,
    3, 18, 25, 22, 11,
    2, 17, 24, 23, 12,
    1, 16, 15, 14, 13
)

@Composable
fun Tablero() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primary,
        border = BorderStroke(4.dp, Dorado),
        shadowElevation = 6.dp
    ) {
        Box(
            modifier = Modifier.padding(10.dp)
        ) {

            Cuadricula { posicion, modifier ->
                Casilla(mapa[posicion], numeros[posicion], modifier)
            }
            CaminoEspiral(Modifier.matchParentSize())
            Cuadricula { posicion, modifier ->
                IconoCasilla(mapa[posicion], modifier)
            }
        }
    }
}


@Composable
fun Cuadricula(contenido: @Composable (posicion: Int, modifier: Modifier) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(5) { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(5) { columna ->
                    contenido(fila * 5 + columna, Modifier.weight(1f))
                }
            }
        }
    }
}


@Composable
fun CaminoEspiral(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val hueco = 6.dp.toPx()
        val lado = (size.width - 4 * hueco) / 5
        fun centro(posicion: Int) = Offset(
            x = (posicion % 5) * (lado + hueco) + lado / 2,
            y = (posicion / 5) * (lado + hueco) + lado / 2
        )


        val puntos = (0 until 25).sortedBy { numeros[it] }.map { centro(it) }


        val penultimo = puntos[puntos.size - 2]
        val direccion = (puntos.last() - penultimo) / (puntos.last() - penultimo).getDistance()
        val punta = puntos.last() - direccion * (lado * 0.3f)
        val baseFlecha = punta - direccion * 12.dp.toPx()

        val colorCamino = Crema.copy(alpha = 0.5f)

        val linea = Path().apply {
            moveTo(puntos.first().x, puntos.first().y)
            puntos.drop(1).dropLast(1).forEach { lineTo(it.x, it.y) }
            lineTo(baseFlecha.x, baseFlecha.y)
        }
        drawPath(
            path = linea,
            color = colorCamino,
            style = Stroke(
                width = 3.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12.dp.toPx(), 8.dp.toPx()))
            )
        )

        val perpendicular = Offset(-direccion.y, direccion.x) * 8.dp.toPx()
        val flecha = Path().apply {
            moveTo(punta.x, punta.y)
            lineTo((baseFlecha + perpendicular).x, (baseFlecha + perpendicular).y)
            lineTo((baseFlecha - perpendicular).x, (baseFlecha - perpendicular).y)
            close()
        }
        drawPath(path = flecha, color = colorCamino)
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaJumanjiPreview() {
    JumanjiTheme {
        PantallaJumanji()
    }
}

@Composable
fun Casilla(tipo: TipoCasilla, numero: Int, modifier: Modifier = Modifier){
    val color = colorDe(tipo)

    Box(
        modifier
        .aspectRatio(1f)
        .clip(RoundedCornerShape(6.dp))
        .background(color),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = numero.toString(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = MaderaOscura,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(3.dp)
        )
    }


}


@Composable
fun IconoCasilla(tipo: TipoCasilla, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iconoDe(tipo),
            fontSize = 32.sp
        )
    }
}
