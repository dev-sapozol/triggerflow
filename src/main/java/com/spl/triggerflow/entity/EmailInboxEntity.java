package com.spl.triggerflow.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_inbox")
public class EmailInboxEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;

  private Long userId;

  private String originalMessageId;

  @Column(name = "`from`")
  private String from;

  @Column(name = "`to`")
  private String to;
  private String cc;
  private String subject;
  private String preview;

  @Column(name = "inbox_type")
  private Integer inboxType;

  @Column(name = "is_read")
  private Boolean isRead;

  @Column(name = "has_attachment")
  private Boolean hasAttachment;
  private Integer importance;

  @Column(name = "in_reply_to")
  private String inReplyTo;

  @Column(name = "`references`")
  private String references;

  @Column(name = "s3_url")
  private String s3Url;

  @Column(name = "thread_id")
  private String threadId;

  @Column(name = "text_body")
  private String textBody;

  @Column(name = "html_body")
  private String htmlBody;

  private Integer folder;

  @Column(name = "deleted_at", nullable = true, updatable = true)
  private LocalDateTime deletedAt;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public EmailInboxEntity() {
  }

  public EmailInboxEntity(Long id, Long userId, String originalMessageId, String from, String to, String cc,
      String subject, String preview, Integer inboxType, Boolean isRead, Boolean hasAttachment, Integer importance,
      String inReplyTo, String references, String s3Url, String threadId, String textBody, String htmlBody,
      Integer folder, LocalDateTime deletedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
    Id = id;
    this.userId = userId;
    this.originalMessageId = originalMessageId;
    this.from = from;
    this.to = to;
    this.cc = cc;
    this.subject = subject;
    this.preview = preview;
    this.inboxType = inboxType;
    this.isRead = isRead;
    this.hasAttachment = hasAttachment;
    this.importance = importance;
    this.inReplyTo = inReplyTo;
    this.references = references;
    this.s3Url = s3Url;
    this.threadId = threadId;
    this.textBody = textBody;
    this.htmlBody = htmlBody;
    this.folder = folder;
    this.deletedAt = deletedAt;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getOriginalMessageId() {
    return originalMessageId;
  }

  public void setOriginalMessageId(String originalMessageId) {
    this.originalMessageId = originalMessageId;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getCc() {
    return cc;
  }

  public void setCc(String cc) {
    this.cc = cc;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getPreview() {
    return preview;
  }

  public void setPreview(String preview) {
    this.preview = preview;
  }

  public Integer getInboxType() {
    return inboxType;
  }

  public void setInboxType(Integer inboxType) {
    this.inboxType = inboxType;
  }

  public Boolean getIsRead() {
    return isRead;
  }

  public void setIsRead(Boolean isRead) {
    this.isRead = isRead;
  }

  public Boolean getHasAttachment() {
    return hasAttachment;
  }

  public void setHasAttachment(Boolean hasAttachment) {
    this.hasAttachment = hasAttachment;
  }

  public Integer getImportance() {
    return importance;
  }

  public void setImportance(Integer importance) {
    this.importance = importance;
  }

  public String getInReplyTo() {
    return inReplyTo;
  }

  public void setInReplyTo(String inReplyTo) {
    this.inReplyTo = inReplyTo;
  }

  public String getReferences() {
    return references;
  }

  public void setReferences(String references) {
    this.references = references;
  }

  public String getS3Url() {
    return s3Url;
  }

  public void setS3Url(String s3Url) {
    this.s3Url = s3Url;
  }

  public String getThreadId() {
    return threadId;
  }

  public void setThreadId(String threadId) {
    this.threadId = threadId;
  }

  public String getTextBody() {
    return textBody;
  }

  public void setTextBody(String textBody) {
    this.textBody = textBody;
  }

  public String getHtmlBody() {
    return htmlBody;
  }

  public void setHtmlBody(String htmlBody) {
    this.htmlBody = htmlBody;
  }

  public Integer getFolder() {
    return folder;
  }

  public void setFolder(Integer folder) {
    this.folder = folder;
  }

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }
  
}
