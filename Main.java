package 斗地主;

public class Main {
    public static void main(String[] args) {
        LandLords landlords = new LandLords();
        landlords.init();//发牌、抢地主
        landlords.startGame();//游戏开始
    }
}
