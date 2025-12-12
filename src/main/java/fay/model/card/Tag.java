package fay.model.card;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="pkmn_user_tag")
public class Tag {

    @Id
    private String id;

    @Column
    private String name;

    @Column
    private String userId;

}
