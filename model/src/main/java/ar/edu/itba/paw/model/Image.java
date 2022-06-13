package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_imageid_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "images_imageid_seq", name = "images_imageid_seq")
    @Column(name = "imageid")
    private Long id;

    @Column(nullable = false)
    private byte[] data;

    Image() {}

    public Image(byte[] data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }
}
