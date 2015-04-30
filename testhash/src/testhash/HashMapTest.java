package testhash;

import java.util.Date;
import java.util.HashMap;


public class HashMapTest {
     
     public static void main(String[] args){
         
         // HashMap 선언
        HashMap<String, String> map = new HashMap<>();
         
         // 데이터 삽입
        map.put("title", "개발이하고싶어요");
         map.put("name", "하이언");
         map.put("copyright", "Private");
         map.put("date", new Date().toString());
         
         // Map 정보
        System.out.println("isEmpty : "+map.isEmpty());
         System.out.println("Size : "+map.size());
         System.out.println("ContainKey : "+map.containsKey("date"));
         System.out.println("ContainValue : "+map.containsKey("하이언"));
         
         // Map 데이터 출력
        System.out.println();
         System.out.println("===== 데이터 출력=====");
         System.out.println("Title : "+map.get("title"));
         System.out.println("Name : "+map.get("name"));
         System.out.println("Copyright : "+map.get("copyright"));
         System.out.println("Date : "+map.get("date"));
         
         // Key "date" 삭제
        map.remove("date");
         
         // Key "title" 값 변경
        map.put("title", "헬로 2014");
         
         // 데이터 변경 후 Map 정보
        System.out.println();
         System.out.println("===== 데이터 변경 후 Map 정보=====");
         System.out.println("isEmpty : "+map.isEmpty());
         System.out.println("Size : "+map.size());
         System.out.println("ContainKey : "+map.containsKey("date"));
         System.out.println("ContainValue : "+map.containsKey("개발이하고싶어요"));
         
         
         // 데이터 변경 후 Map 출력
        System.out.println();
         System.out.println("===== 데이터 출력=====");
         System.out.println("Title : "+map.get("title"));
         System.out.println("Name : "+map.get("name"));
         System.out.println("Copyright : "+map.get("copyright"));
         System.out.println("Date : "+map.get("date"));
         
     }

}
