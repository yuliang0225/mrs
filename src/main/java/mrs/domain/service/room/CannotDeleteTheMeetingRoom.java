package mrs.domain.service.room;

public class CannotDeleteTheMeetingRoom extends RuntimeException{
    public CannotDeleteTheMeetingRoom(String message) {
        super(message);
    }
}
