package com.ordizzz.keyword;

import java.util.*;
import java.util.stream.Stream;

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
        String[] arrayInitString = initString.split("");
        for(int i=0;i<arrayInitString.length;i++) {
            keyNodeList.add(new KeyNode(arrayInitString[i], i));
        }
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
    private String finder(KeyNode keyNode, List<String> seqForFind){
       return "-1";
    }
    public String findWord(String word){
        List<String> seqForFind = new ArrayList<>(Arrays.asList(word.toUpperCase().split("")));
        for(KeyNode keyNode: keyNodeList){
            if(keyNode.keyNodeValue.equals(seqForFind.get(0))){
                List<String> copy = new ArrayList<>(seqForFind);
                copy.remove(0);
                String fromFind = finder(keyNode,copy);
                if (fromFind != "-1") {
                    return keyNode.getKeyNodeAddress().toString() + "->" + fromFind;
                } else {
                    continue;
                }
            }
        }
        return "Not found";
    }
//    private String finder(KeyNode keyNode, List<String> seqForFind) {
//        String result = keyNode.getKeyNodeAddress() + "->";
//        for (Map.Entry<KeyNode.Address, String> entry : keyNode.linkedKeyNodes.entrySet()) {
//            String getValue = entry.getValue();
//            String forEquals = seqForFind.get(1);
//            if (getValue.equals(forEquals)) {
//                if (seqForFind.size() > 0) {
//                    seqForFind.remove(1);
//                    result += entry.getKey().toString() + "->" + finder(KeyNode.getKeyNodeByAddress( entry.getKey()), seqForFind);
//                }
//
//                return result;
//            }
//        }
//        return "Not found";
//    }
//
//    public String findWord(String word) {
//        String normalizedWord = word.toUpperCase();
//        String result = "Not found";
//        boolean cached = false;
//        List<String> fWord = new ArrayList<>(Arrays.asList(normalizedWord.split("")));
//        for (KeyNode keyNode : keyNodeList) {
//            if (keyNode.getValue().equals(fWord.get(0))) {
//                String finderResult = finder(keyNode, fWord);
//                result = !finderResult.equals("-1") ? finderResult : "Not found";
//            }
//        }
//        return result;
//    }

    class KeyNode {
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
        @Override
        public String toString() {
            return "KeyNode: [" + keyNodeValue +
                    "], keyNodeAddress=" + keyNodeAddress +
                    ", linkedKeyNodes=" + linkedKeyNodes;
        }
        public Address getKeyNodeAddress(){
            return this.keyNodeAddress;
        }
        public static KeyNode getKeyNodeByAddress(Address address) {
            KeyNode result = null;
            for (KeyNode keyNode : keyNodeList) {
                if (address.equals(keyNode.keyNodeAddress)) {
                    result = keyNode;
                    return result;
                }
            }
            return result;
        }

        class Address {
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
