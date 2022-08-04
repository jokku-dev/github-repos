package com.jokku.githubrepos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jokku.githubrepos.vo.Contributor
import com.jokku.githubrepos.vo.Repo
import com.jokku.githubrepos.vo.RepoSearchResult
import com.jokku.githubrepos.vo.User

@Database(
    entities = [Contributor::class, Repo::class, RepoSearchResult::class, User::class],
    version = 1,
    exportSchema = false
    )
abstract class GithubDatabase: RoomDatabase() {

    abstract fun repoDao(): RepoDao

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: GithubDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                GithubDatabase::class.java,
                "github_db.db"
            ).build()
    }
}