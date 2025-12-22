package com.anchor.app.msg.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anchor.app.msg.enums.MessageStatus;
import com.anchor.app.msg.enums.MessageType;

@Document(collection = "messages")
public class Message {
    
    @Id
    private String id;
    
    @Indexed
    private String conversationId;
    
    @Indexed
    private String senderId;
    
    private MessageType messageType;
    
    // Content - can be plain or encrypted
    private String content;
    
    // Encryption support
    private Boolean isEncrypted;
    private String encryptionKeyVersion; // For key rotation
    private String iv; // Initialization vector for AES
    
    // Media attachments
    private List<MessageAttachment> attachments;
    
    // Reply functionality
    private MessageReply replyTo;
    
    // Forward functionality
    private MessageForward forwardedFrom;
    
    // Status
    private MessageStatus status;
    
    private Boolean isEdited;
    private Date editedAt;
    
    private Boolean isDeleted;
    private Date deletedAt;
    private List<String> deletedFor; // User IDs who deleted (for "delete for me")
    
    @Indexed
    private Date createdAt;
    private Date updatedAt;
    
    // Delivery tracking
    private List<DeliveryInfo> deliveredTo;
    private List<ReadInfo> readBy;
    
    public Message() {
        this.messageType = MessageType.TEXT;
        this.status = MessageStatus.SENT;
        this.isEncrypted = false;
        this.isEdited = false;
        this.isDeleted = false;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getConversationId() {
        return conversationId;
    }
    
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    
    public String getSenderId() {
        return senderId;
    }
    
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    
    public MessageType getMessageType() {
        return messageType;
    }
    
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Boolean getIsEncrypted() {
        return isEncrypted;
    }
    
    public void setIsEncrypted(Boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }
    
    public String getEncryptionKeyVersion() {
        return encryptionKeyVersion;
    }
    
    public void setEncryptionKeyVersion(String encryptionKeyVersion) {
        this.encryptionKeyVersion = encryptionKeyVersion;
    }
    
    public String getIv() {
        return iv;
    }
    
    public void setIv(String iv) {
        this.iv = iv;
    }
    
    public List<MessageAttachment> getAttachments() {
        return attachments;
    }
    
    public void setAttachments(List<MessageAttachment> attachments) {
        this.attachments = attachments;
    }
    
    public MessageReply getReplyTo() {
        return replyTo;
    }
    
    public void setReplyTo(MessageReply replyTo) {
        this.replyTo = replyTo;
    }
    
    public MessageForward getForwardedFrom() {
        return forwardedFrom;
    }
    
    public void setForwardedFrom(MessageForward forwardedFrom) {
        this.forwardedFrom = forwardedFrom;
    }
    
    public MessageStatus getStatus() {
        return status;
    }
    
    public void setStatus(MessageStatus status) {
        this.status = status;
    }
    
    public Boolean getIsEdited() {
        return isEdited;
    }
    
    public void setIsEdited(Boolean isEdited) {
        this.isEdited = isEdited;
    }
    
    public Date getEditedAt() {
        return editedAt;
    }
    
    public void setEditedAt(Date editedAt) {
        this.editedAt = editedAt;
    }
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public Date getDeletedAt() {
        return deletedAt;
    }
    
    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
    
    public List<String> getDeletedFor() {
        return deletedFor;
    }
    
    public void setDeletedFor(List<String> deletedFor) {
        this.deletedFor = deletedFor;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<DeliveryInfo> getDeliveredTo() {
        return deliveredTo;
    }
    
    public void setDeliveredTo(List<DeliveryInfo> deliveredTo) {
        this.deliveredTo = deliveredTo;
    }
    
    public List<ReadInfo> getReadBy() {
        return readBy;
    }
    
    public void setReadBy(List<ReadInfo> readBy) {
        this.readBy = readBy;
    }
    
    // Inner classes
    public static class MessageAttachment {
        private String id;
        private String type; // IMAGE, VIDEO, AUDIO, DOCUMENT
        private String url;
        private String thumbnailUrl;
        private String fileName;
        private Long fileSize;
        private String mimeType;
        private Integer width;
        private Integer height;
        
        // Getters and Setters
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
        
        public String getThumbnailUrl() {
            return thumbnailUrl;
        }
        
        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }
        
        public String getFileName() {
            return fileName;
        }
        
        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
        
        public Long getFileSize() {
            return fileSize;
        }
        
        public void setFileSize(Long fileSize) {
            this.fileSize = fileSize;
        }
        
        public String getMimeType() {
            return mimeType;
        }
        
        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }
        
        public Integer getWidth() {
            return width;
        }
        
        public void setWidth(Integer width) {
            this.width = width;
        }
        
        public Integer getHeight() {
            return height;
        }
        
        public void setHeight(Integer height) {
            this.height = height;
        }
    }
    
    public static class MessageReply {
        private String messageId;
        private String senderId;
        private String senderName;
        private String preview;
        private String attachmentType;
        
        // Getters and Setters
        public String getMessageId() {
            return messageId;
        }
        
        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }
        
        public String getSenderId() {
            return senderId;
        }
        
        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }
        
        public String getSenderName() {
            return senderName;
        }
        
        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }
        
        public String getPreview() {
            return preview;
        }
        
        public void setPreview(String preview) {
            this.preview = preview;
        }
        
        public String getAttachmentType() {
            return attachmentType;
        }
        
        public void setAttachmentType(String attachmentType) {
            this.attachmentType = attachmentType;
        }
    }
    
    public static class MessageForward {
        private String originalMessageId;
        private String originalSenderId;
        private String originalSenderName;
        private Date forwardedAt;
        
        // Getters and Setters
        public String getOriginalMessageId() {
            return originalMessageId;
        }
        
        public void setOriginalMessageId(String originalMessageId) {
            this.originalMessageId = originalMessageId;
        }
        
        public String getOriginalSenderId() {
            return originalSenderId;
        }
        
        public void setOriginalSenderId(String originalSenderId) {
            this.originalSenderId = originalSenderId;
        }
        
        public String getOriginalSenderName() {
            return originalSenderName;
        }
        
        public void setOriginalSenderName(String originalSenderName) {
            this.originalSenderName = originalSenderName;
        }
        
        public Date getForwardedAt() {
            return forwardedAt;
        }
        
        public void setForwardedAt(Date forwardedAt) {
            this.forwardedAt = forwardedAt;
        }
    }
    
    public static class DeliveryInfo {
        private String userId;
        private Date deliveredAt;
        
        public DeliveryInfo() {}
        
        public DeliveryInfo(String userId, Date deliveredAt) {
            this.userId = userId;
            this.deliveredAt = deliveredAt;
        }
        
        // Getters and Setters
        public String getUserId() {
            return userId;
        }
        
        public void setUserId(String userId) {
            this.userId = userId;
        }
        
        public Date getDeliveredAt() {
            return deliveredAt;
        }
        
        public void setDeliveredAt(Date deliveredAt) {
            this.deliveredAt = deliveredAt;
        }
    }
    
    public static class ReadInfo {
        private String userId;
        private Date readAt;
        
        public ReadInfo() {}
        
        public ReadInfo(String userId, Date readAt) {
            this.userId = userId;
            this.readAt = readAt;
        }
        
        // Getters and Setters
        public String getUserId() {
            return userId;
        }
        
        public void setUserId(String userId) {
            this.userId = userId;
        }
        
        public Date getReadAt() {
            return readAt;
        }
        
        public void setReadAt(Date readAt) {
            this.readAt = readAt;
        }
    }
}
