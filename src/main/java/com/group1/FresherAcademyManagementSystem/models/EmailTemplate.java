package com.group1.FresherAcademyManagementSystem.models;

import com.group1.FresherAcademyManagementSystem.models.EmailSend;
import com.group1.FresherAcademyManagementSystem.models.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "emailTemplate")
public class EmailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "type")
    private String type;
    @Column(name = "category")
    private String category;
    @Column(name = "content",columnDefinition = "TEXT")
    private String content;
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "description")
    private String description;
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDate createDate;
    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDate updateDate;
    @Column(name = "use_dear")
    private Boolean useDear;
    @Column(name = "subject")
    private String subject;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private com.group1.FresherAcademyManagementSystem.models.UserEntity userCreate;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserEntity userUpdate;


    @OneToMany(mappedBy = "emailTemplate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmailSend> emailSendss  = new ArrayList<>();
}
