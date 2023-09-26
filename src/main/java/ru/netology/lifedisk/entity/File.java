package ru.netology.lifedisk.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
//@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "file_info")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long size;

    @Column(name = "type", nullable = false)
    private String type;

    @Lob
    @Column(name = "bytes", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] bytes;

    @Column(name = "upload_date", updatable = false, nullable = false)
    private LocalDateTime uploadDate;

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "username")
    private User owner;

    public File(LocalDateTime uploadDate, String fileName, String type, long size, byte[] bytes, User owner) {
        this.uploadDate = uploadDate;
        this.fileName = fileName;
        this.type = type;
        this.size = size;
        this.bytes = bytes;
        this.owner = owner;
    }
}