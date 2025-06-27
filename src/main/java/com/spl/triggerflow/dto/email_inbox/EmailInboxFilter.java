package com.spl.triggerflow.dto.email_inbox;

import java.time.LocalDateTime;

public class EmailInboxFilter {
  private Boolean isRead;
  private Integer importance;
  private Boolean hasAttachment;
  private Integer folder;
  private String to;
  private String cc;
  private LocalDateTime deletedAt;

  public Boolean getIsRead() {
    return isRead;
  }

  public void setIsRead(Boolean isRead) {
    this.isRead = isRead;
  }

  public Integer getImportance() {
    return importance;
  }

  public void setImportance(Integer importance) {
    this.importance = importance;
  }

  public Boolean getHasAttachment() {
    return hasAttachment;
  }

  public void setHasAttachment(Boolean hasAttachment) {
    this.hasAttachment = hasAttachment;
  }

  public Integer getFolder() {
    return folder;
  }

  public void setFolder(Integer folder) {
    this.folder = folder;
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

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

}
