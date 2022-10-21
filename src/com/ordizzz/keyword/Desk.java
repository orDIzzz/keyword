package com.ordizzz.keyword;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Desk - ���������� ��� ����� � �������.<br>
 *
 * <ul>����� 3 ��������� ����:
 * <li>{@code String initString} - ������������������ ����, ������� ����� �������� �����. ����� ������ ������ ���� ���������
 * ������ �����, ��� ������������ ���������� �����.</li>
 * <li>{@code deskSize} - ���������� ������ �� ����� {@code initString}. ��������� ��� ������� ������ {@code Address} � ������� ����.</li>
 * <li>{@code keyNodeList} - ArrayList<KeyNode>, ���������� ��� ����� �� �����.</li>
 * </ul>
 */
class Desk {
    private final String initString;
    private int deskSize;
    private static ArrayList<Keynode> keynodeList = new ArrayList<>();

    /**
     * ��� �������� ����� � ����������� ���������� {@code initString} - ������ ������������� �����. �����������, ��� �
     * ����� �������� ��������� ������ �����. ���� ���, �.�. �� ����� ����� ��� �������, �� ������������� {@link InputMismatchException}.
     * ����� ��������� {@code ArrayList<Keynode> keynodeList}, ���������� � ���� ��� ������� {@link Keynode} �� �����.
     * @param initString - ������ �������������.
     * @throws InputMismatchException ���� ����� �� ����������, �.�. ����� ������ ������������� �� �������� ���������
     *                                ������ �����.
     */
    public Desk(String initString) {
        this.initString = initString.toUpperCase();
        //��������� �������� �� ����� ������ ��������� ������ �����
        if (Math.sqrt(initString.length()) % 1 == 0) {
            // ��������� ������� ����� � ���������� deskSize
            this.deskSize = (int) Math.sqrt(initString.length());
        } else {
            // ���������� ����������
            throw new InputMismatchException();
        }
        // ��������� ������ �� ������ �����
        String[] arrayInitString = initString.split("");
        // � ����� ��������� ArrayList<Keynode> keyNodeList
        for (int i = 0; i < arrayInitString.length; i++) {
            keynodeList.add(new Keynode(arrayInitString[i], i));
        }
        // ��������� linkedNodes ��� ������� KeyNode
        initLinkedLists();
    }

    /**
     * ��������� {@code linkedKeynodes} ��� ������� Keynode
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
     * @return ���������� ���� ������ Keynode
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
     * Keynode - ���������� ��� ������ �� �����.<br>
     * <ul>����� ��������� ����:
     * <li>{@code Map<Address , Keynode> linkedKeynodes} - ��� ������ ����� ������� ������ �������� ����.</li>
     * <li>{@code Address keynodeAddress} - ����� ����� � ������� {@code [������, �������]}.</li>
     * <li>{@code String keynodeValue} - �������� �����.</li>
     * </ul>
     */
    class Keynode {
        ArrayList<Keynode> linkedKeynodes = new ArrayList<>();
        Address keynodeAddress;
        String keynodeValue;

        /**
         * @param keynode �������� �����, ���������� � keynode
         * @param indexOfKeynode ������ � ������ ������������� ��� ������� ������ keynode.
         */
        private Keynode(String keynode, int indexOfKeynode) {
            // ��������� ������ Address, ����������� �� ������� � ������ ������������� �����
            this.keynodeAddress = new Address(indexOfKeynode);
            this.keynodeValue = keynode;
        }

        @Override
        public String toString() {
            return keynodeValue + "->" + keynodeAddress;
        }

        /**
         * @return {@code Address} ���������� ����� Keynode
         */
        public Address getKeynodeAddress() {
            return this.keynodeAddress;
        }

        public String getValue() {
            return keynodeValue;
        }

        /**
         * @param address {@code Address} ����, ������� ���� �������� ��� ������
         * @return ���������� {@code Keynode} �� ����������� ������
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
         * ������� �����, ������� �������� {@code x} - ������ � {@code y} - �������.
         * ��������� ���������� � 0.
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
             * @param address  ����� ({@code Address}) Keynode
             * @param deskSize ������ �����.
             * @return ���������� ������ � ������ ������������� �� ����������� ������
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
