package ua.com.javarush.gnew.m2.cli;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import ua.com.javarush.gnew.m2.entity.Contact;
import ua.com.javarush.gnew.m2.service.PhoneBookInterface;

import java.util.concurrent.Callable;

@Command(name = "phonebook", mixinStandardHelpOptions = true, version = "PhoneBook CLI 1.0",
        description = "CLI для управління контактами в телефонній книзі")
public class PhoneBookCLI implements Callable<Integer> {

    @Override
    public Integer call() {
        System.out.println("Використовуйте одну з команд: add, search, edit, delete, list");
        return 0;
    }

    @Command(name = "add",aliases = {"-a"}, description = "Додає новий контакт до телефонної книги", mixinStandardHelpOptions = true)
    public static class AddContact implements Callable<Integer> {

        private final PhoneBookInterface phoneBookInterface;

        @Option(names = {"-n", "--name"}, description = "Ім'я контакту", required = true)
        private String name;

        @Option(names = {"-p", "--phone"}, description = "Номер телефону", required = true, arity = "0..3")
        private String[] phones;
        @Option(names = {"-e", "--email"}, description = "Електронна пошта", required = true,arity = "0..3")
        private String[] emails;

        public AddContact(PhoneBookInterface phoneBookInterface) {
            this.phoneBookInterface = phoneBookInterface;
        }

        @Override
        public Integer call() {
            Contact contact = new Contact(name,phones,emails);
            phoneBookInterface.add(contact);
            System.out.println("Контакт додано: " + name + " - " + phones[0]);
            return 0;
        }
    }

    @Command(name = "search",aliases = {"-s"},mixinStandardHelpOptions = true, description = "Шукає контакт за ім'ям")
    public static class SearchContact implements Callable<Integer> {

        private final PhoneBookInterface phoneBookInterface;

        @Parameters(index = "0", description = "Ім'я для пошуку", arity = "0..1")
        private String name;

        public SearchContact(PhoneBookInterface phoneBookInterface) {
            this.phoneBookInterface = phoneBookInterface;
        }

        @Override
        public Integer call() {
            phoneBookInterface.search(name);
            return 0;
        }
    }

    @Command(name = "edit",aliases = {"-e"}, description = "Редагує існуючий контакт за ім'ям")
    public static class EditContact implements Callable<Integer> {
        private final PhoneBookInterface phoneBookInterface;

        @Option(names = {"-n", "--name"}, description = "Ім'я контакту", required = true)
        private String name;

        @Option(names = {"-p", "--phone"}, description = "Новий номер телефону", required = false,arity = "0..1")
        private String[] newPhone;

        @Option(names = {"-e", "--email"}, description = "Електронна пошта", required = false,arity = "0..1")
        private String[] emails;

        public EditContact(PhoneBookInterface phoneBookInterface) {
            this.phoneBookInterface = phoneBookInterface;
        }

        @Override
        public Integer call() {
            Contact contact = new Contact(name,newPhone,emails);
            phoneBookInterface.edit(contact);
            System.out.println("Контакт оновлено: " + name + " - " + newPhone);
            return 0;
        }
    }

    @Command(name = "delete",aliases = {"-d"}, description = "Видаляє контакт за ім'ям",mixinStandardHelpOptions = true)
    public static class DeleteContact implements Callable<Integer> {

        private final PhoneBookInterface phoneBookInterface;

        @Option(names = {"-n", "--name"}, description = "Ім'я для видалення", required = true)
        private String name;

        public DeleteContact(PhoneBookInterface phoneBookInterface) {
            this.phoneBookInterface = phoneBookInterface;
        }

        @Override
        public Integer call() {
            phoneBookInterface.delete(name);
            System.out.println("Контакт видалено: " + name);
            return 0;
        }
    }

    @Command(name = "list",aliases = {"-ls"}, description = "Виводить усі контакти",mixinStandardHelpOptions = true)
    public static class ListContacts implements Callable<Integer> {
        private final PhoneBookInterface phoneBookInterface;

        public ListContacts(PhoneBookInterface phoneBookInterface) {
            this.phoneBookInterface = phoneBookInterface;
        }

        @Override
        public Integer call() {
            phoneBookInterface.list().forEach(System.out::println);
            return 0;
        }
    }
}
