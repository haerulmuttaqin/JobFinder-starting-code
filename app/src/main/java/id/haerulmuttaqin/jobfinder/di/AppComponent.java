package id.haerulmuttaqin.jobfinder.di;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import id.haerulmuttaqin.jobfinder.App;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, ActivityBuilder.class})
public interface AppComponent {
    
    void inject(App app);
    
    @Component.Builder interface Builder {
        @BindsInstance
        Builder application(App app);
        AppComponent build();
    }
    
    
}
