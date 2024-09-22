package com.example.freelance_project_management_platform.data.models;

import com.example.freelance_project_management_platform.data.enums.ProjectOrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project_orders")
public class ProjectOrder extends BaseEntity {

    //? Project associated with the order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    //? Client who made the order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private User client;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private ProjectOrderStatus status;

    @Column(unique = true, nullable = false)
    private String razorpayOrderId;

    private Long createdAt;
    private Long updatedAt;
}
