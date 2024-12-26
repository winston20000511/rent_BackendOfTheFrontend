package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.MessageWithUserDTO;
import com.example.demo.model.ComplaintBean;
import com.example.demo.model.MessageBean;

public interface MessageRepository extends JpaRepository<MessageBean, Long> {

	/*
	 * join user table then me to sort data use user name make front end friend list
	 * can show list 下方方法 是找到兩個人的 MSG
	 */
	// save
	@Query("SELECT m.id AS messageId, m.message AS message, m.timestamp AS timestamp, "
			+ "s.userId AS senderId, s.name AS senderName, " + "r.userId AS receiverId, r.name AS receiverName, "
			+ "m.picture AS picture " + "FROM MessageBean m " + "JOIN m.sender s " + "JOIN m.receiver r " + "WHERE "
			+ "(s.userId = :currentUserId AND r.userId = :targetUserId) OR "
			+ "(s.userId = :targetUserId AND r.userId = :currentUserId) " + "ORDER BY m.timestamp ASC")
	List<MessageWithUserDTO> findMessagesWithUserDetails(@Param("currentUserId") Long currentUserId,
			@Param("targetUserId") Long targetUserId);

	@Query("SELECT m.id AS messageId, m.message AS message, m.timestamp AS timestamp, m.picture AS picture, "
			+ "s.userId AS senderId, s.name AS senderName, " + "r.userId AS receiverId, r.name AS receiverName "
			+ "FROM MessageBean m " + "JOIN m.sender s " + "JOIN m.receiver r "
			+ "WHERE (m.senderId = :currentUserId OR m.receiverId = :currentUserId) " + "ORDER BY m.timestamp ASC")
	List<MessageWithUserDTO> findFriendList(@Param("currentUserId") Long currentUserId);

	// create friend list ******
	// save
	@Query("""
			   SELECT
			       CASE WHEN m.senderId = :currentUserId THEN r.userId ELSE s.userId END,
			       CASE WHEN m.senderId = :currentUserId THEN r.name ELSE s.name END,
			       m.timestamp
			   FROM MessageBean m
			   JOIN m.sender s
			   JOIN m.receiver r
			   WHERE m.timestamp = (
			       SELECT MAX(m2.timestamp)
			       FROM MessageBean m2
			       WHERE (m2.senderId = :currentUserId OR m2.receiverId = :currentUserId)
			         AND CASE WHEN m2.senderId = :currentUserId THEN m2.receiverId ELSE m2.senderId END =
			             CASE WHEN m.senderId = :currentUserId THEN m.receiverId ELSE m.senderId END
			   )
			   ORDER BY m.timestamp DESC
			""")
	List<Object[]> findChatUserDetails(@Param("currentUserId") Long currentUserId);

	// search MSG *******
	// save
	@Query("SELECT m FROM MessageBean m WHERE "
			+ "(m.sender.userId = :currentUserId AND m.receiver.userId = :targetUserId) OR "
			+ "(m.sender.userId = :targetUserId AND m.receiver.userId = :currentUserId) " + "ORDER BY m.timestamp ASC")
	List<MessageBean> findMessagesBetweenUsers(@Param("currentUserId") Long currentUserId,
			@Param("targetUserId") Long targetUserId);

	// save
	@Query("SELECT m FROM MessageBean m WHERE "
			+ "((m.sender.userId = :currentUserId AND m.receiver.userId = :targetUserId) OR "
			+ "(m.sender.userId = :targetUserId AND m.receiver.userId = :currentUserId)) AND "
			+ "m.timestamp > :lastChecked ORDER BY m.timestamp ASC")
	List<MessageBean> findMessagesBetweenUsersAfter(@Param("currentUserId") Long currentUserId,
			@Param("targetUserId") Long targetUserId, @Param("lastChecked") LocalDateTime lastChecked);

	// find between user currentUserId And targeUserId
	List<MessageBean> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(Long senderId, Long receiverId,
			Long receiverId2, Long senderId2);

	// only login user send And receiver
	@Query("SELECT m FROM MessageBean m WHERE m.senderId = :userId OR m.receiverId = :userId ORDER BY m.timestamp ASC")
	List<MessageBean> findMessagesByUserId(@Param("userId") Long userId);

}
