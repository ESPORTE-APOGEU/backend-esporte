package com.esporte.myapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "notification")
public class Notification extends BaseUserRelatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String type;
    private String iconName;
    private String title;
    private String description;
    private LocalDateTime timestamp;
    private String tagText;
    private String tagIcon;
    
    @Column(name = "related_event_id")
    private Long relatedEventId;
    
    @Column(name = "entry_id")
    private Long entryId;
    
    public Long getEntryId() { 
        return entryId; 
    }
    
    public void setEntryId(Long entryId) { 
        this.entryId = entryId;
    }
}