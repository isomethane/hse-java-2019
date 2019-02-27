package ru.hse.mnmalysheva.phonebook;

import org.hibernate.annotations.NaturalId;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "NAME")
class Name {
    @Id
    @GeneratedValue
    @Column(name = "NAME_ID")
    private int id;

    @NaturalId
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "NAME_PHONE",
            joinColumns = { @JoinColumn(name = "NAME_ID") },
            inverseJoinColumns = { @JoinColumn(name = "PHONE_ID") }
    )
    private Set<Phone> phones = new HashSet<>();

    public Name() {}

    public Name(@NotNull String name) {
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotNull Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(@NotNull Set<Phone> phones) {
        this.phones = phones;
    }

    public @NotNull Set<String> getPhoneNumbers() {
        return phones.stream().map(Phone::getPhoneNumber).collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return name.equals(((Name) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
