package ar.edu.itba.paw.model;

public class Image {
    private final long id;
    private final byte[] data;

    public Image(long id, byte[] data) {
        this.id = id;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }
}
