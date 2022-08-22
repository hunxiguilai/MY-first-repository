package 斗地主;


import java.util.Objects;

import static 斗地主.Constants.*;

public class Card {
    public String figure;
    public int value;

    public Card(String figure) {
        this.figure = figure;
        value = calValue();
    }

    //计算牌在数组中对应的下标，以此来比较大小
    public int calValue() {
        for (int i = 0; i < toFigure.length; i++) {
            if (Objects.equals(toFigure[i], figure)) {
                return i;
            }
        }
        return ERROR;
    }

    public void print() {
        assert (value != -1);
        System.out.print(figure + " ");
    }
}
