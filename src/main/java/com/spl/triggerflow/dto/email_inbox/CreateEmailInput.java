package com.spl.triggerflow.dto.email_inbox;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateEmailInput {
  @NotNull
  private Long userId;

  @NotBlank
  private String from;

  @NotBlank
  private String to;

  private String cc;
  private String bcc;

  @NotBlank
  private String subject;

  @NotBlank
  private String preview;

  @NotNull
  private Integer inboxType;

  @NotNull
  private Boolean isRead;

  @NotNull
  private Boolean hasAttachment;

  @NotNull
  private Integer importance;
  
  @NotBlank
  private String textBody;
  
  private String htmlBody;

  private Integer folder;

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
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

  public String getBcc() {
    return bcc;
  }

  public void setBcc(String bcc) {
    this.bcc = bcc;
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
}
