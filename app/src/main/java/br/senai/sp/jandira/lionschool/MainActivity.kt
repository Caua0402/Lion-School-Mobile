package br.senai.sp.jandira.lionschool

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.lionschool.model.ListaCursos
import br.senai.sp.jandira.lionschool.services.RetrofitFactory
import br.senai.sp.jandira.lionschool.ui.theme.LionSchoolTheme
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LionSchoolTheme {
                HomeScreenPreview()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val defaultFont = FontFamily(Font(R.font.roboto_black))
    var textField by remember {
        mutableStateOf("")
    }
    var listCursos by remember {
        mutableStateOf(listOf<br.senai.sp.jandira.lionschool.model.Curso>())
    }

    var cursoSelecionado = ""
    var nomeDoCurso = ""

    val context = LocalContext.current

    var buttonReady by remember {
        mutableStateOf(false)
    }

    val call = RetrofitFactory().getCursosService().getCursos()

    call.enqueue(object : Callback<ListaCursos> {
        override fun onResponse(
            call: Call<ListaCursos>,
            response: Response<ListaCursos>
        ) {
            listCursos = response.body()!!.cursos
        }

        override fun onFailure(call: Call<ListaCursos>, t: Throwable) {
        }

    })

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color(51, 71, 176),
                    shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo da Escola Lion School",
                    modifier = Modifier
                        .size(100.dp, 100.dp)
                        .padding(start = 29.dp)
                )
                Spacer(modifier = Modifier.width(100.dp))
                Text(
                    text = "Lion School",
                    fontFamily = defaultFont,
                    fontSize = 30.sp,
                    color = Color.White,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center
                )
            }

            val buttonStates = remember { mutableStateListOf<Boolean>() }
            buttonStates.addAll(List(listCursos.size) { false })

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(600.dp)
                    .padding(top = 30.dp, start = 80.dp)
            ) {

                itemsIndexed(listCursos) { index, curso ->
                    val buttonState = buttonStates.getOrNull(index) ?: false

                    IconToggleButton(
                        checked = buttonState,
                        onCheckedChange = { checked ->
                            // Desativa todos os outros botões
                            buttonStates.fill(false)
                            // Ativa o botão atual
                            buttonStates[index] = checked

                            if(checked){
                                cursoSelecionado = curso.sigla
                                nomeDoCurso = curso.nome
                            } else {
                                cursoSelecionado = ""
                                nomeDoCurso = ""
                            }

                            buttonReady = checked
                        },
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .height(220.dp)
                            .width(240.dp)
                            .border(
                                width = 2.dp,
                                color = if (!buttonState) Color(255, 194, 63)
                                else Color.White,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .background(
                                color = if (!buttonState) Color(50, 71, 176)
                                else Color(255, 194, 63),
                                shape = RoundedCornerShape(5.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                AsyncImage(
                                    model = curso.icone,
                                    contentDescription = "icone do curso",
                                    colorFilter = ColorFilter.colorMatrix(
                                        ColorMatrix(
                                            floatArrayOf(
                                                -1f, 0f, 0f, 0f, 255f,
                                                0f, -1f, 0f, 0f, 255f,
                                                0f, 0f, -1f, 0f, 255f,
                                                0f, 0f, 0f, 1f, 0f
                                            )
                                        )
                                    ),
                                    modifier = Modifier.size(width = 55.dp, height = 55.dp)
                                )

                                Text(
                                    text = curso.sigla,
                                    color = if (!buttonState) Color(255, 194, 63)
                                    else Color(50, 71, 176),
                                    fontSize = 35.sp,
                                    fontFamily = defaultFont
                                )
                            }
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(text = curso.nome.replace(Regex("[0-9]|-"),
                                    "").drop(2),
                                    color = Color.White,
                                    fontSize = 19.sp,
                                    textAlign = TextAlign.Center,
                                    fontFamily = defaultFont)

                                Spacer(modifier = Modifier.height(25.dp))

                                Text(text = "${curso.carga}hrs",
                                    color = Color.White,
                                    fontSize = 19.sp,
                                    fontFamily = defaultFont,
                                    modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
            Button(modifier = Modifier
                .width(216.dp)
                .height(45.dp),
                shape = RoundedCornerShape(25.dp),
                border = BorderStroke(width = 2.dp, color = Color.White),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(255, 194, 63),
                    contentColor = Color(255, 255, 255, 255),
                    disabledBackgroundColor = Color(92, 111, 205, 125),
                    disabledContentColor = Color(222, 222, 222, 136)
                ),
                enabled = buttonReady,
                onClick = {
                    var openList = Intent(context, ListAlunos::class.java)
                    openList.putExtra("sigla", cursoSelecionado)
                    openList.putExtra("nome", nomeDoCurso)
                    context.startActivity(openList)
                }) {
                Text(text = "Continuar",
                    fontFamily = defaultFont,
                    fontSize = 20.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}