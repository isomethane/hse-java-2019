package ru.hse.mnmalysheva.phonebook;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

/** Console phonebook database UI. **/
public class PhonebookUI {
    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private static final String OPTIONS =
            "*-------------------------------------*\n" +
            "| 0 - exit                            |\n" +
            "| 1 - add record                      |\n" +
            "| 2 - print all phone numbers by name |\n" +
            "| 3 - print all names by phone number |\n" +
            "| 4 - delete record                   |\n" +
            "| 5 - change name of record           |\n" +
            "| 6 - change phone number of record   |\n" +
            "| 7 - print all records               |\n" +
            "*-------------------------------------*\n" +
            "Choose option:";

    /**
     * Database main lifecycle.
     * Available options:
     * - exit
     * - add record
     * - print all phone numbers by name
     * - print all names by phone number
     * - delete record
     * - change name of record
     * - change phone number of record
     * - print all records
     */
    public static void main(String[] args) {
        var phonebook = PhonebookManager.getInstance();
        var scanner = new Scanner(System.in);
        boolean toExit = false;

        while (!toExit) {
            System.out.println(OPTIONS);
            int option;
            try {
                option = Integer.parseInt(scanner.nextLine().replaceAll("\\s", ""));
            } catch (NumberFormatException e) {
                option = -1;
            }
            String name, newName;
            String phone, newPhone;
            String result;
            switch (option) {
                case 0:
                    phonebook.close();
                    toExit = true;
                    break;
                case 1:
                    name = readName("name", scanner);
                    phone = readPhone("phone number", scanner);
                    phonebook.add(name, phone);
                    break;
                case 2:
                    name = readName("name", scanner);
                    result = String.join(", ", phonebook.getPhonesByName(name));
                    if (result.isEmpty()) {
                        System.out.println("There are no phones owned by " + name + " in the phonebook");
                    } else {
                        System.out.println(result);
                    }
                    break;
                case 3:
                    phone = readPhone("phone", scanner);
                    result = String.join(", ", phonebook.getNamesByPhone(phone));
                    if (result.isEmpty()) {
                        System.out.println("There are no owners of " + phone + " in the phonebook");
                    } else {
                        System.out.println(result);
                    }
                    break;
                case 4:
                    name = readName("name", scanner);
                    phone = readPhone("phone number", scanner);
                    phonebook.delete(name, phone);
                    break;
                case 5:
                    name = readName("name", scanner);
                    phone = readPhone("phone number", scanner);
                    newName = readName("new name", scanner);
                    phonebook.changeName(name, phone, newName);
                    break;
                case 6:
                    name = readName("name", scanner);
                    phone = readPhone("phone number", scanner);
                    newPhone = readPhone("new phone number", scanner);
                    phonebook.changePhone(name, phone, newPhone);
                    break;
                case 7:
                    phonebook.getContent().forEach((keyName, phones) ->
                            phones.forEach(
                                    phoneNumber -> System.out.println("Name: " + keyName + ", Phone: " + phoneNumber)
                            )
                    );
                    break;
                default:
                    System.out.println("Incorrect option.");
            }
        }
    }

    private static @NotNull String unifyNumber(@NotNull String phoneNumber) throws NumberParseException {
        var parsedNumber = phoneUtil.parse(phoneNumber, "RU");
        return phoneUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
    }

    private static @NotNull String readName(@NotNull String prompt, @NotNull Scanner scanner) {
        System.out.println("Input " + prompt + ":");
        return scanner.nextLine();
    }

    private static @NotNull String readPhone(@NotNull String prompt, @NotNull Scanner scanner) {
        System.out.println("Input " + prompt + ":");
        boolean isCorrect = false;
        String phone = null;
        while (!isCorrect) {
            try {
                phone = unifyNumber(scanner.nextLine());
                isCorrect = true;
            } catch (NumberParseException e) {
                System.out.println("Incorrect phone number, please try again:");
            }
        }
        return phone;
    }
}
