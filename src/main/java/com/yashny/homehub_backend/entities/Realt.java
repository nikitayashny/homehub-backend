package com.yashny.homehub_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "realts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Realt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private int price;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    private Type type;
    @Column(name = "roomsCount")
    private int roomsCount;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "dealType_id")
    private DealType dealType;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
    @Column(name = "street")
    private String street;
    @Column(name = "house")
    private int house;
    @Column(name = "article", columnDefinition = "text")
    private String article;
    @Column(name = "area")
    private int area;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "realt", orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    private Long previewImageId;

    private LocalDateTime dateOfCreated;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "views", columnDefinition = "bigint default 0")
    private Long views;
    @Column(name = "reposts", columnDefinition = "bigint default 0")
    private Long reposts;
    @Column(name = "likes", columnDefinition = "bigint default 0")
    private Long likes;

    @Transactional
    public void addImageToRealt(Image image) {
        image.setRealt(this);
        images.add(image);
    }

    @PrePersist
    private void onCreate() { dateOfCreated = LocalDateTime.now(); }
}
