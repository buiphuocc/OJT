package com.group1.FresherAcademyManagementSystem.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name  = "score")
    private Float score;

    @Column(name = "score_name")
    private String scoreName;

    @CreationTimestamp
    @Column(name = "submission_date")
    private LocalDate submissionDate;

    @ManyToOne
    @JoinColumn(name = "assignment_id", unique = false)
    @JsonManagedReference
    @JsonIgnore
    @JsonProperty(value = "assignment")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonIgnore
    @JsonProperty(value = "student")
    private Student student;
}
