package com.group1.FresherAcademyManagementSystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "emailSend_student")
public class EmailSend_Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long email_student_id;

    @Column(name = "receiver_type")
    private String receiverType;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    @JsonIgnore
    @JsonProperty(value = "student")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "email_id")
    @JsonIgnore
    @JsonProperty(value = "emailSend")
    private EmailSend emailSend;
}
