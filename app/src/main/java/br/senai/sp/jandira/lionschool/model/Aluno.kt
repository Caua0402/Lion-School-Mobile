package br.senai.sp.jandira.lionschool.model

data class Aluno(
    val foto: String,
    val nome: String,
    val matricula: String,
    val status: String,
    val curso: Curso
)
