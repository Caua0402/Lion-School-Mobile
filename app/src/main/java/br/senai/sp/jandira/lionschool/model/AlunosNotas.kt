package br.senai.sp.jandira.lionschool.model

data class AlunosNotas(
    val foto: String,
    val nome: String,
    val matricula: String,
    val status: String,
    var curso: CursoNotas
)
