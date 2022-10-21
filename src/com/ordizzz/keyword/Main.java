package com.ordizzz.keyword;


public class Main {
    public static void main(String[] args) {
        Desk desk = new Desk("QLGNAEKIRLRNGEAE");
        System.out.println(desk);
        System.out.println(desk.findWord("king"));
    }
}

