package ar.edu.itba.paw.model.keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserDebateKey implements Serializable {

    @Column(name = "userid")
    private Long userId;

    @Column(name = "debateid")
    private Long debateId;

    public UserDebateKey(Long userId, Long debateId) {
        this.userId = userId;
        this.debateId = debateId;
    }

    UserDebateKey() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDebateId() {
        return debateId;
    }

    public void setDebateId(Long debateId) {
        this.debateId = debateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDebateKey that = (UserDebateKey) o;
        return userId.equals(that.userId) && debateId.equals(that.debateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, debateId);
    }
}
