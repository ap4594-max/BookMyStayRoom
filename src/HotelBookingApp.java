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

        public void releaseRoom(String roomType) {
            System.out.println("Room restored to inventory: " + roomType);
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

    public static class Reservation {
        private String guestName;
        private String roomType;
        
        public Reservation(String guestName, String roomType){
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() {
            return guestName;
        }
        public String getRoomType(){
            return roomType;
        }

        @Override
        public String toString() {
            return guestName + " booked a " + roomType + " room";
        }
    }

    public static class BookingRequestQueue{
        private Queue<Reservation> requestQueue;

        public BookingRequestQueue() {
            requestQueue = new LinkedList<>();
        }

        public void addRequest(Reservation reservation){
            requestQueue.offer(reservation);
        }

        public Reservation getNextRequest(){
            return requestQueue.poll();
        }

        public boolean hasPendingrequest(){
            return !requestQueue.isEmpty();
        }
    }

    public static class RoomAllocationService {
        private Set<String> allocatedRoomIds;

        private Map<String, Set<String>> assignedRoomsByType;

        public RoomAllocationService(){
            allocatedRoomIds = new HashSet<>();
            assignedRoomsByType = new HashMap<>();
        }

        public void allocateRoom(Reservation reservation, RoomInventory inventory){
            String roomType = reservation.getRoomType();
            int available = inventory.getRoomAvailability().get(roomType);

            if(available <=0){
                System.out.println("No rooms available for "+roomType);
                return ;
            }

            String roomId = generateRoomId(roomType);

            allocatedRoomIds.add(roomId);

            assignedRoomsByType
                .computeIfAbsent(roomType,k->new HashSet<>())
                .add(roomId);
                inventory.updateAvailability(roomType, available-1);
                System.out.println("Booking Confirmed for Guest : "+reservation.getGuestName()+"Room ID: "+roomId);
        }
        private String generateRoomId(String roomType){
            int count = assignedRoomsByType.getOrDefault(roomType, new HashSet<>())
            .size()+1;
            return roomType + "-" + count;
        }

    }

    public static class Service{
        private String serviceName;
        private double cost;
        public Service(String serviceName, double cost){
            this.serviceName = serviceName;
            this.cost = cost;
        }
        public String getServiceName(){
            return serviceName;
        }
        public double getCost(){
            return cost;
        }
    }

    public static class AddOnServiceManager{
            private Map<String, List<Service>> servicesByReservation;
            public AddOnServiceManager(){
                this.servicesByReservation = new HashMap<>();
            }
            public void addService(String reservationID, List<Service> services){
                    servicesByReservation.put(reservationID, services);

            }
            public double calculateTotalServiceCost(String reservationID){
                double total = 0.0;
                System.out.println("Reservation ID: "+reservationID);
                if(!servicesByReservation.containsKey(reservationID)){
                    return total;
                }
                for(Service service : servicesByReservation.get(reservationID)){
                    total += service.getCost();
                }
                return total;
            }
    }

    public static class BookingHistory{
        private List<Reservation> confirmedReservations;

        public BookingHistory(){
            confirmedReservations = new ArrayList<>(); 
        }

        public void addReservation(Reservation reservation){
            confirmedReservations.add(reservation);   
        }
        public List<Reservation> getConfirmedReservations(){
            return confirmedReservations;
        }
    }

    public static class BookingReportService {
        public void  generateReport(BookingHistory history){
            System.out.println(history.getConfirmedReservations());
        }
    }

    public static class InvalidBookingException extends Exception {
        public InvalidBookingException(String messsage){
            super(messsage);
        }
    }

    public static class ReservationValidator {
        public void validate(String guestName, String room,RoomInventory inventory) throws InvalidBookingException{
            if (!room.equalsIgnoreCase("single") &&
            !room.equalsIgnoreCase("double") &&
            !room.equalsIgnoreCase("suite")) {

            throw new InvalidBookingException("Invalid room type selected.");
        }
        } 
    }
    public class CancellationService {
        private Stack<String> releasedRoomIds;

        private Map<String, String> reservationRoomTypeMap;

        public CancellationService(){
            releasedRoomIds = new Stack<>();
            reservationRoomTypeMap = new HashMap<>();
        }

        public void registerBooking(String reservationId, String roomType){
            reservationRoomTypeMap.put(reservationId, roomType);
            System.out.println("Booking registered: "+reservationId + " -> " + roomType);
        }

        public void cancelBooking(String reservationID, RoomInventory inventory){
            if(!reservationRoomTypeMap.containsKey(reservationID)){
                System.out.println("Invalid reservation ID.");
                return;
            }
            String roomType = reservationRoomTypeMap.get(reservationID);
            int current = inventory.getRoomAvailability().get(roomType);
            inventory.updateAvailability(roomType, current+1);

            releasedRoomIds.push(reservationID);

            reservationRoomTypeMap.remove(reservationID);

            System.out.println("Booking cancelled: "+ reservationID);

        }

        public void showRollbackHistory(){
            if(releasedRoomIds.isEmpty()){
                System.out.println("No cancellations yet.");
                return;
            }
            System.out.println("Rollback History(Most recent first):");

            Stack<String> temStack = new Stack<>();
            temStack.addAll(releasedRoomIds);

            while (!temStack.isEmpty()) {
                System.out.println(temStack.pop()); 
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

        System.out.println("Booking Request Queue: ");
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        Reservation r1 = new Reservation("Abhi","Single");
        Reservation r2 = new Reservation("Subha","Double");
        Reservation r3 = new Reservation("Vanmathi","Suite");

        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        while(bookingQueue.hasPendingrequest()){
            Reservation reservation = bookingQueue.getNextRequest();
            System.out.println("Processing booking for Guest :"+reservation.getGuestName()+", Room Type: "+reservation.getRoomType());
        }

        System.out.println("Room Allocation Processing");
        RoomAllocationService allocator = new RoomAllocationService();

        while(bookingQueue.hasPendingrequest()){
            Reservation reservation  = bookingQueue.getNextRequest();
            allocator.allocateRoom(reservation,inventory);
              System.out.println("Processing booking for Guest :"+reservation.getGuestName()+", Room Type: "+reservation.getRoomType());
        }
        System.out.println("Add-On Service Selection");
        AddOnServiceManager serviceManager = new AddOnServiceManager();
        String[] serviceNames = {"WiFi", "Breakfast", "Parking", "Spa"};
        double[] serviceCosts = {10.0, 25.0, 15.0, 50.0};

        List<Service> services = new ArrayList<>();
        for (int i = 0; i < serviceNames.length; i++) {
            services.add(new Service(serviceNames[i], serviceCosts[i]));
        }
        serviceManager.addService("Single-1",services);
        System.out.println("Total Add-On Cost : "+serviceManager.calculateTotalServiceCost("Single-1"));

        BookingHistory history = new BookingHistory();
        String[] guestnames = {"abhi","Subha","Vanmathi"};
        String[] roomType = {"Single","Double","Suite"};

        for(int i=0;i<guestnames.length;i++){
            Reservation reservation = new Reservation(guestnames[i], roomType[i]);
            history.addReservation(reservation);
        }
        System.out.println("Booking History and Reporting");
        System.out.println();
        System.out.println("Booking History Report");
        for (Reservation r : history.getConfirmedReservations()) {
            System.out.println(r);
        }

        Scanner sc = new Scanner(System.in);

        RoomInventory inventory2 = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();

        System.out.print("Enter guest name: ");
        String name = sc.nextLine();

        System.out.print("Enter room type (Single/Double/Suite): ");
        String room = sc.nextLine();
        try {
            validator.validate(name, room, inventory2);
            System.out.println("Booking successful!");
        } 
        catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
        finally {
            sc.close();
        }
       
    }
}
