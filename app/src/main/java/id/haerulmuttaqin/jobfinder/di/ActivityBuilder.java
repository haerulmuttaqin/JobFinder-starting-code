package id.haerulmuttaqin.jobfinder.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import id.haerulmuttaqin.jobfinder.ui.MainActivity;
import id.haerulmuttaqin.jobfinder.ui.detail.DetailActivity;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector
    abstract DetailActivity detailActivity();

}
