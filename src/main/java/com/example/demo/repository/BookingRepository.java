package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.BookingDetailDTO;
import com.example.demo.dto.BookingListDTO;
import com.example.demo.dto.HouseOwnerDetailDTO;
import com.example.demo.model.BookingBean;

public interface BookingRepository extends JpaRepository<BookingBean, Long> {

	List<BookingBean> findByUserId(Long userId);

	List<BookingBean> findByHouseId(Long houserId);

	@Query("SELECT new com.example.demo.dto.BookingListDTO(b.bookingId AS bookingId, b.houseId AS houseId, "
			+ "h.title AS houseTitle, h.address AS houseAddress, b.userId AS userId, b.createDate AS createDate, "
			+ "h.price AS housePrice, CAST(CONCAT(b.bookingDate, ' ', b.bookingTime) AS LocalDateTime) AS bookingDate, "
			+ "b.status AS status) " + "FROM BookingBean b "
			+ "JOIN b.house h "
			+ "WHERE b.userId = :userId")
	List<BookingListDTO> findBookingListByUserId(Long userId);

	@Query("SELECT CONCAT(bookingDate,' ',bookingTime) FROM BookingBean WHERE houseId = :houseId AND status <=1")
	List<String> findBookingedByHouseId(Long houseId);

	@Query(value = "SELECT new com.example.demo.dto.BookingDetailDTO(ow.name, ow.email, u.name, u.email, "
			+ "b.createDate, b.bookingDate, b.bookingTime, b.status) " + "FROM BookingBean b " + "JOIN b.house h "
			+ "JOIN h.user ow " + "JOIN b.rentUser u " + "WHERE b.bookingId = :id")
	BookingDetailDTO findBookingDetailsById(Long id);

	@Query(value = "SELECT h.house_id, u.email FROM  house_table h JOIN user_table u ON h.user_id = u.user_id WHERE h.user_id = :houseId", nativeQuery = true)
	HouseOwnerDetailDTO findOwnerDetailByHouseId(Long houseId);

	@Query(value = "SELECT count(*) FROM booking_table WHERE house_id = :houseId " + "AND booking_date = :date "
			+ "AND booking_time = CAST(:time AS time) " + "AND status <= 1", nativeQuery = true)
	Integer isExistBooking(Long houseId, LocalDate date, LocalTime time);

	@Transactional
	@Modifying
	@Query("UPDATE BookingBean SET status = 6 WHERE status = 1 AND bookingDate < :date")
	void updateStatusOfOverdue(LocalDate date);

	@Transactional
	@Modifying
	@Query("UPDATE BookingBean SET status = 5 WHERE status = 0 AND bookingDate < :date")
	void updateStatusOfFinish(LocalDate date);

	/*
	 * status 0:待確認 1:屋主接受 2:屋主拒絕 3:屋主取消 4:用戶取消 5:逾期 6:結束
	 */

}
