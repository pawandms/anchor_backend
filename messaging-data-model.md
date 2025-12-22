# Messaging App Data Model

## Entity Relationship Diagram

```mermaid
erDiagram
    CONVERSATIONS ||--o{ CONVERSATION_MEMBERS : "has"
    CONVERSATIONS ||--o{ MESSAGES : "contains"
    MESSAGES ||--o{ MESSAGE_REACTIONS : "receives"
    MESSAGES ||--o| MESSAGES : "replies to"
    USER ||--o{ CONVERSATION_MEMBERS : "participates"
    USER ||--o{ MESSAGES : "sends"
    USER ||--o{ MESSAGE_REACTIONS : "reacts"

    CONVERSATIONS {
        ObjectId _id PK
        String conversationType "ONE_TO_ONE or GROUP"
        String name "Group name"
        String description
        String avatarUrl
        String createdBy FK
        Date createdAt
        Date updatedAt
        Date lastMessageAt
        String lastMessageText
        Boolean isActive
        Array admins "User IDs"
        Object settings
    }

    CONVERSATION_MEMBERS {
        ObjectId _id PK
        String conversationId FK
        String userId FK
        String role "ADMIN or MEMBER"
        Date joinedAt
        String lastReadMessageId FK
        Date lastReadAt
        Boolean isMuted
        Boolean isPinned
        Date leftAt
        String addedBy FK
    }

    MESSAGES {
        ObjectId _id PK
        String conversationId FK
        String senderId FK
        String messageType "TEXT, MEDIA, EMOJI, SYSTEM"
        String content
        Array attachments
        Object replyTo
        Object forwardedFrom
        String status "SENT, DELIVERED, READ, FAILED"
        Boolean isEdited
        Date editedAt
        Boolean isDeleted
        Date deletedAt
        Array deletedFor
        Date createdAt
        Date updatedAt
        Array deliveredTo
        Array readBy
    }

    MESSAGE_REACTIONS {
        ObjectId _id PK
        String messageId FK
        String conversationId FK
        String userId FK
        String emoji
        Date createdAt
    }

    USER {
        String uid PK
        String userName
        String firstName
        String lastName
        String email
        String profileImage
    }
```

## Collection Relationships

### One-to-One Conversation
```mermaid
graph LR
    A[User A] -->|member| C[Conversation]
    B[User B] -->|member| C
    C -->|contains| M1[Message 1]
    C -->|contains| M2[Message 2]
    C -->|contains| M3[Message 3]
```

### Group Conversation
```mermaid
graph LR
    U1[User 1] -->|member| G[Group Conversation]
    U2[User 2] -->|admin| G
    U3[User 3] -->|member| G
    U4[User 4] -->|member| G
    G -->|contains| M1[Message 1]
    G -->|contains| M2[Message 2]
    M1 -->|reply| M2
```

## Message Flow with Features

```mermaid
flowchart TD
    A[User Sends Message] --> B{Message Type?}
    B -->|Text| C[Store in Messages]
    B -->|Media| D[Upload to Storage]
    D --> E[Store URL in Attachments]
    E --> C
    B -->|Emoji| C
    
    C --> F[Update Conversation lastMessageAt]
    F --> G[Notify Members]
    
    H[User Reacts] --> I[Store in message_reactions]
    I --> J[Update Reaction Count]
    
    K[User Replies] --> L[Store replyTo reference]
    L --> C
    
    M[User Forwards] --> N[Copy message with forwardedFrom]
    N --> C
    
    O[User Reads] --> P[Update lastReadMessageId]
    P --> Q[Update message readBy array]
```

## Data Access Patterns

```mermaid
graph TB
    subgraph "Get User Conversations"
    A1[Query conversation_members by userId] --> A2[Get conversation details]
    A2 --> A3[Sort by lastMessageAt]
    A3 --> A4[Calculate unread count]
    end
    
    subgraph "Get Conversation Messages"
    B1[Query messages by conversationId] --> B2[Sort by createdAt DESC]
    B2 --> B3[Paginate results]
    B3 --> B4[Load reactions for each message]
    end
    
    subgraph "Send Message"
    C1[Insert message] --> C2[Update conversation lastMessage]
    C2 --> C3[Push notification to members]
    C3 --> C4[WebSocket broadcast]
    end
```

## Indexes Strategy

```mermaid
graph LR
    subgraph "Conversations"
    I1[updatedAt: -1]
    I2[lastMessageAt: -1]
    end
    
    subgraph "Conversation Members"
    I3[conversationId + userId: unique]
    I4[userId: 1]
    end
    
    subgraph "Messages"
    I5[conversationId + createdAt: -1]
    I6[senderId + createdAt: -1]
    I7[replyTo.messageId: 1]
    end
    
    subgraph "Message Reactions"
    I8[messageId + userId: unique]
    I9[messageId: 1]
    end
```
