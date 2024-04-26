package com.group1.FresherAcademyManagementSystem.models;

import com.group1.FresherAcademyManagementSystem.models.EmailSend_Student;
import com.group1.FresherAcademyManagementSystem.models.EmailTemplate;
import com.group1.FresherAcademyManagementSystem.models.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "emailSend")
public class EmailSend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "send_date")
    private Date sendDate;

    @Column(name = "receiver_type")
    private String receiverType;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserEntity user;

    @Column(name = "action")
    private String action;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private EmailTemplate emailTemplate;

    @OneToMany(mappedBy = "emailSend", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EmailSend_Student> emailSend_students = new ArrayList<>();
}
