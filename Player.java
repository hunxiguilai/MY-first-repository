package 斗地主;

import java.util.List;
import java.util.Objects;

public class Player {
    public String name;
    public List<Card> cards;
    public void arrange(){
        cards.sort((o1, o2) -> o2.value- o1.value);
    }
    public void print(){
        System.out.print(this.name+":\t");
        for (Card card : cards) {
            card.print();
        }
        System.out.println("["+cards.size()+"]\n");
    }

    ///????@
    public List<Card> tip(){
        //提示功能,使自己最小
        CardGroup ret=new CardGroup();
        StringBuilder temp;
        int j,k,m=cards.size();
        for(j=0;j<m;j++){
            temp = new StringBuilder();
            for(k=j;k<m;k++){
                temp.append(cards.get(k).figure);
            }
            ret=new CardGroup(temp.toString(),cards);
            if(ret.type.typeid!=-1){
                return ret.cards;
            }
        }
        ret.cards.clear();
        return ret.cards;
    }
    public void chupai(CardGroup cardgroup){
        //出牌
        System.out.print(this.name+":\t");
        System.out.print(cardgroup.type.typeStr+" ");
        for(int i=0;i<cardgroup.cards.size();i++){
            int j;
            cardgroup.cards.get(i).print();
            for( j=0;j<cards.size();j++){
                if(cards.get(j)==cardgroup.cards.get(i)){
                    break;
                }
            }
            this.cards.remove(j);
        }
        System.out.print("\t["+this.cards.size()+"]\n");
        if(!Objects.equals(cardgroup.type.typeStr, "不出")){
            //记录到 LastCards 中
            LastCards.init().player=this;
            LastCards.init().cardGroup=new CardGroup(cardgroup.cards);
        }
    }
}
