package hu.vrg.demo;

public class Candies {
    public static int countCandies(int startingAmount, int newEvery) {
        int sum = 0;
        int avaliableInRound = startingAmount;
        while (avaliableInRound > 0) {
            int bonusInRound = avaliableInRound / newEvery;
            int eatNow = bonusInRound == 0 ? avaliableInRound : bonusInRound * newEvery;
            int remaining = avaliableInRound - eatNow;
            sum+= eatNow;
            avaliableInRound = bonusInRound + remaining;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(Candies.countCandies(3, 2));
    }
}
