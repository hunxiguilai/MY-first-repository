package 斗地主;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static 斗地主.Constants.VALUECOUNT;

public class LastCards {
    public static LastCards lastCards=null;
    public Player player;
    public CardGroup cardGroup;
    public static LastCards init(){
        if(lastCards==null){
            lastCards=new LastCards();
        }
        return lastCards;
    }
    public List<Card> findCanBeatFrom(List<Card> cardsHold){

        //查找能打得过的牌
        int i,j,k,n=cardsHold.size(),m=cardGroup.cards.size();
        String typeStr=cardGroup.type.typeStr;
        List<Card> ret=new ArrayList<>();
        if(Objects.equals(typeStr, "王炸") ||n<m){
            //打不过，返回空数组
            return ret;
        }
        int value=cardGroup.value();
        //统计各点牌出现的次数
        int[] cntFlag =new int[VALUECOUNT];
        for(i=0;i<n;i++){
            cntFlag[cardsHold.get(i).value]++;
        }
        int continuousCount=1;
        if(cardGroup.type.isContinuous){
            continuousCount=m/(cardGroup.type.cnt1+cardGroup.type.cnt2);
        }
        boolean findFirstFigure;
        for(i=value+1;i<VALUECOUNT;i++){
            findFirstFigure=true;
            for(j=0;j<continuousCount;j++){
                if(cntFlag[i-j]<cardGroup.type.cnt1){
                    findFirstFigure=false;
                    break;
                }
            }
            if(findFirstFigure){
                ret.clear();
                int firstFigure=i;
                for(k=0,j=0;k<cardsHold.size() &&j<continuousCount;k++){
                    if(cardsHold.get(k).value==firstFigure-j){
                        for(int kk=0;j>=0&&kk<cardGroup.type.cnt1;kk++){
                            ret.add(cardsHold.get(k + kk));
                        }
                        j++;
                    }
                }
                if(cardGroup.type.cnt2>0){
                    int[] SecondFigures = new int[5] ;
                    int SecondCount=continuousCount;
                    if(Objects.equals(cardGroup.type.typeStr, "四带二"))
                        SecondCount=2;
                    boolean findSecondFigure=true;
                    for(j=0,k=-1;j<SecondCount &&findSecondFigure;j++){
                        findSecondFigure=false;
                        for(k++;k<VALUECOUNT;k++){
                            SecondFigures[j]=k;
                            if(cntFlag[k]>=cardGroup.type.cnt2 &&cntFlag[k]<cardGroup.type.cnt1){
                                findSecondFigure=true;
                                break;
                            }
                        }
                    }
                    if(findSecondFigure){
                        for(i=0;i<SecondCount;i++){
                            for(j=0;j<cardsHold.size();){
                                if(cardsHold.get(j).value==SecondFigures[i]){
                                    for(k=0;k<cardGroup.type.cnt2;k++){
                                        ret.add(cardsHold.get(j + k));
                                    }
                                    do{
                                        j++;
                                    }while(j<cardsHold.size()&& cardsHold.get(j).value==SecondFigures[i]);
                                }else{
                                    j++;
                                }
                            }
                        }
                        return ret;
                    }
                }
                else{
                    return ret;
                }
            }
        }
        ret.clear();
        //没牌打得过时查找有没有炸dan
        if(!Objects.equals(typeStr, "炸弹")){
            for(i=cardsHold.size()-1;i>=3;i--){
                if(cardsHold.get(i).value== cardsHold.get(i - 3) .value){
                    for(j=0;j<4;j++){
                        ret.add(cardsHold.get(i - j));
                    }
                    break;
                }
            }
        }
        return ret;
    }
}