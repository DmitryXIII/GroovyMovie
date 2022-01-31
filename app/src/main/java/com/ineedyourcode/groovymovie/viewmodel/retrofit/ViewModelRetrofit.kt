package com.ineedyourcode.groovymovie.viewmodel.retrofit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ineedyourcode.groovymovie.model.Movie
import com.ineedyourcode.groovymovie.model.tmdb.TMDBMovieDTO
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.IRetrofitRepository
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.RemoteDataSource
import com.ineedyourcode.groovymovie.model.tmdb.retrofit.RetrofitRepository
import com.ineedyourcode.groovymovie.viewmodel.mainscreen.AppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class ViewModelRetrofit(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitRepository: IRetrofitRepository = RetrofitRepository(RemoteDataSource())
) : ViewModel() {

    fun getData() = liveData

    fun getTopRatedFromRemoteSource(lang: String, page: Int) {
        liveData.value = AppState.Loading
        retrofitRepository.getTopRatedMovies(lang, page)
    }

    private val callback = object : Callback<List<TMDBMovieDTO>> {
        @Throws(IOException::class)
        override fun onResponse(
            call: Call<List<TMDBMovieDTO>>,
            response: Response<List<TMDBMovieDTO>>
        ) {
            val serverResponse: List<TMDBMovieDTO>? = response.body()
            liveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(SERVER_ERROR)
                }
            )
        }

        override fun onFailure(call: Call<List<TMDBMovieDTO>>, t: Throwable) {
            liveData.postValue(AppState.Error(REQUEST_ERROR))
        }
    }

    private fun checkResponse(serverResponse: List<TMDBMovieDTO>): AppState {
        val topRatedList = serverResponse
        return if (topRatedList.isNullOrEmpty()) {
            AppState.Error(CORRUPTED_DATA)
        } else {
            AppState.Success(convertDtoToModel(serverResponse), setOf())
        }
    }

    private fun convertDtoToModel(serverResponse: List<TMDBMovieDTO>): Map<String, Movie> {
        val moviesMap = mutableMapOf<String, Movie>()
        serverResponse.forEach { movieDTO ->
            val movie = Movie(
                movieDTO.id.toString(),
                movieDTO.title,
                movieDTO.releaseDate,
                movieDTO.voteAverage.toString(),
                movieDTO.genreIds[0].toString(),
                movieDTO.overview
            )
            moviesMap[movieDTO.id.toString()] = movie

        }
        return moviesMap
    }
}