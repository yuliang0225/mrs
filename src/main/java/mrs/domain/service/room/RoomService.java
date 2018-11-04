package mrs.domain.service.room;


import java.time.LocalDate;
import java.util.List;

import mrs.domain.model.MeetingRoom;
import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import mrs.domain.model.Reservation;
import mrs.domain.repository.reservation.ReservationRepository;
import mrs.domain.repository.room.MeetingRoomRepository;
import mrs.domain.repository.room.ReservableRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class RoomService {
	@Autowired
	ReservableRoomRepository reservableRoomRepository;
	@Autowired
	MeetingRoomRepository meetingRoomRepository;
	@Autowired
    ReservationRepository reservationRepository;


	public List<ReservableRoom> findReservableRooms(LocalDate date) {
		return reservableRoomRepository.findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(date);
	}

	public MeetingRoom findMeetingRoom(Integer roomId) {
		return meetingRoomRepository.findOne(roomId);
	}
    // Delete the reservable infromation for the roomId
	public void deleteReservableRoom (Integer roomId){
        reservableRoomRepository.delete(reservableRoomRepository.findReservaleRooms(roomId));
    }
    // Find all meetingrooms
	public List <MeetingRoom> findMeetingRoom() {
		return meetingRoomRepository.findAll();
	}
    // Rename the name of the meetingroom by roomId
	public void renameRoom(Integer roomId, String newName){
	    meetingRoomRepository.findOne(roomId).setRoomName(newName); ;
    }
    // Save the new meetingroom and set 7 days for Reservable
    public void addNewMeetingRoom (MeetingRoom meetingRoom){
	    meetingRoomRepository.saveAndFlush(meetingRoom);
	    for (int i =0; i<8; i++) {
	        ReservableRoomId resId = new ReservableRoomId(meetingRoom.getRoomId(),LocalDate.now().plusDays(i));
	        ReservableRoom res = new ReservableRoom();
	        res.setReservableRoomId(resId);
	        res.setMeetingRoom(meetingRoom);
	        reservableRoomRepository.saveAndFlush(res);
	    }

    }
    // Delete the meetingroom by roomId
    public void deleteMeetingRoom(Integer roomId){
	    if (reservationRepository.findByReservableRoom_ReservableRoomId_RoomId(roomId).size() != 0){
	        throw new CannotDeleteTheMeetingRoom("指定の会議室を削除できません。もう予約しました。");
        }
	    deleteReservableRoom(roomId);
	    meetingRoomRepository.delete(roomId);
    }
    // Get all Reservations in the database
    public List<Reservation> getAllReservation(){
	    return reservationRepository.findAll();
    }



}