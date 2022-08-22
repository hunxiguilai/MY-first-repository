package 斗地主;

public class CardsType {
    public int typeid;
    public String typeStr;
    public int cnt1, cnt2;
    public boolean isContinuous;

    public CardsType() {
        typeid = -1;
    }

    public CardsType(String typeStr, int typeid, int cnt1, boolean isContinuous, int cnt2) {
        this.cnt1 = cnt1;
        this.cnt2 = cnt2;
        this.typeid = typeid;
        this.typeStr = typeStr;
        this.isContinuous = isContinuous;
    }


}
