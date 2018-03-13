package de.dominikwieners.working.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.dominikwieners.working.data.TypeDao;
import de.dominikwieners.working.repository.TypeDataSource;
import de.dominikwieners.working.repository.TypeDatabase;
import de.dominikwieners.working.repository.TypeRepository;

/**
 * Created by dominikwieners on 13.03.18.
 */
@Module
public class RoomModule {

    private TypeDatabase typeDatabase;

    public RoomModule(Application application) {
        typeDatabase = Room.databaseBuilder(application, TypeDatabase.class, "wk-type-db").allowMainThreadQueries().build();
    }

    @Provides
    @Singleton
    TypeDatabase provideAppDatabase() {
        return typeDatabase;
    }

    @Singleton
    @Provides
    TypeDao provideTypeDao(TypeDatabase typeDatabase) {
        return typeDatabase.getTypeDao();
    }

    @Singleton
    @Provides
    TypeRepository typeRepository(TypeDao typeDao) {
        return new TypeDataSource(typeDao);
    }
}
