import java.util.*;
import java.util.stream.Collectors;

public class App {
    private static List<Developer> listDev = getDevelopers();

    public static void main(String[] args) {
        sort(); //sortlist
        listToMap(); //list to map va nguoc lai
        parallelList(); //doc list theo parallelstream va nguoc lai
    }
    public static void sort(){
        System.out.println("Truoc khi sort");
        listDev.forEach((developer -> System.out.println(developer.getName()+" "+developer.getAge())));
        System.out.println("------------------");
        System.out.println("Sau khi sort"); //sort by age
        listDev.sort((Developer o1, Developer o2)->o2.getAge()-o1.getAge());
        listDev.forEach(developer -> System.out.println(developer.getName()+" "+developer.getAge()));
        System.out.println("------------------");
    }
    public static void listToMap(){
        System.out.println("Chuyen List sang Map");
        Map<String, Developer> map = listDev.stream().collect(Collectors.toMap(p->p.getName(), p->p));
        System.out.println("Map -" +map);
        Map<Integer, String> map2 = new HashMap<>();
        map2.put(1, "chuoi");
        map2.put(2, "tao ");
        map2.put(3, "le");
        map2.put(4, "man");
        map2.put(5, "dao");
        System.out.println("-------------------------");
        System.out.println("Convert Map to List : key");
        List<Integer> list2 = map2.keySet().stream().collect(Collectors.toList());
        list2.forEach(key1 -> System.out.println(key1));
        System.out.println("--------------------------");
        System.out.println("Convert Map to List: value");
        List<String> list3 = map2.values().stream().collect(Collectors.toList());
        list3.forEach(values -> System.out.println(values));
        System.out.println("-------------------------");
    }
    public static void parallelList(){
        System.out.println("Parallel Stream");
        int n = 10000000;
        List<Developer> listPara = new ArrayList<>();
        Random rd = new Random();
        while(n-->0){
            Developer developer = new Developer("minh", new Integer(rd.nextInt(10000)), new Integer(rd.nextInt(10)+1));
            listPara.add(developer);
        }

        //thoi gian thuc hien stream
        long startTime1 = System.currentTimeMillis();
        listPara.stream().filter(developer -> developer.getAge().equals(9)).count();
        long endTime1 = System.currentTimeMillis();
        System.out.println("Time stream:" + (endTime1-startTime1) + " ms");

        //thoi gian thuc hien parallel stream
        long startTime = System.currentTimeMillis();
        listPara.parallelStream().filter(developer -> developer.getAge().equals(9)).count();
        long endTime = System.currentTimeMillis();
        System.out.println("Time parallel stream :" + (endTime-startTime)+" ms");
    }
    private static List<Developer> getDevelopers(){
        List<Developer> result = new ArrayList<Developer>();
        result.add(new Developer("vuong", new Integer(50000), new Integer(18)));
        result.add(new Developer("trung", new Integer(10000), new Integer(40)));
        result.add(new Developer("duc", new Integer(70000), new Integer(22)));
        result.add(new Developer("huy", new Integer(80000), new Integer(50)));
        return result;
    }
}
