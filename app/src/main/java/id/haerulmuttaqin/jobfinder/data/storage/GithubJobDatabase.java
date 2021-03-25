package id.haerulmuttaqin.jobfinder.data.storage;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import id.haerulmuttaqin.jobfinder.Constants;
import id.haerulmuttaqin.jobfinder.data.entity.GithubJob;


@Database(entities = GithubJob.class, version = 1)
public abstract class GithubJobDatabase extends RoomDatabase {

    public static Context context;
    private static GithubJobDatabase instance;

    public static GithubJobDatabase getDatabase(Context ctx) {
        context = ctx;
        if (instance == null) {
            synchronized (GithubJobDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, GithubJobDatabase.class, Constants.MASTER_DB)
                            .allowMainThreadQueries()
                            .setJournalMode(JournalMode.TRUNCATE)
                            .build();
                }
            }
        }
        return instance;
    }

    public abstract GithubJobDao githubJobDao();
}