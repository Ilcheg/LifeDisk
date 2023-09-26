package ru.netology.lifedisk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.lifedisk.entity.File;
import ru.netology.lifedisk.entity.User;

import java.util.List;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByOwner(User user);

    void deleteByOwnerAndFileName(User user, String fileName);

    File findByOwnerAndFileName(User user, String fileName);
}
