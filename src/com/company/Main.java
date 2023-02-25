package com.company;

import java.util.*;

public class Main {

  public static Map<String,String> RecommendMap=new HashMap<>();
  static {
    //当前推荐关系 递减一集少一半的奖励 整体为等价于1
    //A-------C D ------
    RecommendMap.put("A", "");
    RecommendMap.put("B", "");
    //A->C/D 1
    RecommendMap.put("C", "A");//1
    RecommendMap.put("D", "A");//1
    //c->E
    RecommendMap.put("E", "C");//0.5
    //E->F H
    RecommendMap.put("F", "E");//0.25
    RecommendMap.put("H", "E");//0.25
    RecommendMap.put("q", "E");//0.25
    //D->G
    RecommendMap.put("G", "D");//0.5
    //F->I
    RecommendMap.put("I", "F");//0.125

    RecommendMap.put("j", "I");//0.25

    RecommendMap.put("x", "j");//0.25

    //第七层
    //a->c->e->f->i->j->x
    RecommendMap.put("z", "j");//0.25
  }



    public static void main(String[] args) {
	// write your code here
      //场景 A-B- 拉新人 赠送100积分  B->C A会员50积分 B会员100积分
      //A-B 赠送A 100积分
      //A->B->C  赠送B 100积分 赠送A 50积分
      //A->B->C->... 赠送C 100积分 赠送A B 50积分 依次类推
      //第一次推荐获取1分 第二次推荐获取0.5积分

    //求任意一个用户获取到的收益
      getRecommend("A");
   //计算一次所有团队的奖励
//      test();
    }


    static void getRecommend(String user){
      //A是第1代
      List list=new ArrayList();
      list.add(user);
      Map map=new HashMap<>();
      map.put("score",0.0);
      map.put("level",0.0);
      map.put("recommendList",list);


      Map<String,Object> resultMap=recommendLoop(map);
      Double score = (Double) resultMap.get("score");
      System.out.println(list+","+score);
    }

    // 分数和等级
  public static Map recommendLoop(Map map) {
    double score = (double) map.get("score");
    double level = (double) map.get("level");
    List<String> userList = (List) map.get("recommendList");
    List<String> recommendList = new ArrayList();

    //返回所有的映射关系 A->B B->D 最底层网上算
    //D->0.3^0  0.3^1
    for (Map.Entry<String, String> userMap : RecommendMap.entrySet()) {
      String key = userMap.getKey();
      String value = userMap.getValue();

      //[A],2.2311199999999998
      if (userList.contains(value)) { //说明a下面有人  （b a //c a）  其中一个推荐人/很多推荐人
        //增加积分 等级积分 所有包含A的
        // 第一次计算积分 最多6级                          x   b   // y    b

//                                                        多层集合 vector 包不包含 项目
//                                                            m n
        //[A],3.9375 [A],4.0
//        if (level<=4){
        // 0.2   .25
          //下级b总收益的额度 *(0.25)  100 25
          score = score + Math.pow(0.5, level);//每递减一层，获得2^n个积分 20%  25%   30%
          recommendList.add(key);
        System.out.println(value+":"+score+":"+level+":"+recommendList.size()); //iiiii

//        }else {
//          break;
//        }

      }
    }
    for (int i = 0; i <recommendList.size(); i++) {
      System.out.println(recommendList.get(i));
    }
    //迭代
    while (recommendList != null && recommendList.size() > 0) {
      level += 1;
      Map newMap = new HashMap();
      newMap.put("score", score);
      newMap.put("level", level);
      newMap.put("recommendList", recommendList);

      Map<String, Object> _resultMap = recommendLoop(newMap);
      score = (double) _resultMap.get("score");
      level = (double) _resultMap.get("level");
      recommendList = (List) _resultMap.get("recommendList");
    }

    Map resultMap = new HashMap();
    resultMap.put("score", score);
    resultMap.put("level", level);
    resultMap.put("recommendList", recommendList);
    return resultMap;
  }


  static void test(){
    Map newMap = new HashMap();
    newMap.put(1, 1);
//    System.out.println(1/Math.pow(2,0));
    Iterator<Map.Entry<Integer, Integer>> it=newMap.entrySet().iterator();
    while(it.hasNext()) {
      Map.Entry<Integer,Integer> entry=it.next();
      int key=entry.getKey();
      int value=entry.getValue();
      System.out.println(key+" "+value);
    }

  }


}
