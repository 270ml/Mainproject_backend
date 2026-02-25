package com.kdt03.fashion_api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {

    // DB의 실제 PK는 seq이지만, id가 UNIQUE하고 비즈니스 식별자로 사용됨.
    // Spring JPA에서 @Id를 id로 설정하여 findById(id)가 동작하도록 유지.
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    // seq는 DB GENERATED ALWAYS AS IDENTITY 컬럼.
    // insertable=false, updatable=false로 JPA가 건드리지 않도록 설정.
    @Column(name = "seq", insertable = false, updatable = false)
    private Integer seq;

    private String password;
    private String nickname;

    @Column(nullable = false)
    private String provider;

    private String profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Stores store;
}