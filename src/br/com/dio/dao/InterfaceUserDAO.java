package br.com.dio.dao;

import br.com.dio.model.UserModel;
import java.util.List;

public interface InterfaceUserDAO {
    public String save(final UserModel model);

    public UserModel update(final UserModel model);

    public void delete(final long id);

    public UserModel findById(final long id);
    
    public List<UserModel> findAll();
}
