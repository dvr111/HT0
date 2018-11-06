package by.bsuir.dvornikova.prj01;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Building {

    private String name;

    private Map<String, Room> rooms = new HashMap<>();

    Building(String name) {
        this.name = name;
    }

    void addRoom(String name, int area, int windowsNumber) {
        Room room = new Room(name, area, windowsNumber);

        rooms.put(name, room);
    }

    Room getRoom(String name) {
        return rooms.get(name);
    }

    void describe() {
        StringBuilder sb =  new StringBuilder();
        sb.append(name).append('\n');

        for(Room room: rooms.values()) {
            String name = room.getName();
            int summaryIlluminance = room.getSummaryIlluminance();
            int area = room.getArea();
            int maxUsedArea = room.getMaxUsedArea();
            int minUsedArea = room.getMinUsedArea();
            int freeArea = area - maxUsedArea;
            int windowsNumber = room.getWindowsNumber();
            long freeAreaInPercents = Math.round((freeArea / (area * 1.0)) * 100);
            List<Lamp> lamps = room.getLampsList();

            sb.append('\t').append(name).append('\n');
            sb.append("\t\tОсвещённость = ").append(summaryIlluminance).append(" (");

            sb.append(windowsNumber).append(" окна по " + Room.WINDOW_ILLUMINANCE_POWER +" лк");

            if(lamps.size() > 0) {
                sb.append(", лампочки ");

                for(int i = 0; i < lamps.size(); i++) {
                    sb.append(lamps.get(i).getIlluminationPower()).append(" лк");
                    if(i < lamps.size() - 2) {
                        sb.append(", ");
                    }else if(i < lamps.size() - 1){
                        sb.append(" и ");
                    }
                }
            }

            sb.append(")\n");

            if(maxUsedArea > 0) {
                sb.append("\t\tПлощадь = ").append(area).append("м^2 (занято ").append(minUsedArea).append("-").append(maxUsedArea).append(" м^2, гарантированно свободно ").append(freeArea).append(" м^2 или ").append(freeAreaInPercents).append("% площади)").append('\n');
            }else if(maxUsedArea == 0){
                sb.append("\t\tПлощадь = ").append(area).append(" м^2 (свободно 100%)\n");
            }

            List<Furniture> furnitureList = room.getFurnitureList();
            if(furnitureList.size() > 0) {
                sb.append("\t\tМебель:\n");
                for(Furniture furniture : furnitureList) {
                    String furnitureName = furniture.getName();
                    int furnitureMaxArea = furniture.getMaxArea();
                    int furnitureMinArea = furniture.getMinArea();

                    int furnitureArea = furniture.getMaxArea();

                    sb.append("\t\t\t");

                    if(furnitureMaxArea == furnitureMinArea) {
                        sb.append(furnitureName).append(" (площадь ").append(furnitureArea);
                    }else{
                        sb.append(furnitureName).append("(площадь от ").append(furnitureMinArea).append(" м^2 до ").append(furnitureMaxArea);
                    }

                    sb.append(" м^2)").append('\n');
                }
            }else{
                sb.append("\t\t").append("Мебели нет\n");
            }
        }

        System.out.print(sb.toString());
    }

}
