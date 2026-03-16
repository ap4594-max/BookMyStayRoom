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


    public static void main(String[] args) {
        System.out.println("Welcome to the Hotel Booking Management System.\nSystem initalized successfully\n");

        HotelBookingApp app = new HotelBookingApp();
        Room single = app.new SingleRoom();
        Room doubleRoom = app.new DoubleRoom();
        Room suite = app.new SuiteRoom();

        single.displayRoomDetails();
        System.out.println();
        doubleRoom.displayRoomDetails();
        System.out.println();
        suite.displayRoomDetails();

    }
}
