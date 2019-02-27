package ru.hse.mnmalysheva.phonebook;

import org.hibernate.annotations.NaturalId;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/** Class represents phone entity in phonebook database managed by {@link PhonebookManager}. **/
@Entity
@Table(name = "PHONE")
class Phone {
    @Id
    @GeneratedValue
    @Column(name = "PHONE_ID")
    private int id;

    @NaturalId
    @Column(name = "PHONE_NUMBER", unique = true, nullable = false)
    private String phoneNumber;

    @ManyToMany(mappedBy = "phones")
    private Set<Name> names = new HashSet<>();

    public Phone() {}

    public Phone(@NotNull String phoneNumber) {
        setPhoneNumber(phoneNumber);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotNull String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotNull Set<Name> getNames() {
        return names;
    }

    public void setNames(@NotNull Set<Name> names) {
        this.names = names;
    }

    public @NotNull Set<String> getOwners() {
        return names.stream().map(Name::getName).collect(Collectors.toSet());
    }

    /** Compares entities by unique phone number. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return phoneNumber.equals(((Phone) o).phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber);
    }
}
