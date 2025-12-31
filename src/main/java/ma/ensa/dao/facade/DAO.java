package ma.ensa.dao.facade;

import ma.ensa.model.Livre;
import ma.ensa.model.Membre;
import ma.ensa.model.Result;

import java.util.List;

public interface DAO <T>{
    Result<T> create(T t);
    Result<T> findById(int id);
    Result<List<T>> findAll();
    Result<Boolean> delete(int id);

    Result<T> update(T t);

}
