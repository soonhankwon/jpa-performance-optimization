package dev.jpaperformanceoptimization.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
// 값 타입은 변경 불가능하게 설계
public class Address {

    private String city;
    private String street;
    private String zipCode;

    protected Address() {
    }

    public Address(String city, String street, String zipCode) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }
}
