package 斗地主;

import java.util.*;

import static 斗地主.Constants.VALUECOUNT;
import static 斗地主.Constants.toFigure;

public class CardGroup {
    public List<Card> cards;
    public CardsType type;


    /*    求得牌型
        typeid仅用于区分牌型
        cnt1 指张数最多的牌，cnt2指张数最少的牌
    */
    public void calType() {
        int i, len = cards.size();
        type = new CardsType();
        if (len == 0) {
            type = new CardsType("不出", 14, 0, false, 0);
        }
        if (len == 2 && cards.get(0).value == 15 && cards.get(1).value == 14) {
            type = new CardsType("王炸", 0, 0, false, 0);
        }
        int[] cntFlag = new int[VALUECOUNT];
        //统计同点数牌有多少张
        for (i = 0; i < len; i++) {
            cntFlag[cards.get(i).value]++;
        }
        //统计点数最多和最少的牌
        int maxCnt = 0, minCnt = 4;
        for (i = 0; i < VALUECOUNT; i++) {
            if (maxCnt < cntFlag[i]) {
                maxCnt = cntFlag[i];
            }
            if (cntFlag[i] != 0 && minCnt > cntFlag[i]) {
                minCnt = cntFlag[i];
            }
        }
        if (len == 4 && maxCnt == 4) {
            type = new CardsType("炸弹", 1, 4, false, 0);
        }
        if (len == 1) {
            type = new CardsType("单牌", 2, 1, false, 0);
        }
        if (len == 2 && maxCnt == 2) {
            type = new CardsType("对子", 3, 2, false, 0);
            return;
        }
        if (len == 3 && maxCnt == 3) {
            type = new CardsType("三张 ", 4, 3, false, 0);
            return;
        }
        if (len == 4 && maxCnt == 3) {
            type = new CardsType("三带一", 5, 3, false, 1);
            return;
        }
        if (len == 5 && maxCnt == 3 && minCnt == 2) {
            type = new CardsType("三带一对", 6, 3, false, 2);
            return;
        }
        if (len == 6 && maxCnt == 4) {
            type = new CardsType("四带二", 7, 4, false, 1);
            return;
        }
        if (len == 8 && maxCnt == 4 && minCnt == 2) {
            type = new CardsType("四带二", 8, 4, false, 2);
            return;
        }
        if (len >= 5 && maxCnt == 1 && cards.get(0).value == cards.get(len - 1).value + len - 1) {
            type = new CardsType("顺子", 9, 1, true, 0);
            return;
        }
        if (len >= 6 && maxCnt == 2 && minCnt == 2 && cards.get(0).value == cards.get(len - 1).value + len / 2 - 1) {
            type = new CardsType("连对", 10, 2, true, 0);
            return;
        }
        int fjCnt;//统计连续且大于3三张的牌
        for (i = 0; i < VALUECOUNT && cntFlag[i] < 3; i++) ;
        for (fjCnt = 0; i < VALUECOUNT && cntFlag[i] >= 3; i++, fjCnt++) ;
        if (fjCnt > 1) {
            if (len == fjCnt * 3)
                type = new CardsType("飞机", 11, 3, true, 0);
            else if (len == fjCnt * 4)
                type = new CardsType("飞机", 12, 3, true, 1);
            else if (len == fjCnt * 5 && minCnt == 2)
                type = new CardsType("飞机", 13, 3, true, 2);
        }
    }

    public CardGroup() {
        this.cards = new ArrayList<>();
        this.type = new CardsType();
    }

    public CardGroup(String inputStr, List<Card> cardsHeld) {
        this.cards = new ArrayList<>();
        this.type = new CardsType();
        if (Objects.equals(inputStr, "N")) {
            this.calType();
            return;
        }
        String[] inputStr1 = new String[inputStr.length()];
        for (int m = 0; m < inputStr.length(); m++) {
            inputStr1[m] = inputStr.substring(m, m + 1);
        }
        int i, j;
        //查找输入合不合法
        for (i = 0; i < inputStr1.length; i++) {
            boolean find = false;
            for (j = 0; toFigure[j] != null; j++) {
                if (Objects.equals(inputStr1[i], toFigure[j])) {
                    find = true;
                    break;
                }
            }
            if (!find) {
                return;
            }
        }
        //查找手中有没有这些牌
        int[] visitFlag = new int[20];
        for (i = 0; i < inputStr1.length; i++) {
            Card find = null;
            for (j = 0; j < cardsHeld.size(); j++) {
                if (visitFlag[j] == 0 && Objects.equals(cardsHeld.get(j).figure, inputStr1[i])) {
                    visitFlag[j] = 1;
                    find = cardsHeld.get(j);
                    break;
                }
            }
            if (find != null) {
                this.cards.add(find);
            } else {
                System.out.println(inputStr1[i] + "没有找到\t");
                this.cards.clear();
                return;
            }
        }
        this.arranges();
    }

    public CardGroup(List<Card> newCards) {
        this.cards = new ArrayList<>();
        this.cards.addAll(newCards);
        this.arranges();
    }

    //比较牌的大小
    public boolean isCanBeat(CardGroup cardGroup) {
        if (Objects.equals(cardGroup.type.typeStr, "王炸")) {
            return false;
        } else if (Objects.equals(this.type.typeStr, "王炸")) {
            return true;
        } else if (cardGroup.type == this.type && Objects.equals(this.type.typeStr, "炸弹")) {
            return value() > cardGroup.value();
        } else if (Objects.equals(cardGroup.type.typeStr, "炸弹")) {
            return false;
        } else if (Objects.equals(this.type.typeStr, "炸弹")) {
            return true;
        } else if (Objects.equals(cardGroup.type.typeStr, this.type.typeStr) && this.cards.size() == cardGroup.cards.size()) {
            return this.value() > cardGroup.value();
        } else {
            return false;
        }
    }

    public int value() {
        //计算牌组的比较指标
        int i;
        if (Objects.equals(type.typeStr, "三带一") || Objects.equals(type.typeStr, "三带一对") || Objects.equals(type.typeStr, "飞机")) {
            for (i = 2; i < cards.size(); i++) {
                if (cards.get(i).value == cards.get(i - 2).value) {
                    return cards.get(i).value;
                }
            }
        }
        if (Objects.equals(type.typeStr, "四带二")) {
            for (i = 3; i < cards.size(); i++) {
                if (cards.get(i).value == cards.get(i - 3).value) {
                    return cards.get(i).value;
                }
            }
        }
        return cards.get(0).value;
    }

    public void arranges() {
        this.cards.sort((o1, o2) -> o2.value - o1.value);
        this.calType();
    }
}