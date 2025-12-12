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
@Table(name="pkmn_user_collection")
public class UserCollection {

    @Id
    private String id;

    @Column
    private String userId;

    @Column
    private String isPrivate;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String cover;

    @Column
    private String type; // collection or wishlist

}
