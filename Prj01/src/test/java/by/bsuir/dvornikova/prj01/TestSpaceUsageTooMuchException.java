package by.bsuir.dvornikova.prj01;

class TestSpaceUsageTooMuchException {

    public static void main(String args[]) {
        Building building = new Building("Здание 1");
        building.addRoom("Комната 1", 30, 3);
        building.getRoom("Комната 1").add(new Table("Стол письменный", 21)); //70% от 30 = 21
        building.describe();
    }

}
