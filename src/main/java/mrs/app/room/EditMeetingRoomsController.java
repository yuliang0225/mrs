package mrs.app.room;

import mrs.domain.model.MeetingRoom;
import mrs.domain.model.Reservation;
import mrs.domain.service.room.CannotDeleteTheMeetingRoom;
import mrs.domain.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("meetingrooms")
public class EditMeetingRoomsController {

    @Autowired
    RoomService roomService;

    @PostMapping
    public String jumpToListMeetingRooms(Model model) {
        return "redirect:/meetingrooms";
    }

    @GetMapping
    public String listMeetingRooms(Model model) {
        MeetingRoom newMeetingRoom = new MeetingRoom();
        model.addAttribute("newMeetingRoom", newMeetingRoom);
        model.addAttribute("meetingrooms", roomService.findMeetingRoom());
        model.addAttribute("allreservations",roomService.getAllReservation());
        return "room/meetingroom";
    }

    @PostMapping(params = "editroom", path = "{roomId}")
    public String jumpToEditRoom(@PathVariable("roomId") Integer roomId, @ModelAttribute MeetingRoom meetingRoom, Model model) {
        model.addAttribute("editMeetingRoom",roomService.findMeetingRoom(roomId));
        return "room/editmeetingroom";
    }

    @PostMapping("edit/{roomId}")
    public String editRoomName(@ModelAttribute("editMeetingRoom") MeetingRoom meetingRoom, Model model) {
        roomService.renameRoom(meetingRoom.getRoomId(),meetingRoom.getRoomName());
        return "redirect:/meetingrooms";
    }

    @PostMapping(params = "deleteroom", path = "{roomId}")
    public String deleteRoom(@PathVariable("roomId") Integer roomId ,Model model) {
        try {
            roomService.deleteMeetingRoom(roomId);
        } catch (CannotDeleteTheMeetingRoom e) {
            model.addAttribute("error", e.getMessage());
            return listMeetingRooms(model);
        }
        return "redirect:/meetingrooms";
    }

    @PostMapping("addnewmeetingroom")
    public String addNewRoom(@ModelAttribute MeetingRoom newMeetingRoom) {
        roomService.addNewMeetingRoom(newMeetingRoom);
        return "redirect:/meetingrooms";
    }

//    @PostMapping(params = "cancel")
//    String cancel(@RequestParam("reservationId") Integer reservationId, @PathVariable("roomId") Integer roomId,
//                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date, Model model) {
//        Reservation reservation = reservationService.findOne(reservationId);
//        reservationService.cancel(reservation);
//        return "redirect:/reservations/{date}/{roomId}";
//    }
}
