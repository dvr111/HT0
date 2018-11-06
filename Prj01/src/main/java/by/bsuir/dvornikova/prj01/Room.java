package by.bsuir.dvornikova.prj01;

import java.util.LinkedList;
import java.util.List;

class Room {

    static final int WINDOW_ILLUMINANCE_POWER = 700;

    private static final int MAX_ALLOWED_SUMMARY_ILLUMINANCE = 4000;

    private final double MAX_ALLOWED_SUMMARY_USED_AREA;

    private final String name;

    private final int area;
    private final int windowsNumber;

    private int maxUsedArea;
    private int minUsedArea;

    private int summaryIlluminance;

    private List<Lamp> lamps = new LinkedList<>();
    private List<Furniture> furnitureList = new LinkedList<>();

    Room(String name, int area, int windowsNumber) {
        this.name = name;
        this.area = area;
        this.windowsNumber = windowsNumber;
        summaryIlluminance += windowsNumber * WINDOW_ILLUMINANCE_POWER;

        MAX_ALLOWED_SUMMARY_USED_AREA = area * 0.7;
    }

    void add(Lamp lamp) {
        int power = lamp.getIlluminationPower();

        if(summaryIlluminance + power < MAX_ALLOWED_SUMMARY_ILLUMINANCE) {
            lamps.add(lamp);
            summaryIlluminance += power;
        }else{
            throw new IlluminanceTooMuchException();
        }
    }

    void add(Furniture furniture) {
        int minArea = furniture.getMinArea();
        int maxArea = furniture.getMaxArea();

        if(maxArea + maxUsedArea < MAX_ALLOWED_SUMMARY_USED_AREA) {
            furnitureList.add(furniture);
            maxUsedArea += maxArea;
            minUsedArea += minArea;
        }else{
            throw new SpaceUsageTooMuchException();
        }
    }

    String getName() {
        return name;
    }

    int getArea() {
        return area;
    }

    int getMaxUsedArea() {
        return maxUsedArea;
    }

    int getMinUsedArea() {
        return minUsedArea;
    }

    int getWindowsNumber() {
        return windowsNumber;
    }

    int getSummaryIlluminance() {
        return summaryIlluminance;
    }

    List<Furniture> getFurnitureList() {
        return furnitureList;
    }

    List<Lamp> getLampsList() {
        return lamps;
    }

}
