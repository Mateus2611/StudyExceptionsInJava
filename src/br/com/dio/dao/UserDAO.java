package br.com.dio.dao;

import br.com.dio.exception.EmptyStorageException;
import br.com.dio.exception.FileDontExistException;
import br.com.dio.exception.UserNotFoundException;
import br.com.dio.model.UserModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements InterfaceUserDAO {
    private final String currentDir = System.getProperty("user.dir");
    private final String storeDir = "/managedFiles/IO/";
    private final String fileName;
    private long nextId = 1L;
    private List<UserModel> models = new ArrayList<>();

    public UserDAO(String filename) throws FileDontExistException {
        this.fileName = filename;
        var file = new File(currentDir + storeDir);
        if (!file.exists() && !file.mkdirs())
            throw new FileDontExistException("Erro ao criar o arquivo");

        clearFile();
    }


    public String save(final UserModel model) {
        try (
            var fileWriter = new FileWriter(currentDir + storeDir + fileName, true);
            var bufferedWrite = new BufferedWriter(fileWriter);
            var printWriter = new PrintWriter(bufferedWrite);
        ){
            model.setId(nextId++);
            // models.add(model);
            printWriter.println(model.toCsvString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return model.toCsvString();
    }

    public UserModel update(final UserModel model) {
        var toUpdate = findById(model.getId());
        models.remove(toUpdate);
        models.add(model);
        return model;
    }

    public void delete(final long id) {
        var toDelete = findById(id);
        models.remove(toDelete);
    }

    public UserModel findById(final long id) {
        var message = String.format("Não existe usuário com o id %s cadastrado", id);
        verifyStorage();

        return models.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(message));
    }

    public List<UserModel> findAll() {
        List<UserModel> result;

        try {
            verifyStorage();
            result = models;
        } catch (EmptyStorageException ex) {
            ex.printStackTrace();
            result = new ArrayList<>();
        }

        return result;
    }

    private void verifyStorage() {
        if (models.isEmpty())
            throw new EmptyStorageException("O armazenamento está vazio");
    }

    private void clearFile() {
        try (OutputStream outputStream = new FileOutputStream(currentDir + storeDir + fileName)) {
            System.out.printf("Inicializando recursos (%s) \n", currentDir + storeDir + fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
