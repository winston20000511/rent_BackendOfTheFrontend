package com.example.demo.repository;

import java.sql.Date;
import java.sql.Time;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.BookingDetailDTO;
import com.example.demo.dto.HouseOwnerDetailDTO;
import com.example.demo.model.BookingBean;

public interface BookingRepository extends JpaRepository<BookingBean, Long> {

	@Query(value = "SELECT new com.example.demo.dto.BookingDetailDTO(ow.name, ow.email, u.name, u.email, "
			+ "b.createDate, b.bookingDate, b.fromTime, b.toTime, b.status) " + "FROM BookingBean b "
			+ "JOIN HouseTableBean h ON b.houseId = h.houseId " + "JOIN UserTableBean ow ON h.userId = ow.userId "
			+ "JOIN UserTableBean u ON b.userId = u.userId " + "WHERE b.id = :id", nativeQuery = true)
	BookingDetailDTO findBookingDetailsById(Long id);

	@Query(value = "SELECT h.house_id, u.email FROM  house_table h JOIN user_table u ON h.user_id = u.user_id WHERE h.user_id = :houseId", nativeQuery = true)
	HouseOwnerDetailDTO getOwnerDetailByHouseId(Long houseId);

	@Query(value = "SELECT * FROM booking_table WHERE house_id = :houseId AND booking_date = :date "
			+ "AND CAST(start_time AS datetime) <= CAST(:time AS datetime) "
			+ "AND CAST(end_time AS datetime) > CAST(:time AS datetime) " + "AND status <= 1", nativeQuery = true)
	BookingBean isExistBooking(@Param("houseId") Long houseId, @Param("date") Date date, @Param("time") Time time);

	/* status 0:待確認 1:房東接受 2:房東拒絕 3:預約取消 4:過期 */

}
