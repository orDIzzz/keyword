package com.ordizzz.keyword;

import java.security.Key;
import java.util.*;
import java.util.stream.Stream;

/**
 * Desk - абстракция над полем с буквами.<br>
 *
 * <ul>Имеет 3 приватных поля:
 * <li>{@code String initString} - последовательность букв, которые будет содежать доска. Длина строки должна быть квадратом
 * целого числа, для равномерного заполнения доски.</li>
 * <li>{@code deskSize} - квадратный корень из длины {@code initString}. Необходим для расчета адреса {@code Address} и индекса букв.</li>
 * <li>{@code keyNodeList} - ArrayList<KeyNode>, содержащий все буквы на доске.</li>
 * </ul>
 */
class Desk {
    private final String initString;
    private int deskSize;
    private static ArrayList<Keynode> keynodeList = new ArrayList<>();

    /**
     * При создании доски в конструктор передается {@code initString} - строка инициализации доски. Проверяется, что её
     * длина является квадратом целого числа. Если нет, т.е. не имеет корня без остатка, то выбрасывается {@link InputMismatchException}.
     * Затем создается {@code ArrayList<Keynode> keynodeList}, содержащий в себе все объекты {@link Keynode} на доске.
     * @param initString - строка инициализации.
     * @throws InputMismatchException если доска не квадратная, т.е. длина строки инициализации не является квадратом
     *                                целого числа.
     */
    public Desk(String initString) {
        this.initString = initString.toUpperCase();
        //Проверяем является ли длина строки квадратом целого числа
        if (Math.sqrt(initString.length()) % 1 == 0) {
            // Сохраняем квадрат длины в переменную deskSize
            this.deskSize = (int) Math.sqrt(initString.length());
        } else {
            // Выкидываем исключение
            throw new InputMismatchException();
        }
        // Разбиваем строку на массив строк
        String[] arrayInitString = initString.split("");
        // В цикле заполняем ArrayList<Keynode> keyNodeList
        for (int i = 0; i < arrayInitString.length; i++) {
            keynodeList.add(new Keynode(arrayInitString[i], i));
        }
        // Заполняем linkedNodes для каждого KeyNode
        initLinkedLists();
    }

    /**
     * Заполняет {@code linkedKeyNodes} для каждого Keynode
     */
    private void initLinkedLists() {
        for (Keynode keynode : keynodeList) {
            for (Map.Entry<Keynode.Address, Keynode> entry : keynode.linkedKeynodes.entrySet()) {
                entry.setValue(Keynode.getKeynodeByAddress(entry.getKey()));
            }
        }
    }

    /**
     * @return Возвращает весь список Keynode
     */
    public ArrayList<Keynode> getKeynodeList() {
        return keynodeList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Keynode node : keynodeList) {
            if (node.keynodeAddress.y != deskSize - 1) {
                sb.append(node.keynodeValue).append("\t");
            } else {
                sb.append(node.keynodeValue).append("\n");
            }
        }
        return sb.toString();
    }

    private String finder(Keynode keynode, List<String> toFind, ArrayList<Keynode> excludedNodes){

        String result = "";
        //Создаем копию из linkedNodes текущего keynode
        Map<Keynode.Address,Keynode> copyOfLinkedNodes = new HashMap<>(keynode.linkedKeynodes);
        // Через поток проверяем присутствует ли элемент linkedNodes в excludedNodes
        keynode.linkedKeynodes.forEach((key, value) -> {
            for (Keynode excludedNode : excludedNodes) {
                // Если присутствует то удаляем из копии copyOfLinkedNodes элемент с таким же адресом
                if (excludedNode.keynodeAddress.equals(key)) {
                    copyOfLinkedNodes.remove(key);
                }
            }
        });
        // Каждый элемент отфильтрованного списка проверяем на соответствие следуюей букве для поиска
        for (Map.Entry<Keynode.Address,Keynode> entry: copyOfLinkedNodes.entrySet()) {
            // Если текущий элемент linkedNodes соответствует следующей букве для поиска
            if(
                    // Значение текущего keynode из списка linkedNodes равно первой букве в искомой последовательности
                    entry.getValue().keynodeValue.equals(toFind.get(0)) &
                    // И в строке result нет -1
                    !(result.contains("-1"))
            )
            {
                // Если это последний элемент
                if(toFind.size()==1) {
                    // Возвращаем добавляем в стркоу result адрес последнего keynode и возвращаем строку
                    return result +=entry.getKey().toString();
                } else if(toFind.size()> 1){
                    excludedNodes.add(entry.getValue());
                    List<String> copyToFind = new ArrayList<>(toFind);
                    copyToFind.remove(0);
                    result+=entry.getKey() + finder(entry.getValue(),copyToFind, excludedNodes);
                }
            }
        }
        return "-1";
    }
    public String findWord(String word) {
        // Приводим строку к ArrayList, чтобы было проще
        List<String> normalizedWord = new ArrayList<>(Arrays.asList(word.toUpperCase().split("")));
        String firstElement = normalizedWord.get(0);
        // Создаем список исключенных нод
        ArrayList<Keynode> excludedNodes = new ArrayList<>();
        // Создаем результирующую строку
        String result = "";
        // Ищем первую ноду
        for (Keynode keynode: keynodeList) {
            // Если значение текущего keynode совпадает с первым элементом в искомом слове
            if(keynode.keynodeValue.equals(firstElement)){
                // Если нашли, добавляем первую ноду в список сиключенных
                excludedNodes.add(keynode);
                // Удаляем первый символ из строки поиска, т.к. его уже нашли
                normalizedWord.remove(0);
                result+=keynode.getKeynodeAddress() + "->";
                // Если результируюся строка содержит "-1" то идем дальше по общему списку нод
                if ((result = finder(keynode, normalizedWord, excludedNodes)).toString().contains("-1")){
                    continue;
                };
                return result;
            }

        }
        return "Not found";
    }

    /**
     * Keynode - абстракция над буквой на доске.<br>
     * <ul>Имеет следующие поля:
     * <li>{@code Map<Address , Keynode> linkedKeynodes} - для каждой буквы имеется список соседних букв.</li>
     * <li>{@code Address keynodeAddress} - адрес буквы в формате {@code [строка, столбец]}.</li>
     * <li>{@code String keynodeValue} - значение буквы.</li>
     * </ul>
     */
    class Keynode {
        Map<Address, Keynode> linkedKeynodes = new HashMap<>();
        Address keynodeAddress;
        String keynodeValue;

        /**
         * @param keynode        значение буквы, хранящейся в keynode
         * @param indexOfKeynode индекс в строке инициализации для расчета адреса keynode.
         */
        private Keynode(String keynode, int indexOfKeynode) {
            // Создается объект Address, расчитанный по индексу в строке инициализации доски
            this.keynodeAddress = new Address(indexOfKeynode);
            this.keynodeValue = keynode;
            // Создаются 4 соседние клетки, даже несуществующие
            linkedKeynodes.put((new Address(this.keynodeAddress.x - 1, this.keynodeAddress.y)), null);
            linkedKeynodes.put((new Address(this.keynodeAddress.x + 1, this.keynodeAddress.y)), null);
            linkedKeynodes.put((new Address(this.keynodeAddress.x, this.keynodeAddress.y - 1)), null);
            linkedKeynodes.put((new Address(this.keynodeAddress.x, this.keynodeAddress.y + 1)), null);
            Map<Address, Keynode> copy = new HashMap<>(linkedKeynodes);
            // Затем из списка удаляются несуществующие KeyNode.
            for (Map.Entry<Address, Keynode> entry : copy.entrySet()) {
                // Если адрес KeyNode выходит за границы доски то такой KeyNode удаляется
                if (
                        entry.getKey().x < 0 ||
                                entry.getKey().y < 0 ||
                                entry.getKey().x >= deskSize ||
                                entry.getKey().y >= deskSize
                ) {
                    linkedKeynodes.remove(entry.getKey());
                }
            }
        }

        /**
         * @return String Возвращает значение буквы, хранящейся в Keynode
         */
        public String getValue() {
            return keynodeValue;
        }

        @Override
        public String toString() {
            return keynodeValue + "->" + keynodeAddress;
        }

        /**
         * @return {@code Address} Возвращает адрес Keynode
         */
        public Address getKeynodeAddress() {
            return this.keynodeAddress;
        }

        /**
         * @param address {@code Address} ноды, которую надо получить как объект
         * @return Возвращает {@code Keynode} по переданнаму адресу
         */
        public static Keynode getKeynodeByAddress(Address address) {
            Keynode result = null;
            for (Keynode keynode : keynodeList) {
                if (address.equals(keynode.keynodeAddress)) {
                    result = keynode;
                    return result;
                }
            }
            return result;
        }

        public Map<Address, Keynode> getLinkedKeynodes() {
            return linkedKeynodes;
        }

        /**
         * Простой класс, который содержит {@code x} - строка и {@code y} - столбец.
         * Нумерация начинается с 0.
         */
        class Address {
            final int x;
            final int y;

            private Address(int index) {
                if (index == 0) {
                    this.x = 0;
                    this.y = 0;
                } else {
                    this.x = index / deskSize;
                    this.y = index % deskSize;
                }
            }

            @Override
            public String toString() {
                return "[" + x + ", " + y + "]";
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Address address = (Address) o;
                return x == address.x && y == address.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }

            private Address(int x, int y) {
                this.x = x;
                this.y = y;
            }

            /**
             * @param address  адрес ({@code Address}) Keynode
             * @param deskSize размер доски.
             * @return Возвращает индекс в строке инициализации по переданному адресу
             */
            public static int getIndexByAddress(Address address, int deskSize) {
                return (address.x * deskSize) + address.y;
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }
        }
    }
}
