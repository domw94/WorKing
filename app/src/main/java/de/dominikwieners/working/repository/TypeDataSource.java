package de.dominikwieners.working.repository;

import javax.inject.Inject;

import de.dominikwieners.working.data.TypeDao;

/**
 * Created by dominikwieners on 13.03.18.
 */

public class TypeDataSource implements TypeRepository {
    private TypeDao typeDao;

    @Inject
    public TypeDataSource(TypeDao typeDao) {
        this.typeDao = typeDao;
    }
}
