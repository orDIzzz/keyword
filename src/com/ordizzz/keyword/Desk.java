package com.ordizzz.keyword;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Desk - абстракци€ над полем с буквами.<br>
 *
 * <ul>»меет 3 приватных пол€:
 * <li>{@code String initString} - последовательность букв, которые будет содежать доска. ƒлина строки должна быть квадратом
 * целого числа, дл€ равномерного заполнени€ доски.</li>
 * <li>{@code deskSize} - квадратный корень из длины {@code initString}. Ќеобходим дл€ расчета адреса {@code Address} и индекса букв.</li>
 * <li>{@code keyNodeList} - ArrayList<KeyNode>, содержащий все буквы на доске.</li>
 * </ul>
 */
class Desk {
    private final String initString;
    private int deskSize;
    private static ArrayList<Keynode> keynodeList = new ArrayList<>();

    /**
     * ѕри создании доски в конструктор передаетс€ {@code initString} - строка инициализации доски. ѕровер€етс€, что еЄ
     * длина €вл€етс€ квадратом целого числа. ≈сли нет, т.е. не имеет корн€ без остатка, то выбрасываетс€ {@link InputMismatchException}.
     * «атем создаетс€ {@code ArrayList<Keynode> keynodeList}, содержащий в себе все объекты {@link Keynode} на доске.
     * @param initString - строка инициализации.
     * @throws InputMismatchException если доска не квадратна€, т.е. длина строки инициализации не €вл€етс€ квадратом
     *                                целого числа.
     */
    public Desk(String initString) {
        this.initString = initString.toUpperCase();
        //ѕровер€ем €вл€етс€ ли длина строки квадратом целого числа
        if (Math.sqrt(initString.length()) % 1 == 0) {
            // —охран€ем квадрат длины в переменную deskSize
            this.deskSize = (int) Math.sqrt(initString.length());
        } else {
            // ¬ыкидываем исключение
            throw new InputMismatchException();
        }
        // –азбиваем строку на массив строк
        String[] arrayInitString = initString.split("");
        // ¬ цикле заполн€ем ArrayList<Keynode> keyNodeList
        for (int i = 0; i < arrayInitString.length; i++) {
            keynodeList.add(new Keynode(arrayInitString[i], i));
        }
        // «аполн€ем linkedNodes дл€ каждого KeyNode
        initLinkedLists();
    }

    /**
     * «аполн€ет {@code linkedKeynodes} дл€ каждого Keynode
     */
    private void initLinkedLists() {
        for(Keynode keynode: keynodeList) {
            Keynode.Address currentAddress = keynode.getKeynodeAddress();
            if(keynode.getKeynodeAddress().getRow()>0){
                keynode.linkedKeynodes.add(Keynode.getKeynodeByAddress(keynode.new Address(currentAddress.getRow()-1,currentAddress.getCol())));
            }
            if(keynode.getKeynodeAddress().getRow()<deskSize-1){
                keynode.linkedKeynodes.add(Keynode.getKeynodeByAddress(keynode.new Address(currentAddress.getRow()+1,currentAddress.getCol())));
            }
            if(keynode.getKeynodeAddress().getCol()>0){
                keynode.linkedKeynodes.add(Keynode.getKeynodeByAddress(keynode.new Address(currentAddress.getRow(),currentAddress.getCol()-1)));
            }
            if(keynode.getKeynodeAddress().getCol()<deskSize-1){
                keynode.linkedKeynodes.add(Keynode.getKeynodeByAddress(keynode.new Address(currentAddress.getRow(),currentAddress.getCol()+1)));
            }

        }
    }

    /**
     * @return ¬озвращает весь список Keynode
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
    public String finder(String word,Keynode currentKeynode) {
        List<String> normalizedWord = new ArrayList<>(Arrays.asList(word.split("")));
        if(currentKeynode.getValue().equals(normalizedWord.get(0))){
            String result = currentKeynode.getKeynodeAddress().toString();
            normalizedWord.remove(0);
            for(Keynode keynode: currentKeynode.getLinkedKeynodes()){
                result+=finder(normalizedWord.stream().collect(Collectors.joining("")),keynode);
            }
            return result;
        }

        return "-1";
    }
    public String findWord(String word) {
        List<String> normalizedWord = new ArrayList<>(Arrays.asList(word.toUpperCase().split("")));
        for(Keynode keynode: getKeynodeList()){
            String result = "";
            if((result = finder(normalizedWord.stream().collect(Collectors.joining("")),keynode)).contains("-1")){
               return result;
            }
        }
        return "Not found";
    }

    /**
     * Keynode - абстракци€ над буквой на доске.<br>
     * <ul>»меет следующие пол€:
     * <li>{@code Map<Address , Keynode> linkedKeynodes} - дл€ каждой буквы имеетс€ список соседних букв.</li>
     * <li>{@code Address keynodeAddress} - адрес буквы в формате {@code [строка, столбец]}.</li>
     * <li>{@code String keynodeValue} - значение буквы.</li>
     * </ul>
     */
    class Keynode {
        ArrayList<Keynode> linkedKeynodes = new ArrayList<>();
        Address keynodeAddress;
        String keynodeValue;

        /**
         * @param keynode значение буквы, хран€щейс€ в keynode
         * @param indexOfKeynode индекс в строке инициализации дл€ расчета адреса keynode.
         */
        private Keynode(String keynode, int indexOfKeynode) {
            // —оздаетс€ объект Address, расчитанный по индексу в строке инициализации доски
            this.keynodeAddress = new Address(indexOfKeynode);
            this.keynodeValue = keynode;
        }

        @Override
        public String toString() {
            return keynodeValue + "->" + keynodeAddress;
        }

        /**
         * @return {@code Address} ¬озвращает адрес Keynode
         */
        public Address getKeynodeAddress() {
            return this.keynodeAddress;
        }

        public String getValue() {
            return keynodeValue;
        }

        /**
         * @param address {@code Address} ноды, которую надо получить как объект
         * @return ¬озвращает {@code Keynode} по переданнаму адресу
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

        public ArrayList<Keynode> getLinkedKeynodes() {
            return linkedKeynodes;
        }

        /**
         * ѕростой класс, который содержит {@code x} - строка и {@code y} - столбец.
         * Ќумераци€ начинаетс€ с 0.
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
             * @return ¬озвращает индекс в строке инициализации по переданному адресу
             */
            public static int getIndexByAddress(Address address, int deskSize) {
                return (address.x * deskSize) + address.y;
            }

            public int getRow() {
                return x;
            }

            public int getCol() {
                return y;
            }
        }
    }
}
