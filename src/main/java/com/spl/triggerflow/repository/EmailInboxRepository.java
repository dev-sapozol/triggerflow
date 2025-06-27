package com.spl.triggerflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.spl.triggerflow.entity.EmailInboxEntity;

public interface EmailInboxRepository extends JpaRepository<EmailInboxEntity, Long> {
  List<EmailInboxEntity> findAllByUserId(Long userId);

  List<EmailInboxEntity> findAllByUserIdAndFolder(Long userId, Integer folder);

  @Query("""
        SELECT e FROM EmailInboxEntity e
        WHERE e.userId = :userId
          AND (:folder IS NULL OR e.folder = :folder)
          AND (:isRead IS NULL OR e.isRead = :isRead)
          AND (:importance IS NULL OR e.importance = :importance)
          AND (:hasAtt IS NULL OR e.hasAttachment = :hasAtt)
          AND (:toAddr IS NULL OR e.to LIKE %:toAddr%)
          AND (:ccAddr IS NULL OR e.cc LIKE %:ccAddr%)
          AND (:deleted IS NULL OR (:deleted = TRUE AND e.deletedAt IS NOT NULL) OR (:deleted = FALSE AND e.deletedAt IS NULL))
      """)
  List<EmailInboxEntity> findFiltered(
      Long userId,
      Boolean isRead,
      Integer importance,
      Boolean hasAtt,
      Integer folder,
      String toAddr,
      String ccAddr,
      Boolean deleted);
}
