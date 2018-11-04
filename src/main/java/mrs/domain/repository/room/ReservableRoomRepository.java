package mrs.domain.repository.room;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.LockModeType;

import mrs.domain.model.ReservableRoom;
import mrs.domain.model.ReservableRoomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservableRoomRepository extends JpaRepository<ReservableRoom, ReservableRoomId> {
	@Query("SELECT DISTINCT x FROM ReservableRoom x LEFT JOIN FETCH x.meetingRoom WHERE x.reservableRoomId.reservedDate = :date ORDER BY x.reservableRoomId.roomId ASC")
	List<ReservableRoom> findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(@Param("date") LocalDate reservedDate);

	@Query("SELECT x FROM ReservableRoom x LEFT JOIN FETCH x.meetingRoom WHERE x.reservableRoomId.roomId = :theRoomId")
    List<ReservableRoom> findReservaleRooms(@Param("theRoomId") Integer roomId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	ReservableRoom findOneForUpdateByReservableRoomId(ReservableRoomId reservableRoomId);
}