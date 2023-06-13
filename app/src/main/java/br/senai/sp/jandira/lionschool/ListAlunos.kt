package br.senai.sp.jandira.lionschool

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.lionschool.model.Alunos
import br.senai.sp.jandira.lionschool.model.ListaCursos
import br.senai.sp.jandira.lionschool.services.RetrofitFactory
import br.senai.sp.jandira.lionschool.ui.theme.LionSchoolTheme
import coil.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ListAlunos : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val curso = intent.getStringExtra("sigla")
        val nome = intent.getStringExtra("nome")
        setContent {
            LionSchoolTheme {
                if (curso != null) {
                    if (nome != null) {
                        ListAlunosPreview(curso, nome)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ListAlunosScreen(curso: String, nome: String) {

    val defaultFont = FontFamily(Font(R.font.roboto_black))

    var coursingState by remember {
        mutableStateOf(false)
    }

    var finalizedState by remember {
        mutableStateOf(false)
    }

    var listAlunos by remember {
        mutableStateOf(listOf<br.senai.sp.jandira.lionschool.model.Aluno>())
    }

    val context = LocalContext.current

    fun list() {
        val call = RetrofitFactory().getAlunosService().getAlunosByCourse(siglaCurso = curso)
        call.enqueue(object : Callback<Alunos> {
            override fun onResponse(
                call: Call<Alunos>,
                response: Response<Alunos>
            ) {
                listAlunos = response.body()!!.alunos
            }

            override fun onFailure(call: Call<Alunos>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    list()

    fun filterByStatus(status: String){
        val call = RetrofitFactory().getAlunosService().getAlunoByStatusAndCourse(status = status, siglaCurso = curso)
        call.enqueue(object : Callback<Alunos> {
            override fun onResponse(
                call: Call<Alunos>,
                response: Response<Alunos>
            ) {
                listAlunos = if (response.body() != null){
                    response.body()!!.alunos
                } else {
                    emptyList()
                }
            }

            override fun onFailure(call: Call<Alunos>, t: Throwable) {

            }

        })
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(86.dp)
                    .background(color = Color(51, 71, 176),
                    shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "",
                        modifier = Modifier
                            .height(100.dp)
                            .width(100.dp)
                    )

                    Text(
                        text = "Lion School",
                        fontFamily = defaultFont,
                        fontSize = 30.sp,
                        color = Color.White,
                        modifier = Modifier.padding(end = 30.dp)
                    )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = nome.replace(Regex("[0-9]|-"),
                    "").drop(2),
                fontFamily = defaultFont,
                fontSize = 17.sp,
                color = Color(50, 71, 176)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .width(328.dp)
                    .height(45.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconToggleButton(
                    checked = coursingState,
                    onCheckedChange = { checked ->
                        // Ativa o botão atual
                        coursingState = checked

                        if(coursingState && finalizedState){
                            list()
                        } else if (!coursingState && !finalizedState){
                            list()
                        } else if (coursingState && !finalizedState){
                            filterByStatus("Cursando")
                        } else if(!coursingState && finalizedState){
                            filterByStatus("Finalizado")
                        }
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(156.dp)
                        .background(
                            color = if (!coursingState) Color(50, 71, 176)
                            else Color(255, 194, 63),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_create_24),
                            contentDescription = "cursando",
                            tint = if (!coursingState) Color(255, 194, 63)
                            else Color(50, 71, 176),
                            modifier = Modifier.size(25.dp)
                        )
                        Text(
                            text = "Cursando",
                            color = if (!coursingState) Color(255, 194, 63)
                            else Color(50, 71, 176),
                            fontFamily = defaultFont,
                            fontSize = 19.sp
                        )
                    }
                }
                IconToggleButton(
                    checked = finalizedState,
                    onCheckedChange = { checked ->
                        finalizedState = checked
                        if(coursingState && finalizedState){
                            list()
                        } else if (!coursingState && !finalizedState){
                            list()
                        } else if (coursingState && !finalizedState){
                            filterByStatus("Cursando")
                        } else if(!coursingState && finalizedState){
                            filterByStatus("Finalizado")
                        }
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(156.dp)
                        .background(
                            color = if (!finalizedState) Color(50, 71, 176)
                            else Color(255, 194, 63),
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.concluido),
                            contentDescription = "finalizado",
                            tint = if (!finalizedState) Color(255, 194, 63)
                            else Color(50, 71, 176),
                            modifier = Modifier.size(25.dp)
                        )
                        Text(
                            text = "Finalizado",
                            color = if (!finalizedState) Color(255, 194, 63)
                            else Color(50, 71, 176),
                            fontFamily = defaultFont,
                            fontSize = 19.sp
                        )
                    }


                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            LazyColumn(
                modifier = Modifier
                    .height(431.dp)
                    .width(328.dp)
            ) {
                items(listAlunos) { aluno ->
                    Row(
                        modifier = Modifier
                            .width(328.dp)
                            .height(74.dp)
                            .border(
                                width = 2.dp, color = if (aluno.status == "Cursando")
                                    Color(255, 194, 63)
                                else
                                    Color(50, 71, 176),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .background(
                                color = if (aluno.status == "Cursando")
                                    Color(50, 71, 176)
                                else
                                    Color(255, 194, 63),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .padding(12.dp)
                            .clickable { var openStudent = Intent(context, StudentActivity::class.java)
                                openStudent.putExtra("matricula", aluno.matricula)
                                context.startActivity(openStudent) },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier
                            .fillMaxHeight()
                            .width(50.dp)
                            .background(
                                if (aluno.status == "Cursando")
                                    Color(255, 194, 63)
                                else
                                    Color(50, 71, 176),
                                shape = RoundedCornerShape(10.dp)
                            ),
                            contentAlignment = Center) {
                            AsyncImage(model = aluno.foto, contentDescription = "Foto do Aluno")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween) {
                            Text(text = aluno.nome,
                                color = if (aluno.status == "Cursando")
                                    Color.White
                                else
                                    Color(50, 71, 176),
                                fontFamily = defaultFont,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Start)

                            Row(modifier = Modifier
                                .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = "RM: ${aluno.matricula}",
                                    color = if (aluno.status == "Cursando")
                                        Color.White
                                    else
                                        Color(50, 71, 176),
                                    fontFamily = defaultFont,
                                    fontSize = 10.sp)

                                Text(text = aluno.status.uppercase(),
                                    color = if (aluno.status == "Cursando")
                                        Color.White
                                    else
                                        Color(50, 71, 176),
                                    fontFamily = defaultFont,
                                    fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ListAlunosPreview(curso: String, nome: String) {
    ListAlunosScreen(curso, nome)
}