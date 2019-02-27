package ru.hse.mnmalysheva.phonebook;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PhonebookManager implements AutoCloseable {
    private static PhonebookManager instance;

    private SessionFactory sessionFactory;

    {
        Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        initSessionFactory();
    }
    
    private PhonebookManager() {}
    
    public static @NotNull PhonebookManager getInstance() {
        if (instance == null) {
            instance = new PhonebookManager();
        }
        return instance;
    }

    @Override
    public void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    public boolean add(@NotNull String name, @NotNull String phone) {
        return commitTransaction(session -> {
            var nameEntity = addNameIfNotPresented(session, name);
            var phoneEntity = addPhoneIfNotPresented(session, phone);
            if (nameEntity.getPhones().contains(phoneEntity)) {
                return false;
            }
            nameEntity.getPhones().add(phoneEntity);
            session.update(nameEntity);
            return true;
        });
    }

    public Set<String> getPhonesByName(@NotNull String name) {
        return commitTransaction(session -> {
            var nameEntity = loadName(session, name);
            return nameEntity == null ? Collections.emptySet() : nameEntity.getPhoneNumbers();
        });
    }

    public Set<String> getNamesByPhone(@NotNull String phone) {
        return commitTransaction(session -> {
            var phoneEntity = loadPhone(session, phone);
            return phoneEntity == null ? Collections.emptySet() : phoneEntity.getOwners();
        });
    }

    public boolean delete(@NotNull String name, @NotNull String phone) {
        boolean isSuccessful = deleteConnection(name, phone);
        deleteNameIfRedundant(name);
        deletePhoneIfRedundant(phone);
        return isSuccessful;
    }

    public boolean changeName(@NotNull String name, @NotNull String phone, @NotNull String newName) {
        if (!deleteConnection(name, phone)) {
            return false;
        }
        add(newName, phone);
        deleteNameIfRedundant(name);
        return true;
    }

    public boolean changePhone(@NotNull String name, @NotNull String phone, @NotNull String newPhone) {
        if (!deleteConnection(name, phone)) {
            return false;
        }
        add(name, newPhone);
        deletePhoneIfRedundant(phone);
        return true;
    }

    public @NotNull Map<String, Set<String>> getContent() {
        return commitTransaction(session -> {
            @SuppressWarnings("unchecked")
            var names = (Stream<Name>) session.createQuery("FROM Name").getResultStream();
            return names.collect(
                    Collectors.toMap(Name::getName, Name::getPhoneNumbers)
            );
        });
    }
    public void clear() {
        close();
        initSessionFactory();
    }

    private void initSessionFactory() {
        var registry = new StandardServiceRegistryBuilder().configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    private <R> R commitTransaction(@NotNull Function<Session, R> action) {
        Transaction transaction = null;
        R result;
        try (var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            result = action.apply(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
        return result;
    }

    private void commitTransaction(Consumer<Session> action) {
        commitTransaction(session -> {
            action.accept(session);
            return null;
        });
    }

    private <T> T loadEntity(@NotNull Session session,
                             @NotNull String naturalId,
                             @NotNull Class<T> entityClass) {
        return session.bySimpleNaturalId(entityClass).load(naturalId);
    }

    private Name loadName(@NotNull Session session, @NotNull String name) {
        return loadEntity(session, name, Name.class);
    }

    private Phone loadPhone(@NotNull Session session, @NotNull String phone) {
        return loadEntity(session, phone, Phone.class);
    }

    private <T> T addIfNotPresented(@NotNull Session session,
                                    @NotNull String naturalId,
                                    @NotNull Class<T> entityClass) {
        var entity = loadEntity(session, naturalId, entityClass);
        if (entity == null) {
            try {
                entity = entityClass.getDeclaredConstructor(String.class).newInstance(naturalId);
            } catch (Exception ignored) {}
            session.save(entity);
        }
        return entity;
    }

    private Name addNameIfNotPresented(@NotNull Session session, @NotNull String name) {
        return addIfNotPresented(session, name, Name.class);
    }

    private Phone addPhoneIfNotPresented(@NotNull Session session, @NotNull String phone) {
        return addIfNotPresented(session, phone, Phone.class);
    }

    private boolean deleteConnection(@NotNull String name, @NotNull String phone) {
        return commitTransaction(session -> {
            var nameEntity = loadName(session, name);
            var phoneEntity = loadPhone(session, phone);
            if (nameEntity == null || !nameEntity.getPhones().contains(phoneEntity)) {
                return false;
            }
            nameEntity.getPhones().remove(phoneEntity);
            session.update(nameEntity);
            return true;
        });
    }

    private <T> void deleteIfRedundant(@NotNull String naturalId,
                                       @NotNull Class<T> entityClass,
                                       @NotNull Predicate<T> toDelete) {
        commitTransaction(session -> {
            var entity = loadEntity(session, naturalId, entityClass);
            if (entity != null && toDelete.test(entity)) {
                session.delete(entity);
            }
        });
    }

    private void deleteNameIfRedundant(@NotNull String name) {
        deleteIfRedundant(name, Name.class, nameEntity -> nameEntity.getPhones().isEmpty());
    }

    private void deletePhoneIfRedundant(@NotNull String phone) {
        deleteIfRedundant(phone, Phone.class, phoneEntity -> phoneEntity.getNames().isEmpty());
    }
}
