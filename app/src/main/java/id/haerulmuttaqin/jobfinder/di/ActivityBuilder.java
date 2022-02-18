package id.haerulmuttaqin.jobfinder.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import id.haerulmuttaqin.jobfinder.ui.MainActivity;
import id.haerulmuttaqin.jobfinder.ui.detail.DetailActivity;
import id.haerulmuttaqin.jobfinder.ui.list.ListActivity;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();
    @ContributesAndroidInjector
    abstract DetailActivity detailActivity();
    @ContributesAndroidInjector
    abstract ListActivity listActivity();

}
