package hamza.pekdogan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    static private Random rand = new Random(); //rastgele değer üreten sınıf

    static private int lessonDay = 5; //okula gidilecek gün sayısı
    static private int lessonTime = 7; //okul günlük saat sayısı
    static private int repeat = 1000; // tekrar sayısı
    static private int accuracyPercentage = 100; //100 üzerinden doğruluk tekrar sayısı
    static private int[][] TARGET = {{1, 0, 1, 0, 0, 0, 0}, {1, 1, 1, 0, 1, 0, 1}, {1, 0, 1, 0, 1, 1, 1}, {0, 0, 0, 0, 1, 0, 1}, {1, 1, 0, 0, 1, 1, 1}}; //Hedef
    static private List<int[][]> population = new ArrayList<>(); //Üretilenler
    static private int mutationPercentage = 5; //mutasyon oranı
    static private int[][] bestElement1 = new int[lessonDay][lessonTime]; // en iyi iki element
    static private int[][] bestElement2 = new int[lessonDay][lessonTime]; // en iyi iki element

    public static void main(String[] args) {
        //ilk rastgele 4 eleman oluşturulur
        population.add(randomCreateElement());
        population.add(randomCreateElement());
        population.add(randomCreateElement());
        population.add(randomCreateElement());

        int repeatCount = 0; //başlangıç sayaç

        while (repeatCount < repeat) { //tekrarlar

            for (int[][] element : population) {
                // en iyi iki elemanı buluyoruz (SELECTION)
                checkBest(element); // (FITNESS)
            }

            createElementWithCrossover(); //Crossover

            System.out.println("Best element1: " + ((accuracy(bestElement1) * 100) / (lessonTime * lessonDay)) + "% Best element2: " + ((accuracy(bestElement2) * 100) / (lessonTime * lessonDay)) + "%");

            repeatCount++;
            if (((accuracy(bestElement1) * 100) / (lessonTime * lessonDay)) >= accuracyPercentage) // doğruluk oranı doğruysa bitirir
                break;
        }

        System.out.println("Final Best element1: " + ((accuracy(bestElement1) * 100) / (lessonTime * lessonDay)) + "% FinalBest element2: " + ((accuracy(bestElement2) * 100) / (lessonTime * lessonDay)) + "%");

        //ekrana yazdır
        System.out.println("Target");
        for (int i = 0; i < lessonTime; i++) {
            for (int j = 0; j < lessonDay; j++) {
                System.out.print(" " + TARGET[j][i] + " ");
            }
            System.out.println();
        }

        System.out.println("Best element1");
        for (int i = 0; i < lessonTime; i++) {
            for (int j = 0; j < lessonDay; j++) {
                System.out.print(" " + bestElement1[j][i] + " ");
            }
            System.out.println();
        }
    }

    static public int[][] randomCreateElement() {
        int[][] newElement = new int[lessonDay][lessonTime];
        for (int i = 0; i < lessonDay; i++)
            for (int j = 0; j < lessonTime; j++)
                newElement[i][j] = rand.nextInt(2);
        return newElement;
    }

    static public int accuracy(int[][] element) {
        int result = 0;
        for (int i = 0; i < lessonDay; i++)
            for (int j = 0; j < lessonTime; j++)
                if (TARGET[i][j] == element[i][j])
                    result++;
        return result;
    }

    static public void checkBest(int[][] element) {
        if (accuracy(bestElement1) < accuracy(element)) {
            bestElement2 = bestElement1;
            bestElement1 = element;
        } else if (accuracy(bestElement2) < accuracy(element)) {
            bestElement2 = element;
        }
    }

    static public int[][] mutation(int[][] element) {
        int total = lessonDay * lessonTime;
        int mutationCount = (total) * mutationPercentage / 100; // mutasyon oranı hesaplanıyor
        for (int i = 0; i < mutationCount; i++) {
            element[rand.nextInt(lessonDay)][rand.nextInt(lessonTime)] = rand.nextInt(2); // rastgele değer veriliyor
        }
        return element;
    }

    static public void createElementWithCrossover() {
        int[][] newElement1 = new int[lessonDay][lessonTime];
        int[][] newElement2 = new int[lessonDay][lessonTime];
        boolean crossoverStatus = true;
        for (int i = 0; i < lessonDay; i++)
            for (int j = 0; j < lessonTime; j++) {
                if (bestElement2[i][j] == bestElement1[i][j]) {
                    newElement1[i][j] = bestElement1[i][j];
                    newElement2[i][j] = bestElement1[i][j];
                } else {
                    if (crossoverStatus) {
                        newElement1[i][j] = bestElement2[i][j];
                        newElement2[i][j] = bestElement1[i][j];
                        crossoverStatus = false;
                    } else {
                        newElement1[i][j] = bestElement1[i][j];
                        newElement2[i][j] = bestElement2[i][j];
                        crossoverStatus = true;
                    }
                }
            }
        newElement1 = mutation(newElement1); // mutation
        newElement2 = mutation(newElement2); // mutation
        population.add(newElement1);
        population.add(newElement2);
    }
}
