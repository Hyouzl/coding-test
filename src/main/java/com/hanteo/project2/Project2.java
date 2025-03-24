package com.hanteo.project2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Project2 {

    public static void solution(int sum, int[] coins) {

        int[] dp = new int [sum + 1];
        List<List<List<Integer>>> coinList = new ArrayList<>();

        // 금액 1부터 sum까지 조합 리스트 초기화
        for (int i = 0; i <= sum; i++) {
            coinList.add(new ArrayList<>());
        }

        // 금액 0을 만드는 기본 조합 추가
        coinList.get(0).add(new ArrayList<>());
        dp[0] = 1;

        for (int coin : coins) {
            for (int i = coin; i <= sum; i++) {
                dp[i] += dp[i - coin];
                for (List<Integer> prevCoinList : coinList.get(i - coin)) {
                    List<Integer> newCoinList = new ArrayList<>(prevCoinList);
                    newCoinList.add(coin);
                    coinList.get(i).add(newCoinList);
                }
            }
        }

        System.out.println("조합의 수: " + dp[sum]);
        System.out.println("설명: " + dp[sum] + "가지의 솔루션이 있습니다.");
        List<List<Integer>> sumCoinList = coinList.get(sum);
        for (List<Integer> coinCombination : sumCoinList) {
            System.out.println(coinCombination);
        }


    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("sum = ");
        int sum = sc.nextInt();

        System.out.println("동전 종류의 수: ");
        int n = sc.nextInt();
        int[]  coins = new int[n];

        for (int i = 0; i < n; i++) {
            coins[i] = sc.nextInt();
        }

        System.out.println("coins[] = " + Arrays.toString(coins) + "");
        solution(sum, coins);

    }

}
