package fr.uha.chaguer.trainy.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.uha.chaguer.trainy.database.ExerciseDao
import fr.uha.chaguer.trainy.database.RoutineDao
import fr.uha.chaguer.trainy.database.RoutineProgressDao
import fr.uha.chaguer.trainy.database.TrainyDatabase
import fr.uha.chaguer.trainy.repository.ExerciseRepository
import fr.uha.chaguer.trainy.repository.ProgressRepository
import fr.uha.chaguer.trainy.repository.RoutineRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = TrainyDatabase.create(appContext)

    @Singleton
    @Provides
    fun provideExerciceDao(db: TrainyDatabase) = db.exerciseDAO()

    @Singleton
    @Provides
    fun provideExerciceRepository(
        ioDispatcher: CoroutineDispatcher,
        exerciseDao: ExerciseDao
    ) = ExerciseRepository(ioDispatcher, exerciseDao)

    @Singleton
    @Provides
    fun provideRoutineDao(db: TrainyDatabase) = db.routineDAO()

    @Singleton
    @Provides
    fun provideRoutineRepository(
        ioDispatcher: CoroutineDispatcher,
        routineDao: RoutineDao
    ) = RoutineRepository(ioDispatcher, routineDao)

    @Singleton
    @Provides
    fun provideRoutineProgressDao(db: TrainyDatabase) = db.routineProgressDAO()

    @Singleton
    @Provides
    fun provideProgressRepository(
        ioDispatcher: CoroutineDispatcher,
        routineProgressDao: RoutineProgressDao
    ) = ProgressRepository(ioDispatcher, routineProgressDao)

}