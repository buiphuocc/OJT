package com.group1.FresherAcademyManagementSystem.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "assignment")
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assignment_name")
    private String assignmentName;

    @Column(name = "assignment_type")
    private String assignmentType;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "description")
    private String description;
    @CreationTimestamp
    @Column(name = "create_date")
    private LocalDate createDate;

    @Column(name = "created_by")
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "update_date")
    private LocalDate updateDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Score> scores = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "module_id")
    @JsonManagedReference
    private Module module;
}
