import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final Scanner scan = new Scanner(System.in);
    public static final int MIN_CHOICE = 0;
    public static final int MAX_CHOICE = 3;
    public static final int MIN_VALUE = Integer.MIN_VALUE;
    public static final int MAX_VALUE = Integer.MAX_VALUE;

    public enum Status {
        GOOD,
        NOT_TXT,
        UNREADABLE_FILE,
        BAD_FILE,
        NOT_AN_INT,
        OUT_OF_RANGE,
        NO_FILE,
        ELEMENT_NOT_FOUND
    }

    static final String[] ERR_TEXT = {
            "",
            "Заданный файл имеет неверное расширение.\n",
            "Данный файл невозможно прочитать.\n",
            "Файл содержит некорректные данные.\n",
            "Введенное число не является целым числом.\n",
            "Введенное значение вне диапазона.\n",
            "Файл не открыт. Сначала загрузите файл.\n",
            "Элемент не найден ни в одном списке.\n"
    };

    static class ListNode {
        int data;
        ListNode next;

        ListNode(int data) {
            this.data = data;
            this.next = null;
        }
    }

    public static void writeTask() {
        System.out.println("Данная программа позволяет работать с массивом односвязных списков.");
        System.out.println("Ввод данных производится через текстовые файлы.");
    }

    public static void writeOptions() {
        System.out.println("""
                Выберите действие:
                0 - Выход из программы;
                1 - Загрузить массивы списков из файла;
                2 - Удалить элемент из всех списков;
                3 - Вывести массивы списков на экран;
                """);
    }

    public static int getNum(int min, int max) {
        Status stat;
        int input = 0;
        do {
            stat = Status.GOOD;
            try {
                input = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                stat = Status.NOT_AN_INT;
            }
            if (stat == Status.GOOD && (input < min || input > max)) {
                stat = Status.OUT_OF_RANGE;
            }
            System.err.print(ERR_TEXT[stat.ordinal()]);
        } while (stat != Status.GOOD);
        return input;
    }

    public static int getChoice() {
        writeOptions();
        return getNum(MIN_CHOICE, MAX_CHOICE);
    }

    public static String getFilePath() {
        String pathToFile;
        Status err;
        System.out.println("Введите название текстового файла (путь к файлу).");
        do {
            pathToFile = scan.nextLine();
            if (pathToFile.endsWith(".txt")) {
                err = Status.GOOD;
            } else {
                err = Status.NOT_TXT;
            }
            System.err.print(ERR_TEXT[err.ordinal()]);
        } while (err != Status.GOOD);
        return pathToFile;
    }

    public static Status loadLists(List<ListNode[]> arrayOfLists, String path) {
        try {
            Scanner fileScanner = new Scanner(new File(path));
            arrayOfLists.clear();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] elements = line.split("\\s+");
                ListNode[] listArray = new ListNode[elements.length];

                try {
                    for (int i = 0; i < elements.length; i++) {
                        int value = Integer.parseInt(elements[i]);
                        listArray[i] = new ListNode(value);
                        if (i > 0) {
                            listArray[i-1].next = listArray[i];
                        }
                    }
                    arrayOfLists.add(listArray);
                } catch (NumberFormatException e) {
                    return Status.BAD_FILE;
                }
            }
            fileScanner.close();
            return Status.GOOD;
        } catch (FileNotFoundException e) {
            return Status.UNREADABLE_FILE;
        }
    }

    public static Status deleteElement(List<ListNode[]> arrayOfLists, int element) {
        boolean found = false;

        for (ListNode[] listArray : arrayOfLists) {
            if (listArray == null || listArray.length == 0) continue;
            ListNode current = listArray[0];
            ListNode prev = null;

            while (current != null && current.data == element) {
                found = true;
                if (prev == null) {
                    int index = 0;
                    while (index < listArray.length && listArray[index] == current) {
                        listArray[index] = current.next;
                        index++;
                    }
                    current = current.next;
                }
            }

            if (current != null) {
                prev = current;
                current = current.next;

                while (current != null) {
                    if (current.data == element) {
                        found = true;
                        prev.next = current.next;
                        for (int i = 0; i < listArray.length; i++) {
                            if (listArray[i] == current) {
                                listArray[i] = prev;
                            }
                        }
                        current = prev.next;
                    } else {
                        prev = current;
                        current = current.next;
                    }
                }
            }
        }

        return found ? Status.GOOD : Status.ELEMENT_NOT_FOUND;
    }

    public static void printLists(List<ListNode[]> arrayOfLists) {
        if (arrayOfLists.isEmpty()) {
            System.out.println("Массив списков пуст.");
            return;
        }

        for (int i = 0; i < arrayOfLists.size(); i++) {
            System.out.print("Список " + (i+1) + ": ");
            ListNode[] listArray = arrayOfLists.get(i);
            if (listArray == null || listArray.length == 0) {
                System.out.println("пуст");
                continue;
            }

            ListNode current = listArray[0];
            while (current != null) {
                System.out.print(current.data + " ");
                current = current.next;
            }
            System.out.println();
        }
    }

    public static void workWithLists() {
        List<ListNode[]> arrayOfLists = new ArrayList<>();
        Status stat = Status.NO_FILE;
        boolean running = true;

        while (running) {
            switch (getChoice()) {
                case 0:
                    running = false;
                    stat = Status.GOOD;
                    break;
                case 1:
                    String path = getFilePath();
                    stat = loadLists(arrayOfLists, path);
                    break;
                case 2:
                    if (stat == Status.NO_FILE || arrayOfLists.isEmpty()) {
                        stat = Status.NO_FILE;
                    } else {
                        System.out.println("Введите элемент для удаления:");
                        int element = getNum(MIN_VALUE, MAX_VALUE);
                        stat = deleteElement(arrayOfLists, element);
                    }
                    break;
                case 3:
                    if (stat == Status.NO_FILE || arrayOfLists.isEmpty()) {
                        stat = Status.NO_FILE;
                    } else {
                        printLists(arrayOfLists);
                        stat = Status.GOOD;
                    }
                    break;
            }
            System.err.print(ERR_TEXT[stat.ordinal()]);
            if (stat != Status.NO_FILE && stat != Status.ELEMENT_NOT_FOUND) {
                stat = Status.GOOD;
            }
        }
    }

    public static void main(String[] args) {
        writeTask();
        workWithLists();
        scan.close();
    }
}