import java.util.*;
public class HotelBookingApp {
    public abstract class Room{
        protected int numberOfBeds;
        protected int squareFeets;
        protected double pricePerNight;
        protected int available;

        Room(int numberOfBeds, int squareFeets, double pricePerNight, int available){
            this.numberOfBeds = numberOfBeds;
            this.squareFeets = squareFeets;
            this.pricePerNight = pricePerNight;
            this.available = available;

        }
        public void displayRoomDetails(){
            System.out.println("Beds: " + numberOfBeds);
            System.out.println("Size: " + squareFeets + " sq ft");
            System.out.println("Price per night: " + pricePerNight);
            System.out.println("Available: "+ available);
        }
    }

    public class SingleRoom extends Room {
        SingleRoom(){
            super(1,250,1500.0,5);
        }
    }
    public class DoubleRoom extends Room {
        DoubleRoom(){
            super(2,400,2500.0,3);
        }
    }
    public class SuiteRoom extends Room {
        SuiteRoom(){
            super(3,750,5000.0,2);
        }
    }

    public static class RoomInventory{
        private Map<String, Integer> roomAvailability;

        public RoomInventory(){
            roomAvailability  = new HashMap<>();
            initializeInventory();
        }

        private void initializeInventory(){
            roomAvailability.put("SingleRoom",1);
            roomAvailability.put("DoubleRoom",3);
            roomAvailability.put("SuiteRoom",2);
        }

        public Map<String, Integer> getRoomAvailability(){
            return roomAvailability;
        }

        public void updateAvailability(String roomType, int count){
            roomAvailability.put(roomType,count);
        }
    }

    public static class RoomService{
        public void searchAvailableRooms(RoomInventory inventory, Room singleRoom, Room doubleRoom, Room suiteRoom){
            Map<String,Integer> availability = inventory.getRoomAvailability();

            if(availability.get("SingleRoom")> 0) {
                System.out.println("SingleRoom: ");
                singleRoom.displayRoomDetails();
            }
            if(availability.get("DoubleRoom")> 0) {
                System.out.println("SingleRoom: ");
                doubleRoom.displayRoomDetails();
            }
            if(availability.get("SuiteRoom")> 0) {
                System.out.println("SuiteRoom: ");
                suiteRoom.displayRoomDetails();
            }
        }
    }


    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking Management System.\nSystem initalized successfully\n");

        HotelBookingApp app = new HotelBookingApp();
        Room single = app.new SingleRoom();
        Room doubleRoom = app.new DoubleRoom();
        Room suite = app.new SuiteRoom();

        System.out.println("Hotel Room Inventory Status\n");

        RoomInventory inventory = new RoomInventory();
        for(Map.Entry<String,Integer> entry: inventory.getRoomAvailability().entrySet()){
            String key = entry.getKey();
                if (key.equals("SingleRoom")) {
                    System.out.println("SingleRoom: ");
                    single.displayRoomDetails();
                    System.out.println();
            } else if (key.equals("DoubleRoom")) {
                    System.out.println("DoubleRoom: ");
                    doubleRoom.displayRoomDetails();
                    System.out.println();
            } else if (key.equals("SuiteRoom")) {
                    System.out.println("SuiteRoom: ");
                    suite.displayRoomDetails();
                    System.out.println();
                }
                
                RoomService room = new RoomService();
                room.searchAvailableRooms(inventory, single, doubleRoom, suite);

        }




    }
}
