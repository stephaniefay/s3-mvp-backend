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
@Table(name = "pkmn_collection_card")
public class Card {

    public enum Relation {
        COLLECTION("collection"),
        WISHLIST("wishlist");

        private final String name;

        Relation(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Id
    private String id;

    @Column
    private String externalId;

    @Column
    private String name;

    @Column
    private String image;

    @Column
    private String relationId;

    @Column
    private String relation;

    @Column
    private String userId;

    @Column
    private Integer priority;

    @Column(name = "TAG")
    private String tagId;

    @Column
    private Integer condition;

    @Column
    private String language;

}
