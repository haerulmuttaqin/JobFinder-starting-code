package id.haerulmuttaqin.jobfinder.data.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;
import io.reactivex.annotations.NonNull;


@Database(entities = GithubJob.class, version = 1,exportSchema = false)

public abstract class GithubJobDatabase extends RoomDatabase {

    public static Context context;
    private static GithubJobDatabase instance;

    public static GithubJobDatabase getDatabase(Context ctx) {
        context = ctx;
        if (instance == null) {
            synchronized (GithubJobDatabase.class) {
                if (instance == null) {
//                    instance = Room.databaseBuilder(context, GithubJobDatabase.class, Constants.MASTER_DB)
//                            .allowMainThreadQueries()
//                            .setJournalMode(JournalMode.TRUNCATE)
//                            .build();

                    instance = Room.databaseBuilder(context.getApplicationContext(),GithubJobDatabase.class,"movie_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallBack)
                            .build();

                }
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    public abstract GithubJobDao githubJobDao();
}