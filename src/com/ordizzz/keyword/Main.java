package com.ordizzz.keyword;

import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Desk desk = new Desk("QLGNAEKIRLRNGEAE");
        System.out.println(desk);

    }
}

class Desk {
    private final String initString;
    private int deskSize;
    static ArrayList<KeyNode> keyNodeList = new ArrayList<>();

    public Desk(String initString) {
        this.initString = initString;
        if (Math.sqrt(initString.length()) % 1 == 0) {
            this.deskSize = (int) Math.sqrt(initString.length());
        } else {
            throw new InputMismatchException();
        }
        Stream.of(initString.split("")).forEach(elem -> {
            keyNodeList.add(new KeyNode(elem, initString.indexOf(elem)));
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (KeyNode node : keyNodeList) {
            if (node.keyNodeAddress.y != deskSize - 1) {
                sb.append(node.keyNodeValue).append("\t");
            } else {
                sb.append(node.keyNodeValue).append("\n");
            }
        }
        return sb.toString();
    }

//    private String finder(KeyNode keynode, List<String> seqForFind) {
//        String result = "";
//        StringBuilder sb = new StringBuilder();
//
//        for (Map.Entry<KeyNode.Address, String> entry : nodeMap.entrySet()) {
//            if (entry.getValue().equals(seqForFind.get(0))) {
//                if (seqForFind.size() != 1) {
//                    seqForFind.remove(0);
//                    result = entry.getKey().toString() + "->" + finder(KeyNode.getKeyNodeByAddress(entry.getKey()), seqForFind);
//                }
//
//                return result;
//            }
//        }
//        return "Not found";
//    }
//
//    public String findWord(String word) {
//        List<String> fWord = Arrays.stream(word.split("")).toList();
//        String result = "Not found";
//        for (KeyNode keyNode : keyNodeList) {
//            if (keyNode.getValue().equals(fWord.get(0))) {
//                String finderResult = finder(keyNode, fWord);
//                result = !finderResult.equals("-1") ? finderResult : "Not found";
//            }
//        }
//        return result;
//    }

    class KeyNode {
        @Override
        public String toString() {
            return "KeyNode{" +
                    "linkedKeyNodes=" + linkedKeyNodes +
                    ", keyNodeAddress=" + keyNodeAddress +
                    ", keyNodeValue='" + keyNodeValue + '\'' +
                    '}';
        }

        public static KeyNode getKeyNodeByAddress(Address address) {
            KeyNode result = null;
            for (KeyNode keyNode : keyNodeList) {
                if (address.equals(keyNode.keyNodeAddress)) {
                    result = keyNode;
                }
            }
            return result;
        }

        Map<Address, String> linkedKeyNodes = new HashMap<>();
        Address keyNodeAddress;

        public String getValue() {
            return keyNodeValue;
        }

        String keyNodeValue;

        public KeyNode(String keyNode, int indexOfKeyNode) {
            this.keyNodeAddress = new Address(indexOfKeyNode);
            this.keyNodeValue = keyNode;
            linkedKeyNodes.put((new Address(this.keyNodeAddress.x - 1, this.keyNodeAddress.y)), null);
            linkedKeyNodes.put((new Address(this.keyNodeAddress.x + 1, this.keyNodeAddress.y)), null);
            linkedKeyNodes.put((new Address(this.keyNodeAddress.x, this.keyNodeAddress.y - 1)), null);
            linkedKeyNodes.put((new Address(this.keyNodeAddress.x, this.keyNodeAddress.y + 1)), null);

            Map<Address, String> copy = new HashMap<>(linkedKeyNodes);
            for (Map.Entry<Address, String> entry : copy.entrySet()) {
                if (
                        entry.getKey().x < 0 ||
                                entry.getKey().y < 0 ||
                                entry.getKey().x >= deskSize ||
                                entry.getKey().y >= deskSize
                ) {
                    linkedKeyNodes.remove(entry.getKey());
                } else {
                    linkedKeyNodes.put(
                            entry.getKey(),
                            initString.split("")[Address.getIndexByAddress(entry.getKey(), deskSize)]
                    );
                }
            }
        }


        class Address {
            @Override
            public String toString() {
                return "[ " + x + ", " + y + " ]";
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

            private Address(int x, int y) {
                this.x = x;
                this.y = y;
            }

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