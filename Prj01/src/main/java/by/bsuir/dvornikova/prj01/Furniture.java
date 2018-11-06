package by.bsuir.dvornikova.prj01;

abstract class Furniture {

    private String name;

    private int minArea;
    private int maxArea;

    Furniture(String name, int minArea, int maxArea) {
        this.name = name;
        this.minArea = minArea;
        this.maxArea = maxArea;
    }

    int getMinArea() {
        return minArea;
    }

    int getMaxArea() {
        return maxArea;
    }

    String getName() {
        return name;
    }

}
