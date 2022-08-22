package 斗地主;


import java.util.*;

import static 斗地主.Constants.*;

public class LandLords {
    public List<Player> player;
    public boolean finished, youWin, landlordWin;
    public int landlordIndex;
    public List<Card> cards;

    public LandLords() {
        player = new ArrayList<>();
        cards = new ArrayList<>();
        int i;
        for (i = 0; i < PLAYERCOUNT; i++) {
            this.player.add(new Player());
        }
        //54张牌初始化
        for (i = 0; i < 14; i++) {
            if (Objects.equals(toFigure[i], " ")) {
                continue;
            }
            for (int k = 0; k < 4; k++) {
                this.cards.add(new Card(toFigure[i]));
            }
        }
        this.cards.add(new Card("Y"));
        this.cards.add(new Card("Z"));
    }

    public void init() {
        player.get(0).name = "Me";
        player.get(0).cards = new ArrayList<>();
        player.get(1).name = "玩家2";
        player.get(1).cards = new ArrayList<>();
        player.get(2).name = "玩家3";
        player.get(2).cards = new ArrayList<>();
        finished = false;
        youWin = false;
        landlordWin = false;
        //抢地主
        landlordIndex = ERROR;
        while (landlordIndex == ERROR) {
            Collections.shuffle(cards);
            fapai();
            landlordIndex = chooseLandlord();
        }
        System.out.print(player.get(landlordIndex).name + "\t成为地主\n\n");
        this.add3Cards();
        this.player.get(landlordIndex).arrange();//整理
        for (int i = 0; i < 3; i++) {
            this.player.get(i).print();
        }
        LastCards.init().player = player.get(landlordIndex);
    }

    public void fapai() {
        int k, i;
        for (k = 0, i = 0; i < PLAYERCOUNT; i++) {
            this.player.get(i).cards.clear();
            for (int j = 0; j < 17; j++) {
                this.player.get(i).cards.add(this.cards.get(k++));
            }
            this.player.get(i).arrange();//整理
            this.player.get(i).print();
        }
    }

    public void startGame() {
        CardGroup inputCards;
        for (int iTurns = landlordIndex; !finished; iTurns++) {
            if (iTurns >= PLAYERCOUNT) {
                iTurns = 0;
            }
            if (iTurns == CURRENTPLAYER) {
                System.out.println();
                player.get(iTurns).print();
                System.out.print("输入提示：Z=大王 Y=小王 0=10 输入可无序 N=不出 例如:JKQ0A9\n请出牌：\t");
                do {
                    Scanner sc = new Scanner(System.in);
                    String inputStr = sc.nextLine();
                    inputCards = new CardGroup(inputStr, player.get(iTurns).cards);
                } while (!check(inputCards));
            } else {
                if (player.get(iTurns) == LastCards.init().player) {
                    //若是上次出牌的是自己，启用提示功能
                    inputCards = new CardGroup(player.get(iTurns).tip());
                } else {
                    //查找能打得过上家的牌
                    inputCards = new CardGroup(LastCards.init().findCanBeatFrom(player.get(iTurns).cards));
                    int len = LastCards.init().cardGroup.cards.size();
                    for (int i = 0; i < len; i++) {
                        System.out.print(LastCards.init().cardGroup.cards.get(i).figure);
                    }
                    System.out.println();
                }
            }
            player.get(iTurns).chupai(inputCards);//出牌

            if (player.get(iTurns).cards.size() == 0) {
                //玩家手中没牌了，游戏结束
                finished = true;
                landlordWin = iTurns == landlordIndex;
                if (landlordWin) {
                    youWin = landlordIndex == CURRENTPLAYER;
                } else {
                    youWin = landlordIndex != CURRENTPLAYER;
                }
            }
        }
        System.out.print("\n_________________________ " + (youWin ? "You Win!" : "You Lose!") + " _________________________\n\n");
    }

    public void add3Cards() {
        System.out.print("地主3张牌:  ");//54张牌，i=17*3=51,i<54,51,52,53
        for (int i = PLAYERCOUNT * 17; i < CARDSCOUNT; i++) {
            this.cards.get(i).print();
            player.get(landlordIndex).cards.add(cards.get(i));
        }
        System.out.println();
        player.get(landlordIndex).arrange();
    }

    public int chooseLandlord() {
        System.out.print("\n_________________________ 抢地主 _________________________\n\n");
        Random random = new Random();
        int first = -1, last = -1, cnt = 0, i;
        int j = random.nextInt(3);
        boolean decision;
        for (i = 0; i < PLAYERCOUNT; i++) {
            if (j == 0) {
                decision = makeChoice("是否抢地主？(Y=抢/N=不抢):");
            } else {
                int magic = random.nextInt(2);
                {
                    decision = magic != 1;
                }
            }
            if (decision) {
                cnt++;
                last = j;
                if (first == -1) {
                    first = j;
                }
                System.out.println(this.player.get(j).name + "\t抢地主");
            } else {
                System.out.println(this.player.get(j).name + "\t没有抢");
            }
            j++;
            if (j == 3) {
                j = 0;
            }
        }
        if (cnt == 0) {
            System.out.print("没人抢，重新发牌\n");
            return ERROR;
        }
        if (cnt == 1) {
            //第一轮只有一人抢地主
            return first;
        } else {
            //最后一次争抢
            if (first == CURRENTPLAYER) {
                decision = makeChoice("是否抢地主？(Y=抢/N=不抢):");
            } else {
                int magic = random.nextInt(2);
                {
                    decision = magic != 1;
                }
            }
            if (decision) {
                System.out.print(this.player.get(first).name + "\t抢地主\n");
                return first;
            } else {
                System.out.print(this.player.get(first).name + "\t没有抢\n");
                return last;
            }
        }
    }

    public boolean check(CardGroup cardGroup) {

        if (cardGroup.type.typeid == ERROR) {
            System.out.println("出牌错误，重新输入");
            return false;
        } else if (Objects.equals(cardGroup.type.typeStr, "不出")) {
            return true;
        } else if (LastCards.init().player != player.get(0) && !cardGroup.isCanBeat(LastCards.init().cardGroup)) {
            System.out.println("打不过，重新输入");
            return false;
        } else {
            return true;
        }
    }
}
