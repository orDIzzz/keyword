package com.ordizzz.keyword;

import java.util.*;

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
     * ����� �������� ��������� ������ �����. ���� ���, �.�. �� ����� ����� ��� �������, �� ������������� {@code InputMismatchException}.
     * ����� ��������� {@code ArrayList<Keynode> keynodeList}, ���������� � ���� ��� ������� Keynode �� �����.
     *
     * @param initString - ������ �������������.
     * @throws InputMismatchException ���� ����� �� ����������, �.�. ����� ������ ������������� �� �������� ���������
     *                                ������ �����.
     */
    public Desk(String initString) {
        this.initString = initString;
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
     * ��������� {@code linkedKeyNodes} ��� ������� Keynode
     */
    private void initLinkedLists() {
        for (Keynode keynode : keynodeList) {
            for (Map.Entry<Keynode.Address, Keynode> entry : keynode.linkedKeynodes.entrySet()) {
                entry.setValue(Keynode.getKeynodeByAddress(entry.getKey()));
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

    public String findWord(String word) {
        return "";
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
        Map<Address, Keynode> linkedKeynodes = new HashMap<>();
        Address keynodeAddress;
        String keynodeValue;

        /**
         * @param keynode        �������� �����, ���������� � keynode
         * @param indexOfKeynode ������ � ������ ������������� ��� ������� ������ keynode.
         */
        private Keynode(String keynode, int indexOfKeynode) {
            // ��������� ������ Address, ����������� �� ������� � ������ ������������� �����
            this.keynodeAddress = new Address(indexOfKeynode);
            this.keynodeValue = keynode;
            // ��������� 4 �������� ������, ���� ��������������
            linkedKeynodes.put((new Address(this.keynodeAddress.x - 1, this.keynodeAddress.y)), null);
            linkedKeynodes.put((new Address(this.keynodeAddress.x + 1, this.keynodeAddress.y)), null);
            linkedKeynodes.put((new Address(this.keynodeAddress.x, this.keynodeAddress.y - 1)), null);
            linkedKeynodes.put((new Address(this.keynodeAddress.x, this.keynodeAddress.y + 1)), null);
            Map<Address, Keynode> copy = new HashMap<>(linkedKeynodes);
            // ����� �� ������ ��������� �������������� KeyNode.
            for (Map.Entry<Address, Keynode> entry : copy.entrySet()) {
                // ���� ����� KeyNode ������� �� ������� ����� �� ����� KeyNode ���������
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
         * @return String ���������� �������� �����, ���������� � Keynode
         */
        public String getValue() {
            return keynodeValue;
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

        public Map<Address, Keynode> getLinkedKeynodes() {
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

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }
        }
    }
}
