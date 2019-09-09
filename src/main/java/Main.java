import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeMap;

public class Main {
    private static String statementFile = "data/movementList.csv";
    private static double incomeSum = 0;
    private static double consumptionSum = 0;
    private static TreeMap<String,Double> consumptionCategoryMap = new TreeMap<>();


    public static void main(String[] args) {
        loadAccountStatementFromFile();
        System.out.println("Приход Итого:");
        System.out.println(incomeSum);
        System.out.println("\nРасход Итого:");
        System.out.println(consumptionSum);
        System.out.println("\nКатегории расходов:");
        //consumptionCategoryMap.forEach((k,v)->System.out.println(k + "\t" + v));
        consumptionCategoryMap.forEach((k,v)->System.out.printf("%-30s%-8.3f%n",k,v));

    }

    private static void loadAccountStatementFromFile (){
        double consumption;
        String consumptionCategory;
        try {
            List<String> lines = Files.readAllLines(Paths.get(statementFile));
            lines.remove(0);
            for(String line : lines)
            {
                String[] fragments = line.split(",",8);
                if(fragments.length != 8) {
                    System.out.println("Wrong line: " + line);
                    continue;
                }
                incomeSum += Double.parseDouble(fragments[6].replace("\"","").replace(",","."));
                consumption = Double.parseDouble(fragments[7].replace("\"","").replace(",","."));
                consumptionSum +=consumption;

                if(consumption > 0){
                    String[] detailsColumnFragments = fragments[5].split("\\s{3,}");
                    if(detailsColumnFragments[1].lastIndexOf("\\") >=0) {
                        consumptionCategory = detailsColumnFragments[1].substring(detailsColumnFragments[1].lastIndexOf("\\") + 1).trim().toUpperCase();
                    }else{
                        consumptionCategory = detailsColumnFragments[1].substring(detailsColumnFragments[1].lastIndexOf("/") + 1).trim().toUpperCase();
                    }
                    addToMap(consumptionCategory, consumption);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void addToMap(String consumptionCategory, double consumption){
        if (consumptionCategoryMap.containsKey(consumptionCategory)){
            consumptionCategoryMap.put(consumptionCategory,consumptionCategoryMap.get(consumptionCategory) + consumption);
        } else{
            consumptionCategoryMap.put(consumptionCategory,consumption);
        }
    }


}
