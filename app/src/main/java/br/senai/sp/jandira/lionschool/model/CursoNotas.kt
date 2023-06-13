package br.senai.sp.jandira.lionschool.model

data class CursoNotas(
    val nome: String,
    val sigla: String,
    val disciplinas: List<Diciplina>
)
