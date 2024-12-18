package com.example.demo.repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.BookingDetailDTO;
import com.example.demo.dto.HouseOwnerDetailDTO;
import com.example.demo.model.BookingBean;

public interface BookingRepository extends JpaRepository<BookingBean, Long> {
	
	List<BookingBean> findByUserId(Long userId);
	List<BookingBean> findByHouseId(Long houserId);
	
	@Query("SELECT CONCAT(bookingDate,' ',bookingTime) FROM BookingBean WHERE houseId = :houseId AND status <=1")
	List<String> findBookingedByHouseId(Long houseId);
	
	
	@Query(value = "SELECT new com.example.demo.dto.BookingDetailDTO(ow.name, ow.email, u.name, u.email, "
			+ "b.createDate, b.bookingDate, b.bookingTime, b.status) " + "FROM BookingBean b "
			+ "JOIN b.house h " 
			+ "JOIN h.user ow "
			+ "JOIN b.rentUser u " 
			+ "WHERE b.bookingId = :id")
	BookingDetailDTO findBookingDetailsById(Long id);
	
	@Query(value = "SELECT h.house_id, u.email FROM  house_table h JOIN user_table u ON h.user_id = u.user_id WHERE h.user_id = :houseId", nativeQuery = true)
	HouseOwnerDetailDTO getOwnerDetailByHouseId(Long houseId);

	@Query(value = "SELECT count(*) FROM booking_table WHERE house_id = :houseId "
			+ "AND booking_date = :date "
			+ "AND booking_time = CAST(:time AS time) "
			+ "AND status <= 1", nativeQuery = true)
	Integer isExistBooking(Long houseId, Date date, Time time);
	/* status 0:待確認 1:房東接受 2:房東拒絕 3:預約取消 4:過期 */

}
