package testhash;

import java.util.Date;
import java.util.HashMap;


public class HashMapTest {
     
     public static void main(String[] args){
         
         // HashMap ����
        HashMap<String, String> map = new HashMap<>();
         
         // ������ ����
        map.put("title", "�������ϰ�;��");
         map.put("name", "���̾�");
         map.put("copyright", "Private");
         map.put("date", new Date().toString());
         
         // Map ����
        System.out.println("isEmpty : "+map.isEmpty());
         System.out.println("Size : "+map.size());
         System.out.println("ContainKey : "+map.containsKey("date"));
         System.out.println("ContainValue : "+map.containsKey("���̾�"));
         
         // Map ������ ���
        System.out.println();
         System.out.println("===== ������ ���=====");
         System.out.println("Title : "+map.get("title"));
         System.out.println("Name : "+map.get("name"));
         System.out.println("Copyright : "+map.get("copyright"));
         System.out.println("Date : "+map.get("date"));
         
         // Key "date" ����
        map.remove("date");
         
         // Key "title" �� ����
        map.put("title", "��� 2014");
         
         // ������ ���� �� Map ����
        System.out.println();
         System.out.println("===== ������ ���� �� Map ����=====");
         System.out.println("isEmpty : "+map.isEmpty());
         System.out.println("Size : "+map.size());
         System.out.println("ContainKey : "+map.containsKey("date"));
         System.out.println("ContainValue : "+map.containsKey("�������ϰ�;��"));
         
         
         // ������ ���� �� Map ���
        System.out.println();
         System.out.println("===== ������ ���=====");
         System.out.println("Title : "+map.get("title"));
         System.out.println("Name : "+map.get("name"));
         System.out.println("Copyright : "+map.get("copyright"));
         System.out.println("Date : "+map.get("date"));
         
     }

}
