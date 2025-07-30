import br.com.dio.dao.UserDAO;
import br.com.dio.exception.EmptyStorageException;
import br.com.dio.exception.UserNotFoundException;
import br.com.dio.exception.ValidatorException;
import br.com.dio.model.MenuOption;
import br.com.dio.model.UserModel;
import static br.com.dio.validator.UserValidator.verifyModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    private final static UserDAO dao = new UserDAO();
    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Bem vindo ao cadastro de usuários, selecione a operação desejada");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Ataullizar");
            System.out.println("3 - Excluir");
            System.out.println("4 - Buscar pelo identificador");
            System.out.println("5 - Listar");
            System.out.println("6 - Sair");
            var userInput = scanner.nextInt();

            var selectedOption = MenuOption.values()[userInput - 1];

            switch (selectedOption) {
                case SAVE -> {
                    try {
                        var user = dao.save(requestToSave());
                        System.out.println("Usuário cadastrado.");
                        System.out.println(user);
                    } catch (ValidatorException ex) {
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                case UPDATE -> {
                    try {
                        var user = dao.update(requestToUpdate());
                        System.out.println("usuário atualziado.");
                        System.out.println(user);
                    } catch (UserNotFoundException | EmptyStorageException ex) {
                        System.out.println(ex.getMessage());
                    } catch (ValidatorException ex) {
                        System.out.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                case DELETE -> {
                    try {
                        dao.delete(requestToDelete());
                        System.out.println("Usuário excluído.");
                    } catch (UserNotFoundException | EmptyStorageException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                case FIND_BY_ID -> {
                    try {
                        var user = dao.findById(requestToFindById());
                        System.out.printf("Usuário com id %s \n", user.getId());
                        System.out.println(user);
                    } catch (UserNotFoundException | EmptyStorageException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
                case FIND_ALL -> {
                    var users = dao.findAll();
                    System.out.println("Usuários cadastrados: ");
                    users.forEach(System.out::println);
                }
                case EXIT -> System.exit(0);
            }
        }
    }

    private static UserModel requestToSave() throws ValidatorException {
        System.out.println("Informe o nome do usuário: ");
        var name = scanner.next();
        System.out.println("Informe o email do usuário: ");
        var email = scanner.next();
        System.out.println("Informe a data de nascimento do usuário (dd/MM/yyyy):");
        var birthdayString = scanner.next();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var birthday = LocalDate.parse(birthdayString, formatter);

        return validateInputs((long) 0, name, email, birthday);
    }

    private static UserModel requestToUpdate() throws ValidatorException {
        System.out.println("Informe o identificador do usuário: ");
        var id = scanner.nextLong();
        System.out.println("Informe o nome do usuário: ");
        var name = scanner.next();
        System.out.println("Informe o email do usuário: ");
        var email = scanner.next();
        System.out.println("Informe a data de nascimento do usuário (dd/MM/yyyy)");
        var birthdayString = scanner.next();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var birthday = LocalDate.parse(birthdayString, formatter);

        return validateInputs(id, name, email, birthday);
    }

    private static UserModel validateInputs(final Long id, final String name,
            final String email, final LocalDate birthday) throws ValidatorException {
        var user = new UserModel(id, name, email, birthday);
        verifyModel(user);
        return user;
    }

    private static Long requestToDelete() {
        System.out.println("Informe o identificador do usuário: ");
        return scanner.nextLong();
    }

    private static Long requestToFindById() {
        System.out.println("Informe o identificador do usuário: ");
        return scanner.nextLong();
    }
}
