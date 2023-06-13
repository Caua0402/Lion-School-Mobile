package br.senai.sp.jandira.lionschool.services

import br.senai.sp.jandira.lionschool.model.Alunos
import br.senai.sp.jandira.lionschool.model.AlunosNotas
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AlunosService {
    //https://lionschool-api.cyclic.app/v1/lion-school/

    @GET("alunos")
    fun getAlunos(): Call<Alunos>

    @GET("alunos")
    fun getAlunosByCourse( @Query("curso") siglaCurso: String): Call<Alunos>

    @GET("alunos/{matricula}")
    fun getAlunoByMatricula( @Path("matricula") matricula: String): Call<AlunosNotas>

    @GET("alunos")
    fun getAlunoByNameAndCourse( @Query("nome") nomeAluno: String, @Query("curso") siglaCurso: String): Call<Alunos>

    @GET("alunos")
    fun getAlunoByStatusAndCourse( @Query("curso") siglaCurso: String, @Query("status") status: String): Call<Alunos>

    @GET("alunos")
    fun getAlunoByStatusAndCourseAndName( @Query("nome") nomeAluno: String, @Query("status") status: String, @Query("curso") siglaCurso: String): Call<Alunos>
}