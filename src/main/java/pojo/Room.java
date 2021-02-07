package pojo;

public class Room {

    private int number;
    private RoomType roomType;
    private WindowView windowView;
    private String status;

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public WindowView getWindowView() {
        return windowView;
    }

    public void setWindowView(WindowView windowView) {
        this.windowView = windowView;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
