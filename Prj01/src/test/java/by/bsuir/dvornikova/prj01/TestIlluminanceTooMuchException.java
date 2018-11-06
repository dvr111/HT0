package by.bsuir.dvornikova.prj01;

class TestIlluminanceTooMuchException {

    public static void main(String args[]) {
        Building building = new Building("Здание 1");
        building.addRoom("Комната 1", 100, 5);
        building.getRoom("Комната 1").add(new Lamp(500));
        building.describe();
    }

}
