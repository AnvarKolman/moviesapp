package com.oder.cinema.di

import android.content.Context
import androidx.room.Room
import com.oder.cinema.FavoriteMoviesFragment
import com.oder.cinema.MainActivity
import com.oder.cinema.MoviesFragment
import com.oder.cinema.data.room.MoviesDatabase
import com.oder.cinema.data.MoviesRepository
import com.oder.cinema.data.MoviesRepositoryImpl
import com.oder.cinema.data.MoviesService
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)

    fun inject(moviesFragment: MoviesFragment)
    fun inject(favoriteMoviesFragment: FavoriteMoviesFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

}

@Module(includes = [NetworkModule::class, DatabaseModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(
        moviesService: MoviesService,
        moviesDatabase: MoviesDatabase,
    ): MoviesRepository {
        return MoviesRepositoryImpl(moviesService, moviesDatabase)
    }
}

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideMoviesDatabase(
        applicationContext: Context
    ): MoviesDatabase = Room.databaseBuilder(
        applicationContext, MoviesDatabase::class.java, "movies-database"
    ).build()

}

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideMoviesService(): MoviesService {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

        val retrofit = Retrofit.Builder().baseUrl("https://api.kinopoisk.dev").client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build()
        return retrofit.create()
    }

    //fun provideTestApi(retrofit: Retrofit): TestApi
}